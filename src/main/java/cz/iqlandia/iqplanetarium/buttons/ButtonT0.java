/*
 * Copyright (c) 2023 - IToncek
 *
 * All rights to modifying this source code are granted, except for changing licence.
 * Any and all products generated from this source code must be shared with a link
 * to the original creator with clear and well-defined mention of the original creator.
 * This applies to any lower level copies, that are doing approximately the same thing.
 * If you are not sure, if your usage is within these boundaries, please contact the
 * author on their public email address.
 */

package cz.iqlandia.iqplanetarium.buttons;

import cz.iqlandia.iqplanetarium.*;

import javax.swing.*;
import java.awt.event.*;
import java.time.*;

public class ButtonT0 implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread t = new Thread(() -> {
			String path = JOptionPane.showInputDialog("Co aktuálně říkají hodiny (ve formátu -HH:MM:SS nebo HH:MM:SS)");
			if(path.startsWith("-")) {
				int hh = Integer.parseInt(path.substring(1, 3));
				int mm = Integer.parseInt(path.substring(4, 6));
				int ss = Integer.parseInt(path.substring(7, 9)) + 1;
				int hhnow = LocalTime.now().getHour();
				int mmnow = LocalTime.now().getMinute();
				int ssnow = LocalTime.now().getSecond();
				LocalTime tzero = LocalTime.of(hh + hhnow, mm + mmnow, ss + ssnow);
				LocalDateTime tzerodate = tzero.atDate(LocalDate.now());
				Main.t0 = tzerodate.toInstant(ZoneOffset.systemDefault().getRules().getOffset(tzerodate));
			} else {
				int hh = Integer.parseInt(path.substring(0, 2));
				int mm = Integer.parseInt(path.substring(3, 5));
				int ss = Integer.parseInt(path.substring(6, 8));
				int hhnow = LocalTime.now().getHour();
				int mmnow = LocalTime.now().getMinute();
				int ssnow = LocalTime.now().getSecond();
				LocalTime tzero = LocalTime.of(hh + hhnow, mm + mmnow, ss + ssnow);
				LocalDateTime tzerodate = tzero.atDate(LocalDate.now());
				Main.t0 = tzerodate.toInstant(ZoneOffset.systemDefault().getRules().getOffset(tzerodate));
			}
		});
		
		t.start();
	}
}
