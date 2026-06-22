import racketImage from '../assets/mock_racket.png'

export const shopBrands = [
  { label: 'Yonex', count: 45 },
  { label: 'Victor', count: 32 },
  { label: 'Lining', count: 28 },
  { label: 'Mizuno', count: 15 },
]

export const priceRanges = [
  'Dưới 1 triệu',
  '1 triệu - 2 triệu',
  '2 triệu - 3 triệu',
  'Trên 3 triệu',
]

export const weightRanges = [
  '3U (85-89g)',
  '4U (80-84g)',
  '5U (75-79g)',
]

export const mockProducts = Array.from({ length: 12 }, (_, idx) => {
  const id = idx + 1
  const brand = id % 2 === 0 ? 'Yonex' : 'Victor'
  const price = 1500000 + id * 100000

  return {
    id,
    name: `Vợt Cầu Lông Cao Cấp ${id}`,
    brand,
    price,
    oldPrice: id % 3 === 0 ? price + 200000 : null,
    badge: id % 4 === 0 ? 'New' : null,
    image: racketImage,
    category: brand === 'Yonex' ? 'Tấn công' : 'Công thủ',
    description: 'Mẫu vợt mock dùng để test giao diện danh sách và chi tiết sản phẩm trên local.',
    specs: {
      'Độ cứng': 'Stiff',
      'Trọng lượng': id % 2 === 0 ? '4U / G5' : '3U / G5',
      'Cân bằng': id % 2 === 0 ? 'Cân bằng' : 'Nặng đầu',
      'Mức căng': '20 - 28 lbs',
    },
  }
})

export const featuredProducts = mockProducts.slice(0, 5)

export const getProductById = (id) => mockProducts.find((product) => String(product.id) === String(id))

export const cartItems = [
  {
    id: 1,
    ...mockProducts[0],
    quantity: 2,
    variant: 'Màu: Trắng Hổ Phách, Bản: 4U',
  },
]

export const cartSummary = {
  subtotal: 6900000,
  discount: 0,
  total: 6900000,
}

export const checkoutShipping = {
  city: 'Hà Nội',
  district: 'Cầu Giấy',
  paymentMethod: 'COD',
}
