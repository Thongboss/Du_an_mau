/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import entity_edusys.HocVienEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class HocVienDAO extends EduSysDAO<HocVienEntity, String>{
    
    String insert_sql = "INSERT INTO HOCVIEN(MAHV,MANH,MAKH,DIEM) VALUES(?,?,?,?)";
    String update_sql = "UPDATE HOCVIEN SET DIEM = ? WHERE MAHV = ?";
    String delete_sql = "DELETE HOCVIEN WHERE MAHV =?";
    String select_all = "SELECT*FROM HOCVIEN";
    String select_by_id = "SELECT*FROM HOCVIEN WHERE MAHV = ?";

    @Override
    public void insert(HocVienEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(insert_sql, entity.getMaHV(),entity.getMaNH(),entity.getMaKH(),entity.getDiem());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(HocVienEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(update_sql, entity.getDiem(),entity.getMaHV());
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
    public List<HocVienEntity> selectAll() {
        return this.selectBySQL(select_all);
    }

    @Override
    public HocVienEntity selectById(String key) {
        List<HocVienEntity> list = this.selectBySQL(select_by_id, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public List<HocVienEntity> selectByKhoaHoc(String makh){
        String sql = "SELECT*FROM HOCVIEN WHERE MAKH = ?";
        return this.selectBySQL(sql, makh);
    }

    @Override
    protected List<HocVienEntity> selectBySQL(String sql, Object... args) {
        try {
            List<HocVienEntity> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                HocVienEntity entity = new HocVienEntity();
                entity.setMaHV(rs.getString("MAHV"));
                entity.setMaNH(rs.getString("MANH"));
                entity.setMaKH(rs.getString("MAKH"));
                entity.setDiem(rs.getDouble("DIEM"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
}
