/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giao_dien;

import dao_edusys.NhanVienDAO;
import entity_edusys.NhanVienEntity;
import giao_dien.utils.Auth;
import giao_dien.utils.MsgBox;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class QuanLyNhanVien extends javax.swing.JDialog {

    /**
     * Creates new form QuanLyNhanVien
     */
    NhanVienDAO dao = new NhanVienDAO();
    int row = -1;
    int index = 0;
    int maxindex = 0;
    List<NhanVienEntity> listNV = new ArrayList<NhanVienEntity>();
    List<NhanVienEntity> listNV1 = new ArrayList<NhanVienEntity>();

    public QuanLyNhanVien(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
//        setIconImage(XImage.getAppIcon());
        fillTable();
        this.row = -1;
        if (!Auth.isManager()) {
            updateStatus();
        }
    }

    void setForm(NhanVienEntity nv) {
        txtMaNV.setText(nv.getMaNV());
        txtMatkhau.setText(nv.getMatKhau());
        txtHoten.setText(nv.getHoTen());
        txtxacnhanMK.setText(nv.getMatKhau());
        rdoTruongP.setSelected(nv.getVaiTro());
        rdoNhanvien.setSelected(!nv.getVaiTro());
    }

    NhanVienEntity getForm() {
        NhanVienEntity nv = new NhanVienEntity();
        nv.setMaNV(txtMaNV.getText());
        String mk1 = new String(txtMatkhau.getPassword());
        nv.setMatKhau(mk1);
        nv.setHoTen(txtHoten.getText());
        nv.setVaiTro(rdoTruongP.isSelected());
        return nv;
    }

    void clearForm() {
        NhanVienEntity nv = new NhanVienEntity();
        this.setForm(nv);
        this.row = -1;
//        updateStatus();
    }

    void edit() {
        String manv = (String) tbNhanvien.getValueAt(this.row, 0);
        NhanVienEntity nv = dao.selectById(manv);
        this.setForm(nv);
        this.row = -1;
        tabDanhsach.setSelectedIndex(0);
//        updateStatus();
    }

    void insert() {
        NhanVienEntity nv = getForm();
        if (this.check() == true) {
            return;
        }
        String mk2 = new String(txtxacnhanMK.getPassword());
        String mk1 = new String(txtMatkhau.getPassword());
        if (nv.getMaNV().isEmpty() || nv.getHoTen().isEmpty() || nv.getMatKhau().isEmpty() || mk2.isEmpty()) {
            MsgBox.alert(this, "Thông tin chưa đầy đủ!");
            return;
        }
        if (!mk2.equals(mk1)) {
            MsgBox.alert(this, "Xác nhận mật khẩu sai!");
        } else {
            try {
                dao.insert(nv);
                this.fillTable();
                this.clearForm();
                MsgBox.alert(this, "Thêm mới thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới thất bại!");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    void update() {
        NhanVienEntity nv = getForm();
        String mk2 = new String(txtxacnhanMK.getPassword());
        if (!mk2.equals(nv.getMatKhau())) {
            MsgBox.alert(this, "Xác nhận mật khẩu sai!");
        } else {
            try {
                dao.update(nv);
                this.fillTable();
                MsgBox.alert(this, "Sửa thành công!");
//                this.clearForm();
            } catch (Exception e) {
                MsgBox.alert(this, "Sửa thất bại!");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa nhân viên!");
        } else {
            String manv = txtMaNV.getText();
            if (manv.equals(Auth.user.getMaNV())) {
                MsgBox.alert(this, "Bạn không thể xóa chính bạn!");
            } else if (MsgBox.confirm(this, "Bạn có thực sự muốn xóa nhân viên này không?")) {
                try {
                    dao.delete(manv);
                    fillTable();
                    clearForm();
                    MsgBox.alert(this, "Đã xóa thành công!");
                } catch (Exception e) {
                    MsgBox.alert(this, "Xóa thất bại!");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tbNhanvien.getRowCount() - 1);
        //Trạng thái form
        txtMaNV.setEditable(!edit);
        btAdd.setEnabled(!edit);
        btUpdate.setEnabled(edit);
        btDelete.setEnabled(edit);
        //Trạng thái điều hướng
//        btPrevious.setEnabled(edit && !first)
//        btBack.setEnabled(edit && !first);
//        btNext.setEnabled(edit && !last);
//        btNext2.setEnabled(edit && !last);
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tbNhanvien.getModel();
        model.setRowCount(0);
        try {
            List<NhanVienEntity> list = loadTrang(index);
            for (NhanVienEntity nv : list) {
                String mk = String.valueOf(maHoa(nv.getMatKhau()));
//                System.out.println(mk);
                model.addRow(new Object[]{
                    nv.getMaNV(), mk, nv.getHoTen(), nv.getVaiTro() ? "Trưởng phòng" : "Nhân viên"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    List<NhanVienEntity> loadTrang(int index) {
        List<NhanVienEntity> list = dao.selectAll();
        int test = list.size() / 10;
        int du = list.size() % 10;
        if (du > 0) {
            maxindex = test + 1;
        }
        if (du == 0) {
            maxindex = test;
        }
        lblSotrang.setText(String.valueOf(index+1) + "/" + String.valueOf(maxindex));
        int test1 = list.size();
        listNV1.removeAll(listNV1);
        int start = index * 10;
        int end = (index + 1) * 10;
        for (int i = start; i < end; i++) {
            if (i >= test1) {
                break;
            }
            listNV1.add(list.get(i));
        }
        return listNV1;
    }

    boolean check() {
        String manv = txtMaNV.getText();
        String mk1 = new String(txtMatkhau.getPassword());
        String mk2 = new String(txtxacnhanMK.getPassword());
        String hoten = txtHoten.getText();
        if (manv.isEmpty() || mk1.isEmpty() || mk2.isEmpty() || hoten.isEmpty()) {
            MsgBox.alert(this, "Thông tin chưa đầy đủ!");
            return true;
        }
        List<NhanVienEntity> list = dao.selectAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaNV().equalsIgnoreCase(manv)) {
                MsgBox.alert(this, "Mã nhân viên đã tồn tại!");
                return true;
            }
        }
        return false;
    }

    byte[] maHoa(String password) throws InvalidKeySpecException {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
//            System.out.println(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
//            System.out.println(hash);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tabDanhsach = new javax.swing.JTabbedPane();
        pnUpdate = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtHoten = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdoTruongP = new javax.swing.JRadioButton();
        rdoNhanvien = new javax.swing.JRadioButton();
        btAdd = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btNew = new javax.swing.JButton();
        txtMatkhau = new javax.swing.JPasswordField();
        txtxacnhanMK = new javax.swing.JPasswordField();
        pnDanhS = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbNhanvien = new javax.swing.JTable();
        lblSotrang = new javax.swing.JLabel();
        btBack = new javax.swing.JButton();
        btNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("                                                        QUẢN LÝ NHÂN VIÊN");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN QUẢN TRỊ");

        jLabel2.setText("Mã nhân viên");

        jLabel3.setText("Mât khẩu");

        jLabel4.setText("Xác nhận mật khẩu");

        jLabel5.setText("Họ và tên");

        jLabel6.setText("Vai trò");

        buttonGroup1.add(rdoTruongP);
        rdoTruongP.setText("Trưởng Phòng");

        buttonGroup1.add(rdoNhanvien);
        rdoNhanvien.setSelected(true);
        rdoNhanvien.setText("Nhân Viên");

        btAdd.setText("Thêm");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btUpdate.setText("Sửa");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btDelete.setText("Xóa");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btNew.setText("Mới");
        btNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnUpdateLayout = new javax.swing.GroupLayout(pnUpdate);
        pnUpdate.setLayout(pnUpdateLayout);
        pnUpdateLayout.setHorizontalGroup(
            pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaNV)
                    .addComponent(txtHoten)
                    .addComponent(txtMatkhau)
                    .addComponent(txtxacnhanMK)
                    .addGroup(pnUpdateLayout.createSequentialGroup()
                        .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnUpdateLayout.createSequentialGroup()
                                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95)
                                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addGroup(pnUpdateLayout.createSequentialGroup()
                                    .addComponent(rdoTruongP, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(rdoNhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102)
                        .addComponent(btNew)))
                .addContainerGap())
        );
        pnUpdateLayout.setVerticalGroup(
            pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtxacnhanMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoTruongP)
                    .addComponent(rdoNhanvien))
                .addGap(18, 18, 18)
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd)
                    .addComponent(btUpdate)
                    .addComponent(btDelete)
                    .addComponent(btNew))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        tabDanhsach.addTab("CẬP NHẬT", pnUpdate);

        tbNhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhân viên", "Mật khẩu", "Họ tên", "Vai trò"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbNhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbNhanvienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbNhanvien);

        lblSotrang.setText("lbl số trang");

        btBack.setText("<<");
        btBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBackActionPerformed(evt);
            }
        });

        btNext.setText(">>");
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDanhSLayout = new javax.swing.GroupLayout(pnDanhS);
        pnDanhS.setLayout(pnDanhSLayout);
        pnDanhSLayout.setHorizontalGroup(
            pnDanhSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDanhSLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(pnDanhSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnDanhSLayout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(btBack)
                        .addGap(31, 31, 31)
                        .addComponent(lblSotrang)
                        .addGap(29, 29, 29)
                        .addComponent(btNext))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnDanhSLayout.setVerticalGroup(
            pnDanhSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhSLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDanhSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSotrang)
                    .addComponent(btBack)
                    .addComponent(btNext))
                .addGap(0, 21, Short.MAX_VALUE))
        );

        tabDanhsach.addTab("DANH SÁCH", pnDanhS);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tabDanhsach)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabDanhsach))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbNhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbNhanvienMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tbNhanvien.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tbNhanvienMouseClicked

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btAddActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btNewActionPerformed

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        // TODO add your handling code here:
        index++;
        if (index >= maxindex) {
            index = 0;
        }
        fillTable();
    }//GEN-LAST:event_btNextActionPerformed

    private void btBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBackActionPerformed
        // TODO add your handling code here:
        index--;
        if (index < 0) {
            index = maxindex - 1;
        }
        fillTable();
    }//GEN-LAST:event_btBackActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyNhanVien dialog = new QuanLyNhanVien(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btBack;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btNew;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSotrang;
    private javax.swing.JPanel pnDanhS;
    private javax.swing.JPanel pnUpdate;
    private javax.swing.JRadioButton rdoNhanvien;
    private javax.swing.JRadioButton rdoTruongP;
    private javax.swing.JTabbedPane tabDanhsach;
    private javax.swing.JTable tbNhanvien;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JPasswordField txtMatkhau;
    private javax.swing.JPasswordField txtxacnhanMK;
    // End of variables declaration//GEN-END:variables
}
