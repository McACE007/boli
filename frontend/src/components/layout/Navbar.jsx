import { Link, useNavigate, useLocation } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Gavel, Plus, User, LogOut, Zap } from 'lucide-react'
import useAuthStore from '../../store/authStore'

export default function Navbar() {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()
  const { pathname } = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navLink = (to, label) => (
    <Link
      to={to}
      className={`text-sm font-medium transition-colors duration-200 ${
        pathname === to
          ? 'text-violet-400'
          : 'text-slate-400 hover:text-slate-100'
      }`}
    >
      {label}
    </Link>
  )

  return (
    <header className="sticky top-0 z-50 glass border-b border-white/[0.05]">
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        {/* Logo */}
        <Link to="/auctions" className="flex items-center gap-2 group">
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-violet-600 to-cyan-500
                          flex items-center justify-center shadow-glow-sm
                          group-hover:shadow-glow transition-shadow duration-300">
            <Gavel size={16} className="text-white" />
          </div>
          <span className="font-display font-bold text-lg tracking-tight gradient-text">
            BOLI
          </span>
        </Link>

        {/* Nav links */}
        <nav className="hidden md:flex items-center gap-6">
          {navLink('/auctions', 'Auctions')}
          {navLink('/create', 'Create Auction')}
        </nav>

        {/* Right side */}
        <div className="flex items-center gap-3">
          {user ? (
            <>
              <Link
                to="/create"
                className="hidden sm:flex btn-primary text-sm py-2 px-4 gap-1.5"
              >
                <Plus size={15} />
                New Auction
              </Link>

              <Link
                to="/profile"
                className="flex items-center gap-2 glass glass-hover rounded-xl px-3 py-1.5 text-sm text-slate-300"
              >
                <div className="w-6 h-6 rounded-full bg-gradient-to-br from-violet-500 to-cyan-500
                                flex items-center justify-center text-xs font-bold text-white">
                  {user.username?.[0]?.toUpperCase()}
                </div>
                <span className="hidden sm:block font-medium">{user.username}</span>
              </Link>

              <motion.button
                whileTap={{ scale: 0.93 }}
                onClick={handleLogout}
                className="p-2 text-slate-500 hover:text-red-400 hover:bg-red-500/10
                           rounded-xl transition-colors duration-200"
                title="Logout"
              >
                <LogOut size={17} />
              </motion.button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-ghost text-sm py-2 px-4">
                Sign In
              </Link>
              <Link to="/register" className="btn-primary text-sm py-2 px-4">
                <Zap size={14} />
                Get Started
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
