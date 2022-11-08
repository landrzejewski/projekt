package com.example.restdockerplatform.utils;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class DateTimeService {

    public LocalDateTime getSystemDateTime() {

        return LocalDateTime.now();
    }

}
