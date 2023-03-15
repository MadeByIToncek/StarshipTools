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

public class ButtonEvent implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean fwd = Boolean.parseBoolean(e.getActionCommand());
		if(fwd) {
			if(Main.index + 1 < Main.events.size()) {
				Main.index++;
			}
		} else {
			if(Main.index - 1 >= 0) {
				Main.index--;
			}
		}
		Main.pb.setValue(Main.index + 1);
		Main.ovr.targetlenght = Main.events.get(Main.index).ratio();
		CountdownEvent ce = Main.events.get(Main.index);
		Main.events.set(Main.index, new CountdownEvent(ce.name(), ce.description(), LocalTime.now(), ce.x(), ce.ratio()));
		Main.command.repaint();
	}
}
