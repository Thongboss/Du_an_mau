/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_edusys;

/**
 *
 * @author PC
 */
public class ChuyenDeEntity {
    private String maCD, tenCD, hinh, moTa;
    private int thoiLuong;
    private double hocPhi;

    public ChuyenDeEntity() {
    }

    public ChuyenDeEntity(String maCD, String tenCD, String hinh, String moTa, int thoiLuong, double hocPhi) {
        this.maCD = maCD;
        this.tenCD = tenCD;
        this.hinh = hinh;
        this.moTa = moTa;
        this.thoiLuong = thoiLuong;
        this.hocPhi = hocPhi;
    }

    public void setMaCD(String maCD) {
        this.maCD = maCD;
    }

    public void setTenCD(String tenCD) {
        this.tenCD = tenCD;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public void setHocPhi(double hocPhi) {
        this.hocPhi = hocPhi;
    }

    public String getMaCD() {
        return maCD;
    }

    public String getTenCD() {
        return tenCD;
    }

    public String getHinh() {
        return hinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public double getHocPhi() {
        return hocPhi;
    }

    @Override
    public String toString() {
        return tenCD;
    }
    
    
}
