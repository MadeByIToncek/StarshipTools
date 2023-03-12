package cz.iqlandia.iqplanetarium;

import javax.swing.*;
import java.awt.*;

class Title extends JPanel {
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
