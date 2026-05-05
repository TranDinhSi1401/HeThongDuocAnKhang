package common.entity;

public enum TruongHopDoiTraEnum {
    HANG_LOI_DO_NHA_SAN_XUAT("Hàng lỗi do nhà sản xuất"),
    DI_UNG_MAN_CAM("Dị ứng mẫn cảm"),
    NHU_CAU_KHACH_HANG("Nhu cầu khách hàng");

    private String truongHopDoiTra;

    TruongHopDoiTraEnum(String truongHopDoiTra) {
        this.truongHopDoiTra = truongHopDoiTra;
    }

    public String getTruongHopDoiTra() {
        return truongHopDoiTra;
    }

    @Override
    public String toString() {
        return truongHopDoiTra;
    }
}
