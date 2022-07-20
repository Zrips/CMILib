package net.Zrips.CMILib.Colors;

import java.awt.Color;

import net.Zrips.CMILib.Items.CMIMaterial;

public enum CMIColors {
    White(0, "White", CMIMaterial.WHITE_DYE, new Color(249, 255, 254)),
    Orange(1, "Orange", CMIMaterial.ORANGE_DYE, new Color(249, 128, 29)),
    Magenta(2, "Magenta", CMIMaterial.MAGENTA_DYE, new Color(199, 78, 189)),
    Light_Blue(3, "Light Blue", CMIMaterial.LIGHT_BLUE_DYE, new Color(58, 179, 218)),
    Yellow(4, "Yellow", CMIMaterial.YELLOW_DYE, new Color(254, 216, 61)),
    Lime(5, "Lime", CMIMaterial.LIME_DYE, new Color(128, 199, 31)),
    Pink(6, "Pink", CMIMaterial.PINK_DYE, new Color(243, 139, 170)),
    Gray(7, "Gray", CMIMaterial.GRAY_DYE, new Color(71, 79, 82)),
    Light_Gray(8, "Light Gray", CMIMaterial.LIGHT_GRAY_DYE, new Color(157, 157, 151)),
    Cyan(9, "Cyan", CMIMaterial.CYAN_DYE, new Color(22, 156, 156)),
    Purple(10, "Purple", CMIMaterial.PURPLE_DYE, new Color(137, 50, 184)),
    Blue(11, "Blue", CMIMaterial.BLUE_DYE, new Color(60, 68, 170)),
    Brown(12, "Brown", CMIMaterial.BROWN_DYE, new Color(131, 84, 50)),
    Green(13, "Green", CMIMaterial.GREEN_DYE, new Color(94, 124, 22)),
    Red(14, "Red", CMIMaterial.RED_DYE, new Color(176, 46, 38)),
    Black(15, "Black", CMIMaterial.BLACK_DYE, new Color(29, 29, 33));

    private int id;
    private String name;
    private CMIMaterial mat;
    private Color color;

    CMIColors(int id, String name, CMIMaterial mat, Color color) {
	this.id = id;
	this.name = name;
	this.mat = mat;
	this.color = color;
    }

    CMIColors(int id, String name, CMIMaterial mat) {
	this.id = id;
	this.name = name;
	this.mat = mat;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public static CMIColors getById(int id) {
	for (CMIColors one : CMIColors.values()) {
	    if (one.getId() == id)
		return one;
	}
	return CMIColors.White;
    }

    public CMIMaterial getMat() {
	return mat;
    }

    public void setMat(CMIMaterial mat) {
	this.mat = mat;
    }

    public static CMIMaterial getColorMaterial(CMIMaterial mat) {
	String name = mat.getName().replace(" ", "").replace("_", "").toLowerCase();
	for (CMIColors one : values()) {
	    if (name.contains(one.getName().replace("_", "").toLowerCase()))
		return one.getMat();
	}

	return null;
    }

    public static CMIColors getColor(CMIMaterial mat) {
	String name = mat.getName().replace(" ", "").replace("_", "").toLowerCase();
	for (CMIColors one : values()) {
	    if (name.contains(one.getName().replace("_", "").toLowerCase()))
		return one;
	}
	return null;
    }

    public Color getColor() {
	return color;
    }
}
