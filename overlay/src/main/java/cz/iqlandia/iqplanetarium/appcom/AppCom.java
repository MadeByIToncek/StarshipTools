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

package cz.iqlandia.iqplanetarium.appcom;

import com.github.kusaanko.youtubelivechat.*;
import express.*;
import org.json.*;

import java.io.*;
import java.util.*;

import static cz.iqlandia.iqplanetarium.Main.*;

public class AppCom {
	
	private int port;
	
	public AppCom() {
		cfg();
		new Thread(() -> {
			Express server = new Express();
			
			server.get("/submit/:id", (req, res) -> {
				System.out.println("Selected chat " + Integer.parseInt(req.getParam("id")));
				chatIndex = Integer.parseInt(req.getParam("id"));
				res.send("recived");
			});
			
			server.get("/questions.json", (req, res) -> {
				JSONArray obj = new JSONArray();
				for (int i = 0; i < tools.getMessages().size(); i++) {
					JSONObject a = new JSONObject();
					ChatItem chatItem = tools.getMessages().get(i);
					a.put("a", chatItem.getAuthorName());
					a.put("q", chatItem.getMessage());
					a.put("id", i);
					obj.put(a);
				}
				System.out.println("Requested questions");
				res.send(obj.toString());
			});
			
			
			server.listen(port);
		}).start();
	}
	
	private void cfg() {
		if(!new File("./config/").exists()) {
			new File("./config/").mkdirs();
		}
		
		if(!new File("./config/appcom.json").exists()) {
			try (FileWriter fw = new FileWriter("./config/appcom.json")) {
				JSONObject object = new JSONObject();
				object.put("port", 5555);
				fw.write(object.toString(4));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		JSONObject object;
		
		try (Scanner sc = new Scanner(new File("./config/appcom.json"))) {
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
			object = new JSONObject(sb.toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		port = object.getInt("port");
	}
	
}
