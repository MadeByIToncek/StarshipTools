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
import cz.iqlandia.iqplanetarium.fonts.*;
import cz.iqlandia.iqplanetarium.graphics.*;
import cz.iqlandia.iqplanetarium.obs.*;
import cz.iqlandia.iqplanetarium.utils.*;
import org.apache.commons.configuration2.ex.*;
import org.jetbrains.annotations.NotNull;
import org.json.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class Main {
	public static JFrame command;
	public static String locktime = "";
	public static Color iqPrimary = new Color(0, 163, 224);
	public static State state = State.NOMINAL;
	public static boolean post = false;
	public static boolean simple = true;
	public static boolean cam = true;
	public static int maxlenght = 1728;
	public static List<CountdownEvent> prelaunch;
	public static List<CountdownEvent> postlaunch;
	public static int index = 0;
	public static Instant t0;
	public static JProgressBar pb;
	public static TreeMap<PanelMeta, JPanel> panels;
	public static List<JFrame> frames;
	// TODO: public static ChatTools tools;
	// TODO: public static int chatIndex = -1;
	public static Timer timer;
	public static List<String> questions;
	public static ObsComms obs;
	public static AnimatedInteger bar = new AnimatedInteger(0, 5F);
	public static JSONObject cfg;
	// TODO: public static AppCom appcom;
	
	public static void main(String[] args) throws ConfigurationException, IOException {
		JFrame f = new JFrame("Loading | StarshipTools.jar");
		f.setLocation(-1800, 20);
		JProgressBar pba = new JProgressBar(JProgressBar.HORIZONTAL);
		pba.setValue(0);
		pba.setMaximum(11);
		pba.addChangeListener((c) -> System.out.println(pba.getString()));
		pba.setStringPainted(true);
		pba.setString("Booting");
		f.add(pba);
		f.setSize(400, 100);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				f.setVisible(false);
				shutdown();
				System.exit(0);
			}
		});
		
		
		f.setVisible(true);
		
		//Assigning static variables
		command = new JFrame("Command | StarshipTools.jar");
		pba.setString("Windows initialized | Loading events");
		pba.setValue(pba.getValue() + 1);
		prelaunch = calcEvents(false);
		postlaunch = calcEvents(true);
		pba.setString("Events initialized | Loading cfg");
		pba.setValue(pba.getValue() + 1);
		// TODO: tools = new ChatTools("mhJRzQsLZGg");
		if(!new File("./config/cfg.json").exists()) {
			createCFG();
		}
		cfg = loadCFG();
		pba.setString("Events initialized | Loading T0");
		pba.setValue(pba.getValue() + 1);
		JSONObject time = cfg.getJSONObject("t0");
		t0 = LocalDateTime.of(time.getInt("year"), time.getInt("month"), time.getInt("day"), time.getInt("hour"), time.getInt("minute"), time.getInt("second")).toInstant(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.of(2023, 3, 12, 14, 30, 0)));
		pba.setString("T0 set | Loading overlay");
		pba.setValue(pba.getValue() + 1);
		panels = new TreeMap<>();
		panels.put(new PanelMeta(800, 200, 5, true, "Times Overlay | StarshipTools.jar",1), new TimesOverlay());
//		panels.put(new PanelMeta(1920, 200, 25, true, "Bar Overlay | StarshipTools.jar",2), new BarOverlay());
		panels.put(new PanelMeta(465, 260, 1, false, "Camera Overlay | StarshipTools.jar",0), new CameraOverlay());
		//panels.put(new PanelMeta(1920, 200, 1, false, "Static Overlay | StarshipTools.jar",3), new StaticOverlay());
		pba.setString("Overlay initialized | Loading timer");
		pba.setValue(pba.getValue() + 1);
		timer = new Timer();
		pba.setString("Timer initialized | Loading OBS and AppCom");
		pba.setValue(pba.getValue() + 1);
		// TODO: questions = new ArrayList<>();
		// TODO: obs = new ObsComms();
		// TODO: appcom = new AppCom();
		pba.setString("OBS and AppCom initialized | Constructing command window");
		pba.setValue(pba.getValue() + 1);
		
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

//			for (int i = 1; i < tools.getMessages().size(); i++) {
//				int index = tools.getMessages().size() - i;
//				comboBox.addItem(tools.getMessages().get(index).getAuthorName() + " | " + tools.getMessages().get(index).getMessage());
//			}
			
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
		simpleMode.addActionListener((a) -> new Thread(() -> {
			simple = !simple;
			if(simple) {
				//obs.show("Bar");
			} else {
				//obs.hide("Bar");
			}
		}).start());
		
		JButton camera = new JButton("Cam/Mic toggle");
		camera.addActionListener((a) -> new Thread(() -> {
			cam = !cam;
			if(cam) {
				obs.show("SelfieCam");
				obs.show("OutsideCam");
				obs.show("CameraBox");
				obs.show("mic");
			} else {
				obs.hide("SelfieCam");
				obs.hide("OutsideCam");
				obs.hide("CameraBox");
				obs.hide("mic");
			}
		}).start());
		
		JButton updateLink = new JButton("Update stream");
		updateLink.addActionListener((a) -> new Thread(() -> {
			String path = JOptionPane.showInputDialog("Insert stream url on YouTube:");
			//obs.updateStream(path);
		}).start());

//		JSlider slider = new JSlider(JSlider.HORIZONTAL);
//		slider.setMaximum(100);
//		slider.setMinimum(0);
//		slider.addChangeListener(e -> {
//			ovr.bar.setTarget(slider.getValue() / 100f);
//			System.out.println(slider.getValue() / 100f);
//		});
		
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
//		act.add(toolButton);
		act.add(simpleMode);
		act.add(updateLink);
		act.add(camera);
		
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
		if(Objects.equals(System.getenv("DEV"), "true")) {
			command.setLocation(-1800, 177);
		}
		command.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				shutdown();
			}
		});

		
		pba.setString("Command window set up | Constructing overlay window");
		pba.setValue(8);
		
		// ----------------------------------------- OVERLAY WINDOWS LOADING -------------------------------------------------
		frames = new ArrayList<>();
		int yoff = 20+command.getHeight();
		command.setLocation(20, 20);
		for (Map.Entry<PanelMeta, JPanel> panel : panels.entrySet()) {
			JFrame overlay = new JFrame(panel.getKey().name());
			overlay.add(panel.getValue());
			Dimension fullhd = new Dimension(panel.getKey().x(), panel.getKey().y() + 37);
			overlay.setSize(fullhd);
			overlay.setLocation(20, yoff);
			overlay.setMinimumSize(fullhd);
			overlay.setMaximumSize(fullhd);
			overlay.setResizable(false);
//			if(Objects.equals(System.getenv("DEV"), "true")) {
//				overlay.setLocation(-3830, 50);
//			}
			overlay.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent) {
					shutdown();
				}
			});
			frames.add(overlay);
			overlay.setVisible(true);
			yoff = yoff + panel.getKey().y() + 40;
			if(panel.getKey().refresh()) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						overlay.repaint();
					}
				}, 1000 / panel.getKey().fps(), 1000 / panel.getKey().fps());
				
			}
		}
		command.setVisible(true);

		pba.setString("Overlay window set up | Preparing timer");
		pba.setValue(9);
		
		// ----------------------------------------- THREAD SETUP ----------------------------------------------------------
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));
		pba.setString("Timer set up | Final tweaks");
		pba.setValue(10);
		
		CountdownEvent ev = getCurrent().get(0);
		getCurrent().set(0, new CountdownEvent(ev.name(), ev.description(), LocalTime.now(), ev.x(), ev.ratio()));
		pb.setValue(Main.index + 1);
		bar.setTarget(getCurrent().get(index).ratio());
		pba.setString("Ready!");
		pba.setValue(11);
		// ----------------------------------------- SHOWING WINDOWS -----------------------------------------------------
		f.setVisible(false);
	}
	
	private static JSONObject loadCFG() {
		StringBuilder sb = new StringBuilder();
		try (Scanner sc = new Scanner(new File("./config/cfg.json"))) {
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return new JSONObject(sb.toString());
	}
	
	private static void createCFG() {
		try {
			new File("./config/cfg.json").createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		JSONObject cfg = new JSONObject();
		cfg.put("show-time", false);
		cfg.put("title", "Starship OFT - 1");
		cfg.put("prestream", "Přenos začne v T- 00:30:00");
		cfg.put("pause", "Přenos pozastaven, hned budeme zpět");
		JSONObject t0 = new JSONObject();
		t0.put("year", 2023);
		t0.put("month", 11);
		t0.put("day", 18);
		t0.put("hour", 14);
		t0.put("minute", 0);
		t0.put("second", 0);
		cfg.put("t0", t0);
		
		try (FileWriter fw = new FileWriter("./config/cfg.json")) {
			fw.write(cfg.toString(4));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
				return Font.getFont("Arial");
			}
		} else {
			try {
				return Font.createFont(0, new File("./fonts/" + family.getFolder() + "/" + variant.getFilename() + family.getSuffix()));
			} catch (FontFormatException | IOException e) {
				JDialog dialog = new JDialog();
				dialog.add(new JLabel(e.getLocalizedMessage()));
				return Font.getFont("Arial");
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
		File read;
		if(post) {
			read = new File("./config/postlaunch.json");
			if(!read.exists()) {
				ArrayList<CountdownEvent> temp = getCountdownEvents();

				if(!new File("./config/").exists()) {
					new File("./config/").mkdirs();
				}
				
				try (FileWriter fw = new FileWriter(read)) {
					JSONArray arr = new JSONArray();
					for (CountdownEvent e : temp) {
						JSONObject o = new JSONObject();
						o.put("name", e.name());
						o.put("description", e.description());
						o.put("time", e.time());
						o.put("x", e.x());
						o.put("ratio", e.ratio());
						arr.put(o);
					}
					fw.write(arr.toString(4));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			read = new File("./config/prelaunch.json");
			if(!read.exists()) {
				ArrayList<CountdownEvent> temp = getEvents();

				if(!new File("./config/").exists()) {
					new File("./config/").mkdirs();
				}
				
				try (FileWriter fw = new FileWriter(read)) {
					JSONArray arr = new JSONArray();
					for (CountdownEvent e : temp) {
						JSONObject o = new JSONObject();
						o.put("name", e.name());
						o.put("description", e.description());
						o.put("x", e.x());
						o.put("ratio", e.ratio());
						arr.put(o);
					}
					fw.write(arr.toString(4));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		JSONArray array;
		try (Scanner sc = new Scanner(read)) {
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
			array = new JSONArray(sb.toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		ArrayList<CountdownEvent> temp = new ArrayList<>();
		
		for (Object o : array) {
			JSONObject obj = (JSONObject) o;
			CountdownEvent e = new CountdownEvent(obj.getString("name"), obj.getString("description"), null, obj.getInt("x"), obj.getFloat("ratio"));
			temp.add(e);
		}
		
		
		return temp;
	}

	@NotNull
	private static ArrayList<CountdownEvent> getEvents() {
		ArrayList<CountdownEvent> temp = new ArrayList<>();
		temp.add(new CountdownEvent("Cesta uzavřena", "Cesta TX4 je uzavřena před OFT-1", null, 95, 0.06f));
		temp.add(new CountdownEvent("Rampa evakuována", "Veškerý personál opustil blízkost rakety", null, 400, 0.27f));
		temp.add(new CountdownEvent("Rekondenzátor", "Reknodenzátor byl zapnut", null, 740, 0.43f));
		temp.add(new CountdownEvent("Tankování", "Raketa se plní tekutým metanem a tekutým kyslíkem", null, 1060, 0.60f));
		temp.add(new CountdownEvent("Chlazení motorů", "Raketa začne posílat do motorů kyslík aby ho připravila na chladné plyny.", null, 1300, 0.77f));
		temp.add(new CountdownEvent("Zážeh!", "Motory byly zažehnuty", null, 1593, 0.89f));
		temp.add(new CountdownEvent("START!", "Šťastnou cestu!", null, 1717, 0.97f));
		return temp;
	}

	@NotNull
	private static ArrayList<CountdownEvent> getCountdownEvents() {
		ArrayList<CountdownEvent> temp = new ArrayList<>();
		temp.add(new CountdownEvent("START!", "Šťastnou cestu!", null, 100, 0.03f));
		temp.add(new CountdownEvent("Pitch over", "Raketa začne gravitační oblouk.", null, 300, 0.16f));
		temp.add(new CountdownEvent("Max-Q", "Moment nejvyššího aerodynamického namáhání rakety.", null, 550, 0.29f));
		temp.add(new CountdownEvent("MECO", "B7 vypne svých 33 raptor motorů", null, 700, 0.375f));
		temp.add(new CountdownEvent("Rozdělení stupňů", "B7 zamíří zpět k pevnině, S24 pokračuje dále na orbitu.", null, 850, 0.51f));
		temp.add(new CountdownEvent("RapVac start", "Motory S24 byly zažehnuty", null, 1200, 0.7f));
		temp.add(new CountdownEvent("A co dál?", "A co dále? Nevíme :D", null, 1650, 0.94f));
		return temp;
	}

	public static void shutdown() {
		for (JFrame frame : frames) {
			frame.setVisible(false);
		}
		command.setVisible(false);
		//obs.disconnect();
		System.exit(0);
	}
	
}

