/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import entity_edusys.NhanVienEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


/**
 *
 * @author PC
 */
public class NhanVienDAO extends EduSysDAO<NhanVienEntity, String>{
    
    String insert_sql = "INSERT INTO NHANVIEN(MANV,MATKHAU,HOTEN,VAITRO) VALUES(?,?,?,?)";
    String update_sql = "UPDATE NHANVIEN SET MATKHAU = ?, HOTEN = ?, VAITRO = ? WHERE MANV = ?";
    String delete_sql = "DELETE NHANVIEN WHERE MANV = ?";
    String select_all = "SELECT*FROM NHANVIEN";
    String select_by_id = "SELECT*FROM NHANVIEN WHERE MANV = ?";

    @Override
    public void insert(NhanVienEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(insert_sql, entity.getMaNV(),entity.getMatKhau(),entity.getHoTen(),entity.getVaiTro());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(NhanVienEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(update_sql, entity.getMatKhau(),entity.getHoTen(),entity.getVaiTro(),entity.getMaNV());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String key) {
        try {
            jdbcHelper.JDBC_Helper.update(delete_sql, key);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<NhanVienEntity> selectAll() {
        return this.selectBySQL(select_all);
    }

    @Override
    public NhanVienEntity selectById(String key) {
        List<NhanVienEntity> list = this.selectBySQL(select_by_id, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    protected List<NhanVienEntity> selectBySQL(String sql, Object... args) {
        try {
            List<NhanVienEntity> lstNhanVienEntitys = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                NhanVienEntity entity = new NhanVienEntity();
                entity.setMaNV(rs.getString("MANV"));
                entity.setMatKhau(rs.getString("MATKHAU"));
                entity.setHoTen(rs.getString("HOTEN"));
                entity.setVaiTro(rs.getBoolean("VAITRO"));
                lstNhanVienEntitys.add(entity);
            }
            rs.getStatement().getConnection().close();
            return lstNhanVienEntitys;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
}
