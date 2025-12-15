package org.demo.codegen.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.demo.codegen.meta.ColumnMeta;
import org.demo.codegen.meta.TableMeta;

public class HbmGenerator {

    public File generateSqlQuery(TableMeta t, String hbmDir) throws Exception {
        if (t == null) throw new IllegalArgumentException("TableMeta is null");

        String table = t.getTableName();
        String alias = buildAlias(table);
        String queryName = t.getClassName() + "Query";

        String idCol = guessIdColumn(t.getColumns());

        StringBuilder sql = new StringBuilder();
        sql.append("\n");
        sql.append("select ");

        List<ColumnMeta> cols = t.getColumns();
        for (int i = 0; i < cols.size(); i++) {
            ColumnMeta c = cols.get(i);
            String col = toSqlColumn(c.getColumnName());
            if (i > 0) sql.append(",\n       ");
            sql.append(alias).append(".").append(col);
        }

        sql.append("\n\nfrom ").append(table).append(" ").append(alias).append("\n\n");
        sql.append("where 1=1\n\n");

        // where 条件：每个字段都生成一条；时间字段生成 From/To
        for (int i = 0; i < cols.size(); i++) {
            ColumnMeta c = cols.get(i);
            String col = toSqlColumn(c.getColumnName());
            String p = c.getJavaName();

            if (c.isTimeType()) {
                sql.append("<<and ").append(alias).append(".").append(col).append(" >= :").append(p).append("From>>\n");
                sql.append("<<and ").append(alias).append(".").append(col).append(" <= :").append(p).append("To>>\n");
            } else {
                sql.append("<<and ").append(alias).append(".").append(col).append(" = :").append(p).append(">>\n");
            }
        }

        if (idCol != null) {
            sql.append("\norder by ").append(alias).append(".").append(toSqlColumn(idCol)).append(" asc\n");
        }
        sql.append("\n");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>\n");
        sb.append("<!DOCTYPE hibernate-mapping PUBLIC \n");
        sb.append("    \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n");
        sb.append("    \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
        sb.append("<hibernate-mapping>\n");
        sb.append("    <sql-query name=\"").append(escapeXml(queryName)).append("\">\n");
        sb.append("        <![CDATA[");
        sb.append(sql.toString());
        sb.append("        ]]>\n");
        sb.append("    </sql-query>\n");
        sb.append("</hibernate-mapping>\n");

        File dir = new File((hbmDir == null || hbmDir.trim().length() == 0) ? "output/hbm" : hbmDir.trim());
        if (!dir.exists()) dir.mkdirs();

        File f = new File(dir, queryName + ".hbm.xml");
        writeUtf8(f, sb.toString());
        return f;
    }

    private String buildAlias(String tableName) {
        if (tableName == null || tableName.trim().length() == 0) return "t";
        String[] parts = tableName.trim().split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            if (p.length() == 0) continue;
            sb.append(Character.toLowerCase(p.charAt(0)));
        }
        return sb.length() == 0 ? "t" : sb.toString();
    }

    private String toSqlColumn(String columnName) {
        if (columnName == null) return "";
        // 统一输出小写，贴近你给的参考 sql 风格
        return columnName.trim().toLowerCase();
    }

    private String guessIdColumn(List<ColumnMeta> cols) {
        if (cols == null || cols.isEmpty()) return null;

        // 优先 JOB_ID
        for (int i = 0; i < cols.size(); i++) {
            String cn = cols.get(i).getColumnName();
            if (cn != null && "JOB_ID".equalsIgnoreCase(cn.trim())) return cn.trim();
        }
        // 次优：ID
        for (int i = 0; i < cols.size(); i++) {
            String cn = cols.get(i).getColumnName();
            if (cn != null && "ID".equalsIgnoreCase(cn.trim())) return cn.trim();
        }
        // 再次：*_ID
        for (int i = 0; i < cols.size(); i++) {
            String cn = cols.get(i).getColumnName();
            if (cn == null) continue;
            String s = cn.trim().toUpperCase();
            if (s.endsWith("_ID")) return cn.trim();
        }
        return null;
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private void writeUtf8(File f, String s) throws Exception {
        FileOutputStream fos = null;
        OutputStreamWriter w = null;
        try {
            fos = new FileOutputStream(f);
            w = new OutputStreamWriter(fos, "UTF-8");
            w.write(s);
        } finally {
            if (w != null) try { w.close(); } catch (Exception ignore) {}
            if (fos != null) try { fos.close(); } catch (Exception ignore) {}
        }
    }
}
