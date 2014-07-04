package com.github.abator;

import java.io.InputStream;
import java.sql.*;

/**
 * <ul>
 * <li>http://hsqldb.org/doc/2.0/guide/deployment-chapt.html</li>
 * <li><a
 * href="http://hsqldb.org/doc/2.0/guide/dbproperties-chapt.html">HELP</a></li>
 * </ul>
 */
public class Hsqldb {

    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private String                 databaseName;

    public Hsqldb() {
        databaseName = "abator" + System.currentTimeMillis();
        init();
    }

    private void init() {
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "script/create_table.sql");
            StringBuilder createTableSQL = new StringBuilder();
            byte[] b = new byte[1024];
            while (input.read(b) != -1) {
                createTableSQL.append(new String(b));
            }
            this.execute(createTableSQL.toString());
            LOG.debug("Create table ...");
            InputStream input2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "script/inserts.sql");
            StringBuilder insertsSQL = new StringBuilder();
            byte[] b2 = new byte[1024];
            while (input2.read(b2) != -1) {
                insertsSQL.append(new String(b2));
            }
            this.execute(insertsSQL.toString());
            LOG.debug("Insert ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void execute(String sql) {
        try {
           // LOG.debug(sql);
           // sql = sql.replaceAll("\n\r", "");
            LOG.debug(sql);
            Connection conn = this.getConnection();
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + databaseName
                + ";close_result=true;sql.syntax_mys=true", "sa", "sa");
        if (conn != null) {
            return conn;
        }
        throw new RuntimeException("");
    }

    public static final void main(String args[]) {
        Hsqldb db = new Hsqldb();
        try {
            // 查询数据
            PreparedStatement pstmt = db.getConnection().prepareStatement("SELECT * FROM USER");
            ResultSet rs = pstmt.executeQuery();
            System.out.println("============ 查询结果 ============");
            while (rs.next()) {
                String s = null;
                s = rs.getString(1) + ":" + rs.getString(2);
                System.out.println(s);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

    }
}
