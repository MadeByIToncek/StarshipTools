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
	private final double force;
	private double target;
	
	public AnimatedInteger(double def, double force) {
		this.current = def;
		this.force = force;
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	
	public double getCurrent() {
		return current;
	}
	
	public void setCurrent(double i) {
		current = i;
	}
	
	public double step() {
		if(Math.abs(target - current) < 0.0005) {
			current = target;
		} else {
			current = ((target - current) / force) + current;
		}
		return current;
	}
}
