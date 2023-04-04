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

import javax.swing.*;
import java.awt.*;

import static cz.iqlandia.iqplanetarium.Main.*;

public class StaticOverlay extends JPanel {
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 200);
		
		g.setColor(iqPrimary);
		
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.REGULAR).deriveFont(47F).deriveFont(Font.BOLD));
		FontMetrics fm = getFontMetrics(g.getFont());
		String txt = cfg.getString("prestream");
		int width = fm.stringWidth(txt);
		int height = fm.getHeight();
		
		g.setColor(iqPrimary);
		g.fillRect(10, 10, width + 15, height);
		g.setColor(Color.WHITE);
		
		g.drawString(txt, 15, 55);
		
		g.setColor(iqPrimary);
		txt = cfg.getString("pause");
		width = fm.stringWidth(txt);
		height = fm.getHeight();
		
		g.setColor(iqPrimary);
		g.fillRect(10, 80, width + 15, height);
		g.setColor(Color.WHITE);
		
		g.drawString(txt, 15, 125);
		
		g.dispose();
	}
}
