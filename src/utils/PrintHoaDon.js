export const printInvoice = (
    hoaDon,
    danhSachSanPham
  ) => {
  
    const formatCurrency = (value) => {
      if (!value) return '0 đ'
      return Number(value).toLocaleString('vi-VN') + ' đ'
    }
  
    const printWindow = window.open('', '_blank')
  
    const productRows = danhSachSanPham
      .map((item, index) => `
        <tr>
          <td>${index + 1}</td>
          <td>${item.tenSanPham}</td>
          <td>${item.soLuong}</td>
          <td>${formatCurrency(item.donGia)}</td>
          <td>${formatCurrency(item.thanhTien)}</td>
        </tr>
      `)
      .join('')
  
    printWindow.document.write(`
      <html>
        <head>
          <title>${hoaDon.maHoaDon}</title>
  
          <style>
            body{
              font-family:Arial;
              padding:20px;
            }
  
            .header{
              text-align:center;
            }
  
            .title{
              font-size:26px;
              font-weight:bold;
            }
  
            .sub{
              margin-top:5px;
              color:#555;
            }
  
            table{
              width:100%;
              border-collapse:collapse;
              margin-top:15px;
            }
  
            th,td{
              border:1px solid #ddd;
              padding:8px;
              text-align:center;
            }
  
            .total{
              margin-top:20px;
              text-align:right;
            }
          </style>
        </head>
  
        <body>
  
          <div class="header">
            <div class="title">
              AERION SPORTS
            </div>
  
            <div class="sub">
              HỆ THỐNG CẦU LÔNG CHÍNH HÃNG
            </div>
          </div>
  
          <hr>
  
          <p>
            <b>Mã hóa đơn:</b>
            ${hoaDon.maHoaDon}
          </p>
  
          <p>
            <b>Ngày tạo:</b>
            ${new Date(hoaDon.ngayTao)
              .toLocaleString('vi-VN')}
          </p>
  
          <p>
            <b>Khách hàng:</b>
            ${hoaDon.hoTen}
          </p>
  
          <p>
            <b>SĐT:</b>
            ${hoaDon.sdt}
          </p>
  
          <table>
            <thead>
              <tr>
                <th>STT</th>
                <th>Sản phẩm</th>
                <th>SL</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
              </tr>
            </thead>
  
            <tbody>
              ${productRows}
            </tbody>
          </table>
  
          <div class="total">
  
            <p>
              Tổng tiền hàng:
              <b>
                ${formatCurrency(
                  hoaDon.tongTienHang
                )}
              </b>
            </p>
  
            <p>
              Giảm giá:
              <b>
                ${formatCurrency(
                  hoaDon.tienGiam
                )}
              </b>
            </p>
  
            <p>
              Phí vận chuyển:
              <b>
                ${formatCurrency(
                  hoaDon.tienVanChuyen
                )}
              </b>
            </p>
  
            <h2>
              Tổng thanh toán:
              ${formatCurrency(
                hoaDon.tongTienThanhToan
              )}
            </h2>
  
          </div>
  
          <hr>
  
          <div
            style="
              text-align:center;
              margin-top:30px;
            "
          >
            <h3>Cảm ơn quý khách!</h3>
            <p>Hẹn gặp lại tại AERION SPORTS</p>
          </div>
  
        </body>
      </html>
    `)
  
    printWindow.document.close()
    printWindow.print()
  }