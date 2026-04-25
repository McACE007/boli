import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { useQuery } from '@tanstack/react-query'
import { User, Mail, Shield, Activity, LogOut, Loader2, Gavel } from 'lucide-react'
import { getMe } from '../api/auth'
import useAuthStore from '../store/authStore'

export default function Profile() {
  const navigate  = useNavigate()
  const { user, logout } = useAuthStore()

  const { data, isLoading } = useQuery({
    queryKey: ['me'],
    queryFn: getMe,
  })

  const profile = data?.data

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const statusColor = {
    ACTIVE:   'status-live',
    INACTIVE: 'status-scheduled',
    BLOCKED:  'status-cancelled',
    DELETED:  'status-ended',
  }

  return (
    <div className="max-w-2xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col gap-6"
      >
        {/* Header card */}
        <div className="glass rounded-2xl overflow-hidden">
          <div className="h-24 bg-gradient-to-r from-violet-900 via-dark-800 to-cyan-900 relative">
            <div className="absolute inset-0 opacity-30"
                 style={{ backgroundImage: 'radial-gradient(circle at 40% 50%, rgba(139,92,246,0.8) 0%, transparent 60%)' }} />
          </div>
          <div className="px-6 pb-6">
            <div className="flex items-end gap-4 -mt-8 mb-4">
              <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-violet-600 to-cyan-500
                              flex items-center justify-center text-2xl font-bold text-white
                              shadow-glow ring-4 ring-dark-900">
                {user?.username?.[0]?.toUpperCase()}
              </div>
              {isLoading ? (
                <Loader2 size={18} className="animate-spin text-slate-500 mb-3" />
              ) : (
                <div className="mb-1">
                  <h1 className="font-display font-bold text-xl text-slate-100">
                    {profile?.fullName ?? user?.username}
                  </h1>
                  <p className="text-slate-500 text-sm">@{profile?.username ?? user?.username}</p>
                </div>
              )}
            </div>

            {profile && (
              <div className="flex flex-wrap gap-2">
                <span className={`text-xs font-semibold uppercase tracking-wide
                                 px-2.5 py-1 rounded-full ${statusColor[profile.status] ?? 'status-scheduled'}`}>
                  {profile.status}
                </span>
                <span className="text-xs font-semibold uppercase tracking-wide
                                 px-2.5 py-1 rounded-full bg-indigo-500/15 text-indigo-400 border border-indigo-500/30">
                  {profile.role}
                </span>
              </div>
            )}
          </div>
        </div>

        {/* Details */}
        {profile && (
          <motion.div
            initial={{ opacity: 0, y: 16 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="glass rounded-2xl p-6 flex flex-col gap-4"
          >
            <h2 className="font-display font-semibold text-slate-200 text-lg">Account Details</h2>

            {[
              { icon: User,   label: 'Full Name', value: profile.fullName  },
              { icon: Mail,   label: 'Email',     value: profile.email     },
              { icon: Shield, label: 'Role',      value: profile.role      },
              { icon: Activity, label: 'Status',  value: profile.status    },
            ].map(({ icon: Icon, label, value }) => (
              <div key={label}
                   className="flex items-center gap-4 py-3 border-b border-white/[0.04] last:border-0">
                <div className="w-9 h-9 rounded-xl glass flex items-center justify-center shrink-0">
                  <Icon size={15} className="text-violet-400" />
                </div>
                <div>
                  <p className="text-xs text-slate-600 uppercase tracking-wide">{label}</p>
                  <p className="text-slate-200 text-sm font-medium">{value}</p>
                </div>
              </div>
            ))}
          </motion.div>
        )}

        {/* Quick links */}
        <motion.div
          initial={{ opacity: 0, y: 16 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="glass rounded-2xl p-6"
        >
          <h2 className="font-display font-semibold text-slate-200 text-lg mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={() => navigate('/auctions')}
              className="glass glass-hover rounded-xl p-4 text-left flex items-center gap-3"
            >
              <Gavel size={18} className="text-violet-400" />
              <div>
                <p className="text-sm font-medium text-slate-200">Browse</p>
                <p className="text-xs text-slate-600">View auctions</p>
              </div>
            </button>
            <button
              onClick={() => navigate('/create')}
              className="glass glass-hover rounded-xl p-4 text-left flex items-center gap-3"
            >
              <Gavel size={18} className="text-cyan-400" />
              <div>
                <p className="text-sm font-medium text-slate-200">Create</p>
                <p className="text-xs text-slate-600">New auction</p>
              </div>
            </button>
          </div>
        </motion.div>

        {/* Logout */}
        <motion.button
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.3 }}
          onClick={handleLogout}
          whileTap={{ scale: 0.97 }}
          className="flex items-center justify-center gap-2 w-full py-3.5 rounded-xl
                     border border-red-500/20 text-red-400 hover:bg-red-500/10
                     transition-all duration-200 font-medium text-sm"
        >
          <LogOut size={16} />
          Sign Out
        </motion.button>
      </motion.div>
    </div>
  )
}
