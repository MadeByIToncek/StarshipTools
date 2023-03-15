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

public class Overlay extends JPanel {
	public State state = State.NOMINAL;
	public Color iqPrimary = new Color(0, 163, 224);
	Color iqSecondary = new Color(203, 239, 255);
	public AnimatedInteger bar = new AnimatedInteger(0, 5F);
	int maxlenght = 1728;
	public String locktime = "";
	
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
		// ====================  Background  ====================
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
		
		
		//Rectangles
		g.setColor(iqPrimary);
		//Name
		g.fillRect(76, 855, 400, 55);
		//Bar
		g.fillRect(76, 913, 1768, 127);
		//Camera
		drawDashedSquare(g, 76, 47, 501, 286);
		//GO/ABORT/HOLD/RUD info
		if(state != State.NOMINAL) {
			g.fillRect(state.getBase(), 194, state.getWidth(), 57);
		}
		
		
		// ====================  Texts  ====================
		//Setting up font metrics
		FontMetrics fm;
		int width;
		//T0
		g.setColor(Color.WHITE);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(47F));
		fm = getFontMetrics(g.getFont());
		String tim = locktime;
		if(state == State.GO || state == State.NOMINAL) {
			tim = getT0();
		}
		width = fm.stringWidth(tim);
		
		
		//T0
//		g.fillRect(1555, 138, 306, 54);
		// --> DRAWING TIME BOX HERE <--
		g.setColor(iqPrimary);
		g.fillRect(1851 - width, 138, width + 10, 54);
		g.setColor(Color.WHITE);
		
		g.drawString(tim, 1855 - width, 181);
		
		//Time
		
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(40F));
		fm = getFontMetrics(g.getFont());
		LocalTime bocatime = LocalTime.now().atOffset(ZoneOffset.ofHours(-5)).toLocalTime();
		LocalTime turtime = LocalTime.now();
		
		String times = String.format("%02d:%02d:%02d TX // %02d:%02d:%02d CZ", bocatime.getHour(), bocatime.getMinute(), bocatime.getSecond(), turtime.getHour(), turtime.getMinute(), turtime.getSecond());
		width = fm.stringWidth(times);
		
		// --> DRAWING TIME BOX HERE <--
		g.setColor(iqPrimary);
		g.fillRect(1851 - width, 78, width + 10, 58);
		g.setColor(Color.WHITE);
		
		g.drawString(times, 1860 - width, 123);
		
		// Starship OFT - 1
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(44F));
		g.drawString("Starship OFT - 1", 94, 895);
		
		//State
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(48F));
		if(state != State.NOMINAL) {
			g.drawString(state.name(), state.getTxpos(), 242);
		}
		
		//Bar
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(28F));
		for (CountdownEvent event : getCurrent()) {
			if(event.ratio() > bar.getCurrent()) {
				g.setColor(new Color(58, 65, 68));
			} else if(event.ratio() == bar.getCurrent()) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(new Color(175, 222, 246));
			}
			g.drawChars(event.name().toCharArray(), 0, event.name().toCharArray().length, event.x(), 1015);
			if(event.time() != null) {
				g.setColor(Color.WHITE);
				g.drawString(String.format("%02d:%02d", event.time().getHour(), event.time().getMinute()), (int) (event.ratio() * maxlenght) + 60, 948);
			}
		}
		
		// ====================  Progressbar  ====================
		g.setColor(new Color(58, 65, 68));
		g.fillRect(95, 963, 1730, 8);
		g.setColor(iqSecondary);
//		if(targetlenght - barlenght > 0.0005) {
//			barlenght = ((targetlenght - barlenght) / 5) + barlenght;
//		} else {
//			barlenght = targetlenght;
//		}
		
		g.fillRect(96, 964, (int) Math.round(bar.step() * maxlenght), 6);
		// ====================  Rendering  ====================
		g.dispose();
	}
	
	public void drawDashedSquare(Graphics g, int x1, int y1, int x2, int y2) {
		// Create a copy of the Graphics instance
		Graphics2D g2d = (Graphics2D) g.create();
		
		// Set the stroke of the copy, not the original
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		g2d.setStroke(dashed);
		
		// Draw to the copy
		g2d.drawLine(x1, y1, x1, y2);
		g2d.drawLine(x1, y2, x2, y2);
		g2d.drawLine(x2, y2, x2, y1);
		g2d.drawLine(x2, y1, x1, y1);
		
		// Get rid of the copy
		g2d.dispose();
	}
	
}
