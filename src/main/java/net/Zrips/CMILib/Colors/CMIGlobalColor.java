package net.Zrips.CMILib.Colors;

public enum CMIGlobalColor {

	Primary("p", "{#FFAA00}", "Primary color for general text"),
	Secondary("s", "{#FFFF55}", "Usually used for variables and placeholders"),
	Warning("w", "{#FF7357}", "To indicate error in command usage, usually less important than error"),
	Neutral("n", "{#AAAAAA}", "More neutral color for text which should not attract much of attention"),
	Uplift("u", "{#59C959}", "Positive color to indicate something good, for example creating a home record"),
	Downlift("d", "{#E89280}", "Negative color to indicate something bad, for example removing a home record"),
	Error("e", "{#FF5555}", "To indicate major error");

	private String id;
	private String color = "{#ffffff}";
	private String description;

	private CMIGlobalColor(String id, String color, String description) {
		this.id = CMIChatColor.globalColorPrefix + id + "}";
		this.color = CMIChatColor.translate(color);
		this.description = description;
	}

	public String getColorId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = CMIChatColor.translate(color);
	}

	public String getDescription() {
		return description;
	}
}
