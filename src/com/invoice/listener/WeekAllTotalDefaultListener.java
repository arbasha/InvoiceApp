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
public interface WeekAllTotalDefaultListener {

	public String export(List<WeeksTotal> list);
	public void sendMail(List<WeeksTotal> list, String fileName);

}
