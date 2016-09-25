package com.invoice.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

import org.japura.gui.CheckComboBox;
import org.japura.gui.model.ListCheckModel;
import org.japura.gui.renderer.CheckListRenderer;

import com.invoice.event.FormEvent;
import com.invoice.frame.MainFrame;
import com.invoice.listener.FormListener;
import com.invoice.pojo.UserDetailHolder;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class FormPanel extends JPanel {

	private static final long serialVersionUID = 8236995120817547010L;
	private JLabel nameLabel;
	private JTextField nameField;
	@SuppressWarnings("unused")
	private JTextField billField;
	private JButton btnOk;
	private JButton viewBtnOk;
	private JButton viewAllBtn;
	private FormListener formListener;
	private JList<String> jlist;
	private JComboBox<String> comp;
	private JComboBox<String> customFromWeek;
	private JComboBox<String> customToWeek;
	private JCheckBox check;
	private JLabel taxLabel;
	private JTextField taxField;
	private JRadioButton daily;
	private JRadioButton weekly;
	private JRadioButton defaultView;
	private ButtonGroup group;
	private ListCheckModel listModel;
	private JRadioButton daily1;
	private JRadioButton weekly1;
	private JRadioButton defaultView1;
	private ButtonGroup group1;
	private JDatePickerImpl fromDatePicker;
	private JDatePickerImpl toDatePicker;
	private JDatePickerImpl pmFromDatePicker;
	private JDatePickerImpl pmToDatePicker;
	private CheckComboBox ccb;
	private BufferedImage image;
	private static String currentWeek;
	// Date formatter
	DateFormatter df = DateFormatter.getInstance();

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		dim.height = 800;
		setPreferredSize(dim);
		try {
			image = ImageIO.read(getClass().getResource("/Images/panel.jpg"));
		} catch (IOException ex) {

		}

		nameLabel = new JLabel("Name:");
		nameField = new JTextField(10);
		billField = new JTextField(10);
		comp = new JComboBox<String>();
		customFromWeek = new JComboBox<String>();
		customToWeek = new JComboBox<String>();
		btnOk = new JButton("OK");
		btnOk.setFocusable(false);

		viewBtnOk = new JButton("View");
		viewBtnOk.setFocusable(false);
		viewAllBtn = new JButton("View");
		viewAllBtn.setFocusable(false);

		// set mnemonic
		btnOk.setMnemonic(KeyEvent.VK_O);
		nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		nameLabel.setLabelFor(nameField);

		ccb = new CheckComboBox();
		ccb.setTextFor(CheckComboBox.NONE, "* No Item selected *");
		ccb.setTextFor(CheckComboBox.MULTIPLE, "* Multiple Items *");
		ccb.setTextFor(CheckComboBox.ALL, "* All selected *");

		listModel = ccb.getModel();
		if (MainFrame.getDropdwnList() != null) {
			List<Object> userNames = MainFrame.getDropdwnList();
			listModel.addElement("All");
			listModel.addElement("E5091");
			listModel.addElement("E3187");
			listModel.addElement("E3199");
			for (int i = 0; i < userNames.size(); i++) {
				listModel.addElement(userNames.get(i).toString());
			}
		}
		listModel.addCheck("All");

		ccb.setRenderer(new CheckListRenderer() {

			private static final long serialVersionUID = -5193552580501918278L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value,
						index, isSelected, cellHasFocus);

				c.setBackground(isSelected ? Color.red : Color.white);
				c.setForeground(isSelected ? Color.white : Color.black);
				return c;
			}
		});

		// From Calendar
		UtilCalendarModel model = new UtilCalendarModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		fromDatePicker = new JDatePickerImpl(datePanel);
		fromDatePicker.setPreferredSize(new Dimension(400, 400));

		// To Calendar
		UtilCalendarModel toModel = new UtilCalendarModel();
		JDatePanelImpl toDatePanel = new JDatePanelImpl(toModel);
		toDatePicker = new JDatePickerImpl(toDatePanel);
		toDatePicker.setPreferredSize(new Dimension(400, 400));
		DefaultComboBoxModel<String> comModel = new DefaultComboBoxModel<String>();

		// From Calendar
		UtilCalendarModel model1 = new UtilCalendarModel();
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model1);
		pmFromDatePicker = new JDatePickerImpl(datePanel1);
		pmFromDatePicker.setPreferredSize(new Dimension(400, 400));

		// To Calendar
		UtilCalendarModel toModel1 = new UtilCalendarModel();
		JDatePanelImpl toDatePanel1 = new JDatePanelImpl(toModel1);
		pmToDatePicker = new JDatePickerImpl(toDatePanel1);
		pmToDatePicker.setPreferredSize(new Dimension(400, 400));

		// Date Lister
		List<String> dateList = new ArrayList<String>();
		List<String> dateFromList = new ArrayList<String>();
		List<String> dateToList = new ArrayList<String>();
		int[] year = new int[3];
		year[1] = Integer.parseInt(df.getYYYY(new Date()));
		year[0] = year[1] - 1;
		year[2] = year[1] + 1;
		for (int j = 0; j < year.length; j++) {
			Calendar cal1 = new GregorianCalendar(year[j], 0, 1);
			for (int i = 0, inc = 1; i < 366
					&& cal1.get(Calendar.YEAR) == year[j]; i += inc) {
				if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					// this is a sunday
					StringBuilder sb = new StringBuilder();
					cal1.add(Calendar.DAY_OF_MONTH, -6);
					sb.append(df.formatMMMddyyyy(cal1.getTime()));
					// Used for defaulter's from date
					dateFromList.add(df.formatMMMddyyyy(cal1.getTime()));
					sb.append(" - ");
					cal1.add(Calendar.DAY_OF_MONTH, 6);
					// Used for defaulter's To date
					dateToList.add(df.formatMMMddyyyy(cal1.getTime()));
					sb.append(df.formatMMMddyyyy(cal1.getTime()));
					cal1.add(Calendar.DAY_OF_MONTH, 7);
					inc = 7;
					dateList.add(sb.toString());
				} else {
					cal1.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
		}

		for (int i = 0; i < dateList.size(); i++) {
			comModel.addElement(dateList.get(i));
		}

		Calendar c = Calendar.getInstance();

		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String currentWeek = df.formatMMMddyyyy(c.getTime());
		c.add(Calendar.DAY_OF_WEEK, 6);
		currentWeek = currentWeek.trim() + " - "
				+ df.formatMMMddyyyy(c.getTime()).trim();
		FormPanel.setCurrentWeek(currentWeek);
		comModel.setSelectedItem(currentWeek);
		comp.setModel(comModel);
		/* comp.setBorder(BorderFactory.createLineBorder(Color.BLACK)); */
		comp.setEditable(false);
		/* comp.setPreferredSize(new Dimension(500, 20)); */

		DefaultComboBoxModel<String> cusFromModel = new DefaultComboBoxModel<String>();

		for (int i = 0; i < dateFromList.size(); i++) {
			cusFromModel.addElement(dateFromList.get(i));
		}
		customFromWeek.setModel(cusFromModel);
		System.out.println(currentWeek.split("-")[0]);
		customFromWeek.setSelectedItem(currentWeek.split("-")[0]);
		customFromWeek.setEditable(false);

		DefaultComboBoxModel<String> cusToModel = new DefaultComboBoxModel<String>();

		for (int i = 0; i < dateToList.size(); i++) {
			cusToModel.addElement(dateToList.get(i));
		}
		customToWeek.setModel(cusToModel);
		customToWeek.setSelectedItem(currentWeek.split("-")[1]);
		customToWeek.setEditable(false);

		check = new JCheckBox();
		taxLabel = new JLabel("Tax Name:");
		taxField = new JTextField(10);
		taxLabel.setEnabled(false);
		taxField.setEditable(false);
		check.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (check.isSelected())

					taxLabel.setEnabled(check.isSelected());
				taxField.setEditable(check.isSelected());
			}
		});

		daily = new JRadioButton("Daily");
		weekly = new JRadioButton("Weekly");
		defaultView = new JRadioButton("Default");
		daily.setSelected(true);
		daily.setOpaque(false);
		daily.setBorder(null);
		daily.setFocusable(false);
		daily.setActionCommand("daily");
		weekly.setOpaque(false);
		weekly.setBorder(null);
		weekly.setFocusable(false);
		weekly.setActionCommand("weekly");
		defaultView.setOpaque(false);
		defaultView.setBorder(null);
		defaultView.setFocusable(false);
		defaultView.setActionCommand("default");
		group = new ButtonGroup();
		group.add(daily);
		group.add(weekly);
		group.add(defaultView);

		daily1 = new JRadioButton("Daily");
		weekly1 = new JRadioButton("Weekly");
		defaultView1 = new JRadioButton("Default");
		daily1.setSelected(true);
		daily1.setOpaque(false);
		daily1.setBorder(null);
		daily1.setFocusable(false);
		daily1.setActionCommand("daily");
		weekly1.setOpaque(false);
		weekly1.setBorder(null);
		weekly1.setFocusable(false);
		weekly1.setActionCommand("weekly");
		defaultView1.setOpaque(false);
		defaultView1.setBorder(null);
		defaultView1.setFocusable(false);
		defaultView1.setActionCommand("default");
		group1 = new ButtonGroup();
		group1.add(daily1);
		group1.add(weekly1);
		group1.add(defaultView1);

		TitledBorder innerborder = BorderFactory
				.createTitledBorder("  ");
		Border outerborder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		innerborder.setBorder(new LineBorder(Color.decode("#FFCB05")));

		setBorder(BorderFactory.createCompoundBorder(outerborder, innerborder));
		loadLayout();

		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				FormEvent ev = new FormEvent(this, (String) comp
						.getSelectedItem());
				if (formListener != null) {
					formListener.formEventOcuured(ev);
				}

			}

		});

		viewBtnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar fromDate = (Calendar) fromDatePicker.getModel()
						.getValue();
				Calendar toDate = (Calendar) toDatePicker.getModel().getValue();
				String wd = group.getSelection().getActionCommand();
				if (formListener != null) {
					formListener.customViewEvent(fromDate, toDate, wd);
				}
			}

		});
		viewAllBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar fromDate = (Calendar) pmFromDatePicker.getModel()
						.getValue();
				Calendar toDate = (Calendar) pmToDatePicker.getModel()
						.getValue();
				String wd = group1.getSelection().getActionCommand();
				List<String> listVal = new ArrayList<String>();
				for (Object obj : listModel.getCheckeds()) {
					listVal.add(obj.toString());
				}
				if (formListener != null) {
					formListener.customViewEvent(fromDate, toDate, listVal, wd);
				}
			}

		});
	}

	private static void setCurrentWeek(String currentWeek) {

		FormPanel.currentWeek = currentWeek;
	}

	public static String getCurrentWeek() {

		return FormPanel.currentWeek;
	}

	public void loadLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridy = 0;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("Create Timesheet:"), gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(comp, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(btnOk, gc);

		gc.gridy++;
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(5, 1));
		sep.setForeground(Color.BLACK);
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(5, 0, 0, 0);

		gc.weightx = 0.01;
		gc.weighty = 0.01;

		add(sep, gc);

		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 0.1;
		gc.weighty = 0.1;

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("View Timesheet:"), gc);

		gc.weighty = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 0);
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("From Date:"), gc);

		gc.gridy++;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weighty = 0.1;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 55);
		gc.anchor = GridBagConstraints.LINE_END;
		add(fromDatePicker, gc);

		gc.fill = GridBagConstraints.NONE;
		gc.weighty = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 55);
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("To Date:"), gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weighty = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 55);
		gc.anchor = GridBagConstraints.LINE_END;
		add(toDatePicker, gc);

		gc.fill = GridBagConstraints.NONE;
		gc.weighty = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(5, 10, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(daily, gc);
		gc.weighty = 0.1;
		/* gc.gridy++; */
		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 20);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(weekly, gc);

		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 20);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(defaultView, gc);

		gc.weighty = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(5, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(viewBtnOk, gc);

		/* setLayout(new FlowLayout()); */
		if (UserDetailHolder.getRole().equalsIgnoreCase("PM")
				|| UserDetailHolder.getRole().equalsIgnoreCase("PL")) {
			gc.weighty = 0;
			sep = new JSeparator(JSeparator.HORIZONTAL);
			sep.setPreferredSize(new Dimension(5, 1));
			sep.setForeground(Color.BLACK);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.insets = new Insets(5, 0, 0, 0);
			gc.gridy++;
			gc.gridx = 0;
			add(sep, gc);

			gc.fill = GridBagConstraints.NONE;
			gc.weightx = 0.1;
			gc.weighty = 0.1;

			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(5, 0, 0, 0);
			gc.anchor = GridBagConstraints.FIRST_LINE_START;
			add(new JLabel("View All Users Timesheet:"), gc);

			gc.weighty = 0.1;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(5, 0, 0, 0);
			gc.anchor = GridBagConstraints.LAST_LINE_START;
			add(new JLabel("From Date:"), gc);

			gc.gridy++;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weighty = 0.1;
			gc.gridx = 0;
			gc.insets = new Insets(0, 0, 0, 55);
			gc.anchor = GridBagConstraints.LINE_END;
			add(pmFromDatePicker, gc);

			gc.fill = GridBagConstraints.NONE;
			gc.weighty = 0.1;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(10, 0, 0, 0);
			gc.anchor = GridBagConstraints.LAST_LINE_START;
			add(new JLabel("To Date:"), gc);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weighty = 0.1;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(0, 0, 0, 55);
			gc.anchor = GridBagConstraints.LINE_END;
			add(pmToDatePicker, gc);

			gc.fill = GridBagConstraints.BOTH;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(10, 0, 0, 0);
			gc.anchor = GridBagConstraints.LAST_LINE_START;
			add(new JLabel("Project / User Name:"), gc);

			gc.fill = GridBagConstraints.BOTH;
			gc.weighty = 2.5;
			JScrollPane listScroller = new JScrollPane(jlist);
			listScroller.setPreferredSize(new Dimension(100, 80));
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(0, 0, 0, 55);
			gc.anchor = GridBagConstraints.PAGE_START;
			add(ccb, gc);

			gc.fill = GridBagConstraints.NONE;
			gc.weighty = 0.1;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(5, 10, 0, 0);
			gc.anchor = GridBagConstraints.FIRST_LINE_START;
			add(daily1, gc);
			gc.weighty = 0.1;
			/* gc.gridy++; */
			gc.gridx = 0;
			gc.insets = new Insets(5, 0, 0, 20);
			gc.anchor = GridBagConstraints.PAGE_START;
			add(weekly1, gc);

			gc.gridx = 0;
			gc.insets = new Insets(5, 0, 0, 30);
			gc.anchor = GridBagConstraints.FIRST_LINE_END;
			add(defaultView1, gc);

			gc.fill = GridBagConstraints.NONE;
			gc.weighty = 0.1;
			gc.gridy++;
			gc.gridx = 0;
			gc.insets = new Insets(5, 0, 0, 0);
			gc.anchor = GridBagConstraints.PAGE_START;
			add(viewAllBtn, gc);

		} else {
			gc.weighty = 5;
			sep = new JSeparator(JSeparator.HORIZONTAL);
			sep.setPreferredSize(new Dimension(5, 1));
			sep.setForeground(Color.BLACK);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.insets = new Insets(10, 0, 0, 0);
			gc.gridy++;
			gc.gridx = 0;
			add(sep, gc);
		}

	}

	public JComboBox<String> getCustomFromWeek() {
		return customFromWeek;
	}

	public JComboBox<String> getCustomToWeek() {
		return customToWeek;
	}

	public void setFormListener(FormListener formListener) {

		this.formListener = formListener;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // see javadoc
		// for more
		// info on
		// the
		// parameters
	}
}

class AgeCategory {
	private int id;
	private String billDesc;

	public AgeCategory(int id, String billDesc) {
		this.billDesc = billDesc;
		this.id = id;
	}

	public String toString() {
		return billDesc;
	}

	public int getId() {
		return id;
	}

}
