/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        display: ['Space Grotesk', 'sans-serif'],
      },
      colors: {
        dark: {
          950: '#020817',
          900: '#0b1120',
          800: '#111827',
          700: '#1e293b',
        },
      },
      animation: {
        'float':       'float 8s ease-in-out infinite',
        'float-slow':  'float 12s ease-in-out infinite',
        'pulse-glow':  'pulseGlow 2.5s ease-in-out infinite',
        'slide-up':    'slideUp 0.4s ease-out',
        'fade-in':     'fadeIn 0.5s ease-out',
        'spin-slow':   'spin 8s linear infinite',
      },
      keyframes: {
        float: {
          '0%,100%': { transform: 'translateY(0px) translateX(0px)' },
          '33%':     { transform: 'translateY(-30px) translateX(10px)' },
          '66%':     { transform: 'translateY(-15px) translateX(-10px)' },
        },
        pulseGlow: {
          '0%,100%': { opacity: '0.6', transform: 'scale(1)' },
          '50%':     { opacity: '1',   transform: 'scale(1.05)' },
        },
        slideUp: {
          from: { opacity: '0', transform: 'translateY(16px)' },
          to:   { opacity: '1', transform: 'translateY(0)' },
        },
        fadeIn: {
          from: { opacity: '0' },
          to:   { opacity: '1' },
        },
      },
      boxShadow: {
        'glow-sm':   '0 0 12px rgba(139,92,246,0.3)',
        'glow':      '0 0 24px rgba(139,92,246,0.4)',
        'glow-lg':   '0 0 48px rgba(139,92,246,0.5)',
        'glow-cyan': '0 0 24px rgba(34,211,238,0.4)',
      },
    },
  },
  plugins: [],
}
