package com.invoice.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.invoice.controller.Controller;
import com.invoice.dialog.ChangeInfoDialog;
import com.invoice.dialog.ChangePwdDialog;
import com.invoice.entity.Registration;
import com.invoice.entity.Timesheet;
import com.invoice.entity.WeeksTotal;
import com.invoice.event.FormEvent;
import com.invoice.listener.ButtonListener;
import com.invoice.listener.ChangeInfoListener;
import com.invoice.listener.ChangePWDListener;
import com.invoice.listener.DefaulterListTableListener;
import com.invoice.listener.DisableListener;
import com.invoice.listener.FormListener;
import com.invoice.listener.TimeTableListener;
import com.invoice.listener.WeekAllDailyListener;
import com.invoice.listener.WeekAllTotalDefaultListener;
import com.invoice.listener.WeekDailyListener;
import com.invoice.listener.WeeklyAllTotalListener;
import com.invoice.listener.WeeklyTotalListener;
import com.invoice.main.InvoiceApp;
import com.invoice.panel.DefaulterListTablePanel;
import com.invoice.panel.DisableBookingPanel;
import com.invoice.panel.ErrorMessagePanel;
import com.invoice.panel.FormPanel;
import com.invoice.panel.TablePanel;
import com.invoice.panel.WeekAllDailyPanel;
import com.invoice.panel.WeekAllTotalDefaultPanel;
import com.invoice.panel.WeekAllTotalPanel;
import com.invoice.panel.WeekDailyPanel;
import com.invoice.panel.WeekTotalPanel;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.DefaulterPojo;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.pojo.UserDetailHolder;
import com.invoice.swingutil.PersonalFileFilter;
import com.invoice.swingutil.ToolBar;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class MainFrame extends JFrame {

	private static Logger logger = Logger.getLogger(Controller.class);
	private static final long serialVersionUID = 8378748281928112362L;
	private DateFormatter df = DateFormatter.getInstance();
	private ToolBar toolBar;
	private FormPanel formPanel;
	private DisableBookingPanel disPanel;
	private JFileChooser fileChooser;
	private TablePanel tablePanel;
	private DefaulterListTablePanel defaulterListPanel;
	private WeekDailyPanel weekDailyPanel;
	private WeekTotalPanel weekTotalpanel;
	private WeekAllTotalPanel weekAllTotalpanel;
	private WeekAllTotalDefaultPanel weekAllTotalDefaultpanel;
	private WeekAllDailyPanel weekAllDailyPanel;
	private ChangePwdDialog pwdDialog;
	private ChangeInfoDialog infoDialog;
	private static List<Object> dropdwnList;
	private LoginFrame loginFrame;
	private Controller controller;
	private static Controller control;
	private ErrorMessagePanel erroPanel;
	private String mailDomain = InvoiceApp.prop.getProperty("mail_domain");
	private String scriptFileName = InvoiceApp.prop.getProperty("mail_script_name");
	private String mailScriptPath = InvoiceApp.prop.getProperty("mail_vbs").replace("${user.name}", System.getProperty("user.name"))  + scriptFileName;
	private String excelReport = InvoiceApp.prop.getProperty("excel_reports_path").replace("${user.name}", System.getProperty("user.name"));
	
	public static List<Object> getDropdwnList() {
		if (dropdwnList == null) {
			if (control == null)
				control = new Controller();
			List<Object> userName = control.getUserDropDowns();
			dropdwnList = userName;
		}
		return dropdwnList;
	}

	public static void staticsetDropdwnList(List<Object> dropdwnList) {
		MainFrame.dropdwnList = dropdwnList;
	}

	public MainFrame() {
		super(InvoiceApp.VERSION.substring(0, InvoiceApp.VERSION.lastIndexOf(".")));
		setLayout(new BorderLayout());
		controller = new Controller();
		List<Object> userName = controller.getUserDropDowns();
		if (userName != null) {
			if (dropdwnList == null)
				dropdwnList = userName;
		}
		toolBar = new ToolBar();
		formPanel = new FormPanel();
		disPanel = new DisableBookingPanel(formPanel);
		tablePanel = new TablePanel();
		weekTotalpanel = new WeekTotalPanel();
		weekDailyPanel = new WeekDailyPanel();
		defaulterListPanel = new DefaulterListTablePanel();
		weekAllTotalpanel = new WeekAllTotalPanel();
		weekAllDailyPanel = new WeekAllDailyPanel();
		weekAllTotalDefaultpanel = new WeekAllTotalDefaultPanel();
		pwdDialog = new ChangePwdDialog(this);
		infoDialog = new ChangeInfoDialog(this);
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new PersonalFileFilter());
		erroPanel = new ErrorMessagePanel();

		tablePanel.setWeeklyList(controller.getWeeklyList());
		weekTotalpanel.setTotalWeeklyList(controller.getTotalWeekDate());
		weekAllTotalpanel.setAllTotalWeeklyList(controller.getTotalWeekDate());
		weekDailyPanel.setWeekDailyList(controller.getWeekDailyList());
		weekAllDailyPanel.setWeekAllDailyList(controller.getWeekDailyList());
		weekAllTotalDefaultpanel.setAllTotalWeeklyDefaultList(controller
				.getTotalWeekDefaultDate());
		toolBar.setActionistener(new ButtonListener() {

			@Override
			public void logoutAction() {

				setVisible(false);
				loginFrame.setVisible(true);
				repaint();
				revalidate();

			}
		});
		formPanel.setFormListener(new FormListener() {

			public void formEventOcuured(FormEvent e) {
				// Clear Error Pane
				erroPanel.clearEditorPane();

				tablePanel.setWeeklyList(controller.getWeekList(e.getComp()));
				// Storing the dates from combo box
				tablePanel.setWeekDates(e.getComp());
				/* controller.addEmployee(e); */
				weekTotalpanel.setVisible(false);
				weekDailyPanel.setVisible(false);
				weekAllTotalpanel.setVisible(false);
				weekAllDailyPanel.setVisible(false);
				weekAllTotalDefaultpanel.setVisible(false);
				defaulterListPanel.setVisible(false);
				tablePanel.setVisible(true);
				add(tablePanel, BorderLayout.CENTER);
				tablePanel.refreshWeekModel();
				repaint();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void customViewEvent(Calendar fromDate, Calendar toDate,
					String wd) {
				if (!dateCheck(fromDate, toDate))
					return;
				Object obj = controller.getTotalWeekList(fromDate, toDate, wd);
				if (obj instanceof String) {
					addSingleMsgToErrorPanel((String) obj);
				} else {
					if (wd.equalsIgnoreCase("weekly")) {
						weekTotalpanel
								.setTotalWeeklyList((List<WeeksTotal>) obj);
						weekTotalpanel.refreshTotalWeekModel();
						tablePanel.setVisible(false);
						weekDailyPanel.setVisible(false);
						weekAllTotalpanel.setVisible(false);
						weekAllDailyPanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekTotalpanel.setVisible(true);

						add(weekTotalpanel, BorderLayout.CENTER);
					} else if (wd.equalsIgnoreCase("daily")) {
						weekDailyPanel.setWeekDailyList((List<Timesheet>) obj);
						weekDailyPanel.refreshWeekDailyModel();
						tablePanel.setVisible(false);
						weekTotalpanel.setVisible(false);
						weekAllTotalpanel.setVisible(false);
						weekAllDailyPanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekDailyPanel.setVisible(true);
						add(weekDailyPanel, BorderLayout.CENTER);
					} else {
						weekAllTotalDefaultpanel
								.setAllTotalWeeklyDefaultList((List<WeeksTotal>) obj);
						weekAllTotalDefaultpanel.showMail(false);
						weekAllTotalDefaultpanel.refreshAllTotalWeekModel();
						tablePanel.setVisible(false);
						weekDailyPanel.setVisible(false);
						weekTotalpanel.setVisible(false);
						weekAllDailyPanel.setVisible(false);
						weekAllTotalpanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(true);
						add(weekAllTotalDefaultpanel, BorderLayout.CENTER);

					}

					repaint();
					revalidate();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void customViewEvent(Calendar fromDate, Calendar toDate,
					List<String> listVal, String wd) {

				if (!dateCheck(fromDate, toDate))
					return;
				Object obj = controller.getAllTotalWeekList(fromDate, toDate,
						listVal, wd);
				if (obj instanceof String) {
					addSingleMsgToErrorPanel((String) obj);
				} else {
					if (wd.equalsIgnoreCase("weekly")) {
						weekAllTotalpanel
								.setAllTotalWeeklyList((List<WeeksTotal>) obj);
						weekAllTotalpanel.refreshAllTotalWeekModel();
						tablePanel.setVisible(false);
						weekDailyPanel.setVisible(false);
						weekTotalpanel.setVisible(false);
						weekAllDailyPanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekAllTotalpanel.setVisible(true);
						add(weekAllTotalpanel, BorderLayout.CENTER);
					} else if (wd.equalsIgnoreCase("daily")) {
						weekAllDailyPanel
								.setWeekAllDailyList((List<Timesheet>) obj);
						weekAllDailyPanel.refreshWeekAllDailyModel();
						tablePanel.setVisible(false);
						weekTotalpanel.setVisible(false);
						weekAllTotalpanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(false);
						weekDailyPanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekAllDailyPanel.setVisible(true);

						add(weekAllDailyPanel, BorderLayout.CENTER);
					} else {
						weekAllTotalDefaultpanel
								.setAllTotalWeeklyDefaultList((List<WeeksTotal>) obj);
						weekAllTotalDefaultpanel.refreshAllTotalWeekModel();
						weekAllTotalDefaultpanel.showMail(true);
						tablePanel.setVisible(false);
						weekDailyPanel.setVisible(false);
						weekTotalpanel.setVisible(false);
						weekAllDailyPanel.setVisible(false);
						weekAllTotalpanel.setVisible(false);
						defaulterListPanel.setVisible(false);
						weekAllTotalDefaultpanel.setVisible(true);
						add(weekAllTotalDefaultpanel, BorderLayout.CENTER);

					}

					repaint();
					revalidate();
				}

			}
		});

		weekAllTotalDefaultpanel
				.setTableListener(new WeekAllTotalDefaultListener() {

					@Override
					public String export(List<WeeksTotal> list) {

						return weekListToExcel(list, "All_User");
					}

					@Override
					public void sendMail(List<WeeksTotal> list, String filePath) {

						if (control == null) {
							control = new Controller();
						}
						List<Object> userIDs = control.getAllUserIds();

						StringBuffer to = new StringBuffer();
						if (userIDs != null && !userIDs.isEmpty()) {
							for (Object obj : userIDs) {
								to.append(obj.toString() + mailDomain);
							}
						}
						String[] mailDetails = {
								"cscript",
								mailScriptPath,
								to.toString(),
								"Invocing Tool Timebooking Info - Auto Generated Mail",
								"Hi, <br/> Please see the attached. <br/> Timebooking information made in Invoincg Tool by the individuals ",
								filePath };
						try {
							@SuppressWarnings("unused")
							Process ps = Runtime.getRuntime().exec(mailDetails);
							JOptionPane.showMessageDialog(null,
									"Mail Sent Succesfully!", "",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				});

		weekTotalpanel.setTableListener(new WeeklyTotalListener() {

			@Override
			public String export(List<WeeksTotal> list) {

				return weekListToExcel(list, "weekly");

			}
		});
		tablePanel.setTableListener(new TimeTableListener() {

			@Override
			public void save(List<Timesheet> list) {
				addSingleMsgToErrorPanel(controller.saveWeekList(list,
						tablePanel.getWeekDates()));

			}
		});

		weekDailyPanel.setTableListener(new WeekDailyListener() {

			@Override
			public String export(List<Timesheet> list) {
				return dailyListToExcel(list, "Daily");
			}

		});
		weekAllTotalpanel.setTableListener(new WeeklyAllTotalListener() {

			@Override
			public String export(List<WeeksTotal> list) {
				return weekListToExcel(list, "All_Users_weekly");

			}

			@Override
			public void sendMail(List<WeeksTotal> weekTotalAllList,
					String filePath) {
				if (control == null) {
					control = new Controller();
				}
				List<Object> userIDs = control.getAllUserIds();

				StringBuffer to = new StringBuffer();
				if (userIDs != null && !userIDs.isEmpty()) {
					for (Object obj : userIDs) {
						to.append(obj.toString() + mailDomain + ";");
					}
				}
				String[] mailDetails = {
						"cscript",
						mailScriptPath,
						to.toString(),
						"Invocing Tool Timebooking Info - Auto Generated Mail",
						"Hi, <br/> Please see the attached. <br/> Timebooking information made in Invoicing Tool by the individuals. <br/> Thanks!",
						filePath };
				try {
					@SuppressWarnings("unused")
					Process ps = Runtime.getRuntime().exec(mailDetails);
					JOptionPane.showMessageDialog(null,
							"Mail Sent Succesfully!", "",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

		});

		weekAllDailyPanel.setTableListener(new WeekAllDailyListener() {

			@Override
			public String export(List<Timesheet> list) {
				return dailyListToExcel(list, "All_Users_Daily");
			}

			@Override
			public void sendMail(List<Timesheet> list, String filePath) {
				if (control == null) {
					control = new Controller();
				}
				List<Object> userIDs = control.getAllUserIds();
				StringBuffer to = new StringBuffer();
				if (userIDs != null && !userIDs.isEmpty()) {
					for (Object obj : userIDs) {
						to.append(obj.toString() +mailDomain+ ";");
					}
				}
				System.out.println(to);
				String[] mailDetails = {
						"cscript",
						mailScriptPath,
						to.toString(),
						"Invocing Tool Timebooking Info - Auto Generated Mail",
						" Hi, <br/> Please see the attached. <br/> TimeBooking information done in Invoincg Tool. <br/> <br/> Thanks! ",
						filePath };
				try {
					@SuppressWarnings("unused")
					Process ps = Runtime.getRuntime().exec(mailDetails);
					JOptionPane.showMessageDialog(null,
							"Mail Sent Succesfully!", "",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		});

		disPanel.setDisableListener(new DisableListener() {

			@Override
			public void customDisable(Calendar fromDate, Calendar toDate,
					List<String> listVal) {
				if (!dateCheck(fromDate, toDate))
					return;
				String msg = controller.setDisable(fromDate, toDate, listVal);
				addSingleMsgToErrorPanel(msg);

			}

			@Override
			public void customEnable(Calendar fromDate, Calendar toDate,
					List<String> listVal) {
				if (!dateCheck(fromDate, toDate))
					return;
				String msg = controller.setEnable(fromDate, toDate, listVal);
				addSingleMsgToErrorPanel(msg);

			}

			@Override
			public void getDefaulterList(Date fromDate, Date toDate) {

				Calendar fromCal = Calendar.getInstance();
				fromCal.setTime(fromDate);

				Calendar toCal = Calendar.getInstance();
				toCal.setTime(toDate);

				if (!dateCheck(fromCal, toCal))
					return;

				defaulterListPanel.setDefaulterList(controller
						.getDefaulterList(fromDate, toDate));
				defaulterListPanel.refreshWeekModel();
				tablePanel.setVisible(false);
				weekDailyPanel.setVisible(false);
				weekAllTotalpanel.setVisible(false);
				weekAllDailyPanel.setVisible(false);
				weekTotalpanel.setVisible(false);
				weekAllTotalDefaultpanel.setVisible(false);
				defaulterListPanel.setVisible(true);

				add(defaulterListPanel, BorderLayout.CENTER);

			}

		});

		defaulterListPanel.setTableListener(new DefaulterListTableListener() {

			@SuppressWarnings("deprecation")
			@Override
			public String export(List<DefaulterPojo> list) {
				String Columns[] = { "Name", "P.S NO", "Week", "Project" };
				int rowNo = 0;
				String fileName = System.currentTimeMillis() + "_"
						+ "All_Defulaters" + "_Invoice_Sheet.xls";
				String completePath = excelReport + "\\" + fileName;
				File f = new File(excelReport);
				if (!f.exists()) {
					f.mkdir();
				}
				try {
					FileOutputStream fileOut = new FileOutputStream(
							completePath);
					HSSFWorkbook workbook = new HSSFWorkbook();
					HSSFSheet worksheet = workbook
							.createSheet("Invoice_Defaulter");
					HSSFCellStyle cellStyle = getHeaderStyle(workbook);
					HSSFCellStyle commonStyle = getCommonCellStyle(workbook);

					HSSFRow currentRow = worksheet.createRow((short) 0);
					currentRow.setHeightInPoints((2 * worksheet
							.getDefaultRowHeightInPoints()));
					if (rowNo == 0) {
						int k = 0;
						for (String headers : Columns) {

							HSSFCell cellA1 = currentRow.createCell((short) k);
							cellA1.setCellValue(headers);
							cellA1.setCellStyle(cellStyle);
							k++;
						}
						rowNo++;
					}

					for (int i = 0; i < list.size(); i++) {
						// index from 0,0... cell A1 is cell(0,0)
						HSSFRow row = worksheet.createRow(rowNo);
						rowNo++;

						HSSFCell cell0 = row.createCell((short) 0);
						cell0.setCellStyle(commonStyle);
						cell0.setCellValue(list.get(i).getUserName());

						HSSFCell cell1 = row.createCell((short) 1);
						cell1.setCellStyle(commonStyle);
						cell1.setCellValue(list.get(i).getUserID());

						HSSFCell cell2 = row.createCell((short) 2);
						cell2.setCellStyle(commonStyle);
						cell2.setCellValue(list.get(i).getWeekDate());

						HSSFCell cell3 = row.createCell((short) 3);
						cell3.setCellStyle(commonStyle);
						cell3.setCellValue(list.get(i).getProjectID());

					}
					for (int c = 0; c < Columns.length; c++) {
						worksheet.autoSizeColumn(c);
					}
					workbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
					List<String> msgList = new ArrayList<String>();
					msgList.add("S: File Exported to: " + excelReport);
					msgList.add("S: File name: " + fileName);
					addMsgToErrorPanel(msgList);
					return completePath;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return completePath;
			}

			@Override
			public void sendMail(List<DefaulterPojo> defaulterList,
					String filePath) {
				StringBuffer to = new StringBuffer();
				for (DefaulterPojo defaulterPojo : defaulterList) {
					to.append(defaulterPojo.getUserID() + mailDomain + ";");
				}
				System.out.println(to);

				String[] mailDetails = {
						"cscript",
						mailScriptPath,
						to.toString(),
						"Invocing Tool Defaulter's List - Auto Generated Mail",
						"Hi, <br/> Please see the attached excel sheet for defaulters list and book the timesheet accordingly in Invoicing Tool.<br/> Thanks!",
						filePath };
				try {
					@SuppressWarnings("unused")
					Process ps = Runtime.getRuntime().exec(mailDetails);
					JOptionPane.showMessageDialog(null,
							"Mail Sent Succesfully!", "",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});

		pwdDialog.setChangePWDListener(new ChangePWDListener() {

			@Override
			public void changePWD(String oldPWDValue, String newPWDValue,
					String conPWDValue) {
				List<String> messages = new ArrayList<String>();
				if (oldPWDValue.isEmpty()) {
					messages.add("Old Password field cannot be empty");
				}
				if (newPWDValue.isEmpty()) {
					messages.add("New Password field cannot be empty");
				}
				if (conPWDValue.isEmpty()) {
					messages.add("Confirm Password field cannot be empty");
				}

				if (!newPWDValue.equalsIgnoreCase(conPWDValue)) {
					messages
							.add("New Password and Confirm Password field do not match");
				}

				if (messages.size() > 0) {
					addMsgToErrorPanel(messages);
					return;
				}
				String message = controller.changePassword(oldPWDValue,
						newPWDValue, conPWDValue);
				addSingleMsgToErrorPanel(message);

			}

		});
		infoDialog.setChangeInfoListener(new ChangeInfoListener() {

			@Override
			public void changeInfo(String oldPWDValue, String newPWDValue,
					String conPWDValue) {

			}

			@Override
			public Registration loadValues() {

				return controller.getInfo(UserDetailHolder.getUserID());
			}

			@Override
			public void changePersonalInfo(Registration reg) {
				if (reg.getUserName().isEmpty()) {
					addSingleMsgToErrorPanel("User Name cannot be empty");
				} else {
					loginFrame.setVisible(true);

					String msg = controller.changePersonalInfo(reg);

					setVisible(false);
					JOptionPane.showMessageDialog(null, msg, "",
							JOptionPane.INFORMATION_MESSAGE);

					/*
					 * String systemUserID = System.getProperty("user.home")
					 * .split("\\\\")[2];
					 */

					String[] mailDetails = {
							"cscript",
							mailScriptPath,
							UserDetailHolder.getApproverPSNO() + mailDomain,
							"Invocing Tool - Request for Approval",
							"Hi,<br/><br/>"
									+ UserDetailHolder.getUserName()
									+ ","
									+ " Changed his/her personal information in Invoving Tool, please approve the user. <br/><br/>Thanks!",
							"empty" };
					try {
						@SuppressWarnings("unused")
						Process ps = Runtime.getRuntime().exec(mailDetails);
					} catch (IOException e) {

						e.printStackTrace();
					}
					pwdDialog.setVisible(false);
				}

			}
		});

		setJMenuBar(createMenu());
		/*
		 * btn.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * textPanel.appendText("Hello\n");
		 * 
		 * } });
		 */
		if (UserDetailHolder.getRole().equalsIgnoreCase("PM")
				|| UserDetailHolder.getRole().equalsIgnoreCase("PL")) {
			add(disPanel, BorderLayout.EAST);
			disPanel.setVisible(false);
		}
		add(formPanel, BorderLayout.WEST);
		add(erroPanel, BorderLayout.SOUTH);
		add(toolBar, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);

	}

	@SuppressWarnings("deprecation")
	protected String dailyListToExcel(List<Timesheet> list, String type) {

		String Columns[] = { "Name", "P.S NO", "Date", "Hours Worked",
				"Regular Hours", "Off Hours", "Leave Type", "Status",
				"Backfilled", "LOB", "Remarks", "Reported Hours" };
		int rowNo = 0;
		String fileName = System.currentTimeMillis() + "_" + type
				+ "_Invoice_Sheet.xls";
		String completePath = excelReport + "\\" + fileName;
		File f = new File(excelReport);
		if (!f.exists()) {
			f.mkdir();
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(completePath);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet("Invoice_Daily");

			HSSFCellStyle cellStyle = getHeaderStyle(workbook);
			HSSFCellStyle commonStyle = getCommonCellStyle(workbook);

			HSSFRow currentRow = worksheet.createRow((short) 0);
			currentRow.setHeightInPoints((2 * worksheet
					.getDefaultRowHeightInPoints()));
			if (rowNo == 0) {
				int k = 0;
				for (String headers : Columns) {

					HSSFCell cellA1 = currentRow.createCell((short) k);
					cellA1.setCellValue(headers);
					cellA1.setCellStyle(cellStyle);
					k++;
				}
				rowNo++;
			}

			for (int i = 0; i < list.size(); i++) {
				// index from 0,0... cell A1 is cell(0,0)
				HSSFRow row = worksheet.createRow(rowNo);
				rowNo++;

				HSSFCell cell0 = row.createCell((short) 0);
				if (i != list.size() - 1) {
					cell0.setCellValue(list.get(i).getTsu().getUserName());
					cell0.setCellStyle(commonStyle);
				} else {

					cell0.setCellValue("Total");
					cell0.setCellStyle(cellStyle);

				}
				HSSFCell cell1 = row.createCell((short) 1);
				if (i != list.size() - 1) {
					cell1.setCellValue(Long.parseLong(list.get(i).getTsu()
							.getUserId()));
					cell1.setCellStyle(commonStyle);
				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(cellStyle);
				}

				HSSFCell cell2 = row.createCell((short) 2);
				if (i != list.size() - 1) {
					cell2.setCellValue(df.formatMMMddyyyy(list.get(i)
							.getWeekDate()));
					cell2.setCellStyle(commonStyle);
				} else {
					cell2.setCellValue("");
					cell2.setCellStyle(cellStyle);
				}

				HSSFCell cell3 = row.createCell((short) 3);
				cell3.setCellValue(nullCheck(list.get(i).getHrsWorked()));
				cell3.setCellStyle(commonStyle);

				HSSFCell cell4 = row.createCell((short) 4);
				cell4.setCellValue(nullCheck(list.get(i).getRegHrs()));
				cell4.setCellStyle(commonStyle);

				HSSFCell cell5 = row.createCell((short) 5);
				cell5.setCellValue(nullCheck(list.get(i).getOffHrs()));
				cell5.setCellStyle(commonStyle);

				HSSFCell cell6 = row.createCell((short) 6);

				if (i != list.size() - 1) {
					cell6
							.setCellValue(getLeaveType(list.get(i)
									.getLeaveType()));
					cell6.setCellStyle(commonStyle);
				} else {

					cell6.setCellValue("");

					cell6.setCellStyle(cellStyle);
				}

				HSSFCell cell7 = row.createCell((short) 7);
				if (i != list.size() - 1) {
					cell7.setCellValue(getBillStatus(list.get(i)
							.getBillStatus()));
					cell7.setCellStyle(commonStyle);
				} else {
					cell7.setCellValue("");
					cell7.setCellStyle(cellStyle);
				}

				HSSFCell cell8 = row.createCell((short) 8);
				if (i != list.size() - 1) {
					cell8.setCellValue(checkCovg(list.get(i).isCovergeProv(),
							list.get(i).getLeaveType()));
					cell8.setCellStyle(commonStyle);
				} else {
					cell8.setCellValue("");
					cell8.setCellStyle(cellStyle);
				}

				HSSFCell cell9 = row.createCell((short) 9);
				cell9.setCellValue(nullCheck(list.get(i).getLobHrs()));
				cell9.setCellStyle(commonStyle);

				HSSFCell cell10 = row.createCell((short) 10);
				cell10.setCellValue(list.get(i).getRemarks());
				cell10.setCellStyle(commonStyle);

				HSSFCell cell11 = row.createCell((short) 11);
				cell11.setCellValue(nullCheck(list.get(i).getReportedHrs()));
				cell11.setCellStyle(commonStyle);

				if (i == list.size() - 1) {

					cell3.setCellStyle(cellStyle);
					cell4.setCellStyle(cellStyle);
					cell5.setCellStyle(cellStyle);
					cell9.setCellStyle(cellStyle);
					cell10.setCellStyle(cellStyle);
					cell11.setCellStyle(cellStyle);
				}

				/*
				 * HSSFCell cellD1 = row1.createCell((short) 3);
				 * cellD1.setCellValue(new Date()); cellStyle =
				 * workbook.createCellStyle();
				 * cellStyle.setDataFormat(HSSFDataFormat
				 * .getBuiltinFormat("m/d/yy h:mm"));
				 * cellD1.setCellStyle(cellStyle);
				 */

			}
			for (int c = 0; c < Columns.length; c++) {
				worksheet.autoSizeColumn(c);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			List<String> msgList = new ArrayList<String>();
			msgList.add("S: File Exported to: " + excelReport);
			msgList.add("S: File name: " + fileName);
			addMsgToErrorPanel(msgList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return completePath;
	}

	private HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setWrapText(true);
		return cellStyle;
	}

	@SuppressWarnings("deprecation")
	protected String weekListToExcel(List<WeeksTotal> list, String type) {

		String Columns[] = { "Name", "P.S. NO", "From Date", "To Date",
				"Regular Hours", "Off Hours", "Reported Hours", "Leaves Taken",
				"Backfilled", "Holidays(s)", "LOB", "Remarks" };
		int rowNo = 0;
		File f = new File(excelReport);
		if (!f.exists()) {
			f.mkdir();
		}
		String fileName = System.currentTimeMillis() + "_" + type
				+ "_Invoice_Sheet.xls";
		String completePath = excelReport + "\\" + fileName;
		try {
			FileOutputStream fileOut = new FileOutputStream(completePath);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet("Invoice");

			HSSFCellStyle cellStyle = getHeaderStyle(workbook);
			HSSFCellStyle commonStyle = getCommonCellStyle(workbook);

			HSSFRow currentRow = worksheet.createRow((short) 0);
			currentRow.setHeightInPoints((2 * worksheet
					.getDefaultRowHeightInPoints()));
			if (rowNo == 0) {
				int k = 0;
				for (String headers : Columns) {

					HSSFCell cellA1 = currentRow.createCell((short) k);
					cellA1.setCellValue(headers);
					cellA1.setCellStyle(cellStyle);
					k++;
				}
				rowNo++;
			}

			/*
			 * size = list.size for(Timesheet headers : list) { Method method =
			 * ncpojo.getClass().getMethod("get"+header,new Class[]()); String
			 * fieldValue = (String)Method.invoke(ncpojo,obj);
			 * currentRow.createCell(indexMap .get(i)).setCellValue(fieldValue);
			 * }
			 */

			for (int i = 0; i < list.size(); i++) {
				// index from 0,0... cell A1 is cell(0,0)
				HSSFRow row = worksheet.createRow(rowNo);

				rowNo++;

				HSSFCell cell0 = row.createCell((short) 0);
				if (i != list.size() - 1) {
					cell0.setCellValue(list.get(i).getTimeSheet().getTsu()
							.getUserName());
					cell0.setCellStyle(commonStyle);
				} else {

					cell0.setCellValue("Total");
					cell0.setCellStyle(cellStyle);

				}
				/*
				 * HSSFCellStyle cellStyle = workbook .createCellStyle();
				 * cellStyle = workbook.createCellStyle();
				 * cellStyle.setDataFormat(HSSFDataFormat
				 * .getBuiltinFormat("mm/d/yy")); cell0.setCellStyle(cellStyle);
				 */

				HSSFCell cell1 = row.createCell((short) 1);
				if (i != list.size() - 1) {
					cell1.setCellValue(Long.parseLong(list.get(i)
							.getTimeSheet().getTsu().getUserId()));
					cell1.setCellStyle(commonStyle);
				} else {

					cell1.setCellValue("");
					cell1.setCellStyle(cellStyle);

				}
				HSSFCell cell2 = row.createCell((short) 2);
				if (i != list.size() - 1) {
					cell2.setCellValue(df.formatMMMddyyyy(list.get(i)
							.getWeekStartDate()));
					cell2.setCellStyle(commonStyle);
				} else {

					cell2.setCellValue("");
					cell2.setCellStyle(cellStyle);

				}
				HSSFCell cell3 = row.createCell((short) 3);
				if (i != list.size() - 1) {
					cell3.setCellValue(df.formatMMMddyyyy(list.get(i)
							.getWeekEndDate()));
					cell3.setCellStyle(commonStyle);
				} else {

					cell3.setCellValue("");
					cell3.setCellStyle(cellStyle);

				}
				HSSFCell cell4 = row.createCell((short) 4);
				cell4.setCellValue(nullCheck(list.get(i).getTotalRegHrs()));
				cell4.setCellStyle(commonStyle);

				HSSFCell cell5 = row.createCell((short) 5);
				cell5.setCellValue(nullCheck(list.get(i).getTotalOffHrs()));
				cell5.setCellStyle(commonStyle);

				HSSFCell cell6 = row.createCell((short) 6);
				cell6
						.setCellValue(nullCheck(list.get(i)
								.getTotalReportedHrs()));
				cell6.setCellStyle(commonStyle);

				HSSFCell cell7 = row.createCell((short) 7);
				cell7.setCellValue(nullCheck(list.get(i).getTotalLeaveCount()));
				cell7.setCellStyle(commonStyle);

				HSSFCell cell8 = row.createCell((short) 8);
				cell8.setCellValue(nullCheck(list.get(i).getTotalCovCount()));
				cell8.setCellStyle(commonStyle);

				HSSFCell cell9 = row.createCell((short) 9);
				cell9
						.setCellValue(nullCheck(list.get(i)
								.getTotalHolidayCount()));
				cell9.setCellStyle(commonStyle);

				HSSFCell cell10 = row.createCell((short) 10);
				cell10.setCellValue(nullCheck(list.get(i).getTotalLobHrs()));
				cell10.setCellStyle(commonStyle);

				HSSFCell cell11 = row.createCell((short) 11);
				if (i != list.size() - 1) {
					String remark[] = list.get(i).getConsolidatedRemarks()
							.split("~");
					int remLen = 0;
					if (remark.length == 0 || remark.length < 2) {
						remLen = 2;
					} else {
						remLen = remark.length + 1;
					}
					cell11.setCellValue(list.get(i).getConsolidatedRemarks()
							.replaceAll("\r", "").replaceAll("~", "\n"));

					row.setHeightInPoints((remLen * worksheet
							.getDefaultRowHeightInPoints()));
					cell11.setCellStyle(commonStyle);

				} else {

					cell11.setCellValue("");
					cell11.setCellStyle(cellStyle);

				}

				if (i == list.size() - 1) {

					cell0.setCellStyle(cellStyle);
					cell1.setCellStyle(cellStyle);
					cell2.setCellStyle(cellStyle);
					cell3.setCellStyle(cellStyle);
					cell4.setCellStyle(cellStyle);
					cell5.setCellStyle(cellStyle);
					cell6.setCellStyle(cellStyle);
					cell7.setCellStyle(cellStyle);
					cell8.setCellStyle(cellStyle);
					cell9.setCellStyle(cellStyle);
					cell10.setCellStyle(cellStyle);
					cell11.setCellStyle(cellStyle);
				}

				/*
				 * HSSFCell cellD1 = row1.createCell((short) 3);
				 * cellD1.setCellValue(new Date()); cellStyle =
				 * workbook.createCellStyle();
				 * cellStyle.setDataFormat(HSSFDataFormat
				 * .getBuiltinFormat("m/d/yy h:mm"));
				 * cellD1.setCellStyle(cellStyle);
				 */

			}
			for (int c = 0; c < Columns.length; c++) {
				worksheet.autoSizeColumn(c);
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			List<String> msgList = new ArrayList<String>();
			msgList.add("S: File Exported to: " + excelReport);
			msgList.add("S: File name: " + fileName);
			addMsgToErrorPanel(msgList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return completePath;
	}

	private HSSFCellStyle getCommonCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle commonStyle = workbook.createCellStyle();
		commonStyle.setWrapText(true);
		commonStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		commonStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		commonStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		commonStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return commonStyle;

	}

	public MainFrame(Dimension size, String role, LoginFrame loginFrame) {
		this();
		setSize(size);
		setMinimumSize(new Dimension(1080, 720));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		setVisible(true);
		this.loginFrame = loginFrame;
	}

	private JMenuBar createMenu() {
		JMenuBar jmenuBar = new JMenuBar();

		JMenu jFilemenu = new JMenu("File");
		/*
		 * JMenuItem fileExport = new JMenuItem("File Export"); JMenuItem
		 * fileImport = new JMenuItem("File Import");
		 */
		JMenuItem fileExit = new JMenuItem("Exit");

		/*
		 * fileImport.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * 
		 * if (fileChooser.showOpenDialog(MainFrame.this) ==
		 * JFileChooser.APPROVE_OPTION) { try {
		 * controller.loadFromFile(fileChooser.getSelectedFile()); } catch
		 * (IOException e1) { JOptionPane.showMessageDialog(MainFrame.this,
		 * "Could not Load!", "Error in Load", JOptionPane.ERROR_MESSAGE);
		 * e1.printStackTrace(); } } tablePanel.refreshWeekModel(); } });
		 * fileExport.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * 
		 * if (fileChooser.showOpenDialog(MainFrame.this) ==
		 * JFileChooser.APPROVE_OPTION) { try {
		 * controller.saveToFile(fileChooser.getSelectedFile()); } catch
		 * (IOException e1) { JOptionPane.showMessageDialog(MainFrame.this,
		 * "Could not Save!", "Error in Save", JOptionPane.ERROR_MESSAGE);
		 * e1.printStackTrace(); } }
		 * 
		 * } });
		 * 
		 * jFilemenu.add(fileExport); jFilemenu.add(fileImport);
		 */
		jFilemenu.addSeparator();
		jFilemenu.add(fileExit);

		// set the Mnemonic for exit
		jFilemenu.setMnemonic(KeyEvent.VK_F);
		fileExit.setMnemonic(KeyEvent.VK_X);
		fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		/*
		 * fileImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
		 * ActionEvent.CTRL_MASK));
		 */

		JMenu jWinmenu = new JMenu("Window");
		JMenuItem changePWD = new JMenuItem("Change Password");
		JMenuItem changeInfo = new JMenuItem("Edit Personal Info");
		JMenu jShowmenu = new JMenu("Show");
		/*
		 * JMenuItem prefMenu = new JMenuItem("Preferences");
		 * 
		 * prefMenu.addActionListener(new ActionListener() {
		 * 
		 * public void actionPerformed(ActionEvent e) {
		 * 
		 * prefDialog.setVisible(true);
		 * 
		 * } });
		 */

		JCheckBoxMenuItem fileShow = new JCheckBoxMenuItem("My Timesheet");
		fileShow.setSelected(true);

		fileShow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				formPanel.setVisible(menuItem.isSelected());

			}
		});

		JCheckBoxMenuItem disShow = new JCheckBoxMenuItem("Utilities Panel");
		disShow.setSelected(false);
		disShow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				disPanel.setVisible(menuItem.isSelected());
			}
		});

		fileExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this,
						"Do You Want to Exit?", "Confirm Exit",
						JOptionPane.OK_CANCEL_OPTION);
				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}

			}
		});

		changePWD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pwdDialog.setVisible(true);
			}
		});

		changeInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				infoDialog.setVisible(true);
			}
		});

		jShowmenu.add(fileShow);
		if (UserDetailHolder.getRole().equalsIgnoreCase("PM")
				|| UserDetailHolder.getRole().equalsIgnoreCase("PL"))
			jShowmenu.add(disShow);
		/* jShowmenu.add(prefMenu); */
		jWinmenu.add(jShowmenu);
		jWinmenu.add(changePWD);
		jWinmenu.add(changeInfo);
		jmenuBar.add(jFilemenu);
		jmenuBar.add(jWinmenu);
		return jmenuBar;

	}

	protected void addMsgToErrorPanel(List<String> messages) {

		// clearing out existing text in error message panel
		if (erroPanel.getText() != "") {
			erroPanel.clearEditorPane();
		}

		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).toLowerCase().startsWith("s:")) {
				String msg = messages.get(i).substring(2,
						messages.get(i).length());
				erroPanel
						.addMessage(new String(
								"<html><font color='Green' size=5> Success&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
										+ msg.trim() + "</font></html>"));
				logger.info(msg);
			} else {
				erroPanel
						.addMessage(new String(
								"<html><font color='red' size=5> Error&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
										+ messages.get(i).trim()
										+ "</font></html>"));
				logger.info(messages.get(i));
			}
		}
		// clearing out existing list
		messages.clear();
	}

	protected void addSingleMsgToErrorPanel(String messages) {

		// clearing out existing text in error message panel
		if (erroPanel.getText() != "") {
			erroPanel.clearEditorPane();
		}

		if (messages.toLowerCase().startsWith("s:")) {
			String msg = messages.substring(2, messages.length());
			erroPanel
					.addMessage(new String(
							"<html><font color='Green' size=5> Success&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ msg.trim() + "</font></html>"));
		} else {
			erroPanel
					.addMessage(new String(
							"<html><font color='red' size=5> Error&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ messages.trim() + "</font></html>"));
		}
	}

	public boolean dateCheck(Calendar fromDate, Calendar toDate) {
		erroPanel.clearEditorPane();
		if (fromDate == null && toDate == null) {
			addSingleMsgToErrorPanel("\"From Date\" and \"To Date\" cannot be empty");
			return false;
		} else if (fromDate == null) {
			addSingleMsgToErrorPanel(" \"From Date\" cannot be empty");
			return false;
		} else if (toDate == null) {
			addSingleMsgToErrorPanel(" \"To Date\" cannot be empty");
			return false;
		} else if (toDate.compareTo(fromDate) < 0) {
			addSingleMsgToErrorPanel(" \"To Date\" cannot be lesser than \"From Date\"");
			return false;
		}
		return true;
	}

	private Float nullCheck(Float value) {

		if (value == null)
			return (float) 0;
		return value;
	}

	private Integer nullCheck(Integer value) {

		if (value == null)
			return (Integer) 0;
		return value;
	}

	private String checkCovg(boolean covergeProv,
			LeaveTypeCategory leaveTypeCategory) {
		if (leaveTypeCategory.equals(LeaveTypeCategory.EMPTY)
				|| leaveTypeCategory.equals(LeaveTypeCategory.Holiday))
			return "NA";

		if (covergeProv)
			return "Yes";
		else
			return "No";
	}

	private String getBillStatus(BillCategory billStatus) {

		if (billStatus == null)
			return "NA";

		if (billStatus.equals(BillCategory.Billable))
			return "Billed";

		if (billStatus.equals(BillCategory.Buffer))
			return "Buffer";

		return "";
	}

	private String getLeaveType(LeaveTypeCategory leaveType) {

		if (leaveType == null)
			return "NA";

		if (leaveType.equals(LeaveTypeCategory.CompOff))
			return "Comp Off";

		if (leaveType.equals(LeaveTypeCategory.EMPTY))
			return "NA";

		if (leaveType.equals(LeaveTypeCategory.Holiday))
			return "L&T Holiday";

		if (leaveType.equals(LeaveTypeCategory.Planned))
			return "Planned";

		if (leaveType.equals(LeaveTypeCategory.UnPlanned))
			return "Un Planned";

		if (leaveType.equals(LeaveTypeCategory.SplDay))
			return "Special Day Off";

		return "NA";
	}

}
