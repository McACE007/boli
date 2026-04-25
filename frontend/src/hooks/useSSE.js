import { useEffect, useRef, useState } from 'react'

export default function useSSE(auctionId) {
  const [bids, setBids]     = useState([])
  const [connected, setConnected] = useState(false)
  const esRef = useRef(null)

  useEffect(() => {
    if (!auctionId) return

    const es = new EventSource(`/api/notifications/stream/${auctionId}`)
    esRef.current = es

    es.onopen = () => setConnected(true)

    es.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)
        if (data.type === 'heartbeat') return
        setBids((prev) => [data, ...prev].slice(0, 50))
      } catch {
        // non-JSON heartbeat or unknown message
      }
    }

    es.onerror = () => {
      setConnected(false)
      es.close()
    }

    return () => {
      es.close()
      setConnected(false)
    }
  }, [auctionId])

  return { bids, connected }
}
