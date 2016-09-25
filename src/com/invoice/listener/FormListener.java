package com.invoice.listener;

import java.util.Calendar;
import java.util.EventListener;
import java.util.List;

import com.invoice.event.FormEvent;

/**
 * @author Arshad
 *
 */
public interface FormListener extends EventListener {
public void formEventOcuured(	FormEvent e);
public void customViewEvent(Calendar fromDate, Calendar toDate, String wd);
public void customViewEvent(Calendar fromDate, Calendar toDate,
		List<String> listVal, String wd);
}
