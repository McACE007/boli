import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import { Gavel, Loader2, DollarSign, Clock, AlignLeft, Type } from 'lucide-react'
import { createAuction } from '../api/auctions'
import { toISOStringLocal } from '../utils/format'

const now = () => {
  const d = new Date(Date.now() + 5 * 60_000)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

const Field = ({ label, hint, children }) => (
  <div>
    <label className="flex items-center justify-between mb-1.5">
      <span className="text-sm font-medium text-slate-400">{label}</span>
      {hint && <span className="text-xs text-slate-600">{hint}</span>}
    </label>
    {children}
  </div>
)

export default function CreateAuction() {
  const navigate = useNavigate()
  const qc       = useQueryClient()
  const [form, setForm] = useState({
    title:         '',
    description:   '',
    startingPrice: '',
    minIncrement:  '',
    startTime:     now(),
    endTime:       '',
  })

  const handleChange = (e) =>
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }))

  const { mutate, isPending } = useMutation({
    mutationFn: () => createAuction({
      ...form,
      startingPrice: parseFloat(form.startingPrice),
      minIncrement:  parseFloat(form.minIncrement),
      startTime:     toISOStringLocal(form.startTime),
      endTime:       toISOStringLocal(form.endTime),
    }),
    onSuccess: (res) => {
      toast.success('Auction created!')
      qc.invalidateQueries({ queryKey: ['auctions'] })
      navigate(`/auctions/${res.data.id}`)
    },
    onError: (err) => {
      const errors = err.response?.data?.errors
      if (errors) Object.values(errors).forEach((m) => toast.error(m))
      else toast.error(err.response?.data?.message ?? 'Failed to create auction')
    },
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    if (new Date(form.endTime) <= new Date(form.startTime)) {
      toast.error('End time must be after start time')
      return
    }
    mutate()
  }

  return (
    <div className="max-w-2xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center gap-3 mb-2">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-violet-600 to-cyan-500
                            flex items-center justify-center shadow-glow">
              <Gavel size={20} className="text-white" />
            </div>
            <h1 className="font-display font-bold text-3xl text-slate-100">Create Auction</h1>
          </div>
          <p className="text-slate-500 text-sm ml-13">
            List an item and let the market decide its value.
          </p>
        </div>

        {/* Form */}
        <div className="glass rounded-2xl p-8">
          <form onSubmit={handleSubmit} className="flex flex-col gap-6">
            <Field label="Title">
              <div className="relative">
                <Type size={15} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-600" />
                <input
                  name="title"
                  value={form.title}
                  onChange={handleChange}
                  placeholder="e.g. Vintage Rolex GMT Master"
                  required
                  className="input-field pl-10"
                />
              </div>
            </Field>

            <Field label="Description">
              <div className="relative">
                <AlignLeft size={15} className="absolute left-4 top-3.5 text-slate-600" />
                <textarea
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  placeholder="Describe your item in detail…"
                  required
                  rows={4}
                  className="input-field pl-10 resize-none"
                />
              </div>
            </Field>

            <div className="grid grid-cols-2 gap-4">
              <Field label="Starting Price" hint="USD">
                <div className="relative">
                  <DollarSign size={15} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-600" />
                  <input
                    name="startingPrice"
                    type="number"
                    min="1"
                    step="0.01"
                    value={form.startingPrice}
                    onChange={handleChange}
                    placeholder="100.00"
                    required
                    className="input-field pl-10"
                  />
                </div>
              </Field>

              <Field label="Min Increment" hint="per bid">
                <div className="relative">
                  <DollarSign size={15} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-600" />
                  <input
                    name="minIncrement"
                    type="number"
                    min="1"
                    step="0.01"
                    value={form.minIncrement}
                    onChange={handleChange}
                    placeholder="10.00"
                    required
                    className="input-field pl-10"
                  />
                </div>
              </Field>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <Field label="Start Time">
                <div className="relative">
                  <Clock size={15} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-600" />
                  <input
                    name="startTime"
                    type="datetime-local"
                    value={form.startTime}
                    onChange={handleChange}
                    required
                    className="input-field pl-10 [color-scheme:dark]"
                  />
                </div>
              </Field>

              <Field label="End Time">
                <div className="relative">
                  <Clock size={15} className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-600" />
                  <input
                    name="endTime"
                    type="datetime-local"
                    value={form.endTime}
                    onChange={handleChange}
                    required
                    className="input-field pl-10 [color-scheme:dark]"
                  />
                </div>
              </Field>
            </div>

            {/* Preview card */}
            {form.title && form.startingPrice && (
              <motion.div
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                className="rounded-xl border border-violet-500/20 bg-violet-500/5 p-4"
              >
                <p className="text-xs text-violet-400 uppercase tracking-wide mb-2 font-medium">Preview</p>
                <p className="font-display font-semibold text-slate-200">{form.title}</p>
                <p className="text-sm text-violet-400 font-bold mt-1">
                  Starting at ${parseFloat(form.startingPrice || 0).toFixed(2)}
                </p>
              </motion.div>
            )}

            <button type="submit" disabled={isPending} className="btn-primary w-full py-4 text-base mt-2">
              {isPending ? <Loader2 size={20} className="animate-spin" /> : <Gavel size={20} />}
              {isPending ? 'Creating…' : 'Create Auction'}
            </button>
          </form>
        </div>
      </motion.div>
    </div>
  )
}
