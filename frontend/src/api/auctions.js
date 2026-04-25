import client from './client'

export const getAuctions = (params) =>
  client.get('/api/auctions', { params }).then((r) => r.data)

export const getAuction = (id) =>
  client.get(`/api/auctions/${id}`).then((r) => r.data)

export const createAuction = (data) =>
  client.post('/api/auctions', data).then((r) => r.data)
