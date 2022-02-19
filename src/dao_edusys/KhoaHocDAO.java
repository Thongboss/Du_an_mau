/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import entity_edusys.KhoaHocEntity;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;


/**
 *
 * @author PC
 */
public class KhoaHocDAO extends EduSysDAO<KhoaHocEntity, String>{
    
    String insert_sql = "INSERT INTO KHOAHOC(MAKH,MACD,HOCPHI,THOILUONG,NGAYKG,GHICHU,NGAYTAO,MANV) VALUES(?,?,?,?,?,?,?,?)";
    String update_sql = "UPDATE KHOAHOC SET HOCPHI = ?, THOILUONG = ?, NGAYKG = ?, GHICHU = ?, NGAYTAO = ? WHERE MAKH = ?";
    String delete_sql = "DELETE KHOAHOC WHERE MAKH = ?";
    String select_all = "SELECT*FROM KHOAHOC";
    String select_by_id = "SELECT*FROM KHOAHOC WHERE MAKH = ?";

    @Override
    public void insert(KhoaHocEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(insert_sql, entity.getMaKH(),entity.getMaCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getNgayTao(),entity.getMaNV());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(KhoaHocEntity entity) {
        try {
            jdbcHelper.JDBC_Helper.update(update_sql, entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getNgayTao(),entity.getMaKH());
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
    public List<KhoaHocEntity> selectAll() {
        return this.selectBySQL(select_all);
    }

    @Override
    public KhoaHocEntity selectById(String key) {
        List<KhoaHocEntity> list = this.selectBySQL(select_by_id, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public List<KhoaHocEntity> selectByChuyenDe(String macd){
        String sql = "SELECT*FROM KHOAHOC WHERE MACD = ?";
        return  this.selectBySQL(sql, macd);
    }
    
    public List<Integer> selectYear(){
        String sql = "SELECT DISTINCT YEAR(NGAYKG) AS NAM FROM KHOAHOC ORDER BY NAM DESC";
        List<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql);
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<KhoaHocEntity> selectBySQL(String sql, Object... args) {
        try {
            List<KhoaHocEntity> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                KhoaHocEntity entity = new KhoaHocEntity();
                entity.setMaKH(rs.getString("MAKH"));
                entity.setMaCD(rs.getString("MACD"));
                entity.setHocPhi(rs.getDouble("HOCPHI"));
                entity.setThoiLuong(rs.getInt("THOILUONG"));
                entity.setNgayKG(rs.getDate("NGAYKG"));
                entity.setGhiChu(rs.getString("GHICHU"));
                entity.setNgayTao(rs.getDate("NGAYTAO"));
                entity.setMaNV(rs.getString("MANV"));
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
