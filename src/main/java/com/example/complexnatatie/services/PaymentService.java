package com.example.complexnatatie.services;

import com.example.complexnatatie.builders.PaymentBuilder;
import com.example.complexnatatie.builders.helpers.PaymentType;
import com.example.complexnatatie.builders.helpers.ReportType;
import com.example.complexnatatie.controllers.handlers.exceptions.CustomException;
import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.request.SendEmailRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.ContractDTO;
import com.example.complexnatatie.dtos.PaymentForReport;
import com.example.complexnatatie.dtos.PaymentMonthlyStatistic;
import com.example.complexnatatie.entities.PaymentCash;
import com.example.complexnatatie.entities.PaymentOnline;
import com.example.complexnatatie.entities.PaymentPos;
import com.example.complexnatatie.repositories.PaymentCashRepository;
import com.example.complexnatatie.repositories.PaymentOnlineRepository;
import com.example.complexnatatie.repositories.PaymentPosRepository;
import com.example.complexnatatie.repositories.PaymentRepository;
import com.example.complexnatatie.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    final UtilService utilService;
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

        utilService.xlsxCreate(paymentForReportList);

        String serverPath = servletContext.getRealPath("/");

        utilService.emailSender(
                emailRequest.getToEmail(),
                emailRequest.getSubject(),
                emailRequest.getMessage(),
                serverPath + "/receipts.xlsx"
        );

        return null;
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
