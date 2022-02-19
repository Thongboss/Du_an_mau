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
public class HocVienEntity {
    private String maHV, maNH,maKH;
    private double diem;

    public HocVienEntity() {
    }

    public HocVienEntity(String maHV, String maNH, String maKH, double diem) {
        this.maHV = maHV;
        this.maNH = maNH;
        this.maKH = maKH;
        this.diem = diem;
    }

    public void setMaHV(String maHV) {
        this.maHV = maHV;
    }

    public void setMaNH(String maNH) {
        this.maNH = maNH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public String getMaHV() {
        return maHV;
    }

    public String getMaNH() {
        return maNH;
    }

    public String getMaKH() {
        return maKH;
    }

    public double getDiem() {
        return diem;
    }

    
    
}
