package com.project.sardscanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DBConnector {

    public static Connection getConnection(String connStr) throws SARDScannerException {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new SARDScannerException(e);
        }
    }

}
