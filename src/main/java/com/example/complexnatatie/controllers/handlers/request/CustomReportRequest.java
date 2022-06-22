package com.example.complexnatatie.controllers.handlers.request;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomReportRequest {

    private Date startDate;

    private Date endDate;

}
