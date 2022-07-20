package net.Zrips.CMILib.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.Zrips.CMILib.RawMessages.RawMessage;

public class ChatEditorObject {

    private List<String> hover = new ArrayList<String>();
    private String text;
    private String suggestion;

    private RawMessage line;

    public ChatEditorObject(String text) {
	this.text = text;
    }

    public void onDelete() {

    }

    public void onClick() {

    }

    public List<String> getHover() {
	return hover;
    }

    public void setHover(List<String> hover) {
	this.hover = hover;
    }

    public void setHover(String hover) {
	this.hover.addAll(Arrays.asList(hover.split("\\n")));
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getSuggestion() {
	return suggestion;
    }

    public void setSuggestion(String suggestion) {
	this.suggestion = suggestion;
    }

    public RawMessage getLine() {
	return line;
    }

    public void setLine(RawMessage line) {
	this.line = line;
    }
}
