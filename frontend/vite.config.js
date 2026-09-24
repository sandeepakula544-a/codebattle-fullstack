import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  // sockjs-client (used for the WebSocket connection) expects Node's
  // "global" to exist. Vite doesn't polyfill Node globals by default,
  // so we point it at the browser's globalThis instead.
  define: {
    global: 'globalThis',
  },
  server: {
    port: 5173
  }
})
