package server.service;

import common.dto.NhanVienDTO;
import common.dto.TaiKhoanDTO;
import org.mindrot.jbcrypt.BCrypt;
import server.dao.NhanVienDAO;
import server.dao.TaiKhoanDAO;
import server.entity.NhanVien;
import server.entity.TaiKhoan;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ NhanVien và TaiKhoan.
 */
public class NhanVienService {

    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    // ===== NhanVien =====

    public List<NhanVienDTO> getAllNhanVien() {
        return nhanVienDAO.getAllNhanVien()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public NhanVienDTO getNhanVienTheoMaNV(String maNV) {
        NhanVien nv = nhanVienDAO.getNhanVienTheoMaNV(maNV);
        return nv != null ? EntityMapper.toDTO(nv) : null;
    }

    public NhanVienDTO timNVTheoMa(String ma) {
        NhanVien nv = nhanVienDAO.timNVTheoMa(ma);
        return nv != null ? EntityMapper.toDTO(nv) : null;
    }

    public List<NhanVienDTO> timNVTheoTen(String ten) {
        return nhanVienDAO.timNVTheoTen(ten)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoSDT(String sdt) {
        return nhanVienDAO.timNVTheoSDT(sdt)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoCCCD(String cccd) {
        return nhanVienDAO.timNVTheoCCCD(cccd)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoTrangThai(boolean daNghiViec) {
        return nhanVienDAO.timNVTheoTrangThai(daNghiViec)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getMaNVCuoiCung() {
        return nhanVienDAO.getMaNVCuoiCung();
    }

    public boolean addNhanVien(NhanVienDTO dto) {
        NhanVien entity = EntityMapper.toEntity(dto);
        nhanVienDAO.create(entity);
        return true;
    }

    public boolean suaNhanVien(String maNV, NhanVienDTO dto) {
        NhanVien nvNew = EntityMapper.toEntity(dto);
        return nhanVienDAO.suaNhanVien(maNV, nvNew);
    }

    public boolean xoaNhanVien(String maNV) {
        return nhanVienDAO.xoaNhanVien(maNV);
    }

    // ===== TaiKhoan =====

    public TaiKhoanDTO getTaiKhoanTheoMaNV(String maNV) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoMaNV(maNV);
        return tk != null ? EntityMapper.toDTO(tk) : null;
    }

    public TaiKhoanDTO getTaiKhoanTheoEmail(String email) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoEmail(email);
        return tk != null ? EntityMapper.toDTO(tk) : null;
    }

    public boolean addTaiKhoan(TaiKhoanDTO dto) {
        TaiKhoan entity = EntityMapper.toEntityTaiKhoan(dto);
        // Gắn NhanVien reference
        NhanVien nv = new NhanVien();
        nv.setMaNV(dto.getMaNV());
        entity.setNhanVien(nv);
        taiKhoanDAO.create(entity);
        return true;
    }

    public boolean capNhatTaiKhoan(TaiKhoanDTO dto) {
        TaiKhoan tk = EntityMapper.toEntityTaiKhoan(dto);
        return taiKhoanDAO.capNhatTaiKhoan(tk);
    }

    public boolean xoaTaiKhoan(String maNV) {
        return taiKhoanDAO.xoaTaiKhoan(maNV);
    }

    public boolean updateMatKhau(String maNV, String email, String matKhauHash) {
        return taiKhoanDAO.updateMatKhau(maNV, email, matKhauHash);
    }

    public boolean kiemTraEmailTonTai(String email) {
        return taiKhoanDAO.kiemTraEmailTonTai(email);
    }

    public boolean kiemTraEmailThuocTaiKhoan(String maNV, String email) {
        return taiKhoanDAO.kiemTraEmailThuocTaiKhoan(maNV, email);
    }

    /**
     * Xác thực đăng nhập phía Server.
     * @param tenDangNhap  mã NV / tên đăng nhập
     * @param plainPassword mật khẩu thô từ Client
     * @return TaiKhoanDTO nếu thành công, null nếu sai tài khoản/mật khẩu
     */
    public TaiKhoanDTO login(String tenDangNhap, String plainPassword) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoMaNV(tenDangNhap);
        if (tk == null) return null;
        if (!BCrypt.checkpw(plainPassword, tk.getMatKhau())) return null;
        return EntityMapper.toDTO(tk);
    }

    /**
     * Đổi mật khẩu: kiểm tra mật khẩu cũ (BCrypt), hash mật khẩu mới rồi cập nhật DB.
     * @param maNV     mã nhân viên
     * @param oldPlain mật khẩu cũ dạng thô
     * @param newPlain mật khẩu mới dạng thô
     * @return true nếu đổi thành công
     */
    public boolean doiMatKhau(String maNV, String oldPlain, String newPlain) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoMaNV(maNV);
        if (tk == null) return false;
        if (!BCrypt.checkpw(oldPlain, tk.getMatKhau())) return false;
        String newHash = BCrypt.hashpw(newPlain, BCrypt.gensalt(12));
        return taiKhoanDAO.updateMatKhauTheoMaNV(maNV, newHash);
    }
}
