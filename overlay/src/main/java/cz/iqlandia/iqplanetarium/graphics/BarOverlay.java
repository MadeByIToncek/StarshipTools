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

import static cz.iqlandia.iqplanetarium.Main.*;

public class BarOverlay extends JPanel {
	@Override
	public void paint(Graphics g) {
		// ======================= BG =======================
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 200);
		
		// ======================= TITLE =======================
		int yoff = 32;
		int ysize = 95;
		g.setColor(iqPrimary);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(44F));
		
		FontMetrics fm = getFontMetrics(g.getFont());
		int width = fm.stringWidth(cfg.getString("title"));
		int height = fm.getHeight();
		
		g.fillRect(76, 10, width + 20, height + 5);
		g.setColor(Color.WHITE);
		g.drawString(cfg.getString("title"), 86, height);
		
		// ======================= BAR =======================
		g.setColor(iqPrimary);
		g.fillRect(76, 50 + yoff, 1768, ysize);
		
		g.setColor(new Color(58, 65, 68));
		g.fillRect(95, 105, 1730, 8);
		g.setColor(getLight(state));
		g.fillRect(96, 106, (int) Math.round(bar.step() * maxlenght), 6);
		
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.BOLD).deriveFont(28F));
		for (CountdownEvent event : getCurrent()) {
			if(event.ratio() > bar.getCurrent()) {
				g.setColor(new Color(58, 65, 68));
			} else if(event.ratio() == bar.getCurrent()) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(getLight(state));
			}
			g.drawChars(event.name().toCharArray(), 0, event.name().toCharArray().length, event.x(), 157);
		}
		
		g.dispose();
	}
	
	private Color getLight(State state) {
		return switch (state) {
			case NOMINAL -> new Color(175, 222, 246);
			case GO -> new Color(178, 255, 178);
			case HOLD -> new Color(255, 203, 142);
			case ABORT -> new Color(255, 162, 152);
			case RUD -> new Color(194, 194, 194);
		};
	}
}