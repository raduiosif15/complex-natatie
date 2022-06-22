package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.request.ReportRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.entities.Payment;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentCashRepository;
import com.example.complexnatatie.repositories.PaymentPosRepository;
import com.example.complexnatatie.repositories.PaymentRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;

@Service
public record PaymentService(PaymentRepository paymentRepository,
                             PaymentPosRepository paymentPosRepository,
                             PaymentCashRepository paymentCashRepository,
                             ContractService contractService,
                             SubscriptionService subscriptionService,
                             ServletContext servletContext) {

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

    public List<PaymentDTO> getDaily(ReportRequest reportRequest) {

        final List<Payment> allPayments = new ArrayList<>();

        final Date dateFromRequest = reportRequest.getDate();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromRequest);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        final Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        final Date endDate = calendar.getTime();


        allPayments.addAll(paymentCashRepository.findByDate(startDate, endDate));
        allPayments.addAll(paymentPosRepository.findByDate(startDate, endDate));

        return PaymentBuilder.fromEntities(allPayments);

    }

    public List<PaymentDTO> getMonthly(ReportRequest reportRequest) {

        final List<Payment> allPayments = new ArrayList<>();

        final Date dateFromRequest = reportRequest.getDate();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromRequest);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        final Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        final Date endDate = calendar.getTime();

        allPayments.addAll(paymentCashRepository.findByDate(startDate, endDate));
        allPayments.addAll(paymentPosRepository.findByDate(startDate, endDate));

        return PaymentBuilder.fromEntities(allPayments);

    }

    public Object sendEmail() {

        emailSender("raduiosif15@yahoo.com", "Test", "This is a test email");

        return null;

    }

    public static void emailSender(String toEmail, String subject, String messageText) {


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mailtrap.io");
        props.put("mail.smtp.port", "2525");
        props.put("mail.debug", "true");


        Authenticator auth;

        auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "7329242b12f026",
                        "d1330f881cc9b8"
                );
            }
        };

        Session session = Session.getInstance(props, auth);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@complexnatatie.utcluj.ro"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // multipart for all email content
            Multipart multipart = new MimeMultipart();

            // bodyPart for message
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

//            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//            attachmentBodyPart.attachFile(new File("path/to/file"));
//            multipart.addBodyPart(attachmentBodyPart);

            // add bodyPart to multipart
            multipart.addBodyPart(mimeBodyPart);

            // add multipart to message
            message.setContent(multipart);

            // send message
            Transport.send(message);

            System.out.println("Message Sent.");
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }


    }

    public Object xlsxCreate() {

        String serverPath = servletContext.getRealPath("/");
        File receipts = new File(serverPath + "/receipts.xlsx");

        try {

            FileInputStream fileInputStream = new FileInputStream(receipts);
            XSSFWorkbook receiptsXLSX = new XSSFWorkbook(fileInputStream);

            // get sheet 0
            final XSSFSheet sheet = receiptsXLSX.getSheetAt(0);


            int last = sheet.getLastRowNum();
            for (int i = 1; i < last; i++) {
                final XSSFRow row = sheet.getRow(i);
                sheet.removeRow(row);
            }

            // header
            final List<String> headers = new ArrayList<>();
            headers.add("Nr. Crt.");
            headers.add("Nume si prenume");
            headers.add("Serie chitanta");
            headers.add("Numar chitanta");
            headers.add("Data");
            headers.add("Suma");

            final XSSFRow row = sheet.createRow(0);

            for (int i = 0; i < headers.size(); i++) {

                final XSSFCell cell = row.createCell(i);
                cell.setCellValue(headers.get(i));

            }


            // todo: get receipts by date
            // todo: put in tables
            // todo: send email with receipts


            FileOutputStream fileOutputStream = new FileOutputStream(receipts);

            receiptsXLSX.write(fileOutputStream);
            fileOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
