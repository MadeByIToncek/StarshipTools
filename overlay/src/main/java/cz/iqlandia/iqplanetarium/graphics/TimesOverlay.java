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

package cz.iqlandia.iqplanetarium.graphics;

import cz.iqlandia.iqplanetarium.*;
import cz.iqlandia.iqplanetarium.fonts.*;
import cz.iqlandia.iqplanetarium.utils.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;

import static cz.iqlandia.iqplanetarium.Main.*;

public class TimesOverlay extends JPanel {
	public static String getT0() {
		Instant now = Instant.now();
		long totalSecs = (Main.t0.getEpochSecond() - now.getEpochSecond()) * -1;
		String symbol = "+";
		if(Math.abs(totalSecs) != totalSecs) {
			symbol = "-";
			totalSecs = Math.abs(totalSecs);
		}
		
		long hours = totalSecs / 3600;
		long minutes = (totalSecs % 3600) / 60;
		long seconds = totalSecs % 60;
		
		return String.format("T%s %02d:%02d:%02d", symbol, hours, minutes, seconds);
	}
	
	@Override
	public void paint(Graphics g) {
		// ======================= BG =======================
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 200);
		
		// ======================= T0 =======================
		g.setColor(Color.WHITE);
		g.setFont(Main.font(FontFamily.VCR, FontVariant.REGULAR).deriveFont(47F).deriveFont(Font.BOLD));
		FontMetrics fm = getFontMetrics(g.getFont());
		String tim = locktime;
		if(state == State.GO || state == State.NOMINAL) {
			tim = getT0();
		}
		int width = fm.stringWidth(tim);
		
		g.setColor(iqPrimary);
		g.fillRect(765 - width, 75, width + 10, 54);
		g.setColor(Color.WHITE);
		
		g.drawString(tim, 770 - width, 120);
		
		// ======================= TIME =======================
		
		g.setFont(Main.font(FontFamily.VCR, FontVariant.REGULAR).deriveFont(40F).deriveFont(Font.BOLD));
		fm = getFontMetrics(g.getFont());
		LocalTime time = LocalTime.now();
		LocalTime bc = LocalTime.now().minusHours(5);
		
		String times = String.format("%02d:%02d:%02d TX // %02d:%02d:%02d CZ", bc.getHour(), bc.getMinute(), bc.getSecond(), time.getHour(), time.getMinute(), time.getSecond());
		width = fm.stringWidth(times);
		
		// --> DRAWING TIME BOX HERE <--
		g.setColor(iqPrimary);
		g.fillRect(765 - width, 10, width + 10, 54);
		g.setColor(Color.WHITE);
		
		g.drawString(times, 770 - width, 55);
		
		// ======================= GO =======================
		g.setColor(iqPrimary);
		g.setFont(Main.font(FontFamily.VCR, FontVariant.REGULAR).deriveFont(47F).deriveFont(Font.BOLD));
		fm = getFontMetrics(g.getFont());
		width = fm.stringWidth(state.name());
		
		if(state != State.NOMINAL) {
			g.fillRect(765 - width, 135, width + 10, 54);
		}
		
		//State
		g.setColor(Color.WHITE);
		if(state != State.NOMINAL) {
			g.drawString(state.name(), 770 - width, 180);
		}
		
		g.dispose();
	}
}
