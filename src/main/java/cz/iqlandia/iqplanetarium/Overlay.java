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

package cz.iqlandia.iqplanetarium;

import javax.swing.*;
import java.awt.*;
import java.time.*;

import static cz.iqlandia.iqplanetarium.Main.*;

class Overlay extends JPanel {
	public State state = State.NOMINAL;
	Color iqPrimary = new Color(0, 163, 224);
	Color iqSecondary = new Color(203, 239, 255);
	float barlenght = 0f;
	float targetlenght = 0f;
	int maxlenght = 1728;
	String locktime = "";
	
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
		//Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
		//Rectangles
		g.setColor(iqPrimary);
		g.fillRect(1555, 202, 306, 58);
		g.fillRect(76, 855, 400, 55);
		g.fillRect(76, 913, 1768, 127);
		drawDashedSquare(g, 76, 47, 501, 286);
		if(state != State.NOMINAL) {
			g.fillRect(state.getBase(), 262, state.getWidth(), 57);
		}
		//Texts
		g.setColor(Color.WHITE);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(47F));
		String tim = locktime;
		if(state == State.GO || state == State.NOMINAL) {
			tim = getT0();
		}
		g.drawChars(tim.toCharArray(), 0, tim.toCharArray().length, 1565, 245);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(44F));
		String title = "Starship OFT - 1";
		g.drawChars(title.toCharArray(), 0, title.toCharArray().length, 94, 895);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(48F));
		if(state != State.NOMINAL) {
			g.drawChars(state.name().toCharArray(), 0, state.name().toCharArray().length, state.getTxpos(), 309);
		}
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(28F));
		for (CountdownEvent event : events) {
			if(event.ratio() > barlenght) {
				g.setColor(new Color(58, 65, 68));
			} else if(event.ratio() == barlenght) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(new Color(175, 222, 246));
			}
			g.drawChars(event.name().toCharArray(), 0, event.name().toCharArray().length, event.x(), 1015);
			if(event.time() != null) {
				g.setColor(Color.WHITE);
				g.drawString(event.time().getHour() + ":" + event.time().getMinute(), (int) (event.ratio() * maxlenght) + 60, 948);
			}
		}
		
		//Progressbar
		g.setColor(new Color(58, 65, 68));
		g.fillRect(95, 963, 1730, 8);
		g.setColor(iqSecondary);
		if(targetlenght - barlenght > 0.0005) {
			barlenght = ((targetlenght - barlenght) / 5) + barlenght;
		} else {
			barlenght = targetlenght;
		}
		
		g.fillRect(96, 964, Math.round(barlenght * maxlenght), 6);
		//Rendering
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
