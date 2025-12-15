package org.demo.codegen.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.demo.codegen.meta.ColumnMeta;
import org.demo.codegen.meta.TableMeta;
import org.demo.codegen.util.NamingUtil;
import org.demo.codegen.util.TypeMapper;

public class MysqlDdlParser {

    private final NamingUtil namingUtil = new NamingUtil();
    private final TypeMapper typeMapper = new TypeMapper();

    /**
     * 解析指定 tables（仅解析列定义，忽略 PK/INDEX）
     * - tables 为空：解析全部表
     * - tables 非空：作为过滤条件（大小写不敏感）
     */
    public List<TableMeta> parseTables(String sql, List<String> tables) {
        if (sql == null) throw new IllegalArgumentException("sql is null");

        List<TableMeta> allTables = parseAllTables(sql);

        if (tables == null || tables.isEmpty()) {
            return allTables;
        }

        // 过滤集合：统一 lower + trim
        Set<String> filter = new HashSet<String>();
        for (int i = 0; i < tables.size(); i++) {
            String t = tables.get(i);
            if (t == null) continue;
            String key = t.trim().toLowerCase();
            if (key.length() > 0) filter.add(key);
        }

        if (filter.isEmpty()) {
            return allTables;
        }

        List<TableMeta> result = new ArrayList<TableMeta>();
        for (int i = 0; i < allTables.size(); i++) {
            TableMeta tm = allTables.get(i);
            String tableName = tm.getTableName();
            if (tableName == null) continue;
            String key = tableName.trim().toLowerCase();
            if (filter.contains(key)) {
                result.add(tm);
            }
        }

        // 可选：严格校验 tables 都命中（你想“生成就必须存在”就开）
        // for (String want : filter) {
        //     boolean found = false;
        //     for (int i = 0; i < result.size(); i++) {
        //         if (want.equals(result.get(i).getTableName().trim().toLowerCase())) {
        //             found = true;
        //             break;
        //         }
        //     }
        //     if (!found) throw new IllegalStateException("Table not found in DDL: " + want);
        // }

        return result;
    }

    /**
     * 解析全部 CREATE TABLE 表结构（仅列定义，不含 index/pk）
     */
    private List<TableMeta> parseAllTables(String sql) {
        List<TableMeta> result = new ArrayList<TableMeta>();

        // 支持：
        // CREATE TABLE `cd_wh_loc` ( ... ) ENGINE=...
        // CREATE TABLE IF NOT EXISTS `cd_wh_loc` ( ... ) ENGINE=...
        Pattern p = Pattern.compile(
                "CREATE\\s+TABLE\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?`?([a-zA-Z0-9_]+)`?\\s*\\((.*?)\\)\\s*ENGINE",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );

        Matcher m = p.matcher(sql);
        while (m.find()) {
            String tableName = m.group(1);
            String body = m.group(2);
            if (tableName == null || body == null) continue;

            TableMeta tm = new TableMeta();
            tm.setTableName(tableName.trim());
            tm.setClassName(namingUtil.toClassName(tableName.trim()));

            parseColumnsInto(body, tm);

            // 没列就不加入（避免生成空壳）
            if (tm.getColumns() != null && !tm.getColumns().isEmpty()) {
                result.add(tm);
            }
        }

        return result;
    }

    private void parseColumnsInto(String body, TableMeta tm) {
        String[] lines = body.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() == 0) continue;

            // 只处理以 `COL` 开头的列定义行（跳过 PRIMARY KEY/KEY/UNIQUE 等）
            if (!line.startsWith("`")) continue;

            // 去掉尾部逗号
            if (line.endsWith(",")) {
                line = line.substring(0, line.length() - 1).trim();
            }

            // `CREATE_TIME` datetime NOT NULL COMMENT '创建时间'
            Matcher m = Pattern.compile("^`([^`]+)`\\s+(.+)$").matcher(line);
            if (!m.find()) continue;

            String colName = m.group(1);
            String rest = m.group(2);
            if (colName == null || rest == null) continue;

            String mysqlType = extractMysqlType(rest);
            String comment = extractComment(rest);

            ColumnMeta c = new ColumnMeta();
            c.setColumnName(colName.trim());
            c.setMysqlType(mysqlType);

            String mysqlTypeLower = mysqlType == null ? "" : mysqlType.toLowerCase();
            c.setTimeType(typeMapper.isTimeType(mysqlTypeLower));
            c.setJavaType(typeMapper.toJavaType(mysqlType));
            c.setJavaName(namingUtil.toFieldName(colName));

            // 需要你在 ColumnMeta 增加 comment 字段 + getter/setter
            c.setComment(comment);

            tm.addColumn(c);
        }
    }

    /**
     * 提取类型部分：
     * rest: "datetime NOT NULL COMMENT 'xxx'" -> "datetime"
     * rest: "varchar(32) CHARACTER SET utf8 ..." -> "varchar(32)"
     * rest: "decimal(18, 8) NOT NULL ..." -> "decimal(18, 8)"
     */
    private String extractMysqlType(String rest) {
        String r = rest.trim();
        int paren = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < r.length(); i++) {
            char ch = r.charAt(i);
            if (ch == '(') paren++;
            if (ch == ')') {
                if (paren > 0) paren--;
            }
            if (ch == ' ' && paren == 0) {
                break;
            }
            sb.append(ch);
        }
        return sb.toString().trim();
    }

    /**
     * 提取 COMMENT 'xxx' / COMMENT "xxx"
     * 没有则返回空串（不要返回 null，生成注释更稳）
     */
    private String extractComment(String rest) {
        if (rest == null) return "";
        // COMMENT 'xxx'
        Matcher m1 = Pattern.compile("COMMENT\\s+'([^']*)'", Pattern.CASE_INSENSITIVE).matcher(rest);
        if (m1.find()) {
            return safeTrim(m1.group(1));
        }
        // COMMENT "xxx"
        Matcher m2 = Pattern.compile("COMMENT\\s+\"([^\"]*)\"", Pattern.CASE_INSENSITIVE).matcher(rest);
        if (m2.find()) {
            return safeTrim(m2.group(1));
        }
        return "";
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
