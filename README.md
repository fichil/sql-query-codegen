# sql-query-codegen

A Java code generator for generating QueryCondition, QueryItem, Model and HBM mappings
from MySQL SQL table definitions.

## Background

This project is an internal open-source utility used to standardize query-related
Java code generation across projects.

## Features

- Input: MySQL SQL DDL (Navicat / dump files)
- Output:
  - QueryCondition
  - QueryItem
  - Model (Hibernate/JPA style)
  - HBM sql-query
- Rule-based generation
- Only time fields generate From / To conditions
- No database connection required

## License

MIT
