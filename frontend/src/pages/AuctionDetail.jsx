import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import {
  Gavel, Clock, TrendingUp, User, Calendar, ChevronLeft,
  Loader2, AlertCircle, DollarSign,
} from 'lucide-react'
import { getAuction } from '../api/auctions'
import { placeBid } from '../api/bids'
import useSSE from '../hooks/useSSE'
import useCountdown from '../hooks/useCountdown'
import BidFeed from '../components/BidFeed'
import useAuthStore from '../store/authStore'
import { formatCurrency, formatDate } from '../utils/format'

const STATUS_CLASS = {
  LIVE:      'status-live',
  SCHEDULED: 'status-scheduled',
  ENDED:     'status-ended',
  CANCELLED: 'status-cancelled',
}
const STATUS_LABEL = { LIVE: 'Live', SCHEDULED: 'Upcoming', ENDED: 'Ended', CANCELLED: 'Cancelled' }

function CountdownDisplay({ endTime, startTime, status }) {
  const target   = status === 'SCHEDULED' ? startTime : endTime
  const remaining = useCountdown(target)
  const label     = status === 'SCHEDULED' ? 'Starts in' : 'Ends in'

  if (!remaining) return (
    <div className="text-center">
      <p className="text-slate-600 text-sm">
        {status === 'SCHEDULED' ? 'Starting soon' : 'Auction ended'}
      </p>
    </div>
  )

  const units = [
    { label: 'Days',    val: remaining.days },
    { label: 'Hours',   val: remaining.hours },
    { label: 'Minutes', val: remaining.minutes },
    { label: 'Seconds', val: remaining.seconds },
  ]

  return (
    <div>
      <p className="text-xs text-slate-500 uppercase tracking-widest mb-3 text-center">{label}</p>
      <div className="grid grid-cols-4 gap-2">
        {units.map(({ label, val }) => (
          <div key={label} className="text-center glass rounded-xl py-3">
            <AnimatePresence mode="wait">
              <motion.p
                key={val}
                initial={{ y: -10, opacity: 0 }}
                animate={{ y: 0,   opacity: 1 }}
                exit={{   y: 10,   opacity: 0 }}
                transition={{ duration: 0.15 }}
                className="font-display font-bold text-2xl gradient-text leading-none mb-1"
              >
                {String(val).padStart(2, '0')}
              </motion.p>
            </AnimatePresence>
            <p className="text-[10px] text-slate-600 uppercase tracking-wide">{label}</p>
          </div>
        ))}
      </div>
    </div>
  )
}

export default function AuctionDetail() {
  const { id }       = useParams()
  const navigate     = useNavigate()
  const qc           = useQueryClient()
  const { user }     = useAuthStore()
  const [bidAmt, setBidAmt] = useState('')

  const { data, isLoading, isError } = useQuery({
    queryKey: ['auction', id],
    queryFn: () => getAuction(id),
    refetchInterval: 10_000,
  })

  const { bids, connected } = useSSE(id)

  const { mutate: submitBid, isPending: bidding } = useMutation({
    mutationFn: () => placeBid({ auctionId: Number(id), amount: parseFloat(bidAmt) }),
    onSuccess: (res) => {
      toast.success(
        res.data?.isHighest
          ? '🏆 You are the highest bidder!'
          : 'Bid placed successfully'
      )
      setBidAmt('')
      qc.invalidateQueries({ queryKey: ['auction', id] })
    },
    onError: (err) => {
      toast.error(err.response?.data?.message ?? 'Bid failed')
    },
  })

  if (isLoading) return (
    <div className="flex items-center justify-center py-40 text-slate-500">
      <Loader2 size={32} className="animate-spin mr-3" />
      Loading auction…
    </div>
  )

  if (isError) return (
    <div className="text-center py-40 text-red-400">
      <AlertCircle size={48} className="mx-auto mb-4 opacity-50" />
      Failed to load auction.
    </div>
  )

  const auction = data?.data
  const { title, description, startingPrice, minIncrement,
          startTime, endTime, status, sellerId } = auction

  const isLive      = status === 'LIVE'
  const canBid      = isLive && user
  const minBidAmt   = bids[0]?.amount
    ? bids[0].amount + minIncrement
    : startingPrice

  return (
    <div>
      {/* Back */}
      <button
        onClick={() => navigate('/auctions')}
        className="flex items-center gap-1.5 text-slate-500 hover:text-slate-300
                   text-sm mb-6 transition-colors group"
      >
        <ChevronLeft size={16} className="group-hover:-translate-x-1 transition-transform" />
        Back to Auctions
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
        {/* Left: info */}
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="lg:col-span-3 flex flex-col gap-6"
        >
          {/* Hero banner */}
          <div className="glass rounded-2xl overflow-hidden">
            <div className="h-48 bg-gradient-to-br from-violet-900 via-dark-800 to-indigo-900
                            flex items-center justify-center relative">
              <div className="absolute inset-0 opacity-20"
                   style={{
                     backgroundImage: 'radial-gradient(circle at 30% 50%, rgba(139,92,246,0.8) 0%, transparent 60%), radial-gradient(circle at 70% 50%, rgba(34,211,238,0.6) 0%, transparent 60%)',
                   }} />
              <Gavel size={64} className="text-white/20 relative z-10" />
            </div>

            <div className="p-6">
              <div className="flex items-start justify-between gap-4 mb-4">
                <h1 className="font-display font-bold text-2xl text-slate-100 leading-snug">
                  {title}
                </h1>
                <span className={`shrink-0 text-xs font-semibold uppercase tracking-wide
                                 px-3 py-1.5 rounded-full ${STATUS_CLASS[status]}`}>
                  {status === 'LIVE' && (
                    <span className="inline-block w-1.5 h-1.5 rounded-full bg-emerald-400
                                     animate-pulse mr-1.5" />
                  )}
                  {STATUS_LABEL[status]}
                </span>
              </div>
              <p className="text-slate-500 text-sm leading-relaxed">{description}</p>
            </div>
          </div>

          {/* Details grid */}
          <div className="grid grid-cols-2 gap-4">
            {[
              { label: 'Starting Price', value: formatCurrency(startingPrice), icon: DollarSign, color: 'text-violet-400' },
              { label: 'Min Increment',  value: formatCurrency(minIncrement),  icon: TrendingUp, color: 'text-cyan-400'   },
              { label: 'Start Time',     value: formatDate(startTime),          icon: Calendar,   color: 'text-emerald-400'},
              { label: 'End Time',       value: formatDate(endTime),            icon: Clock,      color: 'text-orange-400' },
            ].map(({ label, value, icon: Icon, color }) => (
              <div key={label} className="glass rounded-xl p-4">
                <div className="flex items-center gap-2 mb-1">
                  <Icon size={14} className={color} />
                  <p className="text-xs text-slate-600 uppercase tracking-wide">{label}</p>
                </div>
                <p className="font-medium text-slate-200 text-sm">{value}</p>
              </div>
            ))}
          </div>

          {/* Seller */}
          <div className="glass rounded-xl p-4 flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-gradient-to-br from-violet-600 to-cyan-500
                            flex items-center justify-center">
              <User size={18} className="text-white" />
            </div>
            <div>
              <p className="text-xs text-slate-600 uppercase tracking-wide">Seller ID</p>
              <p className="text-slate-300 font-medium">#{sellerId}</p>
            </div>
          </div>
        </motion.div>

        {/* Right: bid widget */}
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.1 }}
          className="lg:col-span-2 flex flex-col gap-4"
        >
          {/* Countdown */}
          {(isLive || status === 'SCHEDULED') && (
            <div className="glass rounded-2xl p-5">
              <CountdownDisplay
                endTime={endTime}
                startTime={startTime}
                status={status}
              />
            </div>
          )}

          {/* Current price */}
          <div className="glass rounded-2xl p-5 text-center">
            <p className="text-xs text-slate-600 uppercase tracking-widest mb-2">Current Bid</p>
            <p className="font-display font-bold text-4xl gradient-text leading-none">
              {bids[0]?.amount
                ? formatCurrency(bids[0].amount)
                : formatCurrency(startingPrice)}
            </p>
            {bids[0]?.amount && (
              <p className="text-xs text-slate-600 mt-2">
                Next min: {formatCurrency(bids[0].amount + minIncrement)}
              </p>
            )}
          </div>

          {/* Place bid */}
          {canBid && (
            <div className="glass rounded-2xl p-5 flex flex-col gap-4">
              <h3 className="font-display font-semibold text-slate-200">Place Your Bid</h3>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 text-sm">$</span>
                <input
                  type="number"
                  min={minBidAmt}
                  step={minIncrement}
                  value={bidAmt}
                  onChange={(e) => setBidAmt(e.target.value)}
                  placeholder={minBidAmt.toFixed(2)}
                  className="input-field pl-8"
                />
              </div>
              <button
                onClick={() => submitBid()}
                disabled={bidding || !bidAmt || parseFloat(bidAmt) < minBidAmt}
                className="btn-primary w-full py-3.5"
              >
                {bidding ? <Loader2 size={18} className="animate-spin" /> : <Gavel size={18} />}
                {bidding ? 'Placing bid…' : `Bid ${bidAmt ? formatCurrency(bidAmt) : ''}`}
              </button>
              <p className="text-xs text-slate-600 text-center">
                Minimum bid: {formatCurrency(minBidAmt)}
              </p>
            </div>
          )}

          {!canBid && status === 'LIVE' && !user && (
            <div className="glass rounded-2xl p-5 text-center">
              <p className="text-slate-500 text-sm">
                <a href="/login" className="text-violet-400 hover:underline">Sign in</a> to place a bid
              </p>
            </div>
          )}

          {/* Live bid feed */}
          <div className="glass rounded-2xl p-5">
            <BidFeed bids={bids} connected={connected} />
          </div>
        </motion.div>
      </div>
    </div>
  )
}
