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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

import org.japura.gui.CheckComboBox;
import org.japura.gui.model.ListCheckModel;
import org.japura.gui.renderer.CheckListRenderer;

import com.invoice.frame.MainFrame;
import com.invoice.listener.DisableListener;
import com.invoice.swingutil.JComboCheckBoxDis;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class DisableBookingPanel extends JPanel {

	private static final long serialVersionUID = 4554971236091450016L;
	private JButton enableBtn;
	private JButton disBtn;
	private JButton okBtn;
	private JComboBox<String> customFromWeek;
	private JComboBox<String> customToWeek;
	private JDatePickerImpl pmFromDatePicker;
	private JDatePickerImpl pmToDatePicker;
	private BufferedImage image;
	private DisableListener disableListener;
	private FormPanel formPanel;
	private CheckComboBox ccb;
	private ListCheckModel listModel;

	DateFormatter df = DateFormatter.getInstance();

	public DisableBookingPanel(FormPanel formPanel) {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		dim.height = 800;
		setPreferredSize(dim);
		try {
			image = ImageIO.read(getClass().getResource("/Images/panel.jpg"));
		} catch (IOException ex) {

		}
		this.formPanel = formPanel;
		enableBtn = new JButton("Enable");
		disBtn = new JButton("Disable");
		okBtn = new JButton("OK");
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
			private static final long serialVersionUID = 6899220924728435652L;

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
		UtilCalendarModel model1 = new UtilCalendarModel();
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model1);
		pmFromDatePicker = new JDatePickerImpl(datePanel1);
		pmFromDatePicker.setPreferredSize(new Dimension(400, 400));

		// To Calendar
		UtilCalendarModel toModel1 = new UtilCalendarModel();
		JDatePanelImpl toDatePanel1 = new JDatePanelImpl(toModel1);
		pmToDatePicker = new JDatePickerImpl(toDatePanel1);
		pmToDatePicker.setPreferredSize(new Dimension(400, 400));

		TitledBorder innerborder = BorderFactory
				.createTitledBorder("");
		Border outerborder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		innerborder.setBorder(new LineBorder(Color.decode("#FFCB05")));

		setBorder(BorderFactory.createCompoundBorder(outerborder, innerborder));
		loadLayout();

		enableBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar fromDate = (Calendar) pmFromDatePicker.getModel()
						.getValue();
				Calendar toDate = (Calendar) pmToDatePicker.getModel()
						.getValue();

				if (disableListener != null) {

					disableListener.customEnable(fromDate, toDate,
							JComboCheckBoxDis.selectedDisList);

				}
			}

		});

		disBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar fromDate = (Calendar) pmFromDatePicker.getModel()
						.getValue();
				Calendar toDate = (Calendar) pmToDatePicker.getModel()
						.getValue();

				List<String> listVal = new ArrayList<String>();
				for (Object obj : listModel.getCheckeds()) {
					listVal.add(obj.toString());
				}

				if (disableListener != null) {

					disableListener.customDisable(fromDate, toDate, listVal);

				}
			}

		});

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date fromDate = df.parseMMMddyyyy(customFromWeek
						.getSelectedItem().toString());
				/*
				 * Calendar cal = Calendar.getInstance(); cal.setTime(date);
				 */
				Date toDate = df.parseMMMddyyyy(customToWeek.getSelectedItem()
						.toString());

				if (disableListener != null) {

					disableListener.getDefaulterList(fromDate, toDate);

				}
			}

		});

	}

	public void loadLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 0.1;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(20, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Get Defaulter's List:"), gc);

		gc.fill = GridBagConstraints.NONE;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(20, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("From Date:"), gc);
		gc.gridx = 0;
		gc.insets = new Insets(20, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		// Set default value
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String currentWeek = df.formatMMMddyyyy(c.getTime());
		setCustomFromWeek(formPanel.getCustomFromWeek());
		customFromWeek.setSelectedItem(currentWeek);
		add(formPanel.getCustomFromWeek(), gc);

		gc.fill = GridBagConstraints.NONE;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(20, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("To Date:"), gc);
		gc.gridx = 0;
		gc.insets = new Insets(20, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;

		setCustomToWeek(formPanel.getCustomToWeek());
		// Set default value
		c.add(Calendar.DAY_OF_WEEK, 6);

		customToWeek.setSelectedItem(df.formatMMMddyyyy(c.getTime()).trim());
		add(customToWeek, gc);

		gc.fill = GridBagConstraints.NONE;
		/* gc.weighty = 0.1; */
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(okBtn, gc);

		gc.gridy++;
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(5, 1));
		sep.setForeground(Color.BLACK);
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(10, 0, 0, 0);

		add(sep, gc);

		gc.fill = GridBagConstraints.NONE;

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("Disable / Enable Time Booking:"), gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("From Date:"), gc);

		gc.gridy++;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_END;
		add(pmFromDatePicker, gc);

		gc.gridy++;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 55);
		gc.anchor = GridBagConstraints.LINE_END;
		add(pmFromDatePicker, gc);

		gc.fill = GridBagConstraints.NONE;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("To Date:"), gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 55);
		gc.anchor = GridBagConstraints.LINE_END;
		add(pmToDatePicker, gc);

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		add(new JLabel("Project / User Name:"), gc);

		gc.gridy++;
		gc.fill = GridBagConstraints.BOTH;
		gc.weighty = 0.03;
		/* JScrollPane listScroller = new JScrollPane(jlist); */
		/* listScroller.setPreferredSize(new Dimension(50, 80)); */
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 55);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(ccb, gc);

		gc.fill = GridBagConstraints.NONE;
		gc.weighty = 0;
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(10, 30, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(enableBtn, gc);
		gc.gridx = 0;
		gc.insets = new Insets(10, 0, 0, 30);
		gc.anchor = GridBagConstraints.LINE_END;
		add(disBtn, gc);

		gc.gridy++;
		gc.weighty = 0.1;
		JSeparator sep1 = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(5, 1));
		sep.setForeground(Color.BLACK);
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(10, 0, 0, 0);

		add(sep1, gc);

		gc.gridy++;
		gc.weighty = 5;
		gc.fill = GridBagConstraints.BOTH;
		JPanel empty = new JPanel();
		empty.setVisible(false);
		add(empty, gc);

	}

	public void setDisableListener(DisableListener disableListener) {

		this.disableListener = disableListener;
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

	public void setCustomFromWeek(JComboBox<String> customFromWeek) {
		this.customFromWeek = customFromWeek;
	}

	public void setCustomToWeek(JComboBox<String> customToWeek) {
		this.customToWeek = customToWeek;
	}

}