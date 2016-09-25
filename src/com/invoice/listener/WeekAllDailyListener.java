package com.invoice.listener;

import java.util.List;

import com.invoice.entity.Timesheet;

/**
 * @author Arshad
 *
 */
public interface WeekAllDailyListener {

	public String export(List<Timesheet> list);

	public void sendMail(List<Timesheet> list, String filePath);

}
