package com.invoice.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.invoice.frame.LoginFrame;

public class InvoiceApp {
	
	public static final String PROPERTIES_FILE_NAME = "config.properties";	
	public static Properties prop = loadProperties();
	@SuppressWarnings("unused")
	private static ServerSocket SERVER_SOCKET;
	private static Logger logger = Logger.getLogger(InvoiceApp.class);
	public static final String VERSION = prop.getProperty("version");
	private static final String SHARE_PATH = prop.getProperty("share_path");
	private static final String DB_NAME = prop.getProperty("database_name");
	private static final String DB_PASSWORD = prop.getProperty("database_password");
	private static final String JACKCESS_OPENER_CLASS_NAME = prop.getProperty("jackcessOpener");


	public static void main(String[] args) {
	
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
		
				try {				
					//Build hibernate connection url
					StringBuilder url = new StringBuilder();
					url.append("jdbc:ucanaccess://")
							.append(SHARE_PATH.replace("${user.name}", System.getProperty("user.name")))
							.append(DB_NAME).append(";")
							.append("jackcessOpener=")
							.append(JACKCESS_OPENER_CLASS_NAME).append(";");
					logger.info("hibernate.connection.url: " + url.toString());
					
					//Add hibernate connect details to system property, this will be referred in hibernate.cg.xml file
					System.setProperty("hibernate.connection.url", url.toString());
					System.setProperty("connection.password", DB_PASSWORD);					
					SERVER_SOCKET = new ServerSocket(Integer.parseInt(prop.getProperty("app_port")));
					
					//Load up the frame
					new LoginFrame();

				 //Remove the above new LoginFrame() line and Uncomment the below code for version upgrade logic
				 // How it works? --> The below code compares the version placed in a common share_path to the version of the jar user clicked
			     // If there is a mismatch then an option to upgrade will be prompt, if user accepts latest jar from share_path is placed in user desktop
				 /* String userName = System.getProperty("user.name");
					File folder = new File(SHARE_PATH);
					for (File file : folder.listFiles()) {

						if (file.getAbsolutePath().endsWith("jar")) {
							if (file.getName().equalsIgnoreCase((VERSION))) {
								new LoginFrame();
							} else {
								int action = JOptionPane
										.showConfirmDialog(
												null,
												"Upgrade to Latest Version to use this tool",
												"New Version of Invoiving Tool Available",
												JOptionPane.OK_CANCEL_OPTION);
								if (action == JOptionPane.OK_OPTION) {
									String pathname = "C:\\Users\\" + userName
											+ "\\Desktop\\" + file.getName();
									File newFile = new File(pathname);

									FileUtil.copyFileUsingFileStreams(
											file.getAbsoluteFile(), newFile);
									JOptionPane
											.showMessageDialog(
													null,
													"Upgraded jar placed in location: "
															+ pathname,
													"Upgraded to "
															+ file.getName()
																	.substring(
																			0,
																			file.getName()
																					.lastIndexOf(
																							".")),
													JOptionPane.INFORMATION_MESSAGE);
									System.exit(0);

								} else {
									System.exit(0);
								}
							}
						}
					}*/

				} catch (IOException x) {
					JOptionPane.showMessageDialog(null,
							"Invoicing Tool is already running", "",
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}

			}
		});

	}
	
	private static Properties loadProperties(){
		Properties prop = new Properties();
		InputStream input = null;
		input = InvoiceApp.class.getClassLoader().getResourceAsStream(
				PROPERTIES_FILE_NAME);
		if (input == null) {
			JOptionPane.showMessageDialog(null,
					"Sorry, unable to find " + PROPERTIES_FILE_NAME,
					"", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		try {
			prop.load(input);
		} catch (IOException ex) {
			logger.error("Exception while loading config.properties file",ex);
		}
		return prop;
		
	}

}
