package com.invoice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Arshad
 *
 */
public class DateFormatter {
	static Logger logger = Logger.getLogger(DateFormatter.class);
	private static DateFormatter df = new DateFormatter();

	SimpleDateFormat mdy = new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat dmy = new SimpleDateFormat("dd-MMM-yyyy");
	SimpleDateFormat mmmdy = new SimpleDateFormat("MMM dd yyyy");
	SimpleDateFormat emmmdy = new SimpleDateFormat("EEEE, MMM d, yyyy");
	SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	SimpleDateFormat mm = new SimpleDateFormat("MM");

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private DateFormatter() {
	}

	/* Static 'instance' method */
	public static DateFormatter getInstance() {
		return df;
	}

	public String formatMMddYYYY(Date date)

	{

		return mdy.format(date);

	}

	public Date parseMMddYYYY(String toDate) {

		try {
			return mdy.parse(toDate);
		} catch (ParseException e) {

			logger.error(e);
		}
		return null;
	}

	public String formatddMMMyyyy(Date date) {
		return dmy.format(date);

	}

	public Date parseddMMMyyy(String toDate) {

		try {
			return dmy.parse(toDate);
		} catch (ParseException e) {

			logger.error(e);
		}
		return null;
	}

	public String formatMMMddyyyy(Date date) {
		return mmmdy.format(date);

	}

	public Date parseMMMddyyyy(String toDate) {

		try {
			return mmmdy.parse(toDate);
		} catch (ParseException e) {

			logger.error(e);
		}
		return null;
	}

	public String formatEMMMddyyyy(Date date) {
		return emmmdy.format(date);

	}

	public Date parseEMMMddyyyy(String toDate) {

		try {
			return emmmdy.parse(toDate);
		} catch (ParseException e) {

			logger.error(e);
		}
		return null;

	}

	public String getYYYY(Date date) {
		return yyyy.format(date);

	}

	public String getMM(Date date) {
		return mm.format(date);

	}

}
