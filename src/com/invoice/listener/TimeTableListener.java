package com.invoice.listener;

import java.util.List;

import com.invoice.entity.Timesheet;

/**
 * @author Arshad
 *
 */
public interface TimeTableListener  {
	public void save(List<Timesheet> list) ;
}
