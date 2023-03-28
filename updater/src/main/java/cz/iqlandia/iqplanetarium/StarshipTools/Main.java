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

package cz.iqlandia.iqplanetarium.StarshipTools;

import org.json.*;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws IOException {
		URL downloadLink = null;
		new File("windows.jar").delete();
		try (Scanner sc = new Scanner(new URL("https://api.github.com/repos/MadeByIToncek/StarshipTools/releases").openStream())) {
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
			JSONArray array = new JSONArray(sb.toString());
			JSONArray assets = array.getJSONObject(0).getJSONArray("assets");
			for (Object o : assets) {
				JSONObject asset = (JSONObject) o;
				if(asset.getString("name").equals("windows.jar")) {
					downloadLink = new URL(asset.getString("browser_download_url"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileOutputStream fileOutputStream = new FileOutputStream("windows.jar")) {
			assert downloadLink != null;
			ReadableByteChannel readableByteChannel = Channels.newChannel(downloadLink.openStream());
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		}
		String cmd = "cmd.exe /C START /D \"" + new File("./").getAbsolutePath() + "\" /MIN javaw.exe -jar windows.jar";
		Runtime.getRuntime().exec(cmd);
		System.exit(1);
	}
}