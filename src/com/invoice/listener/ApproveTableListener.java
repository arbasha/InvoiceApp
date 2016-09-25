package com.invoice.listener;

import java.util.List;

import com.invoice.entity.Registration;

/**
 * @author Arshad
 *
 */
public interface ApproveTableListener {

	public void saveAccessList(List<Registration> approveList);

	public void delete(List<Registration> approveList, int[] row);

	public void getAccessList();
}
