package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.Constants;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.builders.helpers.ReportType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.request.SendEmailRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.*;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentOnline;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentCashRepository;
import com.example.complexnatatie.repositories.PaymentOnlineRepository;
import com.example.complexnatatie.repositories.PaymentPosRepository;
import com.example.complexnatatie.repositories.PaymentRepository;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    final PaymentRepository paymentRepository;
    final PaymentPosRepository paymentPosRepository;
    final PaymentCashRepository paymentCashRepository;
    final PaymentOnlineRepository paymentOnlineRepository;
    final ContractService contractService;
    final SubscriptionService subscriptionService;
    final ServletContext servletContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public double preview(int customerId, int months) {

        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new CustomException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        if (months > 12 || months > subscriptionService.getMonthsLeftUnpaid(customerId)) {

            LOGGER.error("Payment exceeds the contractual period");
            throw new CustomException("Payment exceeds the contractual period", HttpStatus.NOT_ACCEPTABLE);

        }

        return months * contractDTO.getMonthly();

    }

    public double previewSelf(int months, Authentication authentication) {

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return preview(userDetails.getId(), months);

    }

    public PaymentResponse pay(PaymentRequest paymentRequest) {

        final int customerId = paymentRequest.getCustomerId();
        final ContractDTO contractDTO = contractService.checkValidContractExists(customerId).getContract();

        if (contractDTO == null) {

            LOGGER.error("Customer with id: {} haven't got any active contract.", customerId);
            throw new CustomException("Customer with id: " + customerId + " haven't got any active contract.", HttpStatus.NOT_FOUND);

        }

        // deny payment for more than 12 months or subscription over contractual interval
        final int monthsToPay = paymentRequest.getMonths();

        if (monthsToPay > 12 || monthsToPay > subscriptionService.getMonthsLeftUnpaid(customerId)) {

            LOGGER.error("Payment exceeds the contractual period");
            throw new CustomException("Payment exceeds the contractual period", HttpStatus.NOT_ACCEPTABLE);

        }


        final Date date = new Date();
        final double value = monthsToPay * contractDTO.getMonthly();

        // create/extend subscription
        final PaymentResponse paymentResponse = subscriptionService.createOrExtendSubscription(customerId, monthsToPay);

        if (paymentRequest.getType().getName().equals(PaymentType.POS.getName())) {

            PaymentPos paymentPos = new PaymentPos(
                    0,
                    date,
                    value,
                    paymentRequest.getDescription(),
                    paymentRequest.getType().getName(),
                    paymentRequest.getCustomerId()
            );

            paymentPos = paymentRepository.save(paymentPos);
            paymentResponse.setPayment(PaymentBuilder.fromEntity(paymentPos));
            return paymentResponse;

        } else if (paymentRequest.getType().getName().equals(PaymentType.ONLINE.getName())) {

            PaymentOnline paymentOnline = new PaymentOnline(
                    0,
                    date,
                    value,
                    paymentRequest.getDescription(),
                    paymentRequest.getType().getName(),
                    paymentRequest.getCustomerId()
            );

            paymentOnline = paymentRepository.save(paymentOnline);
            paymentResponse.setPayment(PaymentBuilder.fromEntity(paymentOnline));
            return paymentResponse;

        }

        PaymentCash paymentCash = new PaymentCash(
                0,
                date,
                value,
                paymentRequest.getDescription(),
                paymentRequest.getType().getName(),
                paymentRequest.getCustomerId()
        );

        paymentCash = paymentRepository.save(paymentCash);
        paymentResponse.setPayment(PaymentBuilder.fromEntity(paymentCash));
        return paymentResponse;

    }

    public PaymentResponse selfPay(int months, Authentication authentication) {

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setCustomerId(userDetails.getId());
        paymentRequest.setDescription("Plata online pentru " + months + " luni" + " de la clientul " + userDetails.getUtcnId());
        paymentRequest.setType(PaymentType.ONLINE);
        paymentRequest.setMonths(months);

        return pay(paymentRequest);

    }


    public List<PaymentForReport> getReport(String date,
                                            String endDate,
                                            String type) {
        try {

            if (type.equals(ReportType.DAILY.getName())) {
                return getDaily(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            }

            if (type.equals(ReportType.MONTHLY.getName())) {
                return getMonthly(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            }

            return getCustom(new SimpleDateFormat("yyyy-MM-dd").parse(date), new SimpleDateFormat("yyyy-MM-dd").parse(endDate));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PaymentForReport> getDaily(Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        final Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        final Date endDate = calendar.getTime();

        final List<PaymentForReport> paymentForReportList = new ArrayList<>();

        final List<Object[]> objectsCash = paymentCashRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsPos = paymentPosRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsOnline = paymentOnlineRepository.findByDate(startDate, endDate);

        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsCash));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsPos));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsOnline));


        return paymentForReportList;

    }

    public List<PaymentForReport> getMonthly(Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        final Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        final Date endDate = calendar.getTime();

        final List<PaymentForReport> paymentForReportList = new ArrayList<>();

        final List<Object[]> objectsCash = paymentCashRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsPos = paymentPosRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsOnline = paymentOnlineRepository.findByDate(startDate, endDate);

        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsCash));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsPos));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsOnline));


        return paymentForReportList;

    }

    public List<PaymentForReport> getCustom(Date startDate, Date endDate) {

        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        startDate = calendar.getTime();


        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        endDate = calendar.getTime();

        final List<PaymentForReport> paymentForReportList = new ArrayList<>();

        final List<Object[]> objectsCash = paymentCashRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsPos = paymentPosRepository.findByDate(startDate, endDate);
        final List<Object[]> objectsOnline = paymentOnlineRepository.findByDate(startDate, endDate);

        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsCash));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsPos));
        paymentForReportList.addAll(PaymentBuilder.fromObjects(objectsOnline));


        return paymentForReportList;
    }

    public Object sendEmailWithXlsx(SendEmailRequest emailRequest) {

        final Date startDate = emailRequest.getDate();
        final Date endDate = emailRequest.getEndDate();
        final ReportType type = emailRequest.getType();

        final List<PaymentForReport> paymentForReportList = new ArrayList<>();

        if (type.getName().equals(ReportType.DAILY.getName())) {
            paymentForReportList.addAll(getDaily(startDate));
        } else if (type.getName().equals(ReportType.MONTHLY.getName())) {
            paymentForReportList.addAll(getMonthly(startDate));
        } else {
            paymentForReportList.addAll(getCustom(startDate, endDate));
        }

        xlsxCreate(paymentForReportList);

        String serverPath = servletContext.getRealPath("/");

        emailSender(
                emailRequest.getToEmail(),
                emailRequest.getSubject(),
                emailRequest.getMessage(),
                serverPath + "/receipts.xlsx"
        );

        return null;
    }

    public void xlsxCreate(List<PaymentForReport> paymentForReportList) {

        try {

            XSSFWorkbook receiptsXLSX = new XSSFWorkbook();

            // get sheet 0
            XSSFSheet sheet = receiptsXLSX.createSheet();

            // header
            final List<String> headers = new ArrayList<>();
            headers.add("Nr. Crt.");
            headers.add("Nume si prenume");
            headers.add("Serie chitanta");
            headers.add("Numar chitanta");
            headers.add("Data");
            headers.add("Suma");

            final XSSFRow row = sheet.createRow(0);

            for (int cel = 0; cel < headers.size(); cel++) {

                final XSSFCell cell = row.createCell(cel);
                cell.setCellValue(headers.get(cel));

            }

            for (int i = 0; i < paymentForReportList.size(); i++) {

                final XSSFRow tableRow = sheet.createRow(i + 1);

                final PaymentDTO payment = paymentForReportList.get(i).getPayment();
                final CustomerDTO customer = paymentForReportList.get(i).getCustomer();

                final XSSFCell noCrt = tableRow.createCell(0);
                noCrt.setCellValue(i + 1);
                final XSSFCell name = tableRow.createCell(1);
                name.setCellValue(customer.getFirstName() + " " + customer.getLastName());
                final XSSFCell series = tableRow.createCell(2);


                final String receiptSeries = payment.getType().getName().equals(PaymentType.POS.getName())
                        ? "UTPOS"
                        : payment.getType().getName().equals(PaymentType.ONLINE.getName())
                        ? "UTONLINE"
                        : "UTC";

                series.setCellValue(receiptSeries);


                final XSSFCell number = tableRow.createCell(3);
                number.setCellValue(payment.getId());
                final XSSFCell date = tableRow.createCell(4);
                date.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(payment.getDate()));
                final XSSFCell total = tableRow.createCell(5);
                total.setCellValue(payment.getValue());

            }

            String serverPath = servletContext.getRealPath("/");
            FileOutputStream fileOutputStream = new FileOutputStream(serverPath + "/receipts.xlsx");

            receiptsXLSX.write(fileOutputStream);
            fileOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void emailSender(String toEmail, String subject, String messageText, String pathToFile) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Constants.EMAIL_HOST);
        props.put("mail.smtp.port", Constants.EMAIL_SMTP_PORT);
        props.put("mail.debug", "true");

        Authenticator auth;

        auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Constants.EMAIL_USER,
                        Constants.EMAIL_PASS
                );
            }
        };

        Session session = Session.getInstance(props, auth);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // multipart for all email content
            Multipart multipart = new MimeMultipart();

            // bodyPart for message
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(pathToFile));
            multipart.addBodyPart(attachmentBodyPart);

            // add bodyPart to multipart
            multipart.addBodyPart(mimeBodyPart);

            // add multipart to message
            message.setContent(multipart);

            // send message
            Transport.send(message);

            System.out.println("Message Sent.");
        } catch (MessagingException | IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    public List<PaymentMonthlyStatistic> getMonthStatisticForYear(int year) {
        final List<PaymentMonthlyStatistic> paymentMonthlyStatisticList = new ArrayList<>();


        final List<Object[]> statisticsCash = paymentCashRepository.getMonthStatisticForYear(year);
        final List<Object[]> statisticsPos = paymentPosRepository.getMonthStatisticForYear(year);
        final List<Object[]> statisticsOnline = paymentOnlineRepository.getMonthStatisticForYear(year);

        paymentMonthlyStatisticList.addAll(PaymentBuilder.fromStatisticObjects(statisticsCash, PaymentType.CASH));
        paymentMonthlyStatisticList.addAll(PaymentBuilder.fromStatisticObjects(statisticsPos, PaymentType.POS));
        paymentMonthlyStatisticList.addAll(PaymentBuilder.fromStatisticObjects(statisticsOnline, PaymentType.ONLINE));

        return paymentMonthlyStatisticList;
    }

}
