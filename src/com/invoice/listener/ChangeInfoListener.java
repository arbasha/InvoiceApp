package com.invoice.listener;

import com.invoice.entity.Registration;

/**
 * @author Arshad
 *
 */
public interface ChangeInfoListener {

	void changeInfo(String oldPWDValue, String newPWDValue, String conPWDValue);

	Registration loadValues();

	void changePersonalInfo(Registration reg);

}
