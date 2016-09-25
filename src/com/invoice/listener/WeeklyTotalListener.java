package com.invoice.listener;

import java.util.List;

import com.invoice.entity.WeeksTotal;

/**
 * @author Arshad
 *
 */
public interface WeeklyTotalListener {

	public String export(List<WeeksTotal> totalWeekList);
}
