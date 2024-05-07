package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;
import com.example.demo.repository.ContactRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveContact(ContactForm contactForm) {
        Contact contact;

        if (contactForm.getId() != null) {
            contact = contactRepository.findById(contactForm.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid contact id: " + contactForm.getId()));
            contact.setUpdatedAt(LocalDateTime.now());
        } else {
            contact = new Contact();
            contact.setCreatedAt(LocalDateTime.now());
        }

        contact.setLastName(contactForm.getLastName());
        contact.setFirstName(contactForm.getFirstName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhone());
        contact.setZipCode(contactForm.getZipCode());
        contact.setAddress(contactForm.getAddress());
        contact.setBuildingName(contactForm.getBuildingName());
        contact.setContactType(contactForm.getContactType());
        contact.setBody(contactForm.getBody());
        contact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(contact);
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }
}