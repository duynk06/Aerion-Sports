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