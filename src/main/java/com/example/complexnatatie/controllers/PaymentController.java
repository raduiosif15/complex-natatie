package com.example.complexnatatie.controllers;

import com.example.complexnatatie.controllers.handlers.request.PaymentRequest;
import com.example.complexnatatie.controllers.handlers.request.ReportRequest;
import com.example.complexnatatie.controllers.handlers.responses.PaymentResponse;
import com.example.complexnatatie.dtos.PaymentDTO;
import com.example.complexnatatie.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.pay(paymentRequest), HttpStatus.CREATED);
    }

    @GetMapping("/daily")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getDaily(@RequestBody ReportRequest reportRequest) {
        return new ResponseEntity<>(paymentService.getDaily(reportRequest), HttpStatus.OK);
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getMonthly(@RequestBody ReportRequest reportRequest) {
        return new ResponseEntity<>(paymentService.getMonthly(reportRequest), HttpStatus.OK);
    }

    @PostMapping("/email")
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<Object> sendEmail() {
        return new ResponseEntity<>(paymentService.sendEmail(), HttpStatus.OK);
    }

// todo: delete these lines
//

//
//
//
//
//    // create file
//
//    serverPath = servletRequest.getSession().getServletContext().getRealPath("/");
//    File reject = new File(serverPath+"/reject.xlsx");
//
//		System.out.println(reject.getAbsolutePath());
//
//    XSSFWorkbook rejectXLSX = new XSSFWorkbook(new FileInputStream(reject));
//
//    final XSSFSheet sheet = rejectXLSX.getSheetAt(0);
//
////    int last = sheet.getLastRowNum();
////
////		for(int i=1; i<last; i++) {
////
////        try {
////            XSSFRow row = sheet.getRow(i);
////            sheet.removeRow(row);
////        }
////        catch (Exception e) {
////
////        }
////    }
//
//    Iterator<Customer> iter = rejectList.iterator();
//
//    // row 0 cap tabel
//
//
//    int rowNr = 1;
//		while(iter.hasNext())
//
//    {
//        customer = iter.next();
//        row = sheet.createRow(rowNr);
//
//        cell = row.createCell(0);
//        cell.setCellValue(customer.getUniqueCode());
//
//        rowNr++;
//    }
//
//    FileOutputStream fos = new FileOutputStream(reject);
//
//		rejectXLSX.write(fos);
//		fos.close();

}
