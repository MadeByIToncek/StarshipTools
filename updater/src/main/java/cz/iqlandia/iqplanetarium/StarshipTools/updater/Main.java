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

package cz.iqlandia.iqplanetarium.StarshipTools.updater;

import org.json.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws IOException {
		JFrame f = new JFrame("Updater | StarshipTools.jar");
		JProgressBar pb = new JProgressBar(JProgressBar.HORIZONTAL);
		pb.setValue(0);
		pb.setMaximum(10);
		pb.setStringPainted(true);
		pb.setString("Checking for updates");
		f.add(pb);
		f.setSize(400, 100);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				f.setVisible(false);
				System.exit(0);
			}
		});
		
		f.setVisible(true);
		String current;
		String online;
		if(!new File("./config/").exists()) new File("./config/").mkdirs();
		if(!new File("./config/version").exists()) {
			try (FileWriter fw = new FileWriter("./config/version")) {
				fw.write("INSTALL");
				pb.setString("Installation will be needed");
				pb.setValue(pb.getValue() + 1);
			}
		}
		try (Scanner sc = new Scanner(new File("./config/version"))) {
			current = sc.next();
		}
		try (Scanner sc = new Scanner(new URL("https://api.github.com/repos/MadeByIToncek/StarshipTools/releases").openStream())) {
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
			JSONArray array = new JSONArray(sb.toString());
			online = array.getJSONObject(0).getString("tag_name");
		}
		pb.setString(current + " x " + online);
		pb.setValue(10);
		
		
		if(!current.equals(online)) {
			URL downloadLink = null;
			new File("windows.jar").delete();
			new File("./fonts/").deleteOnExit();
			new File("./config/version").delete();
			
			pb.setString("Deleting old files");
			pb.setValue(2);
			
			try (Scanner sc = new Scanner(new URL("https://api.github.com/repos/MadeByIToncek/StarshipTools/releases").openStream())) {
				StringBuilder sb = new StringBuilder();
				while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
				JSONArray assets = new JSONArray(sb.toString()).getJSONObject(0).getJSONArray("assets");
				for (Object o : assets) {
					JSONObject asset = (JSONObject) o;
					if(asset.getString("name").equals("windows.jar")) {
						downloadLink = new URL(asset.getString("browser_download_url"));
					}
				}
				pb.setString("Got new version link");
				pb.setValue(4);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (FileOutputStream fileOutputStream = new FileOutputStream("windows.jar")) {
				pb.setString("Downloading new version");
				pb.setValue(6);
				assert downloadLink != null;
				ReadableByteChannel readableByteChannel = Channels.newChannel(downloadLink.openStream());
				fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				pb.setString("New version downloaded");
				pb.setValue(8);
			}
			try (FileWriter fw = new FileWriter("./config/version")) {
				pb.setString("Writing version info");
				pb.setValue(9);
				fw.write(online);
			}
		}
		
		pb.setString("Launching");
		pb.setValue(10);
		String cmd = "cmd.exe /C START /D \"" + new File("./").getAbsolutePath() + "\" /MIN java.exe -jar windows.jar";
		Runtime.getRuntime().exec(cmd);
		f.setVisible(false);
		System.exit(1);
	}
}