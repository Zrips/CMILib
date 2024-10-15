package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.Zrips.CMILib.Logs.CMIDebug;

public class CMIJson {

    private CMIJson() {
        throw new IllegalStateException("Utility class");
    }

    public static <K, V> String serialize(HashMap<K, V> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(serializeObject(entry.getKey())).append(":")
                .append(serializeObject(entry.getValue())).append(",");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }

    public static HashMap<String, Object> deserialize(String json) {
        HashMap<String, Object> map = new HashMap<>();
        json = json.substring(1, json.length() - 1);
        String[] entries = splitJsonEntries(json);

        for (String entry : entries) {
            String[] keyValue = entry.split(":", 2);
            if (keyValue.length == 2) {
                String key = deserializeObject(keyValue[0]).toString();
                Object value = deserializeObject(keyValue[1]);
                map.put(key, value);
            }
        }
        return map;
    }

    private static String serializeObject(Object obj) {
        if (obj == null) {
            return "\"null\"";
        } else if (obj instanceof String) {
            return "\"" + escapeJson(obj.toString()) + "\"";
        } else if (obj instanceof Boolean) {
            return "\"" + ((Boolean) obj ? "1b" : "0b") + "\"";
        } else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            return "\"" + obj.toString() + "\"";
        } else if (obj instanceof List) {
            return serializeList((List<?>) obj);
        } else if (obj instanceof HashMap) {
            return serialize((HashMap<?, ?>) obj);
        }

        return "\"" + obj.toString() + "\"";
    }

    private static String escapeJson(String str) {
        return str
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\b", "\\b")
            .replace("\f", "\\f")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    private static String unescapeJson(String str) {
        return str
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
            .replace("\\b", "\b")
            .replace("\\f", "\f")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t");
    }

    public static String serializeList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object item : list) {
            sb.append(serializeObject(item)).append(",");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    private static Object deserializeObject(String str) {
        str = str.trim();

        if (str.startsWith("\"") && str.endsWith("\""))
            str = str.substring(1, str.length() - 1);

        if ("null".equals(str)) {
            return null;
        } else if (str.equals("true") || str.equals("false")) {
            return Boolean.parseBoolean(str);
        } else if (str.equals("1b")) {
            return true;
        } else if (str.equals("0b")) {
            return false;
        } else if (str.startsWith("[") && str.endsWith("]")) {
            return deserializeList(str);
        } else if (str.startsWith("{") && str.endsWith("}")) {
            return deserialize(str);
        } else {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e1) {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException e2) {
                    try {
                        return Double.parseDouble(str);
                    } catch (NumberFormatException e3) {
                        if (str.startsWith("\"") && str.endsWith("\""))
                            return unescapeJson(str.substring(1, str.length() - 1));
                        return unescapeJson(str);
                    }
                }
            }
        }
    }

    public static List<Object> deserializeList(String str) {
        List<Object> list = new ArrayList<>();
        str = str.substring(1, str.length() - 1);
        String[] items = splitJsonEntries(str);

        for (String item : items) {
            list.add(deserializeObject(item));
        }
        return list;
    }

    private static String[] splitJsonEntries(String str) {
        List<String> entries = new ArrayList<>();
        int braceCount = 0;
        int bracketCount = 0;
        int start = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '{')
                braceCount++;
            if (c == '}')
                braceCount--;
            if (c == '[')
                bracketCount++;
            if (c == ']')
                bracketCount--;
            if (c == ',' && braceCount == 0 && bracketCount == 0) {
                entries.add(str.substring(start, i).trim());
                start = i + 1;
            }
        }
        if (start < str.length()) {
            entries.add(str.substring(start).trim());
        }

        return entries.toArray(new String[0]);
    }

}
