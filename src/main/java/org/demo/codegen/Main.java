package org.demo.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.demo.codegen.config.CodegenConfig;
import org.demo.codegen.config.YamlConfigLoader;
import org.demo.codegen.generator.HbmGenerator;
import org.demo.codegen.generator.ModelGenerator;
import org.demo.codegen.generator.QueryConditionGenerator;
import org.demo.codegen.generator.QueryItemGenerator;
import org.demo.codegen.meta.TableMeta;
import org.demo.codegen.parser.MysqlDdlParser;

public class Main {

    public static void main(String[] args) throws Exception {
        CodegenConfig cfg = new YamlConfigLoader().load(args);

        String sqlText = readAllSql(cfg.getInput().getSqlFiles());
        MysqlDdlParser parser = new MysqlDdlParser();
        List<TableMeta> tables = parser.parseTables(sqlText, cfg.getInput().getTables());

        QueryConditionGenerator qcGen = new QueryConditionGenerator();
        QueryItemGenerator qiGen = new QueryItemGenerator();
        ModelGenerator modelGen = new ModelGenerator();
        HbmGenerator hbmGen = new HbmGenerator();

        for (int i = 0; i < tables.size(); i++) {
            TableMeta t = tables.get(i);

            File qc = qcGen.generate(t, cfg.getOutput().getDir(), cfg.getOutput().getQueryPackage());
            System.out.println("[OK] Generated QueryCondition: " + qc.getAbsolutePath());

            File qi = qiGen.generate(t, cfg.getOutput().getDir(), cfg.getOutput().getQueryPackage());
            System.out.println("[OK] Generated QueryItem: " + qi.getAbsolutePath());

            File model = modelGen.generate(
                    t,
                    cfg.getOutput().getDir(),
                    cfg.getOutput().getModelPackage(),
                    cfg.getOutput().getIdColumn(),
                    cfg.getOutput().getVersionColumn()
            );
            System.out.println("[OK] Generated Model: " + model.getAbsolutePath());

            File hbm = hbmGen.generateSqlQuery(t, cfg.getOutput().getHbmDir());
            System.out.println("[OK] Generated HBM SQL Query: " + hbm.getAbsolutePath());
        }


        System.out.println("[DONE]");
    }

    private static String readAllSql(List<String> sqlFilesOrDirs) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (sqlFilesOrDirs == null) return "";

        for (int i = 0; i < sqlFilesOrDirs.size(); i++) {
            String path = sqlFilesOrDirs.get(i);
            if (path == null || path.trim().length() == 0) continue;

            File f = new File(path.trim());
            if (!f.exists()) {
                throw new IllegalStateException("SQL path not found: " + f.getAbsolutePath());
            }

            if (f.isDirectory()) {
                File[] files = f.listFiles();
                if (files == null) continue;

                // 只取 .sql，并排序，保证输出稳定
                List<File> sqls = new java.util.ArrayList<File>();
                for (int j = 0; j < files.length; j++) {
                    File x = files[j];
                    if (x != null && x.isFile() && x.getName().toLowerCase().endsWith(".sql")) {
                        sqls.add(x);
                    }
                }
                java.util.Collections.sort(sqls, new java.util.Comparator<File>() {
                    public int compare(File a, File b) {
                        return a.getName().compareToIgnoreCase(b.getName());
                    }
                });

                for (int j = 0; j < sqls.size(); j++) {
                    sb.append(readFileUtf8(sqls.get(j))).append("\n");
                }
            } else {
                sb.append(readFileUtf8(f)).append("\n");
            }
        }

        return sb.toString();
    }

    private static String readFileUtf8(File f) throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            byte[] buf = new byte[(int) f.length()];
            int off = 0;
            while (off < buf.length) {
                int n = in.read(buf, off, buf.length - off);
                if (n < 0) break;
                off += n;
            }
            return new String(buf, "UTF-8");
        } finally {
            if (in != null) {
                try { in.close(); } catch (Exception ignore) {}
            }
        }
    }
}
