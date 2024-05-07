package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;

import java.util.List;

public interface ContactService {

    void saveContact(ContactForm contactForm);

    List<Contact> findAll();

    Contact findById(Long id);

    void deleteById(Long id);
}