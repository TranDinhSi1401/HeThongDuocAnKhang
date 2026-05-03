package common.network;

import java.io.Serializable;

/**
 * Danh sách các lệnh (Command) mà Client có thể gửi lên Server.
 * Cả hai phía đều dùng chung enum này.
 *
 * Quy ước đặt tên:  ĐỘNG_TỪ + _ + ĐỐI_TƯỢNG
 * Ví dụ: GET_ALL_SAN_PHAM, ADD_HOA_DON, v.v.
 */
public enum CommandType implements Serializable {

    // ===== SanPham =====
    GET_ALL_SAN_PHAM,
    GET_SAN_PHAM_BY_MA,
    GET_SAN_PHAM_BY_TEN,
    GET_SAN_PHAM_BY_LOAI,
    GET_SAN_PHAM_BY_MA_NCC,
    GET_SAN_PHAM_SAP_HET_HANG,
    GET_SAN_PHAM_DA_HET_HANG,
    GET_MA_SP_BY_MA_VACH,
    GET_MA_SP_CUOI,
    ADD_SAN_PHAM,
    SUA_SAN_PHAM,
    XOA_SAN_PHAM,
    GET_SP_BAN_CHAY_THANG,
    GET_SP_BAN_CHAY_NAM,

    // ===== DonViTinh =====
    GET_DVT_BY_MA_SP,
    GET_DVT_CO_BAN_BY_MA_SP,
    GET_MA_DVT_CUOI,
    ADD_DON_VI_TINH,
    SUA_DON_VI_TINH,
    XOA_DON_VI_TINH,
    XOA_DVT_BY_MA_SP,

    // ===== MaVachSanPham =====
    ADD_MA_VACH,
    DELETE_MA_VACH,

    // ===== KhachHang =====
    GET_ALL_KHACH_HANG,
    GET_KHACH_HANG_BY_SDT,
    GET_KHACH_HANG_BY_MA,
    GET_KHACH_HANG_BY_TEN,
    GET_MA_KH_CUOI,
    ADD_KHACH_HANG,
    SUA_KHACH_HANG,
    XOA_KHACH_HANG,
    UPDATE_DIEM_TICH_LUY,
    TRU_DIEM_TICH_LUY,

    // ===== NhanVien =====
    GET_ALL_NHAN_VIEN,
    GET_NHAN_VIEN_BY_MA,
    GET_NHAN_VIEN_BY_TEN,
    GET_NHAN_VIEN_BY_SDT,
    GET_NHAN_VIEN_BY_CCCD,
    GET_NHAN_VIEN_BY_TRANG_THAI,
    GET_MA_NV_CUOI,
    ADD_NHAN_VIEN,
    SUA_NHAN_VIEN,
    XOA_NHAN_VIEN,

    // ===== TaiKhoan =====
    GET_TAI_KHOAN_BY_MA_NV,
    GET_TAI_KHOAN_BY_EMAIL,
    ADD_TAI_KHOAN,
    CAP_NHAT_TAI_KHOAN,
    XOA_TAI_KHOAN,
    UPDATE_MAT_KHAU,
    KIEM_TRA_EMAIL_TON_TAI,
    KIEM_TRA_EMAIL_THUOC_TAI_KHOAN,

    // ===== HoaDon =====
    GET_HOA_DON_MOI_NHAT_TRONG_NGAY,
    GET_HOA_DON_BY_MA,
    GET_HOA_DON_BY_MA_NV,
    GET_HOA_DON_BY_MA_KH,
    GET_HOA_DON_BY_NGAY,
    GET_HOA_DON_BY_KHOANG_NGAY,
    GET_HOA_DON_BY_SDT_KH,
    GET_HOA_DON_BY_HINH_THUC,
    GET_SO_HD_CUOI_TRONG_NGAY,
    GET_SO_PTH,
    GET_TONG_TIEN_CAC_PTH,
    ADD_HOA_DON,
    GET_DOANH_THU_NGAY,
    GET_DOANH_THU_THANG,
    GET_DOANH_THU_TUNG_NGAY,
    GET_DOANH_THU_TUNG_THANG,
    GET_NAM_HOA_DON,

    // ===== ChiTietHoaDon =====
    GET_CTHD_BY_MA_HD,
    ADD_CHI_TIET_HOA_DON,
    XOA_CTHD_BY_MA_HD,

    // ===== ChiTietXuatLo =====
    ADD_CHI_TIET_XUAT_LO,
    GET_CTXL_BY_MA_CTHD,

    // ===== LoSanPham =====
    GET_LO_BY_MA_SP,
    GET_LO_BY_MA,
    GET_LO_BY_MA_CTHD,
    GET_LO_BY_MA_NCC,
    GET_DS_LO_BY_MA_SP,
    GET_DS_LO_DA_XUAT_BY_CTHD,
    DEM_LO_THEO_TRANG_THAI,
    ADD_LO_SAN_PHAM,
    TRU_SO_LUONG_LO,
    CONG_SO_LUONG_LO,
    CAP_NHAT_SO_LUONG_LO,
    HUY_LO_SAN_PHAM,

    // ===== LichSuLo =====
    GET_LICH_SU_LO_BY_MA_LO,
    ADD_LICH_SU_LO,

    // ===== NhaCungCap =====
    GET_ALL_NHA_CUNG_CAP,
    GET_NCC_BY_MA,
    GET_NCC_BY_TEN,
    GET_NCC_BY_SDT,
    GET_NCC_BY_EMAIL,
    GET_MA_NCC_CUOI,
    ADD_NHA_CUNG_CAP,
    SUA_NHA_CUNG_CAP,
    XOA_NHA_CUNG_CAP,

    // ===== SanPhamCungCap =====
    GET_SPCC_BY_MA_SP,
    ADD_SAN_PHAM_CUNG_CAP,
    DELETE_SAN_PHAM_CUNG_CAP,

    // ===== KhuyenMai =====
    GET_ALL_KHUYEN_MAI,
    GET_KHUYEN_MAI_DANG_HOAT_DONG,
    GET_MA_KM_CUOI,
    ADD_KHUYEN_MAI,
    SUA_KHUYEN_MAI,
    XOA_KHUYEN_MAI,

    // ===== KhuyenMaiSanPham =====
    GET_KMSP_BY_MA_KM,
    GET_KMSP_BY_MA_SP,
    ADD_KHUYEN_MAI_SAN_PHAM,
    DELETE_KHUYEN_MAI_SAN_PHAM,

    // ===== PhieuNhap =====
    GET_PHIEU_NHAP_BY_MA,
    GET_ALL_PHIEU_NHAP,
    GET_SO_PN_CUOI,
    ADD_PHIEU_NHAP,

    // ===== ChiTietPhieuNhap =====
    GET_CTPN_BY_MA_PN,
    ADD_CHI_TIET_PHIEU_NHAP,

    // ===== PhieuTraHang =====
    GET_PTH_BY_MA_HD,
    GET_PTH_BY_MA,
    ADD_PHIEU_TRA_HANG,
    GET_SO_PTH_CUOI,

    // ===== ChiTietPhieuTraHang =====
    GET_CTPTH_BY_MA_PTH,
    ADD_CHI_TIET_PHIEU_TRA_HANG,

    // ===== CaLam =====
    GET_ALL_CA_LAM,
    GET_CA_LAM_BY_TEN,
    ADD_CA_LAM,
    SUA_CA_LAM,
    XOA_CA_LAM,

    // ===== LichSuCaLam =====
    GET_LSCL_BY_MA_NV,
    GET_LSCL_BY_NGAY,
    GET_LSCL_DANG_LAM_BY_MA_NV,
    ADD_LICH_SU_CA_LAM,
    UPDATE_LICH_SU_CA_LAM
}
