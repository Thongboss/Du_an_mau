/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_edusys;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class ThongKeDAO {
    private List<Object[]> getListOfArray(String sql, String[] cols,Object... args){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.JDBC_Helper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public List<Object[]> getBangDiem(String makh){
        String sql = "{CALL SP_BANGDIEM(?)}";
        String cols[] = {"MANH","HOTEN","DIEM"};
        return this.getListOfArray(sql, cols, makh);
    }
    public List<Object[]> getLuongNguoiHoc(){
        String sql = "{CALL SP_LUONGNGUOIHOC}";
        String cols[] = {"NAM","SOLUONG","NGAYDT","NGAYCC"};
        return this.getListOfArray(sql, cols);
    }
    public List<Object[]> getDiemChuyenDe(){
        String sql = "{CALL SP_DIEMCHUYENDE}";
        String cols[] = {"CHUYENDE","SOLUONGHV","DIEMTHAPNHAT","DIEMCAONHAT","DIEMTB"};
        return this.getListOfArray(sql, cols);
    }
    public List<Object[]> getDoanhThu(int nam){
        String sql = "{CALL SP_DOANHTHU(?)}";
        String cols[] = {"TENCHUYENDE","SOKHOAHOC","SOHV","DOANHTHU","HOCPHITHAPNHAT","HOCPHICAONHAT","HOCPHITB"};
        return this.getListOfArray(sql, cols, nam);
    }
}
