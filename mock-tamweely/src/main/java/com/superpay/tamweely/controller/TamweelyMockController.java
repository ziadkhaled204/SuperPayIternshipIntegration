package com.superpay.tamweely.controller;

import com.superpay.tamweely.model.Bill;
import com.superpay.tamweely.service.TamweelyMockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/SuperPay/SuperPayController")
public class TamweelyMockController {

    private final TamweelyMockService service;

    public TamweelyMockController(TamweelyMockService service) {
        this.service = service;
    }

    @PostMapping("/GetSuperPayDataByIDNO")
    public Bill inquiry(@RequestBody Map<String, Object> request) {
        return service.inquiry(request);
    }

    @PostMapping("/SetAmount")
    public Bill payment(@RequestBody Map<String, Object> request) {
        return service.payment(request);
    }

    @PostMapping("/CheckStatus")
    public Bill checkStatus(@RequestBody Map<String, Object> request) {
        return service.checkStatus(request);
    }
}
