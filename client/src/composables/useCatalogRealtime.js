import { onBeforeUnmount, onMounted } from 'vue'
import { API_BASE_URL } from '../services/api'

const listeners = new Set()
let eventSource = null
let reconnectTimer = null

const connect = () => {
  if (eventSource || typeof window === 'undefined' || typeof window.EventSource === 'undefined') {
    return
  }

  const url = `${API_BASE_URL}/api/realtime/catalog/stream`
  const source = new EventSource(url)
  eventSource = source

  source.addEventListener('catalog-update', (event) => {
    let payload = null

    try {
      payload = JSON.parse(event.data)
    } catch {
      payload = { type: 'catalog-update' }
    }

    listeners.forEach((listener) => listener(payload))
  })

  source.onerror = () => {
    source.close()
    eventSource = null

    if (listeners.size > 0 && reconnectTimer == null) {
      reconnectTimer = window.setTimeout(() => {
        reconnectTimer = null
        connect()
      }, 3000)
    }
  }
}

const disconnectIfIdle = () => {
  if (listeners.size > 0) return

  if (eventSource) {
    eventSource.close()
    eventSource = null
  }

  if (reconnectTimer != null) {
    window.clearTimeout(reconnectTimer)
    reconnectTimer = null
  }
}

export function useCatalogRealtime(handler) {
  const listener = (payload) => {
    if (typeof handler === 'function') {
      handler(payload)
    }
  }

  onMounted(() => {
    listeners.add(listener)
    connect()
  })

  onBeforeUnmount(() => {
    listeners.delete(listener)
    disconnectIfIdle()
  })
}
