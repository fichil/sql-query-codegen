package org.demo.codegen.config;

import java.util.ArrayList;
import java.util.List;

public class CodegenConfig {

    private Input input = new Input();
    private Output output = new Output();

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public static class Input {
        private List<String> sqlFiles = new ArrayList<String>();
        private List<String> tables = new ArrayList<String>();

        public List<String> getSqlFiles() {
            return sqlFiles;
        }

        public void setSqlFiles(List<String> sqlFiles) {
            this.sqlFiles = sqlFiles;
        }

        public List<String> getTables() {
            return tables;
        }

        public void setTables(List<String> tables) {
            this.tables = tables;
        }
    }

    public static class Output {
        private String dir;
        private String queryPackage;

        // 先留字段，兼容 yml
        private String modelPackage;
        private String hbmDir;
        private String idColumn;
        private String versionColumn;

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getQueryPackage() {
            return queryPackage;
        }

        public void setQueryPackage(String queryPackage) {
            this.queryPackage = queryPackage;
        }

        public String getModelPackage() {
            return modelPackage;
        }

        public void setModelPackage(String modelPackage) {
            this.modelPackage = modelPackage;
        }

        public String getHbmDir() {
            return hbmDir;
        }

        public void setHbmDir(String hbmDir) {
            this.hbmDir = hbmDir;
        }

        public String getIdColumn() {
            return idColumn;
        }

        public void setIdColumn(String idColumn) {
            this.idColumn = idColumn;
        }

        public String getVersionColumn() {
            return versionColumn;
        }

        public void setVersionColumn(String versionColumn) {
            this.versionColumn = versionColumn;
        }
    }
}
