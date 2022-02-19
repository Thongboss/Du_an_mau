/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_edusys;

import java.util.Date;

/**
 *
 * @author PC
 */
public class KhoaHocEntity {
    private int  thoiLuong;
    private String maKH,maCD, ghiChu, maNV;
    private double hocPhi;
    private Date ngayKG, ngayTao;

    public KhoaHocEntity() {
    }

    public KhoaHocEntity(int thoiLuong, String maKH, String maCD, String ghiChu, String maNV, double hocPhi, Date ngayKG, Date ngayTao) {
        this.thoiLuong = thoiLuong;
        this.maKH = maKH;
        this.maCD = maCD;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.hocPhi = hocPhi;
        this.ngayKG = ngayKG;
        this.ngayTao = ngayTao;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setMaCD(String maCD) {
        this.maCD = maCD;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setHocPhi(double hocPhi) {
        this.hocPhi = hocPhi;
    }

    public void setNgayKG(Date ngayKG) {
        this.ngayKG = ngayKG;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getMaCD() {
        return maCD;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public double getHocPhi() {
        return hocPhi;
    }

    public Date getNgayKG() {
        return ngayKG;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    @Override
    public String toString() {
        return maKH+"("+ngayKG+")";
    }
    
    
    
}
