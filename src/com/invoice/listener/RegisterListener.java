package com.invoice.listener;

import java.util.List;

import com.invoice.event.RegEvent;

public interface RegisterListener {

	public List<Object> getRegDropDowns();

	public void showLoginPanel(boolean b);

	public void insertRegDetails(RegEvent regEvent);

}
