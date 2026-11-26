
package quanlysinhvien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DiemForm extends javax.swing.JFrame {

    public DiemForm() {
        initComponents();

        setLocationRelativeTo(null);

        btnThem.addActionListener(e -> themDiem());
        btnSua.addActionListener(e -> suaDiem());
        btnXoa.addActionListener(e -> xoaDiem());
        btnLamMoi.addActionListener(e -> lamMoi());

        // Chon dong trong bang
        tblDiem.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblDiem.getSelectedRow();

                if (row >= 0) {
                    jTextField1.setText(tblDiem.getValueAt(row, 0).toString());
                    jTextField2.setText(tblDiem.getValueAt(row, 1).toString());
                    jTextField3.setText(tblDiem.getValueAt(row, 2).toString());
                }
            }
        });

        loadDiem();
    }

    // HIEN THI DANH SACH DIEM
    private void loadDiem() {
        DefaultTableModel model =
                (DefaultTableModel) tblDiem.getModel();

        model.setRowCount(0);

        String sql = "SELECT MaSV, MaMon, Diem FROM Diem";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("MaSV"),
                        rs.getString("MaMon"),
                        rs.getDouble("Diem")
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi hiển thị điểm: " + e.getMessage());
        }
    }

    // KIEM TRA DU LIEU
    private boolean kiemTraDuLieu() {
        if (jTextField1.getText().trim().isEmpty()
                || jTextField2.getText().trim().isEmpty()
                || jTextField3.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        try {
            double diem = Double.parseDouble(
                    jTextField3.getText().trim());

            if (diem < 0 || diem > 10) {
                JOptionPane.showMessageDialog(this,
                        "Điểm phải nằm trong khoảng từ 0 đến 10!");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Điểm phải là số!");
            return false;
        }

        return true;
    }

    // THEM DIEM
    private void themDiem() {
        if (!kiemTraDuLieu()) {
            return;
        }

        String sql = "INSERT INTO Diem (MaSV, MaMon, Diem) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, jTextField1.getText().trim());
                ps.setString(2, jTextField2.getText().trim());
                ps.setDouble(3, Double.parseDouble(
                        jTextField3.getText().trim()));

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Thêm điểm thành công!");

            loadDiem();
            lamMoi();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi thêm điểm: " + e.getMessage());
        }
    }

    // SUA DIEM
    private void suaDiem() {
        if (!kiemTraDuLieu()) {
            return;
        }

        String sql = "UPDATE Diem SET Diem=? WHERE MaSV=? AND MaMon=?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDouble(1, Double.parseDouble(
                        jTextField3.getText().trim()));

                ps.setString(2, jTextField1.getText().trim());
                ps.setString(3, jTextField2.getText().trim());

                int kq = ps.executeUpdate();

                if (kq > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Sửa điểm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy điểm cần sửa!");
                }
            }

            loadDiem();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi sửa điểm: " + e.getMessage());
        }
    }

    // XOA DIEM
    private void xoaDiem() {
        String maSV = jTextField1.getText().trim();
        String maMon = jTextField2.getText().trim();

        if (maSV.isEmpty() || maMon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập hoặc chọn mã sinh viên và mã môn học!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa điểm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Diem WHERE MaSV=? AND MaMon=?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, maSV);
                ps.setString(2, maMon);

                int kq = ps.executeUpdate();

                if (kq > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa điểm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy điểm cần xóa!");
                }
            }

            loadDiem();
            lamMoi();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi xóa điểm: " + e.getMessage());
        }
    }

    // LAM MOI
    private void lamMoi() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        tblDiem.clearSelection();
    }

    // GIAO DIEN
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtMaSVInput = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        txtMaMonInput = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        txtDiemInput = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        btnThem = new java.awt.Button();
        btnSua = new java.awt.Button();
        btnXoa = new java.awt.Button();
        btnLamMoi = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDiem = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QUẢN LÝ ĐIỂM");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20));
        jLabel1.setText("QUẢN LÝ ĐIỂM SINH VIÊN");

        txtMaSVInput.setText("Mã sinh viên");
        txtMaMonInput.setText("Mã môn học");
        txtDiemInput.setText("Điểm");

        btnThem.setLabel("Thêm");
        btnSua.setLabel("Sửa");
        btnXoa.setLabel("Xóa");
        btnLamMoi.setLabel("Làm mới");

        tblDiem.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Mã SV", "Mã MH", "Điểm"}
        ));

        jScrollPane1.setViewportView(tblDiem);

        javax.swing.GroupLayout layout =
                new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaSVInput)
                            .addComponent(txtMaMonInput)
                            .addComponent(txtDiemInput))
                        .addGap(18)
                        .addGroup(layout.createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, 250, 250, 250)
                            .addComponent(jTextField2, 250, 250, 250)
                            .addComponent(jTextField3, 250, 250, 250)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSua)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnXoa)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLamMoi))))
                    .addComponent(jScrollPane1,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            500,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17)
                .addComponent(jLabel1)
                .addGap(18)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaSVInput)
                    .addComponent(jTextField1,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaMonInput)
                    .addComponent(jTextField2,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiemInput)
                    .addComponent(jTextField3,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi))
                .addGap(20)
                .addComponent(jScrollPane1,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        250,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() ->
                new DiemForm().setVisible(true));
    }

    // KHAI BAO BIEN
    private java.awt.Button btnLamMoi;
    private java.awt.Button btnSua;
    private java.awt.Button btnThem;
    private java.awt.Button btnXoa;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;

    private javax.swing.JTable tblDiem;

    private javax.swing.JLabel txtDiemInput;
    private javax.swing.JLabel txtMaMonInput;
    private javax.swing.JLabel txtMaSVInput;
}
