package com.example.complexnatatie.controllers;

import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.request.SendEmailRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.PaymentForReport;
import com.example.complexnatatie.dtos.PaymentMonthlyStatistic;
import com.example.complexnatatie.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/preview/{customerId}/{months}")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Double> preview(@PathVariable int customerId, @PathVariable int months) {
        return new ResponseEntity<>(paymentService.preview(customerId, months), HttpStatus.OK);
    }

    @GetMapping("/preview/self/{months}")
    @PreAuthorize("hasRole('ROLE_UTCN_STUDENT') or hasRole('ROLE_UTCN_EMPLOYEE')")
    public ResponseEntity<Double> previewSelf(@PathVariable int months, Authentication authentication) {
        return new ResponseEntity<>(paymentService.previewSelf(months, authentication), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.pay(paymentRequest), HttpStatus.CREATED);
    }

    @PostMapping("/self/{months}")
    @PreAuthorize("hasRole('ROLE_UTCN_STUDENT') or hasRole('ROLE_UTCN_EMPLOYEE')")
    public ResponseEntity<PaymentResponse> selfPay(@PathVariable int months, Authentication authentication) {
        return new ResponseEntity<>(paymentService.selfPay(months, authentication), HttpStatus.CREATED);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentForReport>> getReport(
            @RequestParam String date,
            @RequestParam String type,
            @RequestParam String endDate
    ) {
        return new ResponseEntity<>(paymentService.getReport(date, endDate, type), HttpStatus.OK);
    }

    @PostMapping("/email-with-xlsx")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Object> sendEmailWithXlsx(@RequestBody SendEmailRequest reportRequest) {
        return new ResponseEntity<>(paymentService.sendEmailWithXlsx(reportRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/statistic/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentMonthlyStatistic>> getMonthStatisticForYear(@PathVariable int year) {
        return new ResponseEntity<>(paymentService.getMonthStatisticForYear(year), HttpStatus.OK);
    }

}
