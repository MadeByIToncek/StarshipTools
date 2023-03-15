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

public enum FontVariant {
	THIN("Thin", "thin"),
	LIGHT("Light", "light"),
	BOOK("Book", "book"),
	REGULAR("Regular", "regular"),
	MEDIUM("Medium", "medium"),
	BOLD("Bold", "bold");
	
	private final String variant;
	private final String filename;
	
	FontVariant(String variant, String filename) {
		this.variant = variant;
		this.filename = filename;
	}
	
	public String getVariant() {
		return variant;
	}
	
	public String getFilename() {
		return filename;
	}
}
