
package quanlysinhvien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MonHocForm extends javax.swing.JFrame {

    public MonHocForm() {
        initComponents();
        setLocationRelativeTo(null);

        btnThem.addActionListener(e -> themMonHoc());
        btnSua.addActionListener(e -> suaMonHoc());
        btnXoa.addActionListener(e -> xoaMonHoc());
        btnLamMoi.addActionListener(e -> lamMoi());

        tblMonHoc.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblMonHoc.getSelectedRow();

                if (row >= 0) {
                    txtMaMon.setText(tblMonHoc.getValueAt(row, 0).toString());
                    txtTenMon.setText(tblMonHoc.getValueAt(row, 1).toString());
                    txtSoTC.setText(tblMonHoc.getValueAt(row, 2).toString());
                }
            }
        });

        loadMonHoc();
    }

    // HIEN THI DANH SACH MON HOC
    private void loadMonHoc() {
        DefaultTableModel model =
                (DefaultTableModel) tblMonHoc.getModel();

        model.setRowCount(0);

        String sql = "SELECT MaMon, TenMon, SoTinChi FROM MonHoc";

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
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getInt("SoTinChi")
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi hiển thị môn học: " + e.getMessage());
        }
    }

    // KIEM TRA DU LIEU
    private boolean kiemTraDuLieu() {
        if (txtMaMon.getText().trim().isEmpty()
                || txtTenMon.getText().trim().isEmpty()
                || txtSoTC.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        try {
            int soTC = Integer.parseInt(txtSoTC.getText().trim());

            if (soTC <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Số tín chỉ phải lớn hơn 0!");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Số tín chỉ phải là số nguyên!");
            return false;
        }

        return true;
    }

    // THEM MON HOC
    private void themMonHoc() {
        if (!kiemTraDuLieu()) {
            return;
        }

        String sql = "INSERT INTO MonHoc (MaMon, TenMon, SoTinChi) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaMon.getText().trim());
                ps.setString(2, txtTenMon.getText().trim());
                ps.setInt(3, Integer.parseInt(txtSoTC.getText().trim()));

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Thêm môn học thành công!");

            loadMonHoc();
            lamMoi();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi thêm môn học: " + e.getMessage());
        }
    }

    // SUA MON HOC
    private void suaMonHoc() {
        if (!kiemTraDuLieu()) {
            return;
        }

        String sql = "UPDATE MonHoc SET TenMon=?, SoTinChi=? WHERE MaMon=?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenMon.getText().trim());
                ps.setInt(2, Integer.parseInt(txtSoTC.getText().trim()));
                ps.setString(3, txtMaMon.getText().trim());

                int kq = ps.executeUpdate();

                if (kq > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Sửa môn học thành công!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy môn học cần sửa!");
                }
            }

            loadMonHoc();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi sửa môn học: " + e.getMessage());
        }
    }

    // XOA MON HOC
    private void xoaMonHoc() {
        String maMon = txtMaMon.getText().trim();

        if (maMon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập hoặc chọn mã môn học!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa môn học này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM MonHoc WHERE MaMon=?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối SQL Server!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maMon);

                int kq = ps.executeUpdate();

                if (kq > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa môn học thành công!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy môn học!");
                }
            }

            loadMonHoc();
            lamMoi();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa môn học. Có thể môn học đang được sử dụng trong bảng điểm.\n"
                    + e.getMessage());
        }
    }

    // LAM MOI
    private void lamMoi() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtSoTC.setText("");
        tblMonHoc.clearSelection();
    }

    // GIAO DIEN
    private void initComponents() {

        lblTieuDe = new javax.swing.JLabel();
        lblMaMon = new javax.swing.JLabel();
        txtMaMon = new javax.swing.JTextField();
        lblTenMon = new javax.swing.JLabel();
        txtTenMon = new javax.swing.JTextField();
        lblSoTinChi = new javax.swing.JLabel();
        txtSoTC = new javax.swing.JTextField();

        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();

        jScrollPane1 = new javax.swing.JScrollPane();
        tblMonHoc = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUẢN LÝ MÔN HỌC");

        lblTieuDe.setFont(new java.awt.Font("Segoe UI", 1, 22));
        lblTieuDe.setText("QUẢN LÝ MÔN HỌC");

        lblMaMon.setText("Mã môn học:");
        lblTenMon.setText("Tên môn học:");
        lblSoTinChi.setText("Số tín chỉ:");

        btnThem.setText("Thêm");
        btnSua.setText("Sửa");
        btnXoa.setText("Xóa");
        btnLamMoi.setText("Làm mới");

        tblMonHoc.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Mã môn học", "Tên môn học", "Số tín chỉ"
            }
        ));
        jScrollPane1.setViewportView(tblMonHoc);

        javax.swing.GroupLayout layout =
                new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTieuDe)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaMon)
                            .addComponent(lblTenMon)
                            .addComponent(lblSoTinChi))
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaMon, 250, 250, 250)
                            .addComponent(txtTenMon, 250, 250, 250)
                            .addComponent(txtSoTC, 250, 250, 250)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addGap(10)
                        .addComponent(btnSua)
                        .addGap(10)
                        .addComponent(btnXoa)
                        .addGap(10)
                        .addComponent(btnLamMoi))
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
                .addGap(20)
                .addComponent(lblTieuDe)
                .addGap(20)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaMon)
                    .addComponent(txtMaMon,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenMon)
                    .addComponent(txtTenMon,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12)
                .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoTinChi)
                    .addComponent(txtSoTC,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20)
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
                .addGap(20))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() ->
                new MonHocForm().setVisible(true));
    }

    // KHAI BAO BIEN
    private javax.swing.JLabel lblTieuDe;
    private javax.swing.JLabel lblMaMon;
    private javax.swing.JLabel lblTenMon;
    private javax.swing.JLabel lblSoTinChi;

    private javax.swing.JTextField txtMaMon;
    private javax.swing.JTextField txtTenMon;
    private javax.swing.JTextField txtSoTC;

    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnLamMoi;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMonHoc;
}
