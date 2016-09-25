package com.invoice.listener;

import java.util.List;

/**
 * @author Arshad
 *
 */
public interface DisApproveUserListener {
	public void disableUser(List<String> listVal);

	public void delUser(List<String> listVal);

}
