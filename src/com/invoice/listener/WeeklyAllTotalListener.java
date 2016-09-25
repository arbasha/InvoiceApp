/**
 * 
 */
package com.invoice.listener;

import java.util.List;

import com.invoice.entity.WeeksTotal;

/**
 * @author Arshad
 * 
 */
public interface WeeklyAllTotalListener {

	public String export(List<WeeksTotal> weekTotalAllList);

	public void sendMail(List<WeeksTotal> weekTotalAllList, String filePath);

}
