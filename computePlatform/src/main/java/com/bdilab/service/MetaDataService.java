package com.bdilab.service;

import com.bdilab.model.MySqlModel.TableStructure;

import java.util.List;

public interface MetaDataService {
    List<String> getDataBaseList();
    List<String> getTablesFromDB(String dbName);
    List<TableStructure> getMetaDataFromTables(String dbName);
    List<TableStructure> getMetaDataFromSQLFile(String filePath);
}
