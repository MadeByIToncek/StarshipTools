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

package cz.iqlandia.iqplanetarium.fonts;

public enum FontFamily {
	STOLZL("https://github.com/MadeByIToncek/cdn.itoncek.cf/raw/main/fonts/Stolzl-", ".ttf", "stolzl"),
	VCR("https://github.com/MadeByIToncek/cdn.itoncek.cf/raw/main/fonts/VCR_OSD_MONO-", ".ttf", "vcr_osd_mono");
	
	private final String prefix;
	private final String suffix;
	private final String folder;
	
	FontFamily(String prefix, String suffix, String folder) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.folder = folder;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public String getFolder() {
		return folder;
	}
}
