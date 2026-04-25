import { Outlet } from 'react-router-dom'
import Navbar from './Navbar'
import Background from '../Background'

export default function Layout() {
  return (
    <div className="min-h-screen">
      <Background />
      <Navbar />
      <main className="max-w-7xl mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  )
}
