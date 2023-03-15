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

public class AnimatedInteger {
	
	private double current;
	private double target;
	
	public AnimatedInteger(double def) {
		this.current = def;
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	
	public double step() {
		if(target > current) {
			current = ((target - current) / 5d) + current;
		} else {
			current = current - ((target - current) / 5d);
		}
		return current;
	}
}
