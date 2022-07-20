package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import net.Zrips.CMILib.Colors.CMIChatColor;

public class CMIList {

    public static String listToString(List<String> ls) {
	return listToString(ls, null);
    }

    public static String listToString(List<String> ls, String spliter) {
	spliter = spliter == null ? " \n" : spliter;
	StringBuilder s = new StringBuilder();
	for (String one : ls) {
	    if (!s.toString().isEmpty())
		s.append(spliter);
	    s.append(one);
	}
	return s.toString();
    }

    public static List<String> stringToList(String ls, String spliter) {
	List<String> s = new ArrayList<String>();
	if (ls.contains(spliter)) {
	    s.addAll(Arrays.asList(ls.split(spliter)));
	} else {
	    s.add(ls);
	}
	return s;
    }

    public static List<String> spreadList(List<String> ls) {
	List<String> s = new ArrayList<String>();
	for (int i = 0; i < ls.size(); i++) {
	    if (ls.get(i).contains(" \\n")) {
		s.addAll(Arrays.asList(ls.get(i).split(" \\\\n")));
	    } else if (ls.get(i).contains(" \n")) {
		s.addAll(Arrays.asList(ls.get(i).split(" \\n")));
	    } else
		s.add(ls.get(i));
	}
	return s;
    }

    public static void toLowerCase(List<String> strings) {
	ListIterator<String> iterator = strings.listIterator();
	while (iterator.hasNext()) {
	    iterator.set(iterator.next().toLowerCase());
	}
    }

    public static void removeColors(List<String> strings) {
	ListIterator<String> iterator = strings.listIterator();
	while (iterator.hasNext()) {
	    iterator.set(CMIChatColor.stripColor(iterator.next()));
	}
    }
}
