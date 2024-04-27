package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public class ContactController {
    @PostMapping("/contact")
    public ModelAndView contact(
        @RequestParam("lastName") String lastName,
        @RequestParam("firstName") String firstName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("zipCode") String zipCode,
        @RequestParam("address") String address,
        @RequestParam("buildingName") String buildingName,
        @RequestParam("contactType") String contactType,
        @RequestParam("body") String body,
        ModelAndView mv
    ) {
        mv.setViewName("confirmation");
        mv.addObject("lastName", lastName);
        mv.addObject("lastName", firstName);
        mv.addObject("lastName", email);
        mv.addObject("lastName", phone);
        mv.addObject("lastName", zipCode);
        mv.addObject("lastName", address);
        mv.addObject("lastName", buildingName);
        mv.addObject("lastName", contactType);
        mv.addObject("lastName", body);
        return mv;
    }
}
