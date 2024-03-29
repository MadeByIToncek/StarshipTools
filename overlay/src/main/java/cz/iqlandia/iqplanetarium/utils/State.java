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

package cz.iqlandia.iqplanetarium.utils;

import java.awt.*;

public enum State {
	NOMINAL(new Color(0, 163, 224), 0, 0, 0),
	GO(new Color(4, 173, 4), 1764, 97, 1775),
	HOLD(new Color(204, 109, 1), 1715, 146, 1722),
	ABORT(new Color(175, 20, 0), 1676, 185, 1686),
	RUD(new Color(69, 81, 84), 1748, 113, 1755);
	
	private final Color color;
	private final int base;
	private final int width;
	private final int txpos;
	
	State(Color color, int base, int width, int txpos) {
		this.color = color;
		this.base = base;
		this.width = width;
		this.txpos = txpos;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getBase() {
		return base;
	}
	
	public int getTxpos() {
		return txpos;
	}
}
