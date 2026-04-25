import { useEffect, useState } from 'react'

function getRemaining(endTime) {
  const diff = new Date(endTime).getTime() - Date.now()
  if (diff <= 0) return null
  const s = Math.floor(diff / 1000)
  return {
    days:    Math.floor(s / 86400),
    hours:   Math.floor((s % 86400) / 3600),
    minutes: Math.floor((s % 3600) / 60),
    seconds: s % 60,
  }
}

export default function useCountdown(endTime) {
  const [remaining, setRemaining] = useState(() => getRemaining(endTime))

  useEffect(() => {
    if (!endTime) return
    const id = setInterval(() => {
      const r = getRemaining(endTime)
      setRemaining(r)
      if (!r) clearInterval(id)
    }, 1000)
    return () => clearInterval(id)
  }, [endTime])

  return remaining
}
