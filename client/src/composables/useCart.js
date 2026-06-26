import { computed, ref } from 'vue'

const CART_STORAGE_KEY = 'aerion-sports-cart'

const cartState = ref(loadCart())

function loadCart() {
  if (typeof window === 'undefined') return []

  try {
    const raw = window.localStorage.getItem(CART_STORAGE_KEY)
    const parsed = raw ? JSON.parse(raw) : []
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

function persistCart(items) {
  cartState.value = items

  if (typeof window === 'undefined') return
  window.localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(items))
}

export function useCart() {
  const cartItems = computed(() => cartState.value)
  const cartCount = computed(() => cartState.value.reduce((total, item) => total + Number(item.quantity || 0), 0))

  const sync = () => {
    persistCart(loadCart())
  }

  const addItem = (item) => {
    const next = [...cartState.value]
    const key = `${item.productId}-${item.variantKey || ''}`
    const index = next.findIndex((cartItem) => `${cartItem.productId}-${cartItem.variantKey || ''}` === key)

    if (index >= 0) {
      next[index] = {
        ...next[index],
        quantity: Number(next[index].quantity || 0) + Number(item.quantity || 1),
      }
    } else {
      next.push({
        id: Date.now(),
        quantity: Number(item.quantity || 1),
        productId: item.productId,
        variantKey: item.variantKey || '',
        selectedColor: item.selectedColor || '',
        selectedWeight: item.selectedWeight || '',
      })
    }

    persistCart(next)
  }

  const updateQuantity = (id, quantity) => {
    const next = cartState.value
      .map((item) => (item.id === id ? { ...item, quantity: Number(quantity || 1) } : item))
      .filter((item) => Number(item.quantity || 0) > 0)

    persistCart(next)
  }

  const removeItem = (id) => {
    persistCart(cartState.value.filter((item) => item.id !== id))
  }

  const clearCart = () => persistCart([])

  return {
    cartItems,
    cartCount,
    addItem,
    updateQuantity,
    removeItem,
    clearCart,
    sync,
  }
}
