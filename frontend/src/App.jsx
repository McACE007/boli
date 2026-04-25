import { Routes, Route } from 'react-router-dom'
import Layout from './components/layout/Layout'
import ProtectedRoute from './components/ProtectedRoute'
import Landing from './pages/Landing'
import Login from './pages/Login'
import Register from './pages/Register'
import Auctions from './pages/Auctions'
import AuctionDetail from './pages/AuctionDetail'
import CreateAuction from './pages/CreateAuction'
import Profile from './pages/Profile'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route element={<Layout />}>
        <Route path="/auctions" element={
          <ProtectedRoute><Auctions /></ProtectedRoute>
        } />
        <Route path="/auctions/:id" element={
          <ProtectedRoute><AuctionDetail /></ProtectedRoute>
        } />
        <Route path="/create" element={
          <ProtectedRoute><CreateAuction /></ProtectedRoute>
        } />
        <Route path="/profile" element={
          <ProtectedRoute><Profile /></ProtectedRoute>
        } />
      </Route>
    </Routes>
  )
}
