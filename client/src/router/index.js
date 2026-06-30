import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../pages/Home/HomePage.vue') },
  { path: '/products', name: 'Products', component: () => import('../pages/Products/ProductsPage.vue') },
  { path: '/products/:id', name: 'ProductDetail', component: () => import('../pages/ProductDetail/ProductDetailPage.vue') },
  { path: '/cart', name: 'Cart', component: () => import('../pages/Cart/CartPage.vue') },
  { path: '/checkout', name: 'Checkout', component: () => import('../pages/Checkout/CheckoutPage.vue') },
  { path: '/login', name: 'Login', component: () => import('../pages/Auth/LoginPage.vue') },
  { path: '/register', name: 'Register', component: () => import('../pages/Auth/RegisterPage.vue') },
  { path: '/profile', name: 'Profile', component: () => import('../pages/Profile/ProfilePage.vue') },
  { path: '/my-orders', name: 'MyOrders', component: () => import('../pages/MyOrders/MyOrdersPage.vue') },
  { path: '/order-tracking', name: 'OrderTracking', component: () => import('../pages/OrderTracking/OrderTrackingPage.vue') },
]
const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router