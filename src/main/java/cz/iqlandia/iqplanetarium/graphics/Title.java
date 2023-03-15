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

public class Title extends JPanel {
	@Override
	public void paint(Graphics g) {
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.MEDIUM).deriveFont(20.0F));
		String title = Main.events.get(Main.index).name();
		g.drawChars(title.toCharArray(), 0, title.toCharArray().length, 0, 20);
		g.setFont(Main.font(FontFamily.STOLZL, FontVariant.REGULAR).deriveFont(20.0F));
		String desc = Main.events.get(Main.index).description();
		g.drawChars(desc.toCharArray(), 0, desc.toCharArray().length, 0, 45);
	}
}
