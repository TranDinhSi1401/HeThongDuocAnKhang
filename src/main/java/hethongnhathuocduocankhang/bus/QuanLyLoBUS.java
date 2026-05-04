package hethongnhathuocduocankhang.bus;

import common.dto.LoSanPhamDTO;
import common.dto.SanPhamDTO;
import common.dto.DonViTinhDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class QuanLyLoBUS {

    public List<LoSanPhamDTO> getAllLo() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_LO_SAN_PHAM, null));
        if (res.isSuccess() && res.getData() != null) {
            return (List<LoSanPhamDTO>) res.getData();
        }
        return new ArrayList<>();
    }

    public ArrayList<LoSanPhamDTO> getLoKhongHuy() {
        List<LoSanPhamDTO> all = getAllLo();
        ArrayList<LoSanPhamDTO> kq = new ArrayList<>();
        if (all != null) {
            for (LoSanPhamDTO lo : all) {
                if (!lo.isDaHuy()) {
                    kq.add(lo);
                }
            }
        }
        return kq;
    }

    public Object[] toTableRow(LoSanPhamDTO lo) {
        // Lấy thông tin sản phẩm
        Response resSp = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, lo.getMaSP()));
        SanPhamDTO sp = (resSp.isSuccess() && resSp.getData() != null) ? (SanPhamDTO) resSp.getData() : null;

        // Lấy đơn vị tính cơ bản
        Response resDvt = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_CO_BAN_BY_MA_SP, lo.getMaSP()));
        DonViTinhDTO dvt = (resDvt.isSuccess() && resDvt.getData() != null) ? (DonViTinhDTO) resDvt.getData() : null;

        return new Object[]{
            lo.getMaSP(),
            sp != null ? sp.getTen() : "",
            lo.getMaLoSanPham(),
            dvt != null ? dvt.getTenDonVi() : "",
            lo.getSoLuong()
        };
    }

    public String tinhTrangThaiLo(LoSanPhamDTO lo) {
        if (lo.isDaHuy()) {
            return "Đã hủy";
        }

        long kq = kiemTra(LocalDate.now(), lo.getNgayHetHan());

        if (kq < 0) {
            return "Hết hạn";
        } else if (kq >= 0 && kq < 180) {
            return "Sắp hết hạn";
        } else {
            return "Còn hạn";
        }
    }

    public Map<String, Object> thongKe(List<LoSanPhamDTO> dsLo) {
        int daHuy = 0, hetHan = 0, sapHetHan = 0, conHan = 0;
        ArrayList<LoSanPhamDTO> dsDaHuy = new ArrayList<>();
        ArrayList<LoSanPhamDTO> dsHetHan = new ArrayList<>();
        ArrayList<LoSanPhamDTO> dsSapHetHan = new ArrayList<>();
        ArrayList<LoSanPhamDTO> dsConHan = new ArrayList<>();
        for (LoSanPhamDTO i : dsLo) {
            if (!i.isDaHuy()) {
                long kq = kiemTra(LocalDate.now(), i.getNgayHetHan());
                if (kq >= 0 && kq <= 30) {
                    sapHetHan++;
                    dsSapHetHan.add(i);
                } else if (kq < 0) {
                    hetHan++;
                    dsHetHan.add(i);
                } else {
                    conHan++;
                    dsConHan.add(i);
                }
            } else {
                daHuy++;
                dsDaHuy.add(i);
            }
        }
        Map<String, Object> dsThongKeLo = new HashMap<>();
        dsThongKeLo.put("SoLoDaHuy", daHuy);
        dsThongKeLo.put("SoLoHetHan", hetHan);
        dsThongKeLo.put("SoLoSapHetHan", sapHetHan);
        dsThongKeLo.put("SoLoConHan", conHan);

        dsThongKeLo.put("dsLoDaHuy", dsDaHuy);
        dsThongKeLo.put("dsLoHetHan", dsHetHan);
        dsThongKeLo.put("dsLoSapHetHan", dsSapHetHan);
        dsThongKeLo.put("dsConHan", dsConHan);

        return dsThongKeLo;
    }

    public long kiemTra(LocalDate ht, LocalDate hh) {
        return ChronoUnit.DAYS.between(ht, hh);
    }

    public static String chuyenDinhDang(String date) {
        DateTimeFormatter nhap = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate datee = LocalDate.parse(date, nhap);
        DateTimeFormatter xuat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String newDate = datee.format(xuat);
        return newDate;
    }

    private List<LoSanPhamDTO> timLoTheotenNhaCungCap(List<LoSanPhamDTO> dsLo, String tenNCC) {
        // Tìm NCC theo tên (giả sử có command này)
        Response resNcc = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NCC_BY_TEN, tenNCC));
        if (resNcc.isSuccess() && resNcc.getData() != null) {
            List<common.dto.NhaCungCapDTO> nccs = (List<common.dto.NhaCungCapDTO>) resNcc.getData();
            if (nccs.isEmpty()) return new ArrayList<>();
            String maNCC = nccs.get(0).getMaNCC();
            
            return dsLo.stream().filter(lo -> {
                // Lấy maNCC của SP trong lô
                Response resSpcc = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SPCC_BY_MA_SP, lo.getMaSP()));
                if (resSpcc.isSuccess() && resSpcc.getData() != null) {
                    List<common.dto.SanPhamCungCapDTO> spccs = (List<common.dto.SanPhamCungCapDTO>) resSpcc.getData();
                    for (common.dto.SanPhamCungCapDTO spcc : spccs) {
                        if (spcc.getMaNCC().equals(maNCC)) return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public ArrayList<LoSanPhamDTO> timKiemLoVoiNhieuDieuKien(String tieuChi, String noiDung, String trangThai) {
        String loaiTimKiem = (tieuChi == null) ? "tất cả" : tieuChi.trim().toLowerCase();
        String trangThaiLoc = (trangThai == null) ? "tất cả" : trangThai.trim().toLowerCase();
        String noiDungSafe = (noiDung == null) ? "" : noiDung.trim();
        String noiDungLowerCase = noiDungSafe.toLowerCase();

        if (!"tất cả".equals(loaiTimKiem)) {
            if (noiDungSafe.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bạn phải nhập thông tin tìm kiếm khi chọn tiêu chí cụ thể.");
                return new ArrayList<>();
            }
        }
        
        List<LoSanPhamDTO> allLo = getAllLo();
        Map<String, Object> dsThongKe = thongKe(allLo);
        List<LoSanPhamDTO> dsLoDaLocTheoTrangThai;
        
        dsLoDaLocTheoTrangThai = switch (trangThaiLoc) {
            case "còn hạn" -> (List<LoSanPhamDTO>) dsThongKe.get("dsConHan");
            case "sắp hết hạn" -> (List<LoSanPhamDTO>) dsThongKe.get("dsLoSapHetHan");
            case "hết hạn" -> (List<LoSanPhamDTO>) dsThongKe.get("dsLoHetHan");
            case "đã hủy" -> (List<LoSanPhamDTO>) dsThongKe.get("dsLoDaHuy");
            default -> allLo;
        };

        if (loaiTimKiem.equals("nhà cung cấp")) {
            return new ArrayList<>(timLoTheotenNhaCungCap(dsLoDaLocTheoTrangThai, noiDungSafe));
        }

        List<LoSanPhamDTO> ketQuaLoc = dsLoDaLocTheoTrangThai.stream().filter(lo -> {
            if (loaiTimKiem.equals("tất cả")) {
                return true;
            }
            switch (loaiTimKiem) {
                case "mã lô sản phẩm":
                    return lo.getMaLoSanPham() != null && lo.getMaLoSanPham().toLowerCase().contains(noiDungLowerCase);
                case "mã sản phẩm":
                    return lo.getMaSP() != null && lo.getMaSP().toLowerCase().contains(noiDungLowerCase);
                case "tên sản phẩm":
                    Response resSp = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, lo.getMaSP()));
                    if (resSp.isSuccess() && resSp.getData() != null) {
                        SanPhamDTO sp = (SanPhamDTO) resSp.getData();
                        return sp.getTen().toLowerCase().contains(noiDungLowerCase);
                    }
                    return false;
                default:
                    return true;
            }
        }).collect(Collectors.toList());

        return new ArrayList<>(ketQuaLoc);
    }

    public static boolean tongSoLuongTheoSanPham(String maSP, LoSanPhamDTO loSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LO_BY_MA_SP, maSP));
        if (res.isSuccess() && res.getData() != null) {
            List<LoSanPhamDTO> dsLo = (List<LoSanPhamDTO>) res.getData();
            int tongSL = 0;
            for (LoSanPhamDTO lo : dsLo) {
                tongSL += lo.getSoLuong();
            }
            Response resSp = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, maSP));
            if (resSp.isSuccess() && resSp.getData() != null) {
                SanPhamDTO sp = (SanPhamDTO) resSp.getData();
                if (sp.getTonToiDa() < tongSL) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] layThongKeLoTheoTrangThai() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.DEM_LO_THEO_TRANG_THAI, null));
        if (res.isSuccess() && res.getData() != null) {
            return (int[]) res.getData();
        }
        return new int[]{0, 0, 0, 0};
    }
}
