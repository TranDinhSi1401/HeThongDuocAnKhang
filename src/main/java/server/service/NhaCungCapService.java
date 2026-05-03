package server.service;

import common.dto.NhaCungCapDTO;
import common.dto.SanPhamCungCapDTO;
import server.dao.NhaCungCapDAO;
import server.dao.SanPhamCungCapDAO;
import server.entity.*;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/** Service xử lý nghiệp vụ NhaCungCap và SanPhamCungCap. */
public class NhaCungCapService {

    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    private final SanPhamCungCapDAO sanPhamCungCapDAO = new SanPhamCungCapDAO();

    public List<NhaCungCapDTO> getAllNhaCungCap() {
        return nhaCungCapDAO.getAllNhaCungCap().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public NhaCungCapDTO timNCCTheoMa(String ma) {
        NhaCungCap ncc = nhaCungCapDAO.timNCCTheoMa(ma);
        return ncc != null ? EntityMapper.toDTO(ncc) : null;
    }

    public List<NhaCungCapDTO> timNCCTheoTen(String ten) {
        return nhaCungCapDAO.timNCCTheoTen(ten).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhaCungCapDTO> timNCCTheoSDT(String sdt) {
        return nhaCungCapDAO.timNCCTheoSDT(sdt).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhaCungCapDTO> timNCCTheoEmail(String email) {
        return nhaCungCapDAO.timNCCTheoEmail(email).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getMaNCCCuoiCung() { return nhaCungCapDAO.getMaNCCCuoiCung(); }

    public boolean addNhaCungCap(NhaCungCapDTO dto) {
        nhaCungCapDAO.create(EntityMapper.toEntity(dto));
        return true;
    }

    public boolean suaNhaCungCap(String maNCC, NhaCungCapDTO dto) {
        return nhaCungCapDAO.suaNhaCungCap(maNCC, EntityMapper.toEntity(dto));
    }

    public boolean xoaNhaCungCap(String maNCC) { return nhaCungCapDAO.xoaNhaCungCap(maNCC); }

    public List<SanPhamCungCapDTO> getSPCCTheoMaSP(String maSP) {
        return sanPhamCungCapDAO.getSanPhamCungCapTheoMaSP(maSP).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addSanPhamCungCap(SanPhamCungCapDTO dto) {
        SanPhamCungCap e = new SanPhamCungCap();
        e.setTrangThaiHopTac(dto.isTrangThaiHopTac());
        e.setGiaNhap(dto.getGiaNhap());
        SanPham sp = new SanPham(); sp.setMaSP(dto.getMaSP()); e.setSanPham(sp);
        NhaCungCap ncc = new NhaCungCap(); ncc.setMaNCC(dto.getMaNCC()); e.setNhaCungCap(ncc);
        sanPhamCungCapDAO.create(e);
        return true;
    }

    public boolean deleteSanPhamCungCap(Long id) { return sanPhamCungCapDAO.delete(id); }
}
