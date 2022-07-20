package net.Zrips.CMILib.Container;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CMISort {
    public static Map<String, Integer> sortIntDESC(Map<String, Integer> unsortMap) {

	// Convert Map to List
	List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

	// Sort list with comparator, to compare the Map values
	Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	    @Override
	    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		return (o2.getValue()).compareTo(o1.getValue());
	    }
	});

	// Convert sorted map back to a Map
	Map<String, Integer> sortedMap = new LinkedHashMap<>();
	for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Integer> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public static Map<String, Integer> sortIntASC(Map<String, Integer> unsortMap) {
	List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
	Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	    @Override
	    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		return (o1.getValue()).compareTo(o2.getValue());
	    }
	});
	Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Integer> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public static Map<String, Double> sortDoubleDESC(Map<String, Double> unsortMap) {

	// Convert Map to List
	List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

	// Sort list with comparator, to compare the Map values
	Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	    @Override
	    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
		return (o2.getValue()).compareTo(o1.getValue());
	    }
	});

	// Convert sorted map back to a Map
	Map<String, Double> sortedMap = new LinkedHashMap<>();
	for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Double> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    public static Map<String, Double> sortDoubleASC(Map<String, Double> unsortMap) {

	// Convert Map to List
	List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

	// Sort list with comparator, to compare the Map values
	Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	    @Override
	    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
		return (o1.getValue()).compareTo(o2.getValue());
	    }
	});

	// Convert sorted map back to a Map
	Map<String, Double> sortedMap = new LinkedHashMap<>();
	for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, Double> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }
}
