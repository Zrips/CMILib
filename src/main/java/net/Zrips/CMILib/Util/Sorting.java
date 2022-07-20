package net.Zrips.CMILib.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sorting {

    public HashMap<String, Double> sortByValue(Map<String, Double> unsortMap) {
	List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());
	Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	    @Override
	    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
		return (o2.getValue()).compareTo(o1.getValue());
	    }
	});
	HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Double> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public HashMap<String, Long> sortByValueLong(Map<String, Long> unsortMap) {
	List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());
	Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
	    @Override
	    public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
		return (o2.getValue()).compareTo(o1.getValue());
	    }
	});
	HashMap<String, Long> sortedMap = new LinkedHashMap<String, Long>();
	for (Iterator<Map.Entry<String, Long>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Long> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public HashMap<Object, Integer> sortByValueObject(Map<Object, Integer> unsortMap) {
	List<Map.Entry<Object, Integer>> list = new LinkedList<Map.Entry<Object, Integer>>(unsortMap.entrySet());
	Collections.sort(list, new Comparator<Map.Entry<Object, Integer>>() {
	    @Override
	    public int compare(Map.Entry<Object, Integer> o1, Map.Entry<Object, Integer> o2) {
		return (o1.getValue()).compareTo(o2.getValue());
	    }
	});
	HashMap<Object, Integer> sortedMap = new LinkedHashMap<Object, Integer>();
	for (Iterator<Map.Entry<Object, Integer>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<Object, Integer> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

}
