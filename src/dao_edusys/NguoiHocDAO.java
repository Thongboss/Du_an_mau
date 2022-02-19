/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import entity_edusys.NguoiHocEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author PC
 */
public class NguoiHocDAO extends EduSysDAO<NguoiHocEntity, String> {

    String insert_sql = "INSERT INTO NGUOIHOC(MANH,HOTEN,GIOITINH,NGAYS,DT,EMAIL,GHICHU,MANV,NGAYDK) VALUES(?,?,?,?,?,?,?,?,?)";
    String update_sql = "UPDATE NGUOIHOC SET HOTEN = ?, GIOITINH = ?, NGAYS = ?, DT = ?, EMAIL = ?, NGAYDK = ?, GHICHU = ? WHERE MANH = ?";
    String delete_sql = "DELETE NGUOIHOC WHERE MANH = ?";
    String select_all = "SELECT*FROM NGUOIHOC";
    String select_by_id = "SELECT*FROM NGUOIHOC WHERE MANH = ?";

    @Override
    public void insert(NguoiHocEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(insert_sql, entity.getMaNH(), entity.getHoTen(), entity.getGioitinh(), entity.getNgaySinh(), entity.getDienThoai(), entity.getEmail(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayDK());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(NguoiHocEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(update_sql, entity.getHoTen(), entity.getGioitinh(), entity.getNgaySinh(), entity.getDienThoai(), entity.getEmail(), entity.getNgayDK(), entity.getGhiChu(), entity.getMaNH());
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
    public List<NguoiHocEntity> selectAll() {
        return this.selectBySQL(select_all);
    }

    @Override
    public NguoiHocEntity selectById(String key) {
        List<NguoiHocEntity> list = this.selectBySQL(select_by_id, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<NguoiHocEntity> selectNotlnCouse(String makh, String keyword) {
        String sql = "SELECT*FROM NGUOIHOC\n"
                + "WHERE HOTEN LIKE ? AND \n"
                + "MANH NOT IN (SELECT MANH FROM HOCVIEN WHERE MAKH = ?)";
        return this.selectBySQL(sql, "%" + keyword + "%", makh);
    }

    @Override
    protected List<NguoiHocEntity> selectBySQL(String sql, Object... args) {
        try {
            List<NguoiHocEntity> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                NguoiHocEntity entity = new NguoiHocEntity();
                entity.setMaNH(rs.getString("MANH"));
                entity.setHoTen(rs.getString("HOTEN"));
                entity.setGioitinh(rs.getBoolean("GIOITINH"));
                entity.setNgaySinh(rs.getDate("NGAYS"));
                entity.setDienThoai(rs.getString("DT"));
                entity.setEmail(rs.getString("EMAIL"));
                entity.setGhiChu(rs.getString("GHICHU"));
                entity.setMaNV(rs.getString("MANV"));
                entity.setNgayDK(rs.getDate("NGAYDK"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public List<NguoiHocEntity> selectByKeyWord(String keyWord) {
        String sql = "SELECT*FROM NGUOIHOC WHERE HOTEN LIKE ?";
        return this.selectBySQL(sql, "%" + keyWord + "%");
    }

}
