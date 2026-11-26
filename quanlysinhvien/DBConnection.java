
package quanlysinhvien;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "instanceName=SQLEXPRESS;"
                    + "databaseName=QuanLySinhVien;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

            String user = "qlsv_user";

            // Thay bằng mật khẩu thật của tài khoản qlsv_user
            String password = "Minh@12345678";

            conn = DriverManager.getConnection(url, user, password);

            System.out.println("KET NOI SQL SERVER THANH CONG!");

        } catch (SQLException e) {
            System.out.println("LOI KET NOI SQL SERVER!");
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
