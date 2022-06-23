package com.example.complexnatatie.controllers.handlers.request;

import com.example.complexnatatie.builders.helpers.ReportType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SendEmailRequest {

    private Date date;

    private Date endDate;

    private ReportType type;

    private String toEmail;

    private String subject;

    private String message;

}
