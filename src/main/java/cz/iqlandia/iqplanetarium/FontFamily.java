package cz.iqlandia.iqplanetarium;

public enum FontFamily {
	STOLZL("https://github.com/MadeByIToncek/cdn.itoncek.cf/raw/main/fonts/Stolzl-", ".ttf", "stolzl");
	
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
