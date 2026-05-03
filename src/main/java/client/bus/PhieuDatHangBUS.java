/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.bus;

import java.util.ArrayList;
import server.dao.PhieuDatHangDAO;
import server.entity.DonViTinh;
import server.entity.NhaCungCap;
import server.entity.SanPham;
import client.gui.TaoPhieuDatHangGUI;

/**
 *
 * @author admin
 */
public class PhieuDatHangBUS {
       private PhieuDatHangDAO phieuDatDAO;
    public PhieuDatHangBUS() {
        phieuDatDAO = new PhieuDatHangDAO();
    }
    public ArrayList<SanPham> danhsachSanPham(){
        return phieuDatDAO.dsSanPham();
    }
    public SanPham timSanPham(String ma){
        return phieuDatDAO.timSanPham(ma);
    }
    
    public NhaCungCap timNhaCC(String ma){
        return phieuDatDAO.timNhaCungCap(ma);
    }
    public DonViTinh giaSanPham(String ma){
        return phieuDatDAO.giaSanPham(ma);
    }
       
    public double tinh(int sl, DonViTinh x){
        return x.getGiaBanTheoDonVi()*sl;
    }
    
    

       
}

