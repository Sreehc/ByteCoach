import type { Config } from 'tailwindcss'

export default {
  content: ['./index.html', './src/**/*.{vue,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        ink: '#10182b',
        ember: '#b44933',
        sand: '#f4ecdf',
        fog: '#d5d8de',
        pine: '#2e5b4e'
      },
      fontFamily: {
        display: ['Fraunces', 'Georgia', 'serif'],
        body: ['Manrope', 'Avenir Next', 'sans-serif']
      },
      boxShadow: {
        frame: '0 24px 80px rgba(16, 24, 43, 0.14)'
      }
    }
  },
  plugins: []
} satisfies Config

