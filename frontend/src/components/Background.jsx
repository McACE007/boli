export default function Background() {
  return (
    <div className="fixed inset-0 -z-10 overflow-hidden pointer-events-none">
      {/* Base gradient */}
      <div className="absolute inset-0 bg-gradient-to-br from-dark-950 via-[#070d1f] to-dark-950" />

      {/* Orb 1 — top-left violet */}
      <div className="animate-float absolute -top-32 -left-32 w-[600px] h-[600px] rounded-full
                      bg-violet-600/[0.12] blur-[120px]" />

      {/* Orb 2 — bottom-right cyan */}
      <div className="animate-float-slow absolute -bottom-48 -right-32 w-[700px] h-[700px] rounded-full
                      bg-cyan-500/[0.10] blur-[140px]"
           style={{ animationDelay: '-4s' }} />

      {/* Orb 3 — center purple accent */}
      <div className="animate-float absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2
                      w-[400px] h-[400px] rounded-full
                      bg-purple-700/[0.07] blur-[100px]"
           style={{ animationDelay: '-2s' }} />

      {/* Grid overlay */}
      <div
        className="absolute inset-0 opacity-[0.025]"
        style={{
          backgroundImage: `
            linear-gradient(rgba(139,92,246,0.6) 1px, transparent 1px),
            linear-gradient(90deg, rgba(139,92,246,0.6) 1px, transparent 1px)
          `,
          backgroundSize: '60px 60px',
        }}
      />
    </div>
  )
}
