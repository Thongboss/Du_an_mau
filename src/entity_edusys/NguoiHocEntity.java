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
public class NguoiHocEntity {
    private String maNH,hoTen,dienThoai,email,ghiChu,maNV;
    private boolean gioitinh;
    private Date ngaySinh, ngayDK;

    public NguoiHocEntity() {
    }

    public NguoiHocEntity(String maNH, String hoTen, String dienThoai, String email, String ghiChu, String maNV, boolean gioitinh, Date ngaySinh, Date ngayDK) {
        this.maNH = maNH;
        this.hoTen = hoTen;
        this.dienThoai = dienThoai;
        this.email = email;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.gioitinh = gioitinh;
        this.ngaySinh = ngaySinh;
        this.ngayDK = ngayDK;
    }

    public void setMaNH(String maNH) {
        this.maNH = maNH;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setGioitinh(boolean gioitinh) {
        this.gioitinh = gioitinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setNgayDK(Date ngayDK) {
        this.ngayDK = ngayDK;
    }

    public String getMaNH() {
        return maNH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public String getEmail() {
        return email;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public boolean getGioitinh() {
        return gioitinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public Date getNgayDK() {
        return ngayDK;
    }
    
}
