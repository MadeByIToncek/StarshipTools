package cz.iqlandia.iqplanetarium;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

@SuppressWarnings("ALL")
class ButtonState implements ActionListener {
	
	public static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;
		
		float rgb1[] = new float[3];
		float rgb2[] = new float[3];
		
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		
		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;
		
		if(red < 0) {
			red = 0;
		} else if(red > 255) {
			red = 255;
		}
		if(green < 0) {
			green = 0;
		} else if(green > 255) {
			green = 255;
		}
		if(blue < 0) {
			blue = 0;
		} else if(blue > 255) {
			blue = 255;
		}
		
		Color color = null;
		try {
			color = new Color(red, green, blue);
		} catch (IllegalArgumentException exp) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
			exp.printStackTrace();
		}
		return color;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		State prev = Main.ovr.state;
		State state = State.valueOf(e.getActionCommand());
		Main.ovr.state = state;
		if((state != State.NOMINAL) || (state != State.GO)) {
			Main.ovr.locktime = Overlay.getT0();
		}
		;
		float max = 25;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int i = 0;
			
			@Override
			public void run() {
				if(i <= max) {
					Main.ovr.iqPrimary = blend(state.getColor(), prev.getColor(), i / max);
				} else {
					timer.cancel();
					timer.purge();
				}
				i++;
			}
		}, 40L, 40L);
	}
}