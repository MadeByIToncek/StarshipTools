package cz.iqlandia.iqplanetarium;

import java.awt.*;

public enum State {
	NOMINAL(new Color(0, 163, 224), 0, 0, 0),
	GO(new Color(4, 173, 4), 1764, 97, 1775),
	HOLD(new Color(255, 145, 0), 1715, 146, 1722),
	ABORT(new Color(175, 20, 0), 1676, 185, 1686),
	RUD(new Color(92, 103, 107), 1748, 113, 1755);
	
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
