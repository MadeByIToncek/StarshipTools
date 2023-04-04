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

import javax.swing.*;
import java.awt.*;

import static cz.iqlandia.iqplanetarium.Main.*;

public class CameraOverlay extends JPanel {
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 460, 260);
		
		g.setColor(iqPrimary);
		drawDashedSquare(g, 10, 10, 440, 250);
		
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
