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

package cz.iqlandia.iqplanetarium.chat;

import com.github.kusaanko.youtubelivechat.*;

import java.io.*;
import java.util.*;

public abstract class ChatTools {
	private final YouTubeLiveChat chat;
	private final Timer chatThread;
	private final List<ChatItem> items;
	
	public ChatTools(String addr) throws IOException, IllegalArgumentException {
		items = new ArrayList<>();
		chat = new YouTubeLiveChat(addr, true, IdType.VIDEO);
		chatThread = new Timer();
		
		chatThread.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					chat.update();
					for (ChatItem item : chat.getChatItems()) {
						if(item.getType().equals(ChatItemType.MESSAGE)) {
							onMessage(item);
							items.add(item);
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, 1000, 1000);
		
		chatThread.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					if(!chat.getBroadcastInfo().isLiveNow) {
						shutdown();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, 20000, 20000);
	}
	
	public abstract void onMessage(ChatItem item);
	
	public List<ChatItem> getMessages() {
		return this.items;
	}
	
	public void shutdown() {
		chatThread.cancel();
	}
	
}
