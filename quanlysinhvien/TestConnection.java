
package quanlysinhvien;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("KET NOI SQL SERVER THANH CONG!");
        } else {
            System.out.println("KET NOI THAT BAI!");
        }
    }
}
