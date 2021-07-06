package com.project.sardscanner.db;

import com.project.sardscanner.SARDScannerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadH2Dao {
    Connection conn;
    PreparedStatement ps;

    public ReadH2Dao(Connection conn) throws SARDScannerException {
        this.conn = conn;
        try {
            ps = conn.prepareStatement("select ci.name as ci_name from code_issues ci, code_issues_occurrences cio, nodes n where " +
                    "cio.code_issue_id = ci.id and cio.file_id=n.id and n.signature=?");
        } catch (SQLException throwables) {
            throw new SARDScannerException(throwables);
        }
    }

    public List<String> getCIForFile(String filePath) throws SQLException {
        List<String> list = new ArrayList<>();
        ps.setString(1, filePath);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("ci_name"));
            }
        }
        return list;
    }
}
