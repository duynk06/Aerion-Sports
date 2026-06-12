
INSERT INTO khach_hang
(
    ma_khach_hang,
    ho_ten,
    ngay_sinh,
    gioi_tinh,
    sdt,
    email,
    avatar,
    ngay_tao,
    ngay_cap_nhat,
    trang_thai
)
VALUES
    (
        'KH001',
        N'Nguyễn Văn An',
        '2000-01-15',
        1,
        '0987654321',
        'an@gmail.com',
        'avatar1.jpg',
        GETDATE(),
        GETDATE(),
        1
    ),
    (
        'KH002',
        N'Trần Thị Bình',
        '1999-05-20',
        0,
        '0978123456',
        'binh@gmail.com',
        'avatar2.jpg',
        GETDATE(),
        GETDATE(),
        1
    ),
    (
        'KH003',
        N'Lê Minh Cường',
        '1998-08-10',
        1,
        '0966333444',
        'cuong@gmail.com',
        'avatar3.jpg',
        GETDATE(),
        GETDATE(),
        1
    ),
    (
        'KH004',
        N'Phạm Thu Dung',
        '2001-03-25',
        0,
        '0911222333',
        'dung@gmail.com',
        'avatar4.jpg',
        GETDATE(),
        GETDATE(),
        1
    ),
    (
        'KH005',
        N'Hoàng Đức Anh',
        '1997-11-12',
        1,
        '0933444555',
        'anh@gmail.com',
        'avatar5.jpg',
        GETDATE(),
        GETDATE(),
        1
    );

-- VaiTro
INSERT INTO vai_tro
(ma_vai_tro, ten_vai_tro, ngay_tao, ngay_cap_nhat, nguoi_cap_nhat, trang_thai)
VALUES
    ('ADMIN', 'Quản trị viên', GETDATE(), GETDATE(), 'System', 1),
    ('QL', 'Quản lý', GETDATE(), GETDATE(), 'System', 1),
    ('NV', 'Nhân viên', GETDATE(), GETDATE(), 'System', 1);

-- NhanVien
INSERT INTO nhan_vien
(
    id_vai_tro,
    ma_nv,
    ten_nv,
    sdt,
    email,
    gioi_tinh,
    avatar,
    ngay_sinh,
    ngay_tao,
    dia_chi,
    ngay_sua,
    trang_thai
)
VALUES
    (
        1,
        'NV001',
        N'Nguyễn Văn An',
        '0901234567',
        'an@gmail.com',
        1,
        'avatar1.jpg',
        '2000-01-15',
        GETDATE(),
        N'Hà Nội',
        GETDATE(),
        1
    ),
    (
        2,
        'NV002',
        N'Trần Thị Bình',
        '0901234568',
        'binh@gmail.com',
        0,
        'avatar2.jpg',
        '1999-05-20',
        GETDATE(),
        N'Hồ Chí Minh',
        GETDATE(),
        1
    ),
    (
        3,
        'NV003',
        N'Lê Văn Cường',
        '0901234569',
        'cuong@gmail.com',
        1,
        'avatar3.jpg',
        '2001-09-10',
        GETDATE(),
        N'Đà Nẵng',
        GETDATE(),
        1
    ),
    (
        3,
        'NV004',
        N'Phạm Thị Dung',
        '0901234570',
        'dung@gmail.com',
        0,
        'avatar4.jpg',
        '2002-03-25',
        GETDATE(),
        N'Hải Phòng',
        GETDATE(),
        1
    ),
    (
        2,
        'NV005',
        N'Hoàng Minh Đức',
        '0901234571',
        'duc@gmail.com',
        1,
        'avatar5.jpg',
        '1998-11-30',
        GETDATE(),
        N'Cần Thơ',
        GETDATE(),
        1
    );