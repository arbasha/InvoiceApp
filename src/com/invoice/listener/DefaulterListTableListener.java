package com.invoice.listener;

import java.util.List;

import com.invoice.pojo.DefaulterPojo;

/**
 * @author Arshad
 *
 */
public interface DefaulterListTableListener  {
	public String export(List<DefaulterPojo> defaulterlist) ;

	public void sendMail(List<DefaulterPojo> defaulterList, String filePath);
}
