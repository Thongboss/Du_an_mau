/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import entity_edusys.ChuyenDeEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author PC
 */
public class ChuynDeDAO extends EduSysDAO<ChuyenDeEntity, String>{
    
    String insert_sql = "INSERT INTO CHUYENDE(MACD,TENCD,HOCPHI,THOILUONG,HINH,MOTA) VALUES(?,?,?,?,?,?)";
    String update_sql = "UPDATE CHUYENDE SET TENCD = ?, HOCPHI = ?, THOILUONG = ?, HINH =?, MOTA = ? WHERE MACD = ?";
    String delete_sql = "DELETE CHUYENDE WHERE  MACD = ?";
    String select_all = "SELECT * FROM CHUYENDE";
    String select_by_id = "SELECT * FROM CHUYENDE WHERE MACD = ?";

    @Override
    public void insert(ChuyenDeEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(insert_sql, entity.getMaCD(),entity.getTenCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getHinh(),entity.getMoTa());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(ChuyenDeEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(update_sql, entity.getTenCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getHinh(),entity.getMoTa(),entity.getMaCD());
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
    public List<ChuyenDeEntity> selectAll() {
        return this.selectBySQL(select_all);
    }

    @Override
    public ChuyenDeEntity selectById(String key) {
        List<ChuyenDeEntity> list = this.selectBySQL(select_by_id, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    protected List<ChuyenDeEntity> selectBySQL(String sql, Object... args) {
        try {
            List<ChuyenDeEntity> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                ChuyenDeEntity entity = new ChuyenDeEntity();
                entity.setMaCD(rs.getString("MACD"));
                entity.setTenCD(rs.getString("TENCD"));
                entity.setHocPhi(rs.getDouble("HOCPHI"));
                entity.setThoiLuong(rs.getInt("THOILUONG"));
                entity.setHinh(rs.getString("HINH"));
                entity.setMoTa(rs.getString("MOTA"));
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
