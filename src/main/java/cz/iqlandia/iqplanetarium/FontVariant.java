package cz.iqlandia.iqplanetarium;

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
