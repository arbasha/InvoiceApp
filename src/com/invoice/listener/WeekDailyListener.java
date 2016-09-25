package com.invoice.listener;

import java.util.List;

import com.invoice.entity.Timesheet;

/**
 * @author Arshad
 *
 */
public interface WeekDailyListener {

	String export(List<Timesheet> list);

}
