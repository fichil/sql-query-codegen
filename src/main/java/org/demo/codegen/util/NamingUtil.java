package org.demo.codegen.util;

public class NamingUtil {

    public String toClassName(String tableName) {
        // cd_wh_loc -> CdWhLoc
        String camel = toLowerCamel(tableName);
        return upperFirst(camel);
    }

    public String toFieldName(String columnName) {
        // CREATE_TIME -> createTime
        return toLowerCamel(columnName);
    }

    public String toLowerCamel(String name) {
        if (name == null) return null;
        String s = name.trim();
        if (s.length() == 0) return s;

        s = s.replace('`', ' ');
        s = s.trim();

        String[] parts = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            if (p == null || p.length() == 0) continue;
            p = p.toLowerCase();
            if (sb.length() == 0) {
                sb.append(p);
            } else {
                sb.append(upperFirst(p));
            }
        }
        return sb.toString();
    }

    public String upperFirst(String s) {
        if (s == null || s.length() == 0) return s;
        if (s.length() == 1) return s.toUpperCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public String packageToPath(String pkg) {
        // query -> query
        // org.fichil.query -> org/fichil/query
        return pkg.replace('.', '/');
    }
}
