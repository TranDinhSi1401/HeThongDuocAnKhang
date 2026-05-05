package common.entity;

public enum TinhTrangSanPhamEnum {
    BINH_THUONG("Bình thường"),
    HU_HONG("Hư hỏng"),
    HET_HAN("Hết hạn");

    private String text;

    TinhTrangSanPhamEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
