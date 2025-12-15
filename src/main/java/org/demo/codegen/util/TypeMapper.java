package org.demo.codegen.util;

public class TypeMapper {

    public boolean isTimeType(String mysqlTypeLower) {
        if (mysqlTypeLower == null) return false;
        String t = mysqlTypeLower.trim();
        return t.startsWith("datetime") || t.startsWith("timestamp") || t.startsWith("date");
    }

    public String toJavaType(String mysqlTypeRaw) {
        if (mysqlTypeRaw == null) return "String";
        String t = mysqlTypeRaw.trim().toLowerCase();

        // strip extra spaces
        while (t.contains("  ")) {
            t = t.replace("  ", " ");
        }

        if (t.startsWith("varchar") || t.startsWith("char") || t.startsWith("text") || t.startsWith("longtext")
                || t.startsWith("mediumtext") || t.startsWith("tinytext")) {
            return "String";
        }
        if (t.startsWith("datetime") || t.startsWith("timestamp") || t.startsWith("date")) {
            return "Date";
        }
        if (t.startsWith("bigint") || t.startsWith("int") || t.startsWith("integer") || t.startsWith("smallint")
                || t.startsWith("tinyint")) {
            return "Long";
        }
        if (t.startsWith("decimal") || t.startsWith("numeric")) {
            // decimal(p, s)
            int idx1 = t.indexOf('(');
            int idx2 = t.indexOf(')');
            if (idx1 > 0 && idx2 > idx1) {
                String inside = t.substring(idx1 + 1, idx2);
                int comma = inside.indexOf(',');
                if (comma > 0) {
                    String scaleStr = inside.substring(comma + 1).trim();
                    try {
                        int scale = Integer.parseInt(scaleStr);
                        if (scale > 0) return "Double";
                        return "Long";
                    } catch (Exception ignore) {
                        return "Double";
                    }
                }
            }
            return "Double";
        }
        if (t.startsWith("double") || t.startsWith("float")) {
            return "Double";
        }

        // fallback
        return "String";
    }
}
