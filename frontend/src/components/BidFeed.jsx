import { motion, AnimatePresence } from 'framer-motion'
import { Wifi, WifiOff, TrendingUp, Zap } from 'lucide-react'
import { formatCurrency } from '../utils/format'

function BidItem({ bid, isFirst }) {
  return (
    <motion.div
      layout
      initial={{ opacity: 0, x: 20, scale: 0.95 }}
      animate={{ opacity: 1, x: 0,  scale: 1 }}
      exit={{ opacity: 0, x: -20, scale: 0.95 }}
      transition={{ duration: 0.3 }}
      className={`flex items-center justify-between p-3 rounded-xl
                  ${isFirst
                    ? 'bg-emerald-500/10 border border-emerald-500/20'
                    : 'bg-white/[0.03] border border-white/[0.04]'
                  }`}
    >
      <div className="flex items-center gap-3">
        <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold
                        ${isFirst
                          ? 'bg-gradient-to-br from-emerald-500 to-teal-400 text-white'
                          : 'bg-dark-700 text-slate-400'
                        }`}>
          {String(bid.bidderId ?? '?').slice(-2)}
        </div>
        <div>
          <p className="text-sm font-medium text-slate-200">
            Bidder #{bid.bidderId ?? '—'}
          </p>
          <p className="text-xs text-slate-600">{new Date().toLocaleTimeString()}</p>
        </div>
      </div>
      <div className="text-right">
        <p className={`font-display font-bold text-sm ${isFirst ? 'gradient-text' : 'text-slate-300'}`}>
          {formatCurrency(bid.amount)}
        </p>
        {isFirst && (
          <p className="text-[10px] text-emerald-400 font-medium">Highest</p>
        )}
      </div>
    </motion.div>
  )
}

export default function BidFeed({ bids, connected }) {
  return (
    <div className="flex flex-col gap-3">
      {/* Connection indicator */}
      <div className="flex items-center justify-between">
        <h3 className="font-display font-semibold text-slate-200 flex items-center gap-2">
          <Zap size={15} className="text-violet-400" />
          Live Bids
        </h3>
        <div className={`flex items-center gap-1.5 text-xs font-medium px-2.5 py-1 rounded-full
                        ${connected
                          ? 'bg-emerald-500/15 text-emerald-400'
                          : 'bg-slate-700/50 text-slate-500'
                        }`}>
          {connected ? <Wifi size={11} /> : <WifiOff size={11} />}
          {connected ? 'Connected' : 'Connecting…'}
        </div>
      </div>

      {/* Bid list */}
      <div className="flex flex-col gap-2 max-h-72 overflow-y-auto pr-1">
        <AnimatePresence initial={false}>
          {bids.length === 0 ? (
            <motion.div
              key="empty"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-center py-10 text-slate-600"
            >
              <TrendingUp size={28} className="mx-auto mb-2 opacity-30" />
              <p className="text-sm">No bids yet. Be the first!</p>
            </motion.div>
          ) : (
            bids.map((bid, i) => (
              <BidItem key={`${bid.bidId ?? i}-${bid.amount}`} bid={bid} isFirst={i === 0} />
            ))
          )}
        </AnimatePresence>
      </div>
    </div>
  )
}
