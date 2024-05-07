package com.example.demo.service;

import com.example.demo.form.LoginForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.entity.Admin;
import com.example.demo.form.AdminForm;
import com.example.demo.repository.AdminRepository;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Admin saveAdmin(AdminForm adminForm) {
        Admin admin = new Admin();

        String hashedPassword = hashPassword(adminForm.getPassword());

        admin.setLastName(adminForm.getLastName());
        admin.setFirstName(adminForm.getFirstName());
        admin.setEmail(adminForm.getEmail());
        admin.setPassword(hashedPassword);
        admin.setCurrentSignInAt(LocalDateTime.now());
        adminRepository.save(admin);
        return admin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Admin certification(LoginForm loginForm) {
        Admin admin = adminRepository.findByEmail(loginForm.getEmail());

        if (admin != null && checkPassword(loginForm.getPassword(), admin.getPassword())) {
            return admin;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCurrentSignInAt(Long adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setCurrentSignInAt(LocalDateTime.now());
            adminRepository.save(admin);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLogin(HttpSession session) {
        Object admin = session.getAttribute("admin");
        return admin != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }

}