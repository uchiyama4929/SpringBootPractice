package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.form.AdminForm;
import com.example.demo.form.LoginForm;
import jakarta.servlet.http.HttpSession;

public interface AdminService {

    /**
     * 管理者情報の登録、又は更新を行う
     *
     * @param adminForm view変数
     * @return 登録対象の管理者情報
     */
    Admin saveAdmin(AdminForm adminForm);

    /**
     * パスワードの暗号化処理
     *
     * @param password 入力されたパスワード
     * @return 暗号化されたパスワード
     */
    String hashPassword(String password);

    /**
     * 暗号化後のパスワードと平文のパスワードを認証する処理
     *
     * @param password 平文のパスワード
     * @param hashedPassword 暗号化後のパスワード
     * @return 認証結果 同一の場合はtrueを返す
     */
    boolean checkPassword(String password, String hashedPassword);

    /**
     * 認証処理
     * メールアドレスとパスワードを用いてログイン可能なユーザーか判定する
     *
     * @param loginForm View変数
     * @return ログインユーザーの情報 未登録の場合はnullを返す
     */
    Admin certification(LoginForm loginForm);

    /**
     * 最終ログイン日時の日付を現在時刻で更新する
     *
     * @param adminId 対象の管理者ID
     */
    void updateCurrentSignInAt(Long adminId);

    /**
     * ログイン判定
     *
     * @param session セッション
     * @return ログイン中の場合 trueを返す
     */
    boolean isLogin(HttpSession session);
}