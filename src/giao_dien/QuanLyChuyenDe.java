/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giao_dien;

import dao_edusys.ChuynDeDAO;
import dao_edusys.EduSysDAO;
import entity_edusys.ChuyenDeEntity;
import giao_dien.utils.Auth;
import giao_dien.utils.MsgBox;
import giao_dien.utils.XImage;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class QuanLyChuyenDe extends javax.swing.JDialog {

    /**
     * Creates new form QuanLyChuyenDe
     */
    EduSysDAO<ChuyenDeEntity,String> dao = new ChuynDeDAO();
    int row = -1;
    JFileChooser fileChooser = new JFileChooser();

    public QuanLyChuyenDe(java.awt.Frame parent, boolean modal) {
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

    void setForm(ChuyenDeEntity cd) {
        double diem = 7.6;
        String anh = cd.getHinh();
        txtMaCD.setText(cd.getMaCD());
        txtTenCD.setText(cd.getTenCD());
        txtHocphi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiL.setText(String.valueOf(cd.getThoiLuong()));
        txtMoTa.setText(cd.getMoTa());
        if (cd.getHinh() != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(anh).getImage().getScaledInstance(lblAnhCD.getWidth(), lblAnhCD.getHeight(), Image.SCALE_DEFAULT));
            lblAnhCD.setIcon(icon);
        }
    }

    ChuyenDeEntity getForm() {
        ChuyenDeEntity cd = new ChuyenDeEntity();
        cd.setMaCD(txtMaCD.getText());
        cd.setTenCD(txtTenCD.getText());
        cd.setHocPhi(Double.parseDouble(txtHocphi.getText()));
        cd.setThoiLuong(Integer.parseInt(txtThoiL.getText()));
        cd.setMoTa(txtMoTa.getText());
        cd.setHinh(lblAnhCD.getToolTipText());
        return cd;
    }

    void clearForm() {
        ChuyenDeEntity cd = new ChuyenDeEntity();
        this.setForm(cd);
        this.row = -1;
//        updateStatus();
    }

    void edit() {
        String macd = (String) tbChuyenDe.getValueAt(this.row, 0);
        ChuyenDeEntity cd = dao.selectById(macd);
        this.setForm(cd);
        this.row = -1;
        tabCapnhat.setSelectedIndex(0);
//        updateStatus();
    }

    void chonAnh() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.read(file.getName());
            lblAnhCD.setIcon(icon);
            lblAnhCD.setToolTipText(file.getName());
        }
    }

    void insert() {
        ChuyenDeEntity cd = getForm();
        if (this.check() == true) {
            return;
        }
        try {
            dao.insert(cd);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void update() {
        ChuyenDeEntity cd = getForm();
        try {
            dao.update(cd);
            this.fillTable();
            MsgBox.alert(this, "Sửa thành công!");
//                this.clearForm();
        } catch (Exception e) {
            MsgBox.alert(this, "Sửa thất bại!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa chuyên đề!");
        } else {
            String macd = txtMaCD.getText();
            MsgBox.confirm(this, "Bạn có thực sự muốn xóa chuyên đề này không?");
                try {
                    dao.delete(macd);
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

    void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tbChuyenDe.getRowCount() - 1);
        //Trạng thái form
        txtMaCD.setEditable(!edit);
        btThem.setEnabled(!edit);
        btSua.setEnabled(!edit);
        btXoa.setEnabled(edit);
        //Trạng thái điều hướng
        btBack2.setEnabled(edit && !first);
        btback.setEnabled(edit && !first);
        btNext.setEnabled(edit && !last);
        btNext2.setEnabled(edit && !last);
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tbChuyenDe.getModel();
        model.setRowCount(0);
        boolean vt = true;
        try {
            List<ChuyenDeEntity> list = dao.selectAll();
            for (ChuyenDeEntity cd : list) {
                model.addRow(new Object[]{
                    cd.getMaCD(),cd.getTenCD(),cd.getHocPhi(),cd.getThoiLuong(),cd.getHinh(),cd.getMoTa()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    boolean check(){
        if (txtMaCD.getText().isEmpty() || txtTenCD.getText().isEmpty() || txtHocphi.getText().isEmpty() || txtThoiL.getText().isEmpty()) {
            MsgBox.alert(this, "Bạn chưa điền đủ thông tin!");
            return true;
        }
        List<ChuyenDeEntity> list = dao.selectAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaCD().equalsIgnoreCase(txtMaCD.getText())) {
                MsgBox.alert(this, "Mã chuyên đề đã tồn tại!");
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        tabCapnhat = new javax.swing.JTabbedPane();
        pnUpdate = new javax.swing.JPanel();
        lblAnhCD = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtMaCD = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTenCD = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtHocphi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtThoiL = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        btThem = new javax.swing.JButton();
        btSua = new javax.swing.JButton();
        btXoa = new javax.swing.JButton();
        btMoi = new javax.swing.JButton();
        btBack2 = new javax.swing.JButton();
        btback = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        btNext2 = new javax.swing.JButton();
        pnDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbChuyenDe = new javax.swing.JTable();

        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("                                                           QUẢN LÝ CHUYÊN ĐỀ");

        lblAnhCD.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 0)));
        lblAnhCD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhCDMouseClicked(evt);
            }
        });

        jLabel1.setText("Mã chuyên đề");

        jLabel2.setText("Tên chuyên đề");

        jLabel3.setText("Học phí");

        jLabel4.setText("Thời lượng");

        jLabel5.setText("Mô tả");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        btThem.setText("Thêm");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btSua.setText("Sửa");
        btSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSuaActionPerformed(evt);
            }
        });

        btXoa.setText("Xóa");
        btXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXoaActionPerformed(evt);
            }
        });

        btMoi.setText("Mới");
        btMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMoiActionPerformed(evt);
            }
        });

        btBack2.setText("|<");
        btBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBack2ActionPerformed(evt);
            }
        });

        btback.setText("<<");
        btback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbackActionPerformed(evt);
            }
        });

        btNext.setText(">>");
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextActionPerformed(evt);
            }
        });

        btNext2.setText(">|");
        btNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNext2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnUpdateLayout = new javax.swing.GroupLayout(pnUpdate);
        pnUpdate.setLayout(pnUpdateLayout);
        pnUpdateLayout.setHorizontalGroup(
            pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnUpdateLayout.createSequentialGroup()
                        .addComponent(lblAnhCD, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(txtMaCD)
                            .addComponent(jLabel2)
                            .addComponent(txtTenCD)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnUpdateLayout.createSequentialGroup()
                                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHocphi, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtThoiL, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2)
                    .addGroup(pnUpdateLayout.createSequentialGroup()
                        .addComponent(btThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btMoi)
                        .addGap(35, 35, 35)
                        .addComponent(btBack2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btback)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(btNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btNext2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnUpdateLayout.setVerticalGroup(
            pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnUpdateLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTenCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHocphi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtThoiL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblAnhCD, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(pnUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThem)
                    .addComponent(btSua)
                    .addComponent(btXoa)
                    .addComponent(btMoi)
                    .addComponent(btBack2)
                    .addComponent(btback)
                    .addComponent(btNext)
                    .addComponent(btNext2))
                .addContainerGap())
        );

        tabCapnhat.addTab("CẬP NHẬT", pnUpdate);

        tbChuyenDe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã CD", "Tên CD", "Học phí", "Thời lượng", "Hình", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbChuyenDe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbChuyenDeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbChuyenDe);

        javax.swing.GroupLayout pnDanhsachLayout = new javax.swing.GroupLayout(pnDanhsach);
        pnDanhsach.setLayout(pnDanhsachLayout);
        pnDanhsachLayout.setHorizontalGroup(
            pnDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhsachLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        pnDanhsachLayout.setVerticalGroup(
            pnDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDanhsachLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tabCapnhat.addTab("DANH SÁCH", pnDanhsach);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabCapnhat)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabCapnhat)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblAnhCDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhCDMouseClicked
        // TODO add your handling code here:
        chonAnh();
    }//GEN-LAST:event_lblAnhCDMouseClicked

    private void tbChuyenDeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbChuyenDeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tbChuyenDe.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tbChuyenDeMouseClicked

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btThemActionPerformed

    private void btSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btSuaActionPerformed

    private void btXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btXoaActionPerformed

    private void btMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btMoiActionPerformed

    private void btBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBack2ActionPerformed
        // TODO add your handling code here:
        this.row = 0;
        edit();
    }//GEN-LAST:event_btBack2ActionPerformed

    private void btbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbackActionPerformed
        // TODO add your handling code here:
        row = tbChuyenDe.getSelectedRow();
        if (this.row > 0) {
           row--;
           edit();
        }
    }//GEN-LAST:event_btbackActionPerformed

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        // TODO add your handling code here:
        row = tbChuyenDe.getSelectedRow();
        if (row < tbChuyenDe.getRowCount()-1) {
            row++;
            edit();
        }
    }//GEN-LAST:event_btNextActionPerformed

    private void btNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNext2ActionPerformed
        // TODO add your handling code here:
        row = tbChuyenDe.getRowCount()-1;
        edit();
    }//GEN-LAST:event_btNext2ActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyChuyenDe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyChuyenDe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyChuyenDe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyChuyenDe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyChuyenDe dialog = new QuanLyChuyenDe(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btBack2;
    private javax.swing.JButton btMoi;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btNext2;
    private javax.swing.JButton btSua;
    private javax.swing.JButton btThem;
    private javax.swing.JButton btXoa;
    private javax.swing.JButton btback;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAnhCD;
    private javax.swing.JPanel pnDanhsach;
    private javax.swing.JPanel pnUpdate;
    private javax.swing.JTabbedPane tabCapnhat;
    private javax.swing.JTable tbChuyenDe;
    private javax.swing.JTextField txtHocphi;
    private javax.swing.JTextField txtMaCD;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtTenCD;
    private javax.swing.JTextField txtThoiL;
    // End of variables declaration//GEN-END:variables
}
