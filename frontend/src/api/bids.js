import client from './client'

export const placeBid = (data) =>
  client.post('/api/bids', data).then((r) => r.data)
