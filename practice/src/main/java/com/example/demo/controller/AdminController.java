package com.example.demo.controller;

import com.example.demo.config.InquiryProperties;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;
import com.example.demo.form.LoginForm;
import com.example.demo.service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.form.AdminForm;
import com.example.demo.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/admin")
public final class AdminController {
    private final AdminService adminService;
    private final ContactService contactService;
    private final InquiryProperties inquiryProperties;

    /**
     * 依存性の注入
     *
     * @param adminService 管理者機能
     * @param contactService お問い合わせ機能
     * @param inquiryProperties configファイル
     */
    public AdminController(AdminService adminService, ContactService contactService, InquiryProperties inquiryProperties) {
        this.adminService = adminService;
        this.contactService = contactService;
        this.inquiryProperties = inquiryProperties;
    }

    /**
     * 管理者アカウント新規登録画面 初期表示
     *
     * @param model view変数
     * @param request リクエスト
     * @return 新規登録画面(ログイン時はお問い合わせ一覧画面)
     */
    @GetMapping("/signup")
    public String sign_up(Model model, HttpServletRequest request) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (adminService.isLogin(session)){
            return "redirect:/admin/contacts";
        }

        model.addAttribute("adminForm", new AdminForm());
        return "admin/sign_up";
    }

    /**
     * 管理者登録機能
     *
     * @param adminForm 管理者データ
     * @param errorResult バリデーションエラーデータ
     * @param request リクエスト
     * @return お問い合わせ一覧画面(バリデーションエラー時は管理者登録画面)
     */
    @PostMapping("/signup")
    public String register(
        @Validated @ModelAttribute("adminForm") AdminForm adminForm,
        BindingResult errorResult,
        HttpServletRequest request
    ) {
        // ログインチェック
        HttpSession session = request.getSession();
        if (adminService.isLogin(session)){
            return "redirect:/admin/contacts";
        }

        // バリデーション
        if (errorResult.hasErrors()) {
            return "admin/sign_up";
        }

        // 管理者登録処理
        Admin admin = adminService.saveAdmin(adminForm);

        // ログイン状態の保持
        session.setAttribute("admin", admin);

        return "redirect:/admin/contacts";
    }

    /**
     * ログイン画面 初期表示
     *
     * @param model view変数
     * @param request リクエスト
     * @return ログイン画面(ログイン時はお問い合わせ一覧画面)
     */
    @GetMapping("/signin")
    public String sign_in(Model model, HttpServletRequest request) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (adminService.isLogin(session)){
            return "redirect:/admin/contacts";
        }

        model.addAttribute("loginForm", new LoginForm());
        return "admin/sign_in";
    }

    /**
     * ログイン画面 初期表示
     *
     * @param loginForm view変数
     * @param errorResult バリデーションエラーデータ
     * @param request リクエスト
     * @return お問い合わせ一覧画面(バリデーションエラー時はログイン画面)
     */
    @PostMapping("/signin")
    public String certification(
        @Validated @ModelAttribute("loginForm") LoginForm loginForm,
        BindingResult errorResult,
        HttpServletRequest request
    ) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (adminService.isLogin(session)){
            return "redirect:/admin/contacts";
        }

        // バリデーション
        if (errorResult.hasErrors()) {
            return "admin/sign_in";
        }

        // ログインの確認
        Admin admin = adminService.certification(loginForm);
        if (admin == null){
            return "admin/sign_in";
        }

        //ログイン日時の更新
        adminService.updateCurrentSignInAt(admin.getId());

        //ログイン情報をセッションに保持
        session.setAttribute("admin", admin);

        return "redirect:/admin/contacts";
    }

    /**
     * お問い合わせ一覧画面
     *
     * @param model view変数
     * @param request リクエスト
     * @return お問い合わせ一覧画面(未ログイン時はログイン画面)
     */
    @GetMapping("/contacts")
    public String showContacts(Model model, HttpServletRequest request) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (!adminService.isLogin(session)){
            return "redirect:/admin/signin";
        }

        // お問い合わせデータ全件取得
        List<Contact> contacts = contactService.findAll();
        model.addAttribute("contacts", contacts);


        // お問い合わせ種別のconfigを取得
        model.addAttribute("typeMap", inquiryProperties.getTypes());

        return "admin/contacts";
    }

    /**
     * お問い合わせ詳細画面
     *
     * @param model view変数
     * @param request リクエスト
     * @param id お問い合わせID
     * @return お問い合わせ詳細画面(未ログイン時はログイン画面)
     */
    @GetMapping("/contact/{id}")
    public String showContactDetail(Model model, HttpServletRequest request, @PathVariable(name = "id") Long id) {
        // ログインチェック
        HttpSession session = request.getSession();
        if (!adminService.isLogin(session)){
            return "redirect:/admin/signin";
        }

        _setCommonAttributes(model, id);

        return "admin/contactDetail";
    }

    /**
     * お問い合わせ編集画面
     *
     * @param model view変数
     * @param request リクエスト
     * @param id お問い合わせID
     * @return お問い合わせ編集画面(未ログイン時はログイン画面)
     */
    @GetMapping("/contact/{id}/edit")
    public String ContactEdit(Model model, HttpServletRequest request, @PathVariable(name = "id") Long id) {
        // ログインチェック
        HttpSession session = request.getSession();
        if (!adminService.isLogin(session)) {
            // ログインしていない場合の処理
            return "redirect:/admin/signin";
        }

        _setCommonAttributes(model, id);

        return "admin/contactEdit";
    }

    /**
     * お問い合わせ編集機能
     *
     * @param contactForm view変数
     * @param errorResult バリデーションエラーデータ
     * @param model view変数
     * @param request リクエスト
     * @param id お問い合わせID
     * @return お問い合わせ詳細画面へリダイレクト(バリデーションエラー時は編集画面)
     */
    @PostMapping("/contact/{id}/edit")
    public String ContactUpdate(
        @Validated @ModelAttribute("contactForm") ContactForm contactForm,
        BindingResult errorResult,
        Model model,
        HttpServletRequest request,
        @PathVariable(name = "id") Long id) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (!adminService.isLogin(session)) {
            return "admin/contactEdit";
        }

        // バリデーション
        if (errorResult.hasErrors()) {
            model.addAttribute("contact", contactForm);
            model.addAttribute("typeMap", inquiryProperties.getTypes());

            return "admin/contactEdit";
        }

        contactService.saveContact(contactForm);

        return "redirect:/admin/contact/{id}";
    }

    /**
     * お問い合わせ削除機能
     *
     * @param contactForm view変数
     * @param errorResult バリデーションエラーデータ
     * @param request リクエスト
     * @param id お問い合わせID
     * @return お問い合わせ一覧画面へリダイレクト
     */
    @GetMapping("/contact/{id}/delete")
    public String ContactDelete(
            @Validated @ModelAttribute("contactForm") ContactForm contactForm,
            BindingResult errorResult,
            HttpServletRequest request,
            @PathVariable(name = "id") Long id) {

        // ログインチェック
        HttpSession session = request.getSession();
        if (!adminService.isLogin(session)) {
            return "redirect:/admin/signin";
        }

        contactService.deleteById(id);

        return "redirect:/admin/contacts";
    }

    /**
     * ログアウト機能
     *
     * @param session セッション
     * @return ログイン画面へリダイレクト
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/signin";
    }

    /**
     * お問い合わせ1件分のデータを画面に渡す
     *
     * @param model view変数
     * @param id お問い合わせID
     */
    private void _setCommonAttributes(Model model, Long id) {
        // お問い合わせデータ1件取得
        Contact contact = contactService.findById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setId(contact.getId());
        contactForm.setFirstName(contact.getFirstName());
        contactForm.setLastName(contact.getLastName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhone(contact.getPhone());
        contactForm.setZipCode(contact.getZipCode());
        contactForm.setAddress(contact.getAddress());
        contactForm.setBuildingName(contact.getBuildingName());
        contactForm.setContactType(contact.getContactType());
        contactForm.setBody(contact.getBody());
        contactForm.setCreatedAt(contact.getCreatedAt());
        contactForm.setUpdatedAt(contact.getUpdatedAt());

        model.addAttribute("contactForm", contactForm);

        // お問い合わせ種別のconfigを取得
        model.addAttribute("typeMap", inquiryProperties.getTypes());
    }

}