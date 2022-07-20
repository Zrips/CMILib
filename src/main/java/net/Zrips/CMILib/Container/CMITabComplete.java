package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CMITabComplete {

    public HashMap<String, Object> tabs = new HashMap<String, Object>();

    public List<Object> getTabCompleteList(String[] args) {

	StringBuilder str = new StringBuilder();
	for (int z = 0; z < args.length - 1; z++) {
	    if (!str.toString().isEmpty())
		str.append(" ");
	    str.append(args[z]);
	}

	String comlpStr = getTabComplete(str.toString());

	List<Object> argsList = new ArrayList<Object>();

	if (comlpStr != null) {
	    if (args.length > 2) {
		if (str.toString().contains(" "))
		    argsList.addAll(Arrays.asList(str.toString().split(" ", 2)[1].split(" ")));
		else
		    argsList.addAll(Arrays.asList(str.toString()));
	    }
	    argsList.addAll(Arrays.asList(comlpStr.replace(",", "%%").split(" ")));

	    List<Object> temp = new ArrayList<Object>(argsList);
	    for (int iz = argsList.size() - 1; iz >= 0; iz--) {

		Object one = argsList.get(iz);
		if (!(one instanceof String))
		    continue;
		String st = (String) one;

		StringBuilder strs = new StringBuilder();
		for (String splited : st.split("%%")) {
		    if (!strs.toString().isEmpty())
			strs.append("%%");
		    if (splited.startsWith("!")) {
			splited = splited.substring(1);
			if (notRepeating(splited, args))
			    strs.append(splited);
		    } else
			strs.append(splited);
		}

		temp.set(iz, strs.toString());
	    }
	    argsList = temp;
	}

	return argsList;
    }

    private static boolean notRepeating(String word, String[] args) {

	for (String one : args) {
	    if (one.equalsIgnoreCase(word))
		return false;
	}
	return true;
    }

    public String getTabComplete(String tab) {
	String key = tab.split(" ", 2)[0];
	Object val = tabs.get(key.toLowerCase());

	if (val == null && !key.isEmpty() && tabs.size() >= 100) {

	    if (!key.isEmpty() && tab.contains(" ")) {
		StringBuilder res = new StringBuilder();
		for (Entry<String, Object> value : tabs.entrySet()) {
		    if (value.getValue() == null)
			continue;
		    for (Entry<String, Object> subValue : ((HashMap<String, Object>) value.getValue()).entrySet()) {
			if (!res.toString().isEmpty())
			    res.append(",");
			res.append(subValue.getKey());
		    }
		}
		return res.toString();
	    }

	    return null;
	}

	Object res = getTab(tab.toLowerCase(), tabs);
	return res == null ? null : (String) res;
    }

    private Object getTab(String tab, HashMap<String, Object> map) {

	StringBuilder res = new StringBuilder();
	if (tab.isEmpty()) {
	    for (Entry<String, Object> value : map.entrySet()) {
		if (!res.toString().isEmpty())
		    res.append(",");
		res.append(value.getKey());
	    }
	    return res.toString();
	}
	String key = tab.split(" ", 2)[0];
	Object val = map.get(key.toLowerCase());
	if (val instanceof HashMap && tab.contains(" ")) {
	    return getTab(tab.split(" ", 2)[1], (HashMap<String, Object>) val);
	}

	if (val == null) {
	    // Getting first value by key which contains dynamic variable if we cant determine which one we should pick+
	    for (Entry<String, Object> value : map.entrySet()) {
		if (value.getKey() == null)
		    continue;
		if (value.getKey().contains("[") && value.getKey().contains("]")) {
		    val = value.getValue();
		    break;
		}
	    }

	    if (val instanceof HashMap && tab.contains(" ")) {
		return getTab(tab.split(" ", 2)[1], (HashMap<String, Object>) val);
	    }
	}

	if (val == null) {

	    if (map.containsKey(key.toLowerCase()))
		// returning null as we dont need return anything if we dont have correct variable for it
		return null;

	    // getting all keys in case previous variable was dynamic
	    for (Entry<String, Object> value : map.entrySet()) {
		if (value.getValue() == null)
		    continue;
		for (Entry<String, Object> subValue : ((HashMap<String, Object>) value.getValue()).entrySet()) {
		    if (!res.toString().isEmpty())
			res.append(",");
		    res.append(subValue.getKey());
		}
	    }
	    return res.toString();
	}

	for (Entry<String, Object> value : ((HashMap<String, Object>) val).entrySet()) {
	    if (!res.toString().isEmpty())
		res.append(",");
	    res.append(value.getKey());
	}
	return res.toString();
    }

    public void addTabComplete(String tab) {
	tab = tab.replace("%%", ",");
	addTab(tab, tabs);
    }

    private Object addTab(String tab, HashMap<String, Object> map) {
	if (tab.contains(" ")) {
	    String[] split = tab.split(" ", 2);
	    for (String one : split[0].split(",")) {
		Object old = map.get(one.toLowerCase());
		if (old == null) {
		    old = new HashMap<String, Object>();
		    map.put(one.toLowerCase(), old);
		}
		addTab(split[1], (HashMap<String, Object>) old);
	    }
	    return map;
	}
	for (String one : tab.split(",")) {
	    Object old = map.get(one.toLowerCase());
	    if (old == null) {
		old = new HashMap<String, Object>();
		map.put(one.toLowerCase(), old);
	    }
	}
	return map;
    }
}
