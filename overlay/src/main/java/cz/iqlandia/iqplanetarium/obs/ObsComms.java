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

package cz.iqlandia.iqplanetarium.obs;

import com.google.gson.*;
import io.obswebsocket.community.client.*;
import org.json.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class ObsComms {
	private final OBSRemoteController controller;
	private boolean ready;
	private String address;
	private int port;
	
	
	public ObsComms() {
		cfg();
		controller = OBSRemoteController.builder()
				// set options, register for events, etc.
				// continue reading for more information
				.host(address)
				.port(port)
				.lifecycle()
				.onReady(() -> {
					ready = true;
					System.out.println("OBS connected!");
				})
				.onCommunicatorError((r) -> {
					JDialog dialog = new JDialog();
					dialog.add(new JLabel(r.getReason()));
				})
				.onControllerError((r) -> {
					JDialog dialog = new JDialog();
					dialog.add(new JLabel(r.getReason()));
				})
				.and()
				.build();
		controller.connect();
	}
	
	private void cfg() {
		if(!new File("./config/").exists()) {
			new File("./config/").mkdirs();
		}
		
		if(!new File("./config/obs.json").exists()) {
			try (FileWriter fw = new FileWriter("./config/obs.json")) {
				JSONObject object = new JSONObject();
				object.put("address", "localhost");
				object.put("port", 4444);
				fw.write(object.toString(4));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		JSONObject object;
		try (Scanner sc = new Scanner(new File("./config/obs.json"))) {
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
			object = new JSONObject(sb.toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		address = object.getString("address");
		port = object.getInt("port");
	}
	
	public void hide(String item) {
		if(ready) {
			for (String s : Arrays.asList("Starship EverydayAstronaut", "Starship")) {
				controller.getSceneItemId(s, item, 0, (id) -> controller.setSceneItemEnabled(s, id.getSceneItemId(), false, (c) -> {
					System.out.println("Swiped");
				}));
			}
		}
	}
	
	public void show(String item) {
		if(ready) {
			for (String s : Arrays.asList("Starship EverydayAstronaut", "Starship")) {
				controller.getSceneItemId(s, item, 0, (id) -> controller.setSceneItemEnabled(s, id.getSceneItemId(), true, (c) -> {
					System.out.println("Swiped");
				}));
			}
		}
	}
	
	public String address(String address) {
		try {
			String cmd = "youtube-dl --get-url -f b " + address;
			Process exec = Runtime.getRuntime().exec(cmd);
			Scanner sc = new Scanner(exec.getInputStream());
			Scanner se = new Scanner(exec.getErrorStream());
			StringBuilder sb = new StringBuilder();
			StringBuilder sbe = new StringBuilder();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			while (se.hasNextLine()) {
				sbe.append(se.nextLine());
			}
			System.out.println(sb);
			System.out.println(se);
			return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void updateStream(String address) {
		JSONObject obj = new JSONObject().put("playlist", new JSONArray().put(new JSONObject().put("value", address(address)).put("id", 0)));
		JsonObject o = (JsonObject) JsonParser.parseString(obj.toString());
		controller.setInputSettings("stream", o, false, 1000);
	}
	
	public void disconnect() {
		controller.disconnect();
	}
}
