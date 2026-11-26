
package quanlysinhvien;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    public MainForm() {
        setTitle("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTieuDe = new JLabel(
                "PHẦN MỀM QUẢN LÝ SINH VIÊN",
                SwingConstants.CENTER
        );
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnSinhVien = new JButton("QUẢN LÝ SINH VIÊN");
        JButton btnMonHoc = new JButton("QUẢN LÝ MÔN HỌC");
        JButton btnDiem = new JButton("QUẢN LÝ ĐIỂM");
        JButton btnThoat = new JButton("THOÁT");

        btnSinhVien.addActionListener(e -> {
            new SinhVienForm().setVisible(true);
        });

        btnMonHoc.addActionListener(e -> {
            new MonHocForm().setVisible(true);
        });

        btnDiem.addActionListener(e -> {
            new DiemForm().setVisible(true);
        });

        btnThoat.addActionListener(e -> {
            System.exit(0);
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(
                BorderFactory.createEmptyBorder(40, 100, 40, 100)
        );

        panel.add(btnSinhVien);
        panel.add(btnMonHoc);
        panel.add(btnDiem);
        panel.add(btnThoat);

        setLayout(new BorderLayout(10, 20));
        add(lblTieuDe, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}
