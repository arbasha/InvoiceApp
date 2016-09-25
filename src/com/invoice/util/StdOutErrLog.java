package com.invoice.util;

import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 * @author Arshad
 *
 */
public class StdOutErrLog {

	private static final Logger logger = Logger.getLogger(StdOutErrLog.class);

	public static void tieSystemOutAndErrToLog() {
		System.setOut(createLoggingProxy(System.out));
		System.setErr(createLoggingProxy(System.err));
	}

	public static PrintStream createLoggingProxy(
			final PrintStream realPrintStream) {
		return new PrintStream(realPrintStream) {
			public void print(final String string) {
				realPrintStream.print(string);
				logger.info(string);
			}
		};
	}
}