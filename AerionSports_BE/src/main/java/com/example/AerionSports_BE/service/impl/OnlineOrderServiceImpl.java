package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.dto.request.OnlineCheckoutItemRequest;
import com.example.AerionSports_BE.dto.request.OnlineCheckoutRequest;
import com.example.AerionSports_BE.dto.response.OnlineOrderResponse;
import com.example.AerionSports_BE.entity.ChiTietHoaDon;
import com.example.AerionSports_BE.entity.ChiTietSanPham;
import com.example.AerionSports_BE.entity.HoaDon;
import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.entity.LichSuHoaDon;
import com.example.AerionSports_BE.entity.LichSuThanhToan;
import com.example.AerionSports_BE.entity.NhanVien;
import com.example.AerionSports_BE.entity.PhieuGiamGia;
import com.example.AerionSports_BE.entity.PhuongThucThanhToan;
import com.example.AerionSports_BE.entity.ThanhToan;
import com.example.AerionSports_BE.repository.ChiTietHoaDonRepository;
import com.example.AerionSports_BE.repository.ChiTietSanPhamRepository;
import com.example.AerionSports_BE.repository.HoaDonRepository;
import com.example.AerionSports_BE.repository.KhachHangRepository;
import com.example.AerionSports_BE.repository.LichSuHoaDonRepository;
import com.example.AerionSports_BE.repository.LichSuThanhToanRepository;
import com.example.AerionSports_BE.repository.NhanVienRepository;
import com.example.AerionSports_BE.repository.PhieuGiamGiaRepository;
import com.example.AerionSports_BE.repository.PhuongThucThanhToanRepository;
import com.example.AerionSports_BE.repository.ThanhToanRepository;
import com.example.AerionSports_BE.service.EmailService;
import com.example.AerionSports_BE.realtime.OrderRealtimeService;
import com.example.AerionSports_BE.service.OnlineOrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OnlineOrderServiceImpl implements OnlineOrderService {

    private static final BigDecimal DEFAULT_SHIPPING_FEE = BigDecimal.valueOf(31000);

    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final LichSuThanhToanRepository lichSuThanhToanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final EmailService emailService;
    private final OrderRealtimeService orderRealtimeService;

    public OnlineOrderServiceImpl(
            HoaDonRepository hoaDonRepository,
            KhachHangRepository khachHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            ChiTietHoaDonRepository chiTietHoaDonRepository,
            PhieuGiamGiaRepository phieuGiamGiaRepository,
            PhuongThucThanhToanRepository phuongThucThanhToanRepository,
            ThanhToanRepository thanhToanRepository,
            LichSuHoaDonRepository lichSuHoaDonRepository,
            LichSuThanhToanRepository lichSuThanhToanRepository,
            NhanVienRepository nhanVienRepository,
            EmailService emailService,
            OrderRealtimeService orderRealtimeService
    ) {
        this.hoaDonRepository = hoaDonRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
        this.thanhToanRepository = thanhToanRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.lichSuThanhToanRepository = lichSuThanhToanRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.emailService = emailService;
        this.orderRealtimeService = orderRealtimeService;
    }

    @Override
    @Transactional
    public OnlineOrderResponse checkout(OnlineCheckoutRequest request) {
        validateRequest(request);

        List<OnlineCheckoutItemRequest> items = request.getItems();
        List<ResolvedItem> resolvedItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OnlineCheckoutItemRequest item : items) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay san pham chi tiet: " + item.getChiTietSanPhamId()));

            int quantity = item.getSoLuong() == null ? 0 : item.getSoLuong();
            if (quantity <= 0) {
                throw new RuntimeException("So luong san pham khong hop le.");
            }

            int currentStock = ctsp.getSoLuong() == null ? 0 : ctsp.getSoLuong();
            if (currentStock < quantity) {
                throw new RuntimeException("San pham " + ctsp.getMaCtsp() + " khong du so luong trong kho.");
            }

            BigDecimal unitPrice = safePrice(ctsp.getGiaBan());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            subtotal = subtotal.add(lineTotal);
            resolvedItems.add(new ResolvedItem(ctsp, quantity, unitPrice, lineTotal));
        }

        BigDecimal shippingFee = request.getPhiVanChuyen() != null ? safePrice(request.getPhiVanChuyen()) : DEFAULT_SHIPPING_FEE;
        PhieuGiamGia voucher = resolveVoucher(request.getMaPhieuGiamGia(), subtotal);
        BigDecimal voucherDiscount = voucher != null ? calculateVoucherDiscount(voucher, subtotal) : BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(voucherDiscount).add(shippingFee);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        KhachHang khachHang = resolveOrCreateCustomer(request);
        String maHoaDon = generateOrderCode();

        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setPhieuGiamGia(voucher);
        hoaDon.setMaHoaDon(maHoaDon);
        hoaDon.setLoaiHoaDon(1);
        hoaDon.setTenNguoiNhan(trimToNull(request.getHoTen()));
        hoaDon.setSdtNguoiNhan(trimToNull(request.getSdt()));
        hoaDon.setDiaChiNhan(buildFullAddress(request));
        hoaDon.setGhiChu(trimToNull(request.getGhiChu()));
        hoaDon.setTongTienHang(subtotal);
        hoaDon.setTienGiam(voucherDiscount);
        hoaDon.setTienVanChuyen(shippingFee);
        hoaDon.setTongTienThanhToan(total);
        hoaDon.setTrangThai(0);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNgayCapNhat(LocalDateTime.now());
        hoaDonRepository.save(hoaDon);

        List<OnlineOrderResponse.Item> responseItems = new ArrayList<>();
        int index = 1;
        for (ResolvedItem resolved : resolvedItems) {
            ChiTietSanPham ctsp = resolved.ctsp();

            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setHoaDon(hoaDon);
            chiTietHoaDon.setChiTietSanPham(ctsp);
            chiTietHoaDon.setMaHoaDonChiTiet(maHoaDon + "-" + String.format("%02d", index));
            chiTietHoaDon.setSoLuong(resolved.quantity());
            chiTietHoaDon.setDonGia(resolved.unitPrice());
            chiTietHoaDon.setThanhTien(resolved.lineTotal());
            chiTietHoaDon.setNgayTao(LocalDateTime.now());
            chiTietHoaDon.setNgayCapNhat(LocalDateTime.now());
            chiTietHoaDonRepository.save(chiTietHoaDon);

            responseItems.add(mapItem(ctsp, resolved.quantity(), resolved.unitPrice(), resolved.lineTotal(), chiTietHoaDon.getId()));
            index++;
        }

        createPaymentRecords(hoaDon, request, total, shippingFee);
        createHistoryRecord(hoaDon);
        orderRealtimeService.publishOrderChange(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getTrangThai(),
                getStatusText(hoaDon.getTrangThai()),
                "created"
        );

        if (voucher != null) {
            voucher.setSoLuongDaSuDung((voucher.getSoLuongDaSuDung() == null ? 0 : voucher.getSoLuongDaSuDung()) + 1);
            phieuGiamGiaRepository.save(voucher);
        }

        String email = firstNonBlank(khachHang != null ? khachHang.getEmail() : null, request.getEmail());
        if (email != null && !email.isBlank()) {
            emailService.sendOrderStatusEmail(
                    email,
                    request.getHoTen(),
                    hoaDon.getMaHoaDon(),
                    "Cho xac nhan",
                    hoaDon.getDiaChiNhan(),
                    hoaDon.getSdtNguoiNhan(),
                    hoaDon.getTongTienHang(),
                    hoaDon.getTienGiam(),
                    hoaDon.getTienVanChuyen(),
                    hoaDon.getTongTienThanhToan(),
                    buildEmailItems(resolvedItems),
                    paymentMethodLabel(request.getPhuongThucThanhToan())
            );
        }

        return toResponse(hoaDon, responseItems, paymentMethodLabel(request.getPhuongThucThanhToan()));
    }

    @Override
    @Transactional
    public OnlineOrderResponse getByCode(String maHoaDon) {
        String normalizedCode = trimToNull(maHoaDon);
        if (normalizedCode == null) {
            throw new RuntimeException("Ma hoa don khong hop le.");
        }

        HoaDon hoaDon = hoaDonRepository.findByMaHoaDon(normalizedCode.toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new RuntimeException("Khong tim thay hoa don voi ma: " + normalizedCode));

        return toResponse(hoaDon, null, null);
    }

    @Override
    @Transactional
    public List<OnlineOrderResponse> getMyOrders(Integer customerId) {
        if (customerId == null) {
            throw new RuntimeException("Khach hang khong hop le.");
        }

        List<HoaDon> hoaDons = hoaDonRepository.findAllByKhachHangIdOrderByNgayTaoDesc(customerId);
        List<OnlineOrderResponse> responses = new ArrayList<>();
        for (HoaDon hoaDon : hoaDons) {
            responses.add(toResponse(hoaDon, null, null));
        }
        return responses;
    }

    private void validateRequest(OnlineCheckoutRequest request) {
        if (request == null) {
            throw new RuntimeException("Du lieu don hang khong hop le.");
        }
        if (isBlank(request.getHoTen()) || isBlank(request.getSdt()) || isBlank(request.getEmail())
                || isBlank(request.getDiaChiChiTiet()) || isBlank(request.getPhuongXa())
                || isBlank(request.getQuanHuyen()) || isBlank(request.getTinhThanh())) {
            throw new RuntimeException("Vui long nhap day du thong tin giao hang.");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Gio hang trong.");
        }
    }

    private KhachHang resolveOrCreateCustomer(OnlineCheckoutRequest request) {
        Optional<KhachHang> byPhone = khachHangRepository.findFirstBySdt(request.getSdt());
        if (byPhone.isPresent()) {
            return byPhone.get();
        }

        Optional<KhachHang> byEmail = khachHangRepository.findFirstByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            return byEmail.get();
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(generateCustomerCode());
        khachHang.setHoTen(trimToNull(request.getHoTen()));
        khachHang.setSdt(trimToNull(request.getSdt()));
        khachHang.setEmail(trimToNull(request.getEmail()));
        khachHang.setTrangThai(1);
        khachHang.setNgayCapNhat(LocalDateTime.now());
        return khachHangRepository.save(khachHang);
    }

    private PhieuGiamGia resolveVoucher(String maVoucher, BigDecimal subtotal) {
        if (isBlank(maVoucher)) {
            return null;
        }

        PhieuGiamGia voucher = phieuGiamGiaRepository.findByMaPhieuGiamGia(maVoucher.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new RuntimeException("Ma phieu giam gia khong hop le."));

        LocalDateTime now = LocalDateTime.now();
        boolean active = voucher.getTrangThai() != null && voucher.getTrangThai() == 1
                && voucher.getNgayBatDau() != null && !now.isBefore(voucher.getNgayBatDau())
                && voucher.getNgayKetThuc() != null && !now.isAfter(voucher.getNgayKetThuc())
                && (voucher.getSoLuong() == null || (voucher.getSoLuongDaSuDung() == null ? 0 : voucher.getSoLuongDaSuDung()) < voucher.getSoLuong());

        if (!active) {
            throw new RuntimeException("Ma phieu giam gia da het hieu luc.");
        }
        if (calculateVoucherDiscount(voucher, subtotal).compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Don hang chua dat gia tri toi thieu cua phieu giam gia.");
        }
        return voucher;
    }

    private BigDecimal calculateVoucherDiscount(PhieuGiamGia voucher, BigDecimal subtotal) {
        if (voucher == null || subtotal == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal minOrder = voucher.getGiaTriDonToiThieu() != null ? voucher.getGiaTriDonToiThieu() : BigDecimal.ZERO;
        if (subtotal.compareTo(minOrder) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        boolean percentType = voucher.getLoaiPhieuGiamGia() != null
                && voucher.getLoaiPhieuGiamGia().trim().equalsIgnoreCase("sale %");

        if (percentType) {
            discount = subtotal.multiply(safePrice(voucher.getGiaTriGiam())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (voucher.getGiaTriGiamToiDa() != null && voucher.getGiaTriGiamToiDa().compareTo(BigDecimal.ZERO) > 0) {
                discount = discount.min(voucher.getGiaTriGiamToiDa());
            }
        } else {
            discount = safePrice(voucher.getGiaTriGiam());
        }

        return discount.max(BigDecimal.ZERO).min(subtotal);
    }

    private void createPaymentRecords(HoaDon hoaDon, OnlineCheckoutRequest request, BigDecimal total, BigDecimal shippingFee) {
        PhuongThucThanhToan paymentMethod = resolvePaymentMethod(request.getPhuongThucThanhToan());
        if (paymentMethod == null) {
            throw new RuntimeException("Khong tim thay phuong thuc thanh toan phu hop.");
        }

        String paymentStatus = isBankTransfer(request.getPhuongThucThanhToan())
                ? "Cho xac nhan"
                : "Chua thanh toan";
        String paymentNote = isBankTransfer(request.getPhuongThucThanhToan())
                ? "Chuyen khoan - cho xac nhan giao dich"
                : "COD - thu tien khi giao hang";

        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setHinhThucThanhToan(paymentMethod);
        thanhToan.setMaGiaoDich((isBankTransfer(request.getPhuongThucThanhToan()) ? "ONLINE-" : "COD-") + hoaDon.getMaHoaDon());
        thanhToan.setSoTien(total);
        thanhToan.setGhiChu(paymentNote);
        thanhToan.setTrangThaiThanhToan(paymentStatus);
        thanhToan.setTrangThai(1);
        thanhToan.setNgayTao(LocalDateTime.now());
        thanhToan.setNgayCapNhat(LocalDateTime.now());
        thanhToanRepository.save(thanhToan);

        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setSoTien(total);
        lichSuThanhToan.setPhuongThucThanhToan(paymentMethod.getTenHinhThuc());
        lichSuThanhToan.setTrangThaiThanhToan(paymentStatus);
        lichSuThanhToan.setNgayThanhToan(LocalDateTime.now());
        lichSuThanhToan.setGhiChu(paymentNote);
        lichSuThanhToanRepository.save(lichSuThanhToan);
    }

    private void createHistoryRecord(HoaDon hoaDon) {
        NhanVien nhanVien = nhanVienRepository.findById(1).orElse(null);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setNhanVien(nhanVien);
        lichSuHoaDon.setTrangThaiCu(null);
        lichSuHoaDon.setTrangThaiMoi(0);
        lichSuHoaDon.setHanhDong("Tao don hang online");
        lichSuHoaDon.setGhiChu(nhanVien != null ? "Online checkout" : "Online checkout - system");
        lichSuHoaDon.setThoiGianHanhDong(LocalDateTime.now());
        lichSuHoaDonRepository.save(lichSuHoaDon);
    }

    private PhuongThucThanhToan resolvePaymentMethod(String paymentType) {
        boolean bankTransfer = isBankTransfer(paymentType);
        List<PhuongThucThanhToan> methods = phuongThucThanhToanRepository.findAll();
        if (methods.isEmpty()) {
            return null;
        }

        return methods.stream()
                .filter(item -> {
                    String ten = normalizeText(item.getTenHinhThuc());
                    String ma = normalizeText(item.getMaHinhThuc());
                    if (bankTransfer) {
                        return ten.contains("chuyen") || ten.contains("bank") || ma.contains("bank") || ma.contains("vnpay") || ma.contains("momo");
                    }
                    return ten.contains("cod") || ten.contains("tien mat") || ma.contains("cod") || ma.contains("cash");
                })
                .findFirst()
                .orElse(methods.get(0));
    }

    private OnlineOrderResponse toResponse(HoaDon hoaDon, List<OnlineOrderResponse.Item> items, String paymentMethod) {
        List<OnlineOrderResponse.Item> responseItems = items;
        if (responseItems == null) {
            responseItems = loadItems(hoaDon);
        }

        return new OnlineOrderResponse(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getNgayTao(),
                mapProgressStatus(hoaDon.getTrangThai()),
                getStatusText(hoaDon.getTrangThai()),
                new OnlineOrderResponse.Customer(
                        hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getHoTen() : hoaDon.getTenNguoiNhan(),
                        hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getSdt() : hoaDon.getSdtNguoiNhan(),
                        hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getEmail() : null
                ),
                new OnlineOrderResponse.Receiver(
                        hoaDon.getTenNguoiNhan(),
                        hoaDon.getSdtNguoiNhan(),
                        hoaDon.getDiaChiNhan()
                ),
                paymentMethod != null ? paymentMethod : resolveLatestPaymentMethod(hoaDon),
                isOrderPaid(hoaDon),
                hoaDon.getGhiChu(),
                new OnlineOrderResponse.Summary(
                        safePrice(hoaDon.getTongTienHang()),
                        safePrice(hoaDon.getTienGiam()),
                        safePrice(hoaDon.getTienVanChuyen()),
                        safePrice(hoaDon.getTongTienThanhToan())
                ),
                responseItems
        );
    }

    private List<OnlineOrderResponse.Item> loadItems(HoaDon hoaDon) {
        List<OnlineOrderResponse.Item> items = new ArrayList<>();
        if (hoaDon.getChiTietHoaDons() == null) {
            return items;
        }

        for (ChiTietHoaDon cthd : hoaDon.getChiTietHoaDons()) {
            items.add(mapItem(
                    cthd.getChiTietSanPham(),
                    cthd.getSoLuong(),
                    cthd.getDonGia(),
                    cthd.getThanhTien(),
                    cthd.getId()
            ));
        }
        return items;
    }

    private List<com.example.AerionSports_BE.dto.ChiTietEmailDTO> buildEmailItems(List<ResolvedItem> resolvedItems) {
        List<com.example.AerionSports_BE.dto.ChiTietEmailDTO> items = new ArrayList<>();
        for (ResolvedItem item : resolvedItems) {
            ChiTietSanPham ctsp = item.ctsp();
            items.add(new com.example.AerionSports_BE.dto.ChiTietEmailDTO(
                    ctsp.getIdSanPham() != null ? ctsp.getIdSanPham().getTenSanPham() : "",
                    ctsp.getIdMauSac() != null ? ctsp.getIdMauSac().getTenMauSac() : "",
                    ctsp.getIdTrongLuong() != null ? ctsp.getIdTrongLuong().getTenTrongLuong() : "",
                    item.quantity(),
                    item.unitPrice(),
                    item.lineTotal()
            ));
        }
        return items;
    }

    private OnlineOrderResponse.Item mapItem(ChiTietSanPham ctsp, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal, Integer itemId) {
        String variant = buildVariantLabel(ctsp);
        String image = extractImage(ctsp);
        return new OnlineOrderResponse.Item(
                itemId,
                ctsp != null && ctsp.getIdSanPham() != null ? ctsp.getIdSanPham().getTenSanPham() : "",
                image,
                variant,
                quantity,
                unitPrice,
                null
        );
    }

    private String buildVariantLabel(ChiTietSanPham ctsp) {
        if (ctsp == null) {
            return "";
        }
        String color = ctsp.getIdMauSac() != null ? ctsp.getIdMauSac().getTenMauSac() : "";
        String weight = ctsp.getIdTrongLuong() != null ? ctsp.getIdTrongLuong().getTenTrongLuong() : "";
        List<String> parts = new ArrayList<>();
        if (!isBlank(color)) {
            parts.add(color.trim());
        }
        if (!isBlank(weight)) {
            parts.add(weight.trim());
        }
        return String.join(" / ", parts);
    }

    private String extractImage(ChiTietSanPham ctsp) {
        if (ctsp == null || ctsp.getHinhAnhs() == null || ctsp.getHinhAnhs().isEmpty()) {
            return null;
        }
        return ctsp.getHinhAnhs().stream()
                .filter(item -> item.getDuongDanAnh() != null && !item.getDuongDanAnh().isBlank())
                .findFirst()
                .map(item -> item.getDuongDanAnh())
                .orElse(null);
    }

    private String resolveLatestPaymentMethod(HoaDon hoaDon) {
        return lichSuThanhToanRepository.findByHoaDon_IdOrderByNgayThanhToanDesc(hoaDon.getId())
                .stream()
                .findFirst()
                .map(LichSuThanhToan::getPhuongThucThanhToan)
                .orElse("Chuyen khoan");
    }

    private boolean isOrderPaid(HoaDon hoaDon) {
        return lichSuThanhToanRepository.findByHoaDon_IdOrderByNgayThanhToanDesc(hoaDon.getId())
                .stream()
                .findFirst()
                .map(LichSuThanhToan::getTrangThaiThanhToan)
                .map(this::normalizeText)
                .map(status -> status.contains("thanh cong"))
                .orElse(false);
    }

    private String paymentMethodLabel(String paymentType) {
        return isBankTransfer(paymentType)
                ? "Chuyen khoan qua ngan hang"
                : "Thanh toan khi nhan hang (COD)";
    }

    private boolean isBankTransfer(String paymentType) {
        String value = normalizeText(paymentType);
        return value.contains("bank") || value.contains("transfer") || value.contains("chuyen") || value.contains("vnpay") || value.contains("momo");
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return normalized.toLowerCase(Locale.ROOT).trim();
    }

    private String buildFullAddress(OnlineCheckoutRequest request) {
        List<String> parts = new ArrayList<>();
        addIfPresent(parts, request.getDiaChiChiTiet());
        addIfPresent(parts, request.getPhuongXa());
        addIfPresent(parts, request.getQuanHuyen());
        addIfPresent(parts, request.getTinhThanh());
        return String.join(", ", parts);
    }

    private void addIfPresent(List<String> parts, String value) {
        if (!isBlank(value)) {
            parts.add(value.trim());
        }
    }

    private String generateOrderCode() {
        String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String code;
        do {
            code = "HD" + prefix + ThreadLocalRandom.current().nextInt(100, 999);
        } while (hoaDonRepository.existsByMaHoaDon(code));
        return code;
    }

    private String generateCustomerCode() {
        return "KH" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + ThreadLocalRandom.current().nextInt(10, 99);
    }

    private String getStatusText(Integer status) {
        return switch (status == null ? -1 : status) {
            case 0 -> "Cho xac nhan";
            case 1 -> "Da xac nhan";
            case 2 -> "Cho giao hang";
            case 3 -> "Dang giao hang";
            case 4 -> "Da giao hang";
            case 5 -> "Da hoan thanh";
            case 6 -> "Da huy";
            case 7 -> "Yeu cau huy";
            case 8 -> "Da hoan tien";
            default -> "Khoi tao";
        };
    }

    private int mapProgressStatus(Integer status) {
        if (status == null) {
            return 0;
        }
        return Math.max(0, Math.min(status, 5));
    }

    private BigDecimal safePrice(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String firstNonBlank(String first, String second) {
        if (!isBlank(first)) {
            return first.trim();
        }
        if (!isBlank(second)) {
            return second.trim();
        }
        return null;
    }

    private record ResolvedItem(ChiTietSanPham ctsp, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
}
