package com.invoice.swingutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * @author Arshad
 *
 */
public class Marquee implements ActionListener {

	private static final int RATE = 12;
	private final Timer timer = new Timer(1000 / RATE, this);
	private final JLabel label;
	private final String s;
	private final int n;
	private int index;

	public Marquee(JLabel label, String s, int n) {
		if (s == null || n < 1) {
			throw new IllegalArgumentException("Null string or n < 1");
		}
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			sb.append(' ');
		}
		this.label = label;
		this.s = sb + s + sb;
		this.n = n;
		label.setFont(new Font("time new roman", Font.PLAIN, 36));
		label.setForeground(Color.WHITE);
		label.setText(sb.toString());
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		index++;
		if (index > s.length() - n) {
			index = 0;
		}
		label.setText(s.substring(index, index + n));
	}
}
