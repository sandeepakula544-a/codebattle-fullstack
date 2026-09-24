import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

/**
 * Subscribes to /topic/room/{roomCode} and calls onEvent(event) for every
 * RoomEvent broadcast by the backend (see RoomEvent.java) — e.g.
 * { type: "PLAYER_JOINED", room: {...} } or { type: "BATTLE_STARTED", room: {...} }.
 *
 * Returns a cleanup function that disconnects the client.
 */
const getWsUrl = () => {
  if (import.meta.env.VITE_WS_URL) return import.meta.env.VITE_WS_URL
  if (import.meta.env.VITE_API_URL) {
    return import.meta.env.VITE_API_URL.replace(/\/api\/?$/, '/ws')
  }
  return 'http://localhost:8080/ws'
}

export function subscribeToRoom(roomCode, onEvent, onConnectError) {
  const wsUrl = getWsUrl()
  const client = new Client({
    webSocketFactory: () => new SockJS(wsUrl),
    reconnectDelay: 3000,
    onConnect: () => {
      client.subscribe(`/topic/room/${roomCode}`, (message) => {
        try {
          const event = JSON.parse(message.body)
          onEvent(event)
        } catch (e) {
          console.error('Failed to parse room event', e)
        }
      })
    },
    onStompError: (frame) => {
      console.error('STOMP error', frame)
      if (onConnectError) onConnectError(frame)
    },
  })

  client.activate()

  return () => {
    client.deactivate()
  }
}
