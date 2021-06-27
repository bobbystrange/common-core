package org.dreamcat.common.legacy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

enum DatabaseEnum {

    // jdbc:oracle:thin:@localhost:1521:testdb
    // jdbc:oracle:oci:@testdb
    Oracle("oracle.jdbc.driver.OracleDriver"),

    // jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=gbk
    MySQL("com.mysql.jdbc.Driver"),

    // jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=testdb
    SQLServer("com.microsoft.sqlserver.jdbc.SQLServerDriver"),

    // jdbc:db2://localhost:5000/testdb
    DB2("com.ibm.db2.jdbc.app.DB2Driver"),

    // jdbc:postgresql://localhost/testdb
    PostgreSQL("org.postgresql.Driver"),

    // jdbc:sybase:Tds:localhost:4500/testdb
    Sybase("com.sybase.jdbc3.jdbc.SybDriver"),

    // jdbc:odbc:driver={MicroSoft Access Driver (*.mdb)};DBQ=/../test.mdb
    // jdbc:odbc:driver={Microsoft Excel Driver (*.xls)};DBQ=/../test.xls
    Access("sun.jdbc.odbc.JdbcOdbcDriver"),

    // jdbc:sqlite:/../test.db
    Sqlite("org.sqlite.JDBC"),

    // jdbc:firebirdsql:embedded:/../test.fdb
    Firebird("org.firebirdsql.jdbc.FBDriver"),

    // jdbc:h2:file:/../test.db
    // jdbc:h2:mem:/../test.db  memory
    // jdbc:h2:tcp://localhost:8084/testdb
    // jdbc:h2:ssl://localhost:8085/testdb
    H2("org.h2.Driver"),

    // jdbc:derby;/../test.db;create=true,user=,password=;
    DerbyEmbedded("org.apache.derby.jdbc.EmbeddedDriver"),

    // jdbc:derby;//localhost:1527/testdb;create=true,user=,password=;
    Derby("org.apache.derby.jdbc.ClientDriver");

    private final String value;

    DatabaseEnum(String value) {
        this.value = value;
    }

    public static Statement openStatement(String sql, DatabaseEnum kind, String url,
            String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(kind.toString());
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            return connection.prepareStatement(sql);
        }
    }

    public static Statement openStatement(String sql, DatabaseEnum kind, String url)
            throws ClassNotFoundException, SQLException {
        return openStatement(sql, kind, url, null, null);
    }

    @Override
    public String toString() {
        return value;
    }
}
