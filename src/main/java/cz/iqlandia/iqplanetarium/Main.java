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

package cz.iqlandia.iqplanetarium;

import cz.iqlandia.iqplanetarium.buttons.*;
import cz.iqlandia.iqplanetarium.chat.*;
import cz.iqlandia.iqplanetarium.fonts.*;
import cz.iqlandia.iqplanetarium.graphics.*;
import cz.iqlandia.iqplanetarium.obs.*;
import cz.iqlandia.iqplanetarium.utils.State;
import cz.iqlandia.iqplanetarium.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

import static java.lang.Thread.*;

public class Main {
	public static JFrame overlay = new JFrame("Starship Overlay | StarshipTools.jar");
	public static JFrame command = new JFrame("Command | StarshipTools.jar");
	public static boolean post = false;
	public static boolean simple = true;
	public static List<CountdownEvent> prelaunch = calcEvents(false);
	public static List<CountdownEvent> postlaunch = calcEvents(true);
	public static int index = 0;
	public static Instant t0 = LocalDateTime.of(2023, 3, 12, 14, 30, 0).toInstant(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.of(2023, 3, 12, 14, 30, 0)));
	public static JProgressBar pb;
	public static Overlay ovr = new Overlay();
	public static ChatTools tools = new ChatTools("mhJRzQsLZGg");
	public static Timer timer = new Timer();
	public static List<String> questions = new ArrayList<>();
	public static ObsComms obs = new ObsComms();
	
	public static void main(String[] args) {
		// ----------------------------------------- COMMAND WINDOW LOADING -------------------------------------------------
		JButton next = new JButton();
		next.setText("Next event");
		next.setActionCommand("true");
		next.addActionListener(new ButtonEvent());
		JButton prev = new JButton();
		prev.setText("Previous event");
		prev.setActionCommand("false");
		prev.addActionListener(new ButtonEvent());
		pb = new JProgressBar(0, getCurrent().size() + 1);
		pb.setValue(index + 1);
		
		JButton toolButton = new JButton("Select message");
		toolButton.addActionListener(e -> {
			JFrame frame = new JFrame("ChatPicker | StarshipTools.jar");
			
			JComboBox<String> comboBox = new JComboBox<>();
			
			for (int i = 1; i < tools.getMessages().size(); i++) {
				int index = tools.getMessages().size() - i;
				comboBox.addItem(tools.getMessages().get(index).getAuthorName() + " | " + tools.getMessages().get(index).getMessage());
			}
			
			JButton done = new JButton("Submit");
			done.addActionListener(e1 -> {
				frame.setVisible(false);
				questions.add((String) comboBox.getSelectedItem());
			});
			JPanel panel = new JPanel();
			panel.add(comboBox);
			panel.add(done);
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
		});
		
		JButton simpleMode = new JButton("Switch modes");
		simpleMode.addActionListener((a) -> {
			new Thread(() -> {
				try {
					obs.hide();
					sleep(1100);
					simple = !simple;
					sleep(1100);
					obs.show();
				} catch (InterruptedException e) {
					JDialog dialog = new JDialog();
					dialog.add(new JLabel(e.getLocalizedMessage()));
					e.printStackTrace();
				}
			}).start();
		});
		
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL);
		slider.setMaximum(100);
		slider.setMinimum(0);
		slider.addChangeListener(e -> {
			ovr.bar.setTarget(slider.getValue() / 100f);
			System.out.println(slider.getValue() / 100f);
		});
		
		JPanel prevnext = new JPanel(new GridLayout(1, 2));
		prevnext.add(prev);
		prevnext.add(next);
		
		JPanel abt = new JPanel(new GridLayout(1, State.values().length));
		for (State value : State.values()) {
			JButton state = new JButton();
			state.setText(value.name());
			state.setActionCommand(value.name());
			state.addActionListener(new ButtonState());
			state.setBackground(value.getColor());
			if(value == State.RUD) {
				state.setForeground(Color.WHITE);
			}
			abt.add(state);
		}
		
		JPanel t0tweaks = new JPanel(new GridLayout(1, 1));
		JButton t0set = new JButton();
		t0set.setText("Set T0");
		t0set.addActionListener(new ButtonT0());
		t0tweaks.add(t0set);
		
		JPanel act = new JPanel(new GridLayout(1, 2));
		act.add(toolButton);
		act.add(simpleMode);
		
		JPanel fin = new JPanel(new GridLayout(6, 1));
		fin.add(new Title());
		fin.add(prevnext);
		fin.add(act);
		fin.add(abt);
		fin.add(t0tweaks);
		//fin.add(slider);
		fin.add(pb);
		
		command.add(fin);
		Dimension dim = new Dimension(400, 400);
		command.setSize(dim);
		command.setMaximumSize(dim);
		command.setMaximumSize(dim);
		command.setLocation(-1800, 142);
		command.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				overlay.setVisible(false);
				command.setVisible(false);
				System.exit(0);
			}
		});
		command.setVisible(true);
		
		// ----------------------------------------- OVERLAY WINDOW LOADING -------------------------------------------------
		
		overlay.add(ovr);
		Dimension fullhd = new Dimension(1920, 1080 + 37);
		overlay.setSize(fullhd);
		overlay.setMinimumSize(fullhd);
		overlay.setMaximumSize(fullhd);
		overlay.setResizable(false);
		overlay.setLocation(-3830, 50);
		overlay.setVisible(true);
		
		// ----------------------------------------- THREAD SETUP ----------------------------------------------------------
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				overlay.repaint();
			}
		}, 40, 40);
		
		Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
		
		CountdownEvent ev = getCurrent().get(0);
		getCurrent().set(0, new CountdownEvent(ev.name(), ev.description(), LocalTime.now(), ev.x(), ev.ratio()));
		pb.setValue(Main.index + 1);
		ovr.bar.setTarget(getCurrent().get(index).ratio());
	}
	
	public static Font font(FontFamily family, FontVariant variant) {
		if(!new File("./fonts/").exists()) {
			new File("./fonts/").mkdirs();
		}
		
		if(!new File("./fonts/" + family.getFolder()).exists()) {
			new File("./fonts/" + family.getFolder()).mkdirs();
		}
		
		if(!new File("./fonts/" + family.getFolder() + "/" + variant.getFilename() + family.getSuffix()).exists()) {
			try (FileOutputStream fos = new FileOutputStream("./fonts/" + family.getFolder() + "/" + variant.getFilename() + family.getSuffix());
					InputStream stream = new URL(family.getPrefix() + variant.getVariant() + family.getSuffix()).openStream()) {
				stream.transferTo(fos);
				return Font.createFont(0, new File("./fonts/" + family.getFolder() + "/" + variant.getFilename() + family.getSuffix()));
			} catch (IOException | FontFormatException e) {
				JDialog dialog = new JDialog();
				dialog.add(new JLabel(e.getLocalizedMessage()));
				return null;
			}
		} else {
			try {
				return Font.createFont(0, new File("./fonts/" + family.getFolder() + "/" + variant.getFilename() + family.getSuffix()));
			} catch (FontFormatException | IOException e) {
				JDialog dialog = new JDialog();
				dialog.add(new JLabel(e.getLocalizedMessage()));
				return null;
			}
		}
	}
	
	public static List<CountdownEvent> getCurrent() {
		if(post) {
			return postlaunch;
		} else {
			return prelaunch;
		}
	}
	
	public static ArrayList<CountdownEvent> calcEvents(boolean post) {
		ArrayList<CountdownEvent> temp = new ArrayList<>();
		if(post) {
			temp.add(new CountdownEvent("START!", "Šťastnou cestu!", null, 100, 0.03f));
			temp.add(new CountdownEvent("Pitch over", "Raketa začne gravitační oblouk.", null, 300, 0.16f));
			temp.add(new CountdownEvent("Max-Q", "Moment nejvyššího aerodynamického namáhání rakety.", null, 550, 0.29f));
			temp.add(new CountdownEvent("MECO", "B7 vypne svých 33 raptor motorů", null, 700, 0.375f));
			temp.add(new CountdownEvent("Rozdělení stupňů", "B7 zamíří zpět k pevnině, S24 pokračuje dále na orbitu.", null, 850, 0.51f));
			temp.add(new CountdownEvent("RapVac start", "Motory S24 byly zažehnuty", null, 1200, 0.7f));
			temp.add(new CountdownEvent("A co dál?", "A co dále? Nevíme :D", null, 1650, 0.94f));
		} else {
			temp.add(new CountdownEvent("Cesta uzavřena", "Cesta TX4 je uzavřena před OFT-1", null, 95, 0.06f));
			temp.add(new CountdownEvent("Rampa evakuována", "Veškerý personál opustil blízkost rakety", null, 400, 0.27f));
			temp.add(new CountdownEvent("Rekondenzátor", "Reknodenzátor byl zapnut", null, 740, 0.43f));
			temp.add(new CountdownEvent("Tankování", "Raketa se plní tekutým metanem a tekutým kyslíkem", null, 1060, 0.60f));
			temp.add(new CountdownEvent("Chlazení motorů", "Raketa začne posílat do motorů kyslík aby ho připravila na chladné plyny.", null, 1300, 0.77f));
			temp.add(new CountdownEvent("Zážeh!", "Motory byly zažehnuty", null, 1593, 0.89f));
			temp.add(new CountdownEvent("START!", "Šťastnou cestu!", null, 1717, 0.97f));
		}
		return temp;
	}
	
}

