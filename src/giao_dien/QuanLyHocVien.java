/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giao_dien;

import dao_edusys.ChuynDeDAO;
import dao_edusys.HocVienDAO;
import dao_edusys.KhoaHocDAO;
import dao_edusys.NguoiHocDAO;
import entity_edusys.ChuyenDeEntity;
import entity_edusys.HocVienEntity;
import entity_edusys.KhoaHocEntity;
import entity_edusys.NguoiHocEntity;
import giao_dien.utils.Auth;
import giao_dien.utils.MsgBox;
import giao_dien.utils.XImage;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class QuanLyHocVien extends javax.swing.JDialog {

    /**
     * Creates new form QuanLyHocVien
     */
    ChuynDeDAO cddao = new ChuynDeDAO();
    KhoaHocDAO khdao = new KhoaHocDAO();
    NguoiHocDAO nhdao = new NguoiHocDAO();
    HocVienDAO hvdao = new HocVienDAO();
    int row;
    double diem;

    public QuanLyHocVien(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
//        setIconImage(XImage.getAppIcon());
        fillComboBoxChuyenDe();
    }

    void fillComboBoxChuyenDe() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbChuyende.getModel();
        model.removeAllElements();
        List<ChuyenDeEntity> list = cddao.selectAll();
        for (ChuyenDeEntity cd : list) {
            model.addElement(cd);
        }
        this.fillComboBoxKhoaHoc();
    }

    void fillComboBoxKhoaHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbKhoahoc.getModel();
        model.removeAllElements();
        ChuyenDeEntity cd = (ChuyenDeEntity) cbbChuyende.getSelectedItem();
        if (cd != null) {
            List<KhoaHocEntity> list = khdao.selectByChuyenDe(cd.getMaCD());
            for (KhoaHocEntity kho : list) {
                model.addElement(kho);
            }
            this.fillTableHocVien();
        }
    }

    void fillTableHocVien() {
        DefaultTableModel model = (DefaultTableModel) tbHocvien.getModel();
        model.setRowCount(0);
        KhoaHocEntity kh = (KhoaHocEntity) cbbKhoahoc.getSelectedItem();
        if (kh != null) {
            List<HocVienEntity> list = hvdao.selectByKhoaHoc(kh.getMaKH());
            for (int i = 0; i < list.size(); i++) {
                HocVienEntity hv = list.get(i);
                String hoten = nhdao.selectById(hv.getMaNH()).getHoTen();
                model.addRow(new Object[]{
                    hv.getMaHV(), hv.getMaKH(), hoten, hv.getDiem()
                });
            }
            this.fillTableNguoiHoc();
        }
    }

    void fillTableNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tbNguoihoc.getModel();
        model.setRowCount(0);
        KhoaHocEntity khoahoc = (KhoaHocEntity) cbbKhoahoc.getSelectedItem();
        String keyword = txtFind.getText();
        List<NguoiHocEntity> list = nhdao.selectNotlnCouse(khoahoc.getMaKH(), keyword);
        for (NguoiHocEntity nh : list) {
            model.addRow(new Object[]{
                nh.getMaNH(), nh.getHoTen(), nh.getDienThoai(), nh.getEmail()
            });
        }
    }

    void addHocVien() {
        KhoaHocEntity khoa = (KhoaHocEntity) cbbKhoahoc.getSelectedItem();
        List<HocVienEntity> list = hvdao.selectAll();
        String ma = list.get(list.size() - 1).getMaHV();
        int mat = Integer.parseInt(ma.substring(2));
        for (int row : tbNguoihoc.getSelectedRows()) {
            HocVienEntity hv = new HocVienEntity();
            mat++;
            String mahv = "HV" + String.valueOf(mat);
            hv.setMaHV(mahv);
            hv.setMaKH(khoa.getMaKH());
            hv.setDiem(0);
            hv.setMaNH((String) tbNguoihoc.getValueAt(row, 0));
            hvdao.insert(hv);
        }
        this.fillTableHocVien();
        this.tabNay.setSelectedIndex(0);
    }

    void removeHocVien() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa học viên!");
        } else {
            if (MsgBox.confirm(this, "Bạn có muốn xóa học viên được chọn?")) {
                for (int row : tbHocvien.getSelectedRows()) {
                    String mahv = (String) tbHocvien.getValueAt(row, 0);
                    hvdao.delete(mahv);
                }
                this.fillTableHocVien();
            }
        }
    }

//    void updateDiem() {
//        try {
//
////            for (int i = 0; i < tbHocvien.getRowCount(); i++) {
////                String mahv = (String) tbHocvien.getValueAt(i, 0);
////                String diem = tbHocvien.getValueAt(i, 3).toString();
////                HocVienEntity hv = hvdao.selectById(mahv);
////                hv.setDiem(Double.parseDouble(diem));
////                hvdao.update(hv);
////            }
//            String mahv = (String) tbHocvien.getValueAt(row, 0);
//            HocVienEntity hv = hvdao.selectById(mahv);
//            hv.setDiem(diem);
//            hvdao.update(hv);
//            MsgBox.alert(this, "Cập nhật điểm thành công!");
//        } catch (Exception e) {
//            MsgBox.alert(this, "cập nhật thất bại!");
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbbChuyende = new javax.swing.JComboBox<>();
        cbbKhoahoc = new javax.swing.JComboBox<>();
        tabNay = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbHocvien = new javax.swing.JTable();
        btxoaNH = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtFind = new javax.swing.JTextField();
        btFind = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbNguoihoc = new javax.swing.JTable();
        btAddKH = new javax.swing.JButton();
        btBack = new javax.swing.JButton();
        lblSotrang = new javax.swing.JLabel();
        btNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("                                                               QUẢN LÝ HỌC VIÊN");

        cbbChuyende.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbChuyendeItemStateChanged(evt);
            }
        });
        cbbChuyende.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbChuyendeActionPerformed(evt);
            }
        });

        tbHocvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã học viên", "Mã khóa học", "Họ tên", "điểm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbHocvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbHocvienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbHocvien);

        btxoaNH.setText("Xóa khỏi khóa học");
        btxoaNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btxoaNHActionPerformed(evt);
            }
        });

        jLabel1.setText("Click 2 lần vào bảng để sửa điểm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btxoaNH, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btxoaNH)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        tabNay.addTab("HỌC VIÊN", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        btFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search.png"))); // NOI18N
        btFind.setText("Tìm kiếm");
        btFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btFind)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btFind, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbNguoihoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NH", "Họ Tên", "Điện thoại", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbNguoihoc);

        btAddKH.setText("Thêm vào khóa học");
        btAddKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddKHActionPerformed(evt);
            }
        });

        btBack.setText("<<");

        lblSotrang.setText("1/1");

        btNext.setText(">>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSotrang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btAddKH, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAddKH)
                    .addComponent(btBack)
                    .addComponent(lblSotrang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btNext))
                .addContainerGap())
        );

        tabNay.addTab("NGƯỜI HỌC", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(cbbChuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(cbbKhoahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabNay)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbChuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbKhoahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabNay))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btxoaNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btxoaNHActionPerformed
        // TODO add your handling code here:
        removeHocVien();
    }//GEN-LAST:event_btxoaNHActionPerformed

    private void btAddKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddKHActionPerformed
        // TODO add your handling code here:
        addHocVien();
    }//GEN-LAST:event_btAddKHActionPerformed

    private void btFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFindActionPerformed
        // TODO add your handling code here:
        fillTableNguoiHoc();
    }//GEN-LAST:event_btFindActionPerformed

    private void cbbChuyendeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbChuyendeItemStateChanged
        // TODO add your handling code here:
        fillComboBoxKhoaHoc();
    }//GEN-LAST:event_cbbChuyendeItemStateChanged

    private void tbHocvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbHocvienMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            row = tbHocvien.getSelectedRow();
            diem = Double.parseDouble(MsgBox.prompt(this, "Mời bạn nhập điểm: "));
            if (row < 0) {
                MsgBox.alert(this, "bạn chưa chọn sinh viên cần cập nhật điểm!");
                return;
            }
            if (0 > diem || diem > 10) {
                MsgBox.alert(this, "Điểm phải từ 0 đến 10");
                return;
            }
            try {
                String mahv = (String) tbHocvien.getValueAt(row, 0);
                HocVienEntity hv = hvdao.selectById(mahv);
                hv.setDiem(diem);
                hvdao.update(hv);
                MsgBox.alert(this, "Cập nhật điểm thành công!");
                fillTableHocVien();
            } catch (Exception e) {
                MsgBox.alert(this, "cập nhật thất bại!");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
//            updateDiem();
        }
    }//GEN-LAST:event_tbHocvienMouseClicked

    private void cbbChuyendeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbChuyendeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbChuyendeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyHocVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyHocVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyHocVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyHocVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyHocVien dialog = new QuanLyHocVien(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddKH;
    private javax.swing.JButton btBack;
    private javax.swing.JButton btFind;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btxoaNH;
    private javax.swing.JComboBox<String> cbbChuyende;
    private javax.swing.JComboBox<String> cbbKhoahoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSotrang;
    private javax.swing.JTabbedPane tabNay;
    private javax.swing.JTable tbHocvien;
    private javax.swing.JTable tbNguoihoc;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}
