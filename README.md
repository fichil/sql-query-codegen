sql-query-codegen

一个 基于 MySQL DDL 的代码生成工具，用于从数据库建表 SQL 自动生成：

QueryCondition（查询条件对象，时间字段自动生成 From / To）

QueryItem（查询结果 Item）

Model Entity（JPA + Hibernate 注解，符合既有项目规范）

Hibernate sql-query HBM（全字段 select + 动态 where）

目标：减少重复手写 Model / Query / HBM SQL 的时间成本，保证结构一致性。

Features

解析 MySQL CREATE TABLE DDL

自动识别字段类型（String / Long / Double / Date）

时间字段自动生成 From / To 查询条件

生成符合企业规范的 Model：

@Entity

@Table

@Column

@Version

@GenericGenerator

生成 Hibernate <sql-query> HBM：

全字段 select

每个字段一条动态条件

ID / Version 字段支持配置或自动猜测

纯 Java 实现，无数据库依赖

Project Structure
sql-query-codegen
├── src/main/java/org.demo.codegen
│   ├── Main.java
│   ├── config
│   ├── generator
│   ├── meta
│   ├── parser
│   └── util
├── src/main/resources/input
│   └── *.sql
├── output
│   ├── model
│   ├── query
│   └── hbm
└── README.md

Quick Start
1️⃣ 准备 DDL

src/main/resources/input/cd_wh_loc.sql

CREATE TABLE cd_wh_loc (
  JOB_ID bigint,
  LOC_CODE varchar(64),
  CREATE_TIME datetime,
  REC_VER bigint
);

2️⃣ 配置 codegen.yml
input:
  sqlFiles:
    - src/main/resources/input   # 支持目录，自动读取所有 .sql

output:
  dir: output
  queryPackage: query
  modelPackage: model
  hbmDir: output/hbm
  idColumn: JOB_ID
  versionColumn: REC_VER


不配置 tables 时，默认生成 SQL 中的所有表。

3️⃣ 运行
java -cp target/classes org.demo.codegen.Main

Generated Output
✅ Model（节选）
@Entity
@Table(name = "CD_WH_LOC")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class CdWhLocModel extends BaseModel implements OperationLog {

    // 主键
    private Long jobId;

    @Id
    @GeneratedValue(generator = "CommonGenerator")
    @Column(name = "JOB_ID")
    public Long getJobId() { ... }
}

✅ QueryCondition（时间字段自动拆分）
private Date createTime;
private Date createTimeFrom;
private Date createTimeTo;

✅ Hibernate sql-query HBM
<sql-query name="CdWhLocQuery">
<![CDATA[

select cwl.job_id,
       cwl.loc_code,
       cwl.create_time
from cd_wh_loc cwl
where 1=1

<<and cwl.create_time >= :createTimeFrom>>
<<and cwl.create_time <= :createTimeTo>>

order by cwl.job_id asc

]]>
</sql-query>

Design Notes

不连接数据库，仅依赖 DDL

输出结构偏向企业项目规范

时间字段统一使用 From / To

使用 <sql-query>，避免 Criteria / HQL 复杂度

ID / Version 支持显式配置，避免误判

Limitations

仅支持 MySQL DDL

不解析外键 / 索引

不处理多表 join（刻意保持简单）

Roadmap

列注释解析（COMMENT 'xxx'）

like / in / between 条件策略化

多表 join SQL 生成

输出目录按 module 拆分

Maven Plugin 化

License

MIT License