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

import com.github.kusaanko.youtubelivechat.*;
import cz.iqlandia.iqplanetarium.*;
import cz.iqlandia.iqplanetarium.chat.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class ToolButtonListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		String link = JOptionPane.showInputDialog("Vložte adresu vašeho youtube streamu");
		try {
			Main.tools = new ChatTools(link) {
				@Override
				public void onMessage(ChatItem item) {
				
				}
			};
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
