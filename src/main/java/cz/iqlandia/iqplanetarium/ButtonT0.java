package cz.iqlandia.iqplanetarium;

import javax.swing.*;
import java.awt.event.*;
import java.time.*;

class ButtonT0 implements ActionListener {
	
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
