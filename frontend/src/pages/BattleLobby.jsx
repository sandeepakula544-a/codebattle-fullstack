import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getRoom, startBattle } from '../api/rooms'
import { useAuth } from '../auth/AuthContext.jsx'
import { subscribeToRoom } from '../ws/roomSocket'

export default function BattleLobby() {
  const { roomCode } = useParams()
  const navigate = useNavigate()
  const [room, setRoom] = useState(null)
  const [error, setError] = useState(null)
  const [copied, setCopied] = useState(false)
  const [starting, setStarting] = useState(false)

  const { username: myName } = useAuth()

  useEffect(() => {
    let cancelled = false

    getRoom(roomCode)
      .then((data) => {
        if (!cancelled) setRoom(data)
        if (!cancelled && data.status === 'IN_PROGRESS') {
          navigate(`/contest/${roomCode}`, { replace: true })
        }
      })
      .catch((err) => {
        if (!cancelled) setError(err.message)
      })

    const unsubscribe = subscribeToRoom(roomCode, (event) => {
      if (cancelled) return
      setRoom(event.room)
      if (event.type === 'BATTLE_STARTED') {
        navigate(`/contest/${roomCode}`, { replace: true })
      }
    })

    return () => {
      cancelled = true
      unsubscribe()
    }
  }, [roomCode, navigate])

  const handleStart = async () => {
    setError(null)
    setStarting(true)
    try {
      await startBattle(roomCode)
    } catch (err) {
      setError(err.message)
    } finally {
      setStarting(false)
    }
  }

  const handleCopy = () => {
    navigator.clipboard.writeText(roomCode)
    setCopied(true)
    setTimeout(() => setCopied(false), 2000)
  }

  if (error) {
    return (
      <div className="container">
        <div className="error-banner">{error}</div>
      </div>
    )
  }

  if (!room) {
    return (
      <div className="container">
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem' }}>
          <div className="status-dot" style={{ width: 14, height: 14 }} />
          <p style={{ color: 'var(--text-muted)' }}>Entering arena lobby...</p>
        </div>
      </div>
    )
  }

  const playerOne = room.players?.find((p) => p.role === 'PLAYER_ONE')
  const playerTwo = room.players?.find((p) => p.role === 'PLAYER_TWO')
  const isCreator = myName && myName === room.creatorName
  const canStart = isCreator && room.status === 'READY' && !starting

  return (
    <div className="container">
      <div className="form-card" style={{ maxWidth: 520, textAlign: 'center' }}>
        <div
          style={{
            fontSize: '0.78rem',
            fontWeight: 700,
            letterSpacing: '0.12em',
            textTransform: 'uppercase',
            color: 'var(--neon-cyan)',
            marginBottom: '0.65rem',
          }}
        >
          ARENA MATCH ROOM
        </div>
        <h2 style={{ marginBottom: '1.25rem', textAlign: 'center' }}>Battle Lobby</h2>

        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '1.25rem' }}>
          <div className="room-code-pill" style={{ margin: 0 }}>
            <span>{room.roomCode}</span>
            <button
              className="btn btn-secondary"
              style={{ padding: '0.35rem 0.75rem', fontSize: '0.78rem', borderRadius: '8px' }}
              onClick={handleCopy}
            >
              {copied ? '✓ Copied!' : 'Copy Code'}
            </button>
          </div>
        </div>

        <div
          style={{
            display: 'inline-flex',
            gap: '0.85rem',
            padding: '0.45rem 1rem',
            borderRadius: '999px',
            background: 'rgba(255, 255, 255, 0.04)',
            border: '1px solid var(--border-subtle)',
            fontSize: '0.85rem',
            color: 'var(--text-muted)',
            marginBottom: '1.75rem',
          }}
        >
          <span>🎯 {room.topic?.replace('_', ' ')}</span>
          <span>•</span>
          <span>📝 {room.numberOfQuestions} Problems</span>
          <span>•</span>
          <span>⏱️ {room.duration} Mins</span>
        </div>

        {/* Versus Players Grid */}
        <div className="player-list">
          <div className="player-slot" style={{ border: '1px solid var(--border-medium)' }}>
            <span style={{ display: 'flex', alignItems: 'center' }}>
              <span className="status-dot" />
              <strong style={{ color: 'var(--text-white)' }}>{playerOne?.playerName}</strong>
              <span
                style={{
                  fontSize: '0.72rem',
                  padding: '0.15rem 0.5rem',
                  background: 'rgba(124, 92, 255, 0.2)',
                  borderRadius: '6px',
                  marginLeft: '0.6rem',
                  color: 'var(--neon-violet)',
                  fontWeight: 600,
                }}
              >
                HOST
              </span>
            </span>
            <span style={{ fontSize: '0.82rem', color: 'var(--neon-emerald)', fontWeight: 600 }}>READY</span>
          </div>

          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '-0.35rem 0',
            }}
          >
            <span
              style={{
                fontFamily: 'var(--font-heading)',
                fontSize: '0.85rem',
                fontWeight: 800,
                padding: '0.2rem 0.7rem',
                borderRadius: '999px',
                background: 'rgba(236, 72, 153, 0.15)',
                border: '1px solid rgba(236, 72, 153, 0.4)',
                color: 'var(--neon-pink)',
                letterSpacing: '0.1em',
              }}
            >
              VS
            </span>
          </div>

          {playerTwo ? (
            <div className="player-slot" style={{ border: '1px solid var(--border-medium)' }}>
              <span style={{ display: 'flex', alignItems: 'center' }}>
                <span className="status-dot" />
                <strong style={{ color: 'var(--text-white)' }}>{playerTwo.playerName}</strong>
                <span
                  style={{
                    fontSize: '0.72rem',
                    padding: '0.15rem 0.5rem',
                    background: 'rgba(56, 189, 248, 0.2)',
                    borderRadius: '6px',
                    marginLeft: '0.6rem',
                    color: 'var(--neon-cyan)',
                    fontWeight: 600,
                  }}
                >
                  CHALLENGER
                </span>
              </span>
              <span style={{ fontSize: '0.82rem', color: 'var(--neon-emerald)', fontWeight: 600 }}>READY</span>
            </div>
          ) : (
            <div className="player-slot empty">
              <span style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
                <span
                  className="status-dot"
                  style={{ background: 'var(--warning)', boxShadow: '0 0 10px rgba(245, 158, 11, 0.6)' }}
                />
                Waiting for Player 2 to join...
              </span>
            </div>
          )}
        </div>

        <button
          className="btn btn-primary"
          style={{ width: '100%', padding: '0.9rem 1.5rem', fontSize: '1.05rem', marginTop: '0.5rem' }}
          disabled={!canStart}
          onClick={handleStart}
        >
          {starting ? '⚔️ Initializing Battle...' : '⚔️ Start Coding Battle'}
        </button>

        {!canStart && !starting && (
          <p style={{ color: 'var(--text-muted)', fontSize: '0.84rem', marginTop: '0.85rem' }}>
            {!isCreator
              ? 'Waiting for room host to initiate the battle.'
              : 'Share the room code with your opponent. Both players must be in lobby to start.'}
          </p>
        )}
      </div>
    </div>
  )
}
