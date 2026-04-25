import client from './client'

export const login = (data) =>
  client.post('/api/auth/login', data).then((r) => r.data)

export const register = (data) =>
  client.post('/api/auth/register', data).then((r) => r.data)

export const getMe = () =>
  client.get('/api/me').then((r) => r.data)
