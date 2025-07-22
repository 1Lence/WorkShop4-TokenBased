package org.example.workshop4tokenbased.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vip")
public class VipController {

    @GetMapping
    public String getVip() {
        return "This is a VIP controller";
    }
}