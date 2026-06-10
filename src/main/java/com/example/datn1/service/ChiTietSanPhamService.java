    package com.example.datn1.service;

    import com.example.datn1.dto.Request.ChiTietSanPhamFilter;
    import com.example.datn1.dto.Request.ChiTietSanPhamRequest;
    import com.example.datn1.dto.Response.ChiTietSanPhamResponse;
    import com.example.datn1.entity.*;
    import com.example.datn1.repository.ChiTietSanPhamRepository;
    import com.example.datn1.service.impl.IChiTietSanPhamService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.stereotype.Service;

    import java.time.Instant;
    import java.util.List;

    @Service
    public class ChiTietSanPhamService implements IChiTietSanPhamService {

        @Autowired
        private ChiTietSanPhamRepository repo;

        private ChiTietSanPhamResponse toRes(ChiTietSanPham e) {
            return new ChiTietSanPhamResponse(
                    e.getId(),
                    e.getIdSanPham().getId(),
                    e.getIdSanPham().getTenSanPham(),
                    e.getMaCtsp(),
                    e.getIdMauSac() != null ? e.getIdMauSac().getTenMauSac() : null,
                    e.getIdTrongLuong() != null ? e.getIdTrongLuong().getTenTrongLuong() : null,
                    e.getIdChuViCanVot() != null ? e.getIdChuViCanVot().getTenChuViCanVot() : null, // <-- Dòng kiểm tra chu vi cán
                    e.getIdDoCung() != null ? e.getIdDoCung().getTenDoCung() : null,
                    e.getIdDiemCanBang() != null ? e.getIdDiemCanBang().getTenDiemCanBang() : null,
                    e.getIdChatLieuThanVot() != null ? e.getIdChatLieuThanVot().getTenChatLieuThanVot() : null,
                    e.getIdChatLieuKhungVot() != null ? e.getIdChatLieuKhungVot().getTenChatLieuKhungVot() : null,
                    e.getIdDanhMuc() != null ? e.getIdDanhMuc().getTenDanhMuc() : null,
                    e.getGiaNhap(),
                    e.getGiaBan(),
                    e.getSoLuong(),
                    e.getTrangThai(),
                    e.getNgayTao(),
                    e.getNgayCapNhat()
            );
        }

        @Override
        public List<ChiTietSanPhamResponse> getAll() {
            return repo.findByTrangThai(1).stream()
                    .map(this::toRes)
                    .toList();
        }

        @Override
        public Page<ChiTietSanPhamResponse> search(ChiTietSanPhamFilter f) {
            return repo.search(f.getKeyword(), f.getIdSanPham(), f.getIdDanhMuc(), f.getIdMauSac(),
                    f.getIdTrongLuong(), f.getIdChuViCanVot(), f.getIdDoCung(), f.getIdDiemCanBang(), f.getTrangThai(),
                    f.getGiaTu(), f.getGiaDen(), PageRequest.of(f.getPage(), f.getSize())).map(this::toRes);
        }

        @Override
        public ChiTietSanPhamResponse save(ChiTietSanPhamRequest r) {
            if (repo.existsByMaCtsp(r.getMaCtsp())) throw new RuntimeException("Mã CTSP này đã tồn tại!");
            ChiTietSanPham e = new ChiTietSanPham();
            mapFields(e, r);
            e.setNgayTao(Instant.now());
            e.setNgayCapNhat(Instant.now());
            return toRes(repo.save(e));
        }

        @Override
        public ChiTietSanPhamResponse update(Integer id, ChiTietSanPhamRequest r) {
            ChiTietSanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm!"));
            mapFields(e, r);
            e.setNgayCapNhat(Instant.now());
            return toRes(repo.save(e));
        }

        private void mapFields(ChiTietSanPham e, ChiTietSanPhamRequest r) {
            SanPham sp = new SanPham(); sp.setId(r.getIdSanPham()); e.setIdSanPham(sp);

            if (r.getIdMauSac() != null) { MauSac m = new MauSac(); m.setId(r.getIdMauSac()); e.setIdMauSac(m); }
            if (r.getIdTrongLuong() != null) { TrongLuong t = new TrongLuong(); t.setId(r.getIdTrongLuong()); e.setIdTrongLuong(t); }
            if (r.getIdChuViCanVot() != null) { ChuViCanVot cv = new ChuViCanVot(); cv.setId(r.getIdChuViCanVot()); e.setIdChuViCanVot(cv); } // Ánh xạ Entity khóa ngoại mới
            if (r.getIdDoCung() != null) { DoCung d = new DoCung(); d.setId(r.getIdDoCung()); e.setIdDoCung(d); }
            if (r.getIdDiemCanBang() != null) { DiemCanBang dcb = new DiemCanBang(); dcb.setId(r.getIdDiemCanBang()); e.setIdDiemCanBang(dcb); }
            if (r.getIdChatLieuThanVot() != null) { ChatLieuThanVot tv = new ChatLieuThanVot(); tv.setId(r.getIdChatLieuThanVot()); e.setIdChatLieuThanVot(tv); }
            if (r.getIdChatLieuKhungVot() != null) { ChatLieuKhungVot kv = new ChatLieuKhungVot(); kv.setId(r.getIdChatLieuKhungVot()); e.setIdChatLieuKhungVot(kv); }
            if (r.getIdDanhMuc() != null) { DanhMuc dm = new DanhMuc(); dm.setId(r.getIdDanhMuc()); e.setIdDanhMuc(dm); }

            e.setMaCtsp(r.getMaCtsp());
            e.setGiaNhap(r.getGiaNhap());
            e.setGiaBan(r.getGiaBan());
            e.setSoLuong(r.getSoLuong());
            e.setTrangThai(r.getTrangThai());
        }

        @Override
        public void delete(Integer id) {
            ChiTietSanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy!"));
            e.setTrangThai(0);
            repo.save(e);
        }

        @Override
        public void updateTrangThai(Integer id, Integer trangThai) {
            ChiTietSanPham e = repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm!"));
            e.setTrangThai(trangThai);
            repo.save(e);
        }
    }