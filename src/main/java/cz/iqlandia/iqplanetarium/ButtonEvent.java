package cz.iqlandia.iqplanetarium;

import java.awt.event.*;
import java.time.*;

class ButtonEvent implements ActionListener {
	
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
