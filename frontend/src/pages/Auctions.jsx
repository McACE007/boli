import { useState } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { useQuery } from '@tanstack/react-query'
import { Plus, Search, SlidersHorizontal, Loader2, Gavel } from 'lucide-react'
import { getAuctions } from '../api/auctions'
import AuctionCard from '../components/AuctionCard'

const STATUSES = ['ALL', 'LIVE', 'SCHEDULED', 'ENDED']

export default function Auctions() {
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [page, setPage] = useState(0)

  const params = {
    page,
    size: 12,
    ...(statusFilter !== 'ALL' && { status: statusFilter }),
  }

  const { data, isLoading, isError } = useQuery({
    queryKey: ['auctions', params],
    queryFn: () => getAuctions(params),
  })

  const pageData   = data?.data ?? {}
  const auctions   = pageData.content ?? []
  const totalPages = pageData.totalPages ?? 1

  return (
    <div>
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8"
      >
        <div>
          <h1 className="font-display font-bold text-3xl text-slate-100">Auctions</h1>
          <p className="text-slate-500 text-sm mt-1">
            {pageData.totalElements ?? 0} total listings
          </p>
        </div>
        <Link to="/create" className="btn-primary text-sm py-2.5 px-5 self-start sm:self-auto">
          <Plus size={16} /> New Auction
        </Link>
      </motion.div>

      {/* Status filter pills */}
      <motion.div
        initial={{ opacity: 0, y: 12 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.1 }}
        className="flex flex-wrap gap-2 mb-8"
      >
        {STATUSES.map((s) => (
          <button
            key={s}
            onClick={() => { setStatusFilter(s); setPage(0) }}
            className={`text-sm font-medium px-4 py-2 rounded-xl border transition-all duration-200 ${
              statusFilter === s
                ? 'bg-violet-600/20 border-violet-500/50 text-violet-300'
                : 'glass border-white/[0.06] text-slate-400 hover:text-slate-200 hover:border-white/20'
            }`}
          >
            {s === 'ALL' ? 'All' : s === 'LIVE' ? '🟢 Live' :
             s === 'SCHEDULED' ? '🔵 Upcoming' : '⚫ Ended'}
          </button>
        ))}
      </motion.div>

      {/* Grid */}
      {isLoading ? (
        <div className="flex items-center justify-center py-32 text-slate-500">
          <Loader2 size={32} className="animate-spin mr-3" />
          Loading auctions…
        </div>
      ) : isError ? (
        <div className="text-center py-32 text-red-400">
          Failed to load auctions. Is the backend running?
        </div>
      ) : auctions.length === 0 ? (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-center py-32"
        >
          <Gavel size={48} className="mx-auto mb-4 text-slate-700" />
          <p className="text-slate-500 text-lg">No auctions found</p>
          <p className="text-slate-600 text-sm mt-1">Be the first to{' '}
            <Link to="/create" className="text-violet-400 hover:underline">create one</Link>
          </p>
        </motion.div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {auctions.map((auction, i) => (
            <AuctionCard key={auction.id} auction={auction} index={i} />
          ))}
        </div>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-center gap-3 mt-10">
          <button
            onClick={() => setPage((p) => Math.max(0, p - 1))}
            disabled={page === 0}
            className="btn-ghost text-sm py-2 px-5 disabled:opacity-30"
          >
            Previous
          </button>
          <span className="text-slate-500 text-sm">
            Page {page + 1} of {totalPages}
          </span>
          <button
            onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
            disabled={page >= totalPages - 1}
            className="btn-ghost text-sm py-2 px-5 disabled:opacity-30"
          >
            Next
          </button>
        </div>
      )}
    </div>
  )
}
