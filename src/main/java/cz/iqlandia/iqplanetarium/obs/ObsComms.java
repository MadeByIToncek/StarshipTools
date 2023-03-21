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

import javax.swing.*;

public class ObsComms {
	private final OBSRemoteController controller;
	private boolean ready;
	
	public ObsComms() {
		controller = OBSRemoteController.builder()
				// set options, register for events, etc.
				// continue reading for more information
				.host("192.168.99.64")
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
