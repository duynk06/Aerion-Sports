export const normalizeOnlineOrder = (order) => {
  if (!order || typeof order !== 'object') return order

  const statusIndex = Number(order.statusIndex)
  const isCompleted = statusIndex === 5

  return {
    ...order,
    isPaid: Boolean(order.isPaid) || isCompleted,
  }
}
