package com.example.complexnatatie.builders.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportType {

    private String name;


    public static final ReportType DAILY = new ReportType("DAILY");
    public static final ReportType MONTHLY = new ReportType("MONTHLY");
    public static final ReportType CUSTOM = new ReportType("CUSTOM");

    public static ReportType[] values = new ReportType[]{
            DAILY,
            MONTHLY,
            CUSTOM,
    };
}
