package com.invoice.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Arshad
 *
 */
public interface DisableListener {

	void customDisable(Calendar fromDate, Calendar toDate, List<String> listVal);

	void customEnable(Calendar fromDate, Calendar toDate, List<String> listVal);

	void getDefaulterList(Date fromDate, Date toDate);

}
