import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Clock, TrendingUp, ArrowRight } from 'lucide-react'
import useCountdown from '../hooks/useCountdown'
import { formatCurrency, formatDate } from '../utils/format'

const STATUS_CLASS = {
  LIVE:      'status-live',
  SCHEDULED: 'status-scheduled',
  ENDED:     'status-ended',
  CANCELLED: 'status-cancelled',
}

const STATUS_LABEL = {
  LIVE: 'Live', SCHEDULED: 'Upcoming', ENDED: 'Ended', CANCELLED: 'Cancelled',
}

function CountdownBadge({ endTime, status }) {
  const remaining = useCountdown(endTime)
  if (status !== 'LIVE' && status !== 'SCHEDULED') return null
  if (!remaining) return <span className="text-xs text-red-400">Ended</span>

  const { days, hours, minutes, seconds } = remaining
  const parts = days > 0
    ? `${days}d ${hours}h ${minutes}m`
    : `${hours}h ${minutes}m ${seconds}s`

  return (
    <span className="flex items-center gap-1 text-xs text-slate-400">
      <Clock size={11} />
      {parts}
    </span>
  )
}

export default function AuctionCard({ auction, index = 0 }) {
  const { id, title, description, startingPrice, status, endTime } = auction

  return (
    <motion.div
      initial={{ opacity: 0, y: 24 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: index * 0.07, duration: 0.4 }}
      whileHover={{ y: -4, transition: { duration: 0.2 } }}
    >
      <Link to={`/auctions/${id}`} className="block group">
        <div className="glass glass-hover rounded-2xl overflow-hidden
                        hover:shadow-glow-sm transition-all duration-300 h-full">
          {/* Color band */}
          <div className={`h-1 w-full ${
            status === 'LIVE'      ? 'bg-gradient-to-r from-emerald-500 to-teal-400' :
            status === 'SCHEDULED' ? 'bg-gradient-to-r from-violet-600 to-indigo-400' :
            'bg-gradient-to-r from-slate-700 to-slate-600'
          }`} />

          <div className="p-5 flex flex-col gap-3">
            {/* Header */}
            <div className="flex items-start justify-between gap-3">
              <h3 className="font-display font-semibold text-slate-100 text-base leading-snug
                             group-hover:gradient-text transition-all duration-300 line-clamp-2">
                {title}
              </h3>
              <span className={`shrink-0 text-[11px] font-semibold uppercase tracking-wide
                               px-2.5 py-1 rounded-full ${STATUS_CLASS[status]}`}>
                {status === 'LIVE' && (
                  <span className="inline-block w-1.5 h-1.5 rounded-full bg-emerald-400
                                   animate-pulse mr-1.5" />
                )}
                {STATUS_LABEL[status]}
              </span>
            </div>

            {/* Description */}
            <p className="text-slate-500 text-sm line-clamp-2 leading-relaxed">
              {description}
            </p>

            {/* Price + timer */}
            <div className="flex items-center justify-between mt-auto pt-3
                            border-t border-white/[0.05]">
              <div>
                <p className="text-[11px] text-slate-600 uppercase tracking-wide mb-0.5">
                  Starting Bid
                </p>
                <p className="font-display font-bold text-lg gradient-text leading-none">
                  {formatCurrency(startingPrice)}
                </p>
              </div>
              <div className="flex flex-col items-end gap-1">
                <CountdownBadge endTime={endTime} status={status} />
                <span className="flex items-center gap-1 text-xs text-violet-500
                                 group-hover:text-violet-400 transition-colors">
                  View
                  <ArrowRight size={11} className="group-hover:translate-x-1 transition-transform" />
                </span>
              </div>
            </div>
          </div>
        </div>
      </Link>
    </motion.div>
  )
}
