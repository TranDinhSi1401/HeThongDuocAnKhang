# Refactoring LoSanPhamGUI - Client-Server Migration

## Status: IN PROGRESS

### Completed Refactoring:
1. ✅ **Imports**: Replaced all DAO/BUS imports with SocketClient, Request, Response, CommandType
2. ✅ **Constructor**: Removed `throws SQLException`, wrapped in try-catch
3. ✅ **loadDanhSachLoSanPham()**: Uses SocketClient with `CommandType.GET_ALL_LO_SAN_PHAM_KHONG_HUY`
4. ✅ **loadLaiDanhSachLo()**: Uses SocketClient instead of local DAO
5. ✅ **capNhatSoLo()**: Uses SocketClient with `CommandType.DEM_LO_THEO_TRANG_THAI`
6. ✅ **loadLichSuLo()**: Uses SocketClient with `CommandType.GET_ALL_LICH_SU_LO`
7. ✅ **Table Row Selection Listener**: Replaced DAO calls with SocketClient

### Remaining Refactoring Needed:

#### High Priority:
1. **btnHuyLoActionPerformed()** (line 989):
   - Replace: `LoSanPhamDAO.timLoSanPham()` → Use SocketClient with `CommandType.GET_LO_SAN_PHAM_BY_MA`
   - Replace: `LoSanPhamDAO.huyLoSanPham()` → Use SocketClient with `CommandType.HUY_LO_SAN_PHAM`
   - Replace: `LichSuLoDAO.addLichSuLo()` → Use SocketClient with `CommandType.ADD_LICH_SU_LO`

2. **btnXacNhanActionPerformed()** (line 1203):
   - Replace: `MaVachSanPhamDAO.timMaSPTheoMaVach()` → Use SocketClient
   - Replace: `PhieuNhapDAO.themPhieuNhap()` → Use SocketClient
   - Replace: `PhieuNhapDAO.getPhieuNhapMoiNhat()` → Use SocketClient
   - Replace: `ChiTietPhieuNhapDAO.getChiTietPhieuNhap()` → Use SocketClient
   - Replace: `LoSanPhamDAO.timLoSanPham()` → Use SocketClient
   - Replace: `LoSanPhamDAO.capNhatSoLuongLo()` → Use SocketClient
   - Replace: `LoSanPhamDAO.themLoSanPham()` → Use SocketClient
   - Replace: `SanPhamDAO.timSPTheoMa()` → Use SocketClient
   - Replace: `NhaCungCapDAO.getNhaCungCapTheoTen()` → Use SocketClient
   - Replace: `ChiTietPhieuNhapDAO.themChiTietPhieuNhap()` → Use SocketClient

3. **txtTimKiemActionPerformed()** (line 1322):
   - Replace: `LoSanPhamDAO.timLoSanPham()` → Use SocketClient
   - Replace: `DonViTinhDAO.getMotDonViTinhTheoMaSP()` → Use SocketClient
   - Replace: `SanPhamCungCapDAO.getSanPhamCungCap()` → Use SocketClient
   - Replace: `NhaCungCapDAO.timNCCTheoMa()` → Use SocketClient

4. **btnXoaTrangLoActionPerformed()** (line 1389):
   - Replace: `LoSanPhamDAO.dsLoSanPham()` → Use SocketClient

5. **btnTimLoHetHanActionPerformed()** (line 1530):
   - Replace: `LoSanPhamDAO.dsLoSanPham()` → Use SocketClient

#### Medium Priority:
1. **btnThemSanPhamTuExcelActionPerformed()**: Extract Excel logic, use SocketClient for DB operations
2. **btnTimTheoThongTinActionPerformed()**: Replace BUS calls with SocketClient

#### Must Add:
1. **reLoadTheoDoiVaCanhBao()**: Remove `throws SQLException`, call capNhatSoLo() and loadLichSuLo()
2. **chonTatCa()** and **xoaSanPhamDaChon()**: Already UI-only, no changes needed

### New CommandTypes Needed:
- `GET_ALL_LO_SAN_PHAM_KHONG_HUY`
- `GET_LO_SAN_PHAM_BY_MA`
- `HUY_LO_SAN_PHAM`
- `DEM_LO_THEO_TRANG_THAI`
- `GET_ALL_LICH_SU_LO`
- `ADD_LICH_SU_LO`
- `ADD_LO_SAN_PHAM`
- etc.

### Menu Items Added:
- ✅ "About" (Giới thiệu) - Shows project info
- ✅ "Help" (Hướng dẫn sử dụng) - Shows user guide

## Notes:
- All DTO conversions are handled via helper method `dtoToTableRow()`
- SocketClient uses singleton pattern - ready to use
- Server must have corresponding handlers for all CommandType enums
- All async operations use SwingWorker to prevent UI freezing
