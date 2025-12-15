package org.demo.codegen.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlConfigLoader {

    /**
     * 优先级：
     * 1) args[0] 指定的文件路径
     * 2) classpath: codegen.yml
     * 3) 项目根目录: codegen.yml
     */
    public CodegenConfig load(String[] args) throws Exception {
        InputStream in = null;

        if (args != null && args.length > 0 && args[0] != null && args[0].trim().length() > 0) {
            File f = new File(args[0].trim());
            if (!f.exists()) {
                throw new IllegalArgumentException("Config file not found: " + f.getAbsolutePath());
            }
            in = new FileInputStream(f);
        } else {
            in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("codegen.yml");
            if (in == null) {
                File f = new File("codegen.yml");
                if (f.exists()) {
                    in = new FileInputStream(f);
                }
            }
        }

        if (in == null) {
            throw new IllegalStateException(
                    "Cannot find codegen.yml. Put it in src/main/resources or project root, or pass as arg.");
        }

        Yaml yaml = new Yaml(new Constructor(CodegenConfig.class));
        CodegenConfig cfg = yaml.load(in);

        if (cfg == null) {
            throw new IllegalStateException("Failed to parse codegen.yml (config is null).");
        }

        // ===== input 校验 =====
        if (cfg.getInput() == null) {
            throw new IllegalArgumentException("input section is required.");
        }

        if (cfg.getInput().getSqlFiles() == null || cfg.getInput().getSqlFiles().isEmpty()) {
            throw new IllegalArgumentException("input.sqlFiles is required.");
        }

        // tables 是可选的：为空 = 全表解析
        if (cfg.getInput().getTables() == null) {
            cfg.getInput().setTables(new ArrayList<String>());
        }

        // ===== output 校验 =====
        if (cfg.getOutput() == null) {
            throw new IllegalArgumentException("output section is required.");
        }

        if (cfg.getOutput().getDir() == null || cfg.getOutput().getDir().trim().length() == 0) {
            throw new IllegalArgumentException("output.dir is required.");
        }

        if (cfg.getOutput().getQueryPackage() == null
                || cfg.getOutput().getQueryPackage().trim().length() == 0) {
            throw new IllegalArgumentException("output.queryPackage is required.");
        }

        // modelPackage / hbmDir / idColumn / versionColumn 都是可选
        return cfg;
    }
}