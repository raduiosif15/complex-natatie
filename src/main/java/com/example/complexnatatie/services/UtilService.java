package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.helpers.Constants;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.dtos.CustomerDTO;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.dtos.PaymentForReport;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public record UtilService(ServletContext servletContext) {

    public void xlsxCreate(List<PaymentForReport> paymentForReportList) {

        try {

            XSSFWorkbook receiptsXLSX = new XSSFWorkbook();
            // set sheet 0
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
                final XSSFCell noCrt = tableRow.createCell(0);
                noCrt.setCellValue(i + 1);


                final PaymentDTO payment = paymentForReportList.get(i).getPayment();
                final CustomerDTO customer = paymentForReportList.get(i).getCustomer();

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
            FileOutputStream fileOutputStream = new FileOutputStream(
                    serverPath + "/receipts.xlsx"
            );

            receiptsXLSX.write(fileOutputStream);
            fileOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void emailSender(String toEmail, String subject, String messageText, String pathToFile) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", Constants.EMAIL_HOST);
        properties.put("mail.smtp.port", Constants.EMAIL_SMTP_PORT);
        properties.put("mail.debug", "true");
        Authenticator authenticator;
        authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Constants.EMAIL_USER,
                        Constants.EMAIL_PASS
                );
            }
        };

        Session session = Session.getInstance(properties, authenticator);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.EMAIL_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // multipart for all email content
            Multipart multipart = new MimeMultipart();

            // bodyPart
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");
            mimeBodyPart.attachFile(new File(pathToFile));

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

}
