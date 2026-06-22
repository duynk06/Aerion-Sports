const fs = require('fs');
const path = require('path');

const baseDir = "d:/A1/fe/Aerion-Sports/client";

const files = {
    "src/styles/global.css": `
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700;800&family=Inter:wght@300;400;500;600&display=swap');

:root {
  --primary-color: #0b162c; /* Dark Navy Blue */
  --primary-light: #1a294d;
  --secondary-color: #ff6b00; /* Vibrant Orange */
  --secondary-hover: #e66000;
  --background: #f8f9fa;
  --surface: #ffffff;
  --text-main: #333333;
  --text-muted: #6c757d;
  --border-color: #e9ecef;
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 16px;
  --shadow-sm: 0 2px 4px rgba(0,0,0,0.05);
  --shadow-md: 0 4px 12px rgba(0,0,0,0.08);
  --shadow-lg: 0 8px 24px rgba(0,0,0,0.12);
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Inter', sans-serif;
  color: var(--text-main);
  background-color: var(--background);
  line-height: 1.6;
}

h1, h2, h3, h4, h5, h6 {
  font-family: 'Montserrat', sans-serif;
  font-weight: 700;
  color: var(--primary-color);
}

a {
  text-decoration: none;
  color: inherit;
}

button {
  cursor: pointer;
  font-family: 'Montserrat', sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Utilities */
.btn-primary {
  background-color: var(--secondary-color);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: var(--radius-sm);
  font-weight: 600;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-primary:hover {
  background-color: var(--secondary-hover);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.btn-outline {
  background-color: transparent;
  color: var(--primary-color);
  border: 2px solid var(--primary-color);
  padding: 10px 24px;
  border-radius: var(--radius-sm);
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-outline:hover {
  background-color: var(--primary-color);
  color: white;
}

.text-orange {
  color: var(--secondary-color);
}
`,
    "src/router/index.js": `
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../pages/Home/HomePage.vue') },
  { path: '/products', name: 'Products', component: () => import('../pages/Products/ProductsPage.vue') },
  { path: '/products/:id', name: 'ProductDetail', component: () => import('../pages/ProductDetail/ProductDetailPage.vue') },
  { path: '/cart', name: 'Cart', component: () => import('../pages/Cart/CartPage.vue') },
  { path: '/checkout', name: 'Checkout', component: () => import('../pages/Checkout/CheckoutPage.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
`,
    "src/main.js": `
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './styles/global.css'

const app = createApp(App)
app.use(router)
app.mount('#app')
`,
    "src/App.vue": `
<template>
  <div class="app-wrapper">
    <AppHeader />
    <main class="main-content">
      <router-view />
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from './components/layout/AppHeader.vue'
import AppFooter from './components/layout/AppFooter.vue'
</script>

<style scoped>
.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.main-content {
  flex: 1;
  margin-top: 80px;
}
</style>
`,
    "src/components/layout/AppHeader.vue": `
<template>
  <header class="header">
    <div class="container header-inner">
      <router-link to="/" class="logo">
        <h2>AERION <span class="text-orange">SPORTS</span></h2>
      </router-link>
      
      <nav class="nav-links">
        <router-link to="/">Trang chủ</router-link>
        <router-link to="/products">Vợt Cầu Lông</router-link>
        <router-link to="#">Thương hiệu</router-link>
        <router-link to="#">Kiến thức</router-link>
        <router-link to="#">Liên hệ</router-link>
      </nav>

      <div class="header-actions">
        <div class="search-box">
          <input type="text" placeholder="Tìm kiếm vợt..." />
        </div>
        <router-link to="/cart" class="cart-icon">
          <span style="font-size:20px;">🛒</span>
          <span class="cart-count">2</span>
        </router-link>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: white;
  box-shadow: var(--shadow-sm);
  z-index: 100;
  display: flex;
  align-items: center;
}
.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.logo h2 {
  font-size: 24px;
  font-style: italic;
  letter-spacing: -0.5px;
  margin: 0;
}
.nav-links {
  display: flex;
  gap: 30px;
}
.nav-links a {
  font-family: 'Montserrat', sans-serif;
  font-weight: 600;
  color: var(--primary-color);
  font-size: 15px;
  text-transform: uppercase;
  transition: color 0.2s;
}
.nav-links a:hover, .nav-links a.router-link-active {
  color: var(--secondary-color);
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}
.search-box {
  position: relative;
}
.search-box input {
  padding: 10px 16px;
  border-radius: 20px;
  border: 1px solid var(--border-color);
  background: var(--background);
  width: 200px;
  outline: none;
  transition: all 0.3s;
}
.search-box input:focus {
  border-color: var(--secondary-color);
  background: white;
  box-shadow: 0 0 0 3px rgba(255, 107, 0, 0.1);
}
.cart-icon {
  position: relative;
}
.cart-count {
  position: absolute;
  top: -8px;
  right: -10px;
  background: var(--secondary-color);
  color: white;
  font-size: 12px;
  font-weight: bold;
  height: 20px;
  width: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
`,
    "src/components/layout/AppFooter.vue": `
<template>
  <footer class="footer">
    <div class="container footer-inner">
      <div class="footer-col">
        <h3>AERION <span class="text-orange">SPORTS</span></h3>
        <p>Chuyên cung cấp vợt cầu lông chính hãng. Nơi hội tụ đam mê của các lông thủ với chất lượng dịch vụ hàng đầu.</p>
      </div>
      <div class="footer-col">
        <h4>Về Chúng Tôi</h4>
        <ul>
          <li><a href="#">Giới thiệu</a></li>
          <li><a href="#">Hệ thống cửa hàng</a></li>
          <li><a href="#">Tin tức & Sự kiện</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>Chính Sách</h4>
        <ul>
          <li><a href="#">Chính sách đổi trả</a></li>
          <li><a href="#">Chính sách bảo hành</a></li>
          <li><a href="#">Chính sách vận chuyển</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>Liên Hệ</h4>
        <p>📞 1900 6868</p>
        <p>✉️ support@aerionsports.vn</p>
        <p>📍 123 Đường Vợt Cầu Lông, Hà Nội</p>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2026 Aerion Sports. All rights reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background-color: var(--primary-color);
  color: white;
  padding-top: 60px;
  margin-top: 60px;
}
.footer-inner {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1.5fr;
  gap: 40px;
  margin-bottom: 40px;
}
.footer h3 {
  color: white;
  font-size: 24px;
  font-style: italic;
  margin-bottom: 20px;
}
.footer h4 {
  color: white;
  font-size: 18px;
  margin-bottom: 20px;
  position: relative;
  padding-bottom: 10px;
}
.footer h4::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 2px;
  background-color: var(--secondary-color);
}
.footer p, .footer ul {
  color: #a0aec0;
  font-size: 14px;
  list-style: none;
}
.footer ul li {
  margin-bottom: 12px;
}
.footer ul li a {
  transition: color 0.3s;
}
.footer ul li a:hover {
  color: var(--secondary-color);
}
.footer-bottom {
  border-top: 1px solid rgba(255,255,255,0.1);
  padding: 20px 0;
  text-align: center;
  font-size: 14px;
  color: #a0aec0;
}
</style>
`,
    "src/pages/Home/HomePage.vue": `
<template>
  <div class="home">
    <section class="hero">
      <div class="container hero-content">
        <div class="hero-text">
          <h1>SỨC MẠNH VƯỢT TRỘI<br/><span class="text-orange">ĐAM MÊ BẤT TẬN</span></h1>
          <p>Khám phá bộ sưu tập vợt cầu lông chuyên nghiệp từ các thương hiệu hàng đầu thế giới. Nâng tầm lối chơi của bạn ngay hôm nay.</p>
          <div class="hero-actions">
            <router-link to="/products" class="btn-primary">Xem Tất Cả Vợt</router-link>
            <router-link to="/products" class="btn-outline">Tư Vấn Chọn Vợt</router-link>
          </div>
        </div>
        <div class="hero-image">
          <div class="mock-hero-img"></div>
        </div>
      </div>
    </section>

    <section class="features container">
      <div class="feature-box">
        <div class="icon">🏸</div>
        <h3>100% Chính Hãng</h3>
        <p>Cam kết chất lượng tuyệt đối</p>
      </div>
      <div class="feature-box">
        <div class="icon">🛡️</div>
        <h3>Bảo Hành Uy Tín</h3>
        <p>Lỗi 1 đổi 1 trong 30 ngày</p>
      </div>
      <div class="feature-box">
        <div class="icon">⚡</div>
        <h3>Giao Hàng Siêu Tốc</h3>
        <p>Giao hàng toàn quốc 2-3 ngày</p>
      </div>
      <div class="feature-box">
        <div class="icon">🎁</div>
        <h3>Quà Tặng Hấp Dẫn</h3>
        <p>Tặng kèm cước, quấn cán</p>
      </div>
    </section>

    <section class="hot-products container">
      <div class="section-header">
        <h2>VỢT BÁN CHẠY NHẤT</h2>
        <router-link to="/products" class="view-more">Xem thêm →</router-link>
      </div>
      <div class="product-grid">
        <ProductCard v-for="i in 4" :key="'hot-'+i" :id="i" :name="'Yonex Astrox 99 Pro'" :price="3450000" :brand="'Yonex'" :badge="i === 1 ? 'Best Seller' : ''" />
      </div>
    </section>

    <section class="new-products container">
      <div class="section-header">
        <h2>SẢN PHẨM MỚI</h2>
        <router-link to="/products" class="view-more">Xem thêm →</router-link>
      </div>
      <div class="product-grid">
        <ProductCard v-for="i in 4" :key="'new-'+i" :id="i+4" :name="'Victor Thruster Ryuga II'" :price="3200000" :brand="'Victor'" :badge="'New'" />
      </div>
    </section>
  </div>
</template>

<script setup>
import ProductCard from '../../components/product/ProductCard.vue'
</script>

<style scoped>
.hero {
  background: linear-gradient(135deg, var(--primary-color) 0%, #1a294d 100%);
  padding: 80px 0;
  color: white;
  overflow: hidden;
}
.hero-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.hero-text {
  flex: 1;
  max-width: 600px;
}
.hero h1 {
  color: white;
  font-size: 48px;
  line-height: 1.2;
  margin-bottom: 20px;
}
.hero p {
  font-size: 18px;
  color: #a0aec0;
  margin-bottom: 40px;
}
.hero-actions {
  display: flex;
  gap: 20px;
}
.hero-actions .btn-outline {
  border-color: white;
  color: white;
}
.hero-actions .btn-outline:hover {
  background: white;
  color: var(--primary-color);
}
.hero-image {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  position: relative;
}
.mock-hero-img {
  width: 400px;
  height: 500px;
  background: url('https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80') center/cover;
  border-radius: 20px;
  transform: rotate(15deg);
  box-shadow: 0 20px 50px rgba(0,0,0,0.5);
  border: 4px solid var(--secondary-color);
}
.features {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: -40px;
  position: relative;
  z-index: 10;
  margin-bottom: 60px;
}
.feature-box {
  background: white;
  padding: 30px 20px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  text-align: center;
  transition: transform 0.3s;
}
.feature-box:hover {
  transform: translateY(-5px);
}
.feature-box .icon {
  font-size: 40px;
  margin-bottom: 15px;
}
.feature-box h3 {
  font-size: 16px;
  margin-bottom: 8px;
}
.feature-box p {
  font-size: 14px;
  color: var(--text-muted);
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 30px;
  margin-top: 60px;
}
.section-header h2 {
  font-size: 28px;
  margin: 0;
}
.view-more {
  color: var(--secondary-color);
  font-weight: 600;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}
</style>
`,
    "src/components/product/ProductCard.vue": `
<template>
  <div class="product-card">
    <div v-if="badge" class="badge">{{ badge }}</div>
    <div class="product-image">
      <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60" :alt="name" />
      <div class="overlay">
        <router-link :to="'/products/' + id" class="btn-primary">Xem Chi Tiết</router-link>
      </div>
    </div>
    <div class="product-info">
      <span class="brand">{{ brand }}</span>
      <h3 class="name"><router-link :to="'/products/' + id">{{ name }}</router-link></h3>
      <div class="price-wrap">
        <span class="price">{{ formatPrice(price) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  id: Number,
  name: String,
  price: Number,
  brand: String,
  badge: String
})

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
  position: relative;
  border: 1px solid var(--border-color);
}
.product-card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-5px);
}
.badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: var(--secondary-color);
  color: white;
  font-size: 12px;
  font-weight: bold;
  padding: 4px 10px;
  border-radius: 20px;
  z-index: 2;
  text-transform: uppercase;
}
.product-image {
  position: relative;
  padding-top: 100%;
  background: #f4f4f4;
  overflow: hidden;
}
.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.product-card:hover .product-image img {
  transform: scale(1.05);
}
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(11, 22, 44, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}
.product-card:hover .overlay {
  opacity: 1;
}
.product-info {
  padding: 16px;
}
.brand {
  font-size: 12px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
}
.name {
  font-size: 16px;
  margin: 8px 0;
  line-height: 1.4;
  font-weight: 600;
}
.name a {
  color: var(--text-main);
  transition: color 0.2s;
}
.name a:hover {
  color: var(--secondary-color);
}
.price {
  font-size: 18px;
  font-weight: 700;
  color: var(--secondary-color);
  font-family: 'Montserrat', sans-serif;
}
</style>
`,
    "src/pages/Products/ProductsPage.vue": `
<template>
  <div class="products-page container">
    <div class="breadcrumb">
      <router-link to="/">Trang chủ</router-link>
      <span>/</span>
      <span>Vợt Cầu Lông</span>
    </div>
    
    <div class="page-header">
      <h1>VỢT CẦU LÔNG CHUYÊN NGHIỆP</h1>
      <div class="sort-box">
        <select>
          <option>Mới nhất</option>
          <option>Giá tăng dần</option>
          <option>Giá giảm dần</option>
          <option>Bán chạy</option>
        </select>
      </div>
    </div>

    <div class="catalog-layout">
      <aside class="sidebar">
        <div class="filter-group">
          <h3>Thương Hiệu</h3>
          <div class="checkbox-list">
            <label><input type="checkbox" /> Yonex (45)</label>
            <label><input type="checkbox" /> Victor (32)</label>
            <label><input type="checkbox" /> Lining (28)</label>
            <label><input type="checkbox" /> Mizuno (15)</label>
          </div>
        </div>
        
        <div class="filter-group">
          <h3>Mức Giá</h3>
          <div class="checkbox-list">
            <label><input type="checkbox" /> Dưới 1 triệu</label>
            <label><input type="checkbox" /> 1 triệu - 2 triệu</label>
            <label><input type="checkbox" /> 2 triệu - 3 triệu</label>
            <label><input type="checkbox" /> Trên 3 triệu</label>
          </div>
        </div>

        <div class="filter-group">
          <h3>Trọng Lượng</h3>
          <div class="checkbox-list">
            <label><input type="checkbox" /> 3U (85-89g)</label>
            <label><input type="checkbox" /> 4U (80-84g)</label>
            <label><input type="checkbox" /> 5U (75-79g)</label>
          </div>
        </div>
      </aside>
      
      <main class="product-list">
        <div class="grid">
          <ProductCard v-for="i in 12" :key="i" :id="i" :name="'Vợt Cầu Lông Cao Cấp ' + i" :price="1500000 + (i * 100000)" :brand="i % 2 === 0 ? 'Yonex' : 'Victor'" />
        </div>
        <div class="pagination">
          <button class="active">1</button>
          <button>2</button>
          <button>3</button>
          <button>Tiếp →</button>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import ProductCard from '../../components/product/ProductCard.vue'
</script>

<style scoped>
.products-page {
  padding: 40px 20px;
}
.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--text-muted);
}
.breadcrumb span {
  margin: 0 8px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  border-bottom: 2px solid var(--border-color);
  padding-bottom: 20px;
}
.page-header h1 {
  font-size: 32px;
  margin: 0;
}
.sort-box select {
  padding: 10px 16px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-color);
  outline: none;
  font-family: inherit;
}
.catalog-layout {
  display: flex;
  gap: 40px;
}
.sidebar {
  width: 260px;
  flex-shrink: 0;
}
.filter-group {
  margin-bottom: 30px;
}
.filter-group h3 {
  font-size: 16px;
  margin-bottom: 15px;
  text-transform: uppercase;
  letter-spacing: 1px;
}
.checkbox-list label {
  display: block;
  margin-bottom: 10px;
  cursor: pointer;
  font-size: 15px;
}
.product-list {
  flex: 1;
}
.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 50px;
}
.pagination button {
  width: 40px;
  height: 40px;
  border: 1px solid var(--border-color);
  background: white;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}
.pagination button.active, .pagination button:hover {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}
</style>
`,
    "src/pages/ProductDetail/ProductDetailPage.vue": `
<template>
  <div class="product-detail container">
    <div class="breadcrumb">
      <router-link to="/">Trang chủ</router-link>
      <span>/</span>
      <router-link to="/products">Vợt Cầu Lông</router-link>
      <span>/</span>
      <span class="current">Yonex Astrox 99 Pro</span>
    </div>

    <div class="detail-main">
      <div class="images">
        <div class="main-image">
          <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Vợt" />
        </div>
        <div class="thumb-list">
          <div class="thumb active"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" alt="" /></div>
          <div class="thumb"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" alt="" /></div>
          <div class="thumb"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" alt="" /></div>
        </div>
      </div>

      <div class="info">
        <div class="brand">Yonex</div>
        <h1>Vợt Cầu Lông Yonex Astrox 99 Pro Chính Hãng</h1>
        <div class="price">3.450.000 ₫</div>
        
        <div class="variants">
          <div class="variant-group">
            <h4>Trọng lượng / Kích thước cán:</h4>
            <div class="options">
              <button class="active">3U / G5</button>
              <button>4U / G5</button>
            </div>
          </div>
          <div class="variant-group">
            <h4>Màu sắc:</h4>
            <div class="options">
              <button class="active">Trắng Hổ Phách (White Tiger)</button>
              <button>Đỏ Đậm (Cherry Sunburst)</button>
            </div>
          </div>
        </div>

        <div class="actions">
          <div class="qty">
            <button>-</button>
            <input type="text" value="1" />
            <button>+</button>
          </div>
          <button class="btn-primary add-to-cart">Thêm Vào Giỏ Hàng</button>
        </div>

        <div class="perks">
          <p>✔️ Tặng cước Yonex BG65 Ti</p>
          <p>✔️ Miễn phí công căng</p>
          <p>✔️ Tặng quấn cán Yonex xịn</p>
          <p>✔️ Bảo hành 3 tháng lỗi nhà sản xuất</p>
        </div>
      </div>
    </div>

    <div class="detail-tabs">
      <div class="tabs-header">
        <button class="active">Thông Số Kỹ Thuật</button>
        <button>Mô Tả Sản Phẩm</button>
      </div>
      <div class="tabs-content">
        <table class="specs-table">
          <tbody>
            <tr><td>Độ cứng</td><td>Siêu Cứng (Extra Stiff)</td></tr>
            <tr><td>Khung vợt</td><td>H.M. GRAPHITE + Namd + VOLUME CUT RESIN + Tungsten</td></tr>
            <tr><td>Thân vợt</td><td>H.M. Graphite + Namd</td></tr>
            <tr><td>Điểm cân bằng</td><td>Nặng đầu</td></tr>
            <tr><td>Mức căng tối đa</td><td>3U: 21 - 29 lbs, 4U: 20 - 28 lbs</td></tr>
            <tr><td>Trình độ phù hợp</td><td>Người chơi chuyên nghiệp, có lực cổ tay khỏe, lối đánh thiên công.</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-detail {
  padding: 40px 20px;
}
.breadcrumb {
  margin-bottom: 30px;
  font-size: 14px;
  color: var(--text-muted);
}
.breadcrumb span {
  margin: 0 8px;
}
.breadcrumb .current {
  color: var(--text-main);
  font-weight: 500;
}
.detail-main {
  display: flex;
  gap: 60px;
  margin-bottom: 60px;
}
.images {
  flex: 1;
}
.main-image {
  background: white;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  padding: 20px;
  margin-bottom: 16px;
}
.main-image img {
  width: 100%;
  height: auto;
  display: block;
}
.thumb-list {
  display: flex;
  gap: 16px;
}
.thumb {
  width: 80px;
  height: 80px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  padding: 5px;
  cursor: pointer;
  background: white;
}
.thumb.active {
  border-color: var(--secondary-color);
}
.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.info {
  flex: 1;
}
.brand {
  font-size: 14px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 10px;
}
.info h1 {
  font-size: 32px;
  margin-bottom: 20px;
  line-height: 1.3;
}
.price {
  font-size: 36px;
  font-weight: 700;
  color: var(--secondary-color);
  font-family: 'Montserrat', sans-serif;
  margin-bottom: 30px;
}
.variant-group {
  margin-bottom: 20px;
}
.variant-group h4 {
  font-size: 14px;
  margin-bottom: 10px;
  color: var(--text-main);
}
.options {
  display: flex;
  gap: 10px;
}
.options button {
  padding: 10px 20px;
  border: 1px solid var(--border-color);
  background: white;
  border-radius: var(--radius-sm);
  transition: all 0.2s;
}
.options button.active, .options button:hover {
  border-color: var(--secondary-color);
  color: var(--secondary-color);
  background: rgba(255, 107, 0, 0.05);
}
.actions {
  display: flex;
  gap: 20px;
  margin-top: 40px;
  margin-bottom: 40px;
  padding-top: 30px;
  border-top: 1px solid var(--border-color);
}
.qty {
  display: flex;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  overflow: hidden;
}
.qty button {
  width: 40px;
  border: none;
  background: white;
  font-size: 20px;
}
.qty input {
  width: 50px;
  border: none;
  text-align: center;
  border-left: 1px solid var(--border-color);
  border-right: 1px solid var(--border-color);
  font-size: 16px;
  font-weight: bold;
}
.add-to-cart {
  flex: 1;
  font-size: 18px;
  height: 50px;
}
.perks {
  background: #f8f9fa;
  padding: 20px;
  border-radius: var(--radius-md);
  border-left: 4px solid var(--secondary-color);
}
.perks p {
  margin-bottom: 8px;
  font-weight: 500;
}
.detail-tabs {
  margin-top: 60px;
}
.tabs-header {
  display: flex;
  gap: 40px;
  border-bottom: 1px solid var(--border-color);
  margin-bottom: 30px;
}
.tabs-header button {
  padding: 15px 0;
  background: none;
  border: none;
  font-size: 20px;
  color: var(--text-muted);
  position: relative;
}
.tabs-header button.active {
  color: var(--primary-color);
}
.tabs-header button.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 3px;
  background: var(--secondary-color);
}
.specs-table {
  width: 100%;
  max-width: 800px;
  border-collapse: collapse;
}
.specs-table td {
  padding: 15px;
  border-bottom: 1px solid var(--border-color);
}
.specs-table td:first-child {
  font-weight: 600;
  width: 30%;
  background: #f8f9fa;
}
</style>
`,
    "src/pages/Cart/CartPage.vue": `
<template>
  <div class="cart-page container">
    <h1>GIỎ HÀNG CỦA BẠN</h1>
    <div class="cart-layout">
      <div class="cart-items">
        <div class="cart-header">
          <div class="col-product">Sản phẩm</div>
          <div class="col-price">Đơn giá</div>
          <div class="col-qty">Số lượng</div>
          <div class="col-total">Thành tiền</div>
        </div>
        
        <div class="cart-item" v-for="i in 2" :key="i">
          <div class="col-product info-cell">
            <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=60" alt="" />
            <div>
              <h3>Yonex Astrox 99 Pro</h3>
              <p>Màu: Trắng Hổ Phách, Bản: 4U</p>
              <button class="remove-btn">Xóa</button>
            </div>
          </div>
          <div class="col-price">3.450.000 ₫</div>
          <div class="col-qty">
            <div class="qty-control">
              <button>-</button>
              <input type="text" value="1" />
              <button>+</button>
            </div>
          </div>
          <div class="col-total">3.450.000 ₫</div>
        </div>
      </div>
      
      <div class="cart-summary">
        <h3>Tóm Tắt Đơn Hàng</h3>
        <div class="summary-row">
          <span>Tạm tính</span>
          <span>6.900.000 ₫</span>
        </div>
        <div class="summary-row">
          <span>Khuyến mãi</span>
          <span>- 0 ₫</span>
        </div>
        <div class="summary-total">
          <span>Tổng cộng</span>
          <span class="total-price">6.900.000 ₫</span>
        </div>
        <p class="tax-note">(Đã bao gồm VAT nếu có)</p>
        <router-link to="/checkout" class="btn-primary w-100">Tiến Hành Đặt Hàng</router-link>
        <router-link to="/products" class="continue-shopping">Tiếp tục mua hàng</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.cart-page {
  padding: 40px 20px;
}
.cart-page h1 {
  margin-bottom: 40px;
}
.cart-layout {
  display: flex;
  gap: 40px;
}
.cart-items {
  flex: 1;
}
.cart-header {
  display: flex;
  padding-bottom: 15px;
  border-bottom: 2px solid var(--border-color);
  font-weight: 600;
  text-transform: uppercase;
  font-size: 14px;
}
.col-product { flex: 2; }
.col-price { flex: 1; text-align: center; }
.col-qty { flex: 1; text-align: center; }
.col-total { flex: 1; text-align: right; }

.cart-item {
  display: flex;
  align-items: center;
  padding: 30px 0;
  border-bottom: 1px solid var(--border-color);
}
.info-cell {
  display: flex;
  gap: 20px;
}
.info-cell img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-color);
}
.info-cell h3 {
  font-size: 18px;
  margin-bottom: 5px;
}
.info-cell p {
  color: var(--text-muted);
  font-size: 14px;
  margin-bottom: 10px;
}
.remove-btn {
  background: none;
  border: none;
  color: #dc3545;
  text-decoration: underline;
  font-size: 14px;
}
.qty-control {
  display: inline-flex;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  overflow: hidden;
}
.qty-control button {
  width: 30px;
  height: 35px;
  background: white;
  border: none;
}
.qty-control input {
  width: 40px;
  height: 35px;
  border: none;
  border-left: 1px solid var(--border-color);
  border-right: 1px solid var(--border-color);
  text-align: center;
}
.cart-item .col-price, .cart-item .col-total {
  font-weight: 600;
}
.cart-item .col-total {
  color: var(--secondary-color);
}

.cart-summary {
  width: 360px;
  background: #f8f9fa;
  padding: 30px;
  border-radius: var(--radius-md);
  height: fit-content;
}
.cart-summary h3 {
  margin-bottom: 25px;
  font-size: 20px;
}
.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.summary-total {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid var(--border-color);
  font-size: 18px;
  font-weight: bold;
}
.total-price {
  color: var(--secondary-color);
  font-size: 24px;
}
.tax-note {
  font-size: 12px;
  color: var(--text-muted);
  text-align: right;
  margin-bottom: 30px;
}
.w-100 {
  width: 100%;
  padding: 15px;
  font-size: 16px;
  text-align: center;
}
.continue-shopping {
  display: block;
  text-align: center;
  margin-top: 20px;
  color: var(--primary-color);
  text-decoration: underline;
  font-weight: 500;
}
</style>
`,
    "src/pages/Checkout/CheckoutPage.vue": `
<template>
  <div class="checkout-page container">
    <h1>THÔNG TIN THANH TOÁN</h1>
    <div class="checkout-layout">
      <div class="checkout-form">
        <div class="form-section">
          <h3>Thông tin giao hàng</h3>
          <div class="form-row">
            <div class="form-group">
              <label>Họ và tên</label>
              <input type="text" placeholder="Nhập họ tên" />
            </div>
            <div class="form-group">
              <label>Số điện thoại</label>
              <input type="text" placeholder="Nhập số điện thoại" />
            </div>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="email" placeholder="Nhập email" />
          </div>
          <div class="form-group">
            <label>Địa chỉ</label>
            <input type="text" placeholder="Nhập địa chỉ nhận hàng" />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Tỉnh/Thành phố</label>
              <select><option>Hà Nội</option></select>
            </div>
            <div class="form-group">
              <label>Quận/Huyện</label>
              <select><option>Cầu Giấy</option></select>
            </div>
          </div>
          <div class="form-group">
            <label>Ghi chú đơn hàng (Tùy chọn)</label>
            <textarea rows="3" placeholder="Ghi chú về mức căng dây, thời gian giao hàng..."></textarea>
          </div>
        </div>

        <div class="form-section mt-40">
          <h3>Phương thức thanh toán</h3>
          <div class="payment-methods">
            <label class="payment-method">
              <input type="radio" name="payment" checked />
              <div class="method-info">
                <h4>Thanh toán khi nhận hàng (COD)</h4>
                <p>Khách hàng trả tiền mặt khi giao hàng</p>
              </div>
            </label>
            <label class="payment-method">
              <input type="radio" name="payment" />
              <div class="method-info">
                <h4>Chuyển khoản qua ngân hàng</h4>
                <p>Thanh toán an toàn qua VNPAY / Momo</p>
              </div>
            </label>
          </div>
        </div>
      </div>
      
      <div class="order-summary">
        <h3>Đơn Hàng Của Bạn</h3>
        <div class="summary-items">
          <div class="item">
            <div class="item-name">Yonex Astrox 99 Pro x 2</div>
            <div class="item-price">6.900.000 ₫</div>
          </div>
        </div>
        <div class="summary-row">
          <span>Tạm tính</span>
          <span>6.900.000 ₫</span>
        </div>
        <div class="summary-row">
          <span>Phí vận chuyển</span>
          <span>Miễn phí</span>
        </div>
        <div class="summary-total">
          <span>Tổng cộng</span>
          <span class="total-price">6.900.000 ₫</span>
        </div>
        <button class="btn-primary w-100 mt-20">ĐẶT HÀNG NGAY</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.checkout-page {
  padding: 40px 20px;
}
.checkout-page h1 {
  margin-bottom: 40px;
}
.checkout-layout {
  display: flex;
  gap: 50px;
}
.checkout-form {
  flex: 2;
}
.form-section h3 {
  font-size: 20px;
  margin-bottom: 25px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}
.mt-40 { margin-top: 40px; }
.mt-20 { margin-top: 20px; }
.form-row {
  display: flex;
  gap: 20px;
}
.form-group {
  margin-bottom: 20px;
  flex: 1;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  font-size: 14px;
}
.form-group input, .form-group select, .form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  background: white;
  font-family: inherit;
  transition: border-color 0.2s;
}
.form-group input:focus, .form-group select:focus, .form-group textarea:focus {
  border-color: var(--secondary-color);
  outline: none;
}
.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.payment-method {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;
}
.payment-method:has(input:checked) {
  border-color: var(--secondary-color);
  background: rgba(255, 107, 0, 0.02);
}
.payment-method input {
  margin-top: 5px;
}
.method-info h4 {
  margin-bottom: 5px;
  font-size: 16px;
  color: var(--text-main);
}
.method-info p {
  font-size: 14px;
  color: var(--text-muted);
}

.order-summary {
  flex: 1;
  background: white;
  padding: 30px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  height: fit-content;
  position: sticky;
  top: 100px;
}
.order-summary h3 {
  font-size: 20px;
  margin-bottom: 25px;
}
.summary-items {
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 20px;
  margin-bottom: 20px;
}
.item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.item-name { font-weight: 500; }
.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.summary-total {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid var(--border-color);
  font-size: 18px;
  font-weight: bold;
}
.total-price {
  color: var(--secondary-color);
  font-size: 24px;
}
.w-100 {
  width: 100%;
  padding: 15px;
  font-size: 16px;
}
</style>
`
};

for (const [filepath, content] of Object.entries(files)) {
    const fullPath = path.join(baseDir, filepath);
    fs.mkdirSync(path.dirname(fullPath), { recursive: true });
    fs.writeFileSync(fullPath, content.trim(), 'utf8');
}
console.log('Files generated successfully!');
