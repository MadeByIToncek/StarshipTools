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

package cz.iqlandia.iqplanetarium.buttons;

import cz.iqlandia.iqplanetarium.*;
import cz.iqlandia.iqplanetarium.utils.*;

import java.awt.event.*;
import java.time.*;

import static cz.iqlandia.iqplanetarium.Main.*;

public class ButtonEvent implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean fwd = Boolean.parseBoolean(e.getActionCommand());
		if(fwd) {
			if(index + 1 < getCurrent().size()) {
				index++;
			} else if(!post) {
				switchScene();
			}
		} else {
			if(index - 1 >= 0) {
				index--;
			} else if(post) {
				switchScene();
			}
		}
		pb.setValue(index + 1);
		bar.setTarget(Main.getCurrent().get(index).ratio());
		CountdownEvent ce = Main.getCurrent().get(index);
		if(ce.time() == null) {
			getCurrent().set(index, new CountdownEvent(ce.name(), ce.description(), LocalTime.now(), ce.x(), ce.ratio()));
		}
		command.repaint();
	}
	
	private void switchScene() {
		post = !post;
		if(post) {
			index = 0;
			bar.setCurrent(0);
		} else {
			index = getCurrent().size() - 1;
			bar.setCurrent(1);
		}
		CountdownEvent tmp = getCurrent().get(0);
		getCurrent().set(0, new CountdownEvent(tmp.name(), tmp.description(), LocalTime.now(), tmp.x(), tmp.ratio()));
	}
}
