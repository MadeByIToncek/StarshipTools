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

import io.obswebsocket.community.client.*;
import io.obswebsocket.community.client.message.response.sceneitems.*;
import org.json.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static java.lang.Thread.*;

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
				.host("localhost")
				.port(4444)
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
				sleep(200);
			} catch (IOException | InterruptedException e) {
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
	
	public void hide() {
		if(ready) {
			GetSceneItemIdResponse sceneItemId = controller.getSceneItemId("Starship", "StarshipTools", 0, 1000);
			controller.setSceneItemEnabled("Starship", sceneItemId.getSceneItemId(), false, 1000);
		}
	}
	
	public void show() {
		if(ready) {
			GetSceneItemIdResponse sceneItemId = controller.getSceneItemId("Starship", "StarshipTools", 0, 1000);
			controller.setSceneItemEnabled("Starship", sceneItemId.getSceneItemId(), true, 1000);
		}
	}
}