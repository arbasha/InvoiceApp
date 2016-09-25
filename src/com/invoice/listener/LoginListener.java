package com.invoice.listener;

import com.invoice.dialog.ForgetPwdDialog;
import com.invoice.event.LoginEvent;


/**
 * @author Arshad
 *
 */
public interface LoginListener {
public void setloginDetails(LoginEvent le);
public void showRegPanel(boolean flag);
public void setRememberDetails(LoginEvent loginEvent);
public void clearDefaults();
public void sendMail(String userid, ForgetPwdDialog pwdDialog);

}
