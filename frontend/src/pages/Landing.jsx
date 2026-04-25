import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Gavel, Zap, Shield, TrendingUp, ArrowRight, ChevronDown } from 'lucide-react'
import Background from '../components/Background'

const fadeUp = (delay = 0) => ({
  initial:  { opacity: 0, y: 32 },
  animate:  { opacity: 1, y: 0 },
  transition: { duration: 0.6, delay, ease: [0.22, 1, 0.36, 1] },
})

const features = [
  {
    icon: Zap,
    title: 'Real-Time Bidding',
    desc: 'Live auction updates pushed directly to your browser. Never miss a bid.',
    color: 'from-violet-500 to-purple-600',
    glow: 'shadow-glow-sm',
  },
  {
    icon: Shield,
    title: 'Secure & Trustless',
    desc: 'JWT-protected accounts. Every transaction verified and recorded.',
    color: 'from-cyan-500 to-teal-600',
    glow: 'shadow-glow-cyan',
  },
  {
    icon: TrendingUp,
    title: 'Smart Price Discovery',
    desc: 'Minimum increments ensure fair competition. Market-driven pricing.',
    color: 'from-emerald-500 to-green-600',
    glow: '',
  },
]

const stats = [
  { value: '10K+', label: 'Auctions Hosted' },
  { value: '$2.4M', label: 'Total Volume' },
  { value: '50K+', label: 'Active Bidders' },
  { value: '99.9%', label: 'Uptime' },
]

export default function Landing() {
  return (
    <div className="min-h-screen relative overflow-hidden">
      <Background />

      {/* Nav */}
      <header className="relative z-10 max-w-7xl mx-auto px-6 py-6 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-violet-600 to-cyan-500
                          flex items-center justify-center shadow-glow">
            <Gavel size={18} className="text-white" />
          </div>
          <span className="font-display font-bold text-xl tracking-tight gradient-text">BOLI</span>
        </div>
        <div className="flex items-center gap-3">
          <Link to="/login" className="btn-ghost text-sm py-2 px-5">Sign In</Link>
          <Link to="/register" className="btn-primary text-sm py-2 px-5">
            <Zap size={14} /> Get Started
          </Link>
        </div>
      </header>

      {/* Hero */}
      <section className="relative z-10 max-w-7xl mx-auto px-6 pt-20 pb-32 text-center">
        <motion.div {...fadeUp(0.1)}
          className="inline-flex items-center gap-2 glass border border-violet-500/20
                     px-4 py-2 rounded-full text-sm text-violet-300 mb-8">
          <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
          Live auctions happening now
        </motion.div>

        <motion.h1 {...fadeUp(0.2)}
          className="font-display font-bold text-6xl md:text-8xl leading-[1.05] tracking-tight mb-6">
          <span className="text-slate-100">The Future of</span>
          <br />
          <span className="gradient-text">Decentralized</span>
          <br />
          <span className="text-slate-100">Auctions.</span>
        </motion.h1>

        <motion.p {...fadeUp(0.35)}
          className="text-slate-400 text-xl max-w-2xl mx-auto mb-10 leading-relaxed">
          Bid, win, and own digital assets in real time. No intermediaries.
          No delays. Just pure market-driven price discovery.
        </motion.p>

        <motion.div {...fadeUp(0.45)} className="flex flex-col sm:flex-row gap-4 justify-center">
          <Link to="/register"
            className="btn-primary text-base py-3.5 px-8 shadow-glow">
            Start Bidding
            <ArrowRight size={18} />
          </Link>
          <Link to="/login"
            className="btn-ghost text-base py-3.5 px-8">
            Sign In
          </Link>
        </motion.div>

        {/* Floating auction preview cards */}
        <motion.div
          initial={{ opacity: 0, y: 48 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.6, duration: 0.8, ease: [0.22, 1, 0.36, 1] }}
          className="mt-20 relative max-w-4xl mx-auto"
        >
          {/* Glow behind cards */}
          <div className="absolute inset-x-0 -top-8 h-32 bg-violet-600/10 blur-3xl rounded-full" />

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 relative">
            {[
              { title: 'Vintage Rolex GMT', price: '$12,400', status: 'LIVE',      time: '2h 14m' },
              { title: 'Bored Ape #7291',   price: '$8,200',  status: 'LIVE',      time: '45m 30s' },
              { title: '1957 Les Paul',      price: '$5,800',  status: 'SCHEDULED', time: '1d 6h' },
            ].map((item, i) => (
              <motion.div
                key={i}
                whileHover={{ y: -6, transition: { duration: 0.2 } }}
                className="glass rounded-2xl p-4 text-left border border-white/[0.07]
                           hover:border-violet-500/30 transition-all duration-300
                           hover:shadow-glow-sm"
              >
                <div className="h-24 rounded-xl mb-3 overflow-hidden">
                  <div className={`w-full h-full bg-gradient-to-br ${
                    i === 0 ? 'from-violet-800 to-indigo-900' :
                    i === 1 ? 'from-cyan-900 to-teal-800' :
                             'from-purple-900 to-pink-900'
                  } flex items-center justify-center`}>
                    <Gavel size={28} className="text-white/40" />
                  </div>
                </div>
                <div className={`inline-flex items-center gap-1.5 text-[10px] font-semibold
                                uppercase tracking-wide px-2 py-0.5 rounded-full mb-2
                                ${item.status === 'LIVE' ? 'status-live' : 'status-scheduled'}`}>
                  {item.status === 'LIVE' &&
                    <span className="w-1 h-1 rounded-full bg-emerald-400 animate-pulse" />}
                  {item.status === 'LIVE' ? 'Live' : 'Upcoming'}
                </div>
                <p className="font-display font-semibold text-slate-100 text-sm mb-1 truncate">
                  {item.title}
                </p>
                <p className="font-bold gradient-text font-display">{item.price}</p>
                <p className="text-[11px] text-slate-600 mt-0.5">{item.time} remaining</p>
              </motion.div>
            ))}
          </div>
        </motion.div>
      </section>

      {/* Stats */}
      <section className="relative z-10 border-y border-white/[0.05]">
        <div className="max-w-7xl mx-auto px-6 py-14 grid grid-cols-2 md:grid-cols-4 gap-8">
          {stats.map((s, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.1 }}
              className="text-center"
            >
              <p className="font-display font-bold text-4xl gradient-text mb-1">{s.value}</p>
              <p className="text-slate-500 text-sm">{s.label}</p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Features */}
      <section className="relative z-10 max-w-7xl mx-auto px-6 py-24">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="font-display font-bold text-4xl md:text-5xl mb-4">
            <span className="gradient-text">Why Boli?</span>
          </h2>
          <p className="text-slate-500 text-lg max-w-xl mx-auto">
            Built for speed, transparency, and trust.
          </p>
        </motion.div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {features.map((f, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, y: 32 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.1, duration: 0.5 }}
              whileHover={{ y: -6 }}
              className="glass glass-hover rounded-2xl p-6 group"
            >
              <div className={`w-12 h-12 rounded-2xl bg-gradient-to-br ${f.color}
                              flex items-center justify-center mb-4 ${f.glow}
                              group-hover:scale-110 transition-transform duration-300`}>
                <f.icon size={22} className="text-white" />
              </div>
              <h3 className="font-display font-semibold text-slate-100 text-lg mb-2">{f.title}</h3>
              <p className="text-slate-500 text-sm leading-relaxed">{f.desc}</p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* CTA */}
      <section className="relative z-10 max-w-4xl mx-auto px-6 py-20 text-center">
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          whileInView={{ opacity: 1, scale: 1 }}
          viewport={{ once: true }}
          className="glass rounded-3xl p-12 border border-violet-500/20 relative overflow-hidden"
        >
          <div className="absolute inset-0 bg-gradient-to-br from-violet-600/5 to-cyan-600/5" />
          <div className="relative">
            <h2 className="font-display font-bold text-4xl text-slate-100 mb-4">
              Ready to start bidding?
            </h2>
            <p className="text-slate-500 mb-8 text-lg">
              Join thousands of bidders competing for rare items every day.
            </p>
            <Link to="/register" className="btn-primary text-base py-3.5 px-10 shadow-glow-lg">
              Create Free Account
              <ArrowRight size={18} />
            </Link>
          </div>
        </motion.div>
      </section>

      {/* Footer */}
      <footer className="relative z-10 border-t border-white/[0.05] py-8 text-center text-slate-600 text-sm">
        © {new Date().getFullYear()} Boli. All rights reserved.
      </footer>
    </div>
  )
}
