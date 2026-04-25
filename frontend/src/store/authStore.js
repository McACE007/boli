import { create } from 'zustand'
import { jwtDecode } from 'jwt-decode'

const decodeToken = (token) => {
  try {
    return jwtDecode(token)
  } catch {
    return null
  }
}

const stored = localStorage.getItem('token')
const initialDecoded = stored ? decodeToken(stored) : null

const useAuthStore = create((set) => ({
  token: stored || null,
  user: initialDecoded
    ? {
        username: initialDecoded.sub,
        userId:   initialDecoded.userId,
        role:     initialDecoded.role,
        status:   initialDecoded.status,
      }
    : null,

  setToken: (token) => {
    localStorage.setItem('token', token)
    const decoded = decodeToken(token)
    set({
      token,
      user: decoded
        ? {
            username: decoded.sub,
            userId:   decoded.userId,
            role:     decoded.role,
            status:   decoded.status,
          }
        : null,
    })
  },

  logout: () => {
    localStorage.removeItem('token')
    set({ token: null, user: null })
  },
}))

export default useAuthStore
