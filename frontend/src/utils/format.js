export const formatCurrency = (amount) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount)

export const formatDate = (iso) =>
  new Intl.DateTimeFormat('en-US', {
    month: 'short', day: 'numeric', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  }).format(new Date(iso))

export const formatDateInput = (iso) => {
  const d = new Date(iso)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

export const toISOStringLocal = (localDateTimeStr) =>
  new Date(localDateTimeStr).toISOString()

export const statusLabel = (status) =>
  ({ SCHEDULED: 'Scheduled', LIVE: 'Live', ENDED: 'Ended', CANCELLED: 'Cancelled' }[status] ?? status)
