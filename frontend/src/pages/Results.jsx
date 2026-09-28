import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getResults } from '../api/stats'
import { getRoom, endBattle } from '../api/rooms'
import { useAuth } from '../auth/AuthContext.jsx'
import { subscribeToRoom } from '../ws/roomSocket'
import { formatTime, parseServerDate } from '../utils/time'

export default function Results() {
  const { roomCode } = useParams()
  const navigate = useNavigate()
  const { username } = useAuth()

  const [room, setRoom] = useState(null)
  const [results, setResults] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [remainingSeconds, setRemainingSeconds] = useState(null)

  const loadResults = async () => {
    try {
      const data = await getResults(roomCode)
      setResults(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // Initial load: check room status first
  useEffect(() => {
    let cancelled = false

    async function checkState() {
      try {
        const roomData = await getRoom(roomCode)
        if (cancelled) return
        setRoom(roomData)

        if (roomData.status === 'COMPLETED') {
          // Battle completed - load final results
          const data = await getResults(roomCode)
          if (!cancelled) {
            setResults(data)
            setLoading(false)
          }
        } else {
          // Battle still in progress (opponent is still coding)
          if (!cancelled) setLoading(false)
        }
      } catch (err) {
        if (!cancelled) {
          setError(err.message)
          setLoading(false)
        }
      }
    }

    checkState()

    // Listen to WebSocket for when the battle ends
    const unsubscribe = subscribeToRoom(roomCode, (event) => {
      if (cancelled) return
      if (event.type === 'BATTLE_ENDED') {
        loadResults()
      }
    })

    return () => {
      cancelled = true
      unsubscribe()
    }
  }, [roomCode])

  // Countdown timer for remaining match time while waiting for opponent
  useEffect(() => {
    if (!room?.startedAt || room.status === 'COMPLETED' || results) return

    const startTime = parseServerDate(room.startedAt)
    const durationMs = (room.duration || 30) * 60 * 1000
    const endTime = startTime + durationMs

    const tick = async () => {
      const now = Date.now()
      const secondsLeft = Math.max(0, Math.round((endTime - now) / 1000))
      setRemainingSeconds(secondsLeft)

      if (secondsLeft <= 0) {
        try {
          await endBattle(roomCode)
        } catch (ignored) {}
        loadResults()
      }
    }

    tick()
    const interval = setInterval(tick, 1000)
    return () => clearInterval(interval)
  }, [room, results])

  if (error) {
    return (
      <div className="container">
        <div className="error-banner">{error}</div>
        <button className="btn btn-secondary" onClick={() => navigate('/')}>
          Back to Arena Home
        </button>
      </div>
    )
  }

  if (loading) {
    return (
      <div className="container">
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem' }}>
          <div className="status-dot" style={{ width: 14, height: 14 }} />
          <p style={{ color: 'var(--text-muted)' }}>Connecting to arena results feed...</p>
        </div>
      </div>
    )
  }

  // State 1: Battle is still IN_PROGRESS (You finished early, waiting for opponent)
  if (!results && room?.status === 'IN_PROGRESS') {
    const myPlayer = room.players?.find((p) => p.playerName === username)
    const opponent = room.players?.find((p) => p.playerName !== username)

    return (
      <div className="container">
        <div className="form-card" style={{ maxWidth: 580, textAlign: 'center' }}>
          <div
            style={{
              fontSize: '3.5rem',
              marginBottom: '0.65rem',
              animation: 'pulseGlow 2.5s infinite alternate',
            }}
          >
            ⏳
          </div>

          <div
            style={{
              fontSize: '0.78rem',
              fontWeight: 800,
              letterSpacing: '0.12em',
              textTransform: 'uppercase',
              color: 'var(--neon-cyan)',
              marginBottom: '0.35rem',
            }}
          >
            SUBMISSION RECORDED
          </div>

          <h2 style={{ marginBottom: '0.5rem', textAlign: 'center', fontSize: '1.9rem' }}>
            Waiting for Opponent...
          </h2>

          <p style={{ color: 'var(--text-muted)', lineHeight: 1.6, marginBottom: '1.65rem', fontSize: '0.95rem' }}>
            You have completed and submitted your test! Please wait while your opponent finishes their battle.
            The final winner and scorecards will automatically appear as soon as they submit or time expires.
          </p>

          {/* Match Timer countdown */}
          <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '1.75rem' }}>
            <div
              className="hud-timer"
              style={{
                color: remainingSeconds !== null && remainingSeconds <= 60 ? 'var(--danger)' : 'var(--neon-cyan)',
                border: '1px solid var(--border-medium)',
                background: 'rgba(7, 9, 19, 0.8)',
                padding: '0.45rem 1.25rem',
                fontSize: '1.15rem',
              }}
            >
              <span>⏱️ Match Time Remaining:</span>
              <strong>{formatTime(remainingSeconds ?? room.duration * 60)}</strong>
            </div>
          </div>

          {/* Versus Player Status Cards */}
          <div className="player-list" style={{ textAlign: 'left', marginBottom: '1.75rem' }}>
            {/* You */}
            <div
              className="player-slot"
              style={{
                border: '1px solid var(--neon-cyan)',
                background: 'rgba(56, 189, 248, 0.08)',
                padding: '1.1rem 1.25rem',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <span
                  style={{
                    width: 10,
                    height: 10,
                    borderRadius: '50%',
                    background: 'var(--neon-cyan)',
                    boxShadow: '0 0 10px rgba(56, 189, 248, 0.8)',
                    display: 'inline-block',
                  }}
                />
                <strong style={{ fontSize: '1.05rem', color: 'var(--text-white)' }}>
                  {username} (You)
                </strong>
              </div>
              <span
                style={{
                  fontSize: '0.82rem',
                  fontWeight: 700,
                  color: 'var(--neon-emerald)',
                  background: 'rgba(16, 185, 129, 0.15)',
                  padding: '0.25rem 0.65rem',
                  borderRadius: '999px',
                }}
              >
                ✓ Finished · {myPlayer?.score ?? 0} pts
              </span>
            </div>

            {/* Opponent */}
            <div
              className="player-slot"
              style={{
                border: '1px solid var(--border-medium)',
                background: 'rgba(10, 14, 32, 0.8)',
                padding: '1.1rem 1.25rem',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <span className="status-dot" style={{ background: 'var(--warning)', boxShadow: '0 0 10px rgba(245, 158, 11, 0.8)' }} />
                <strong style={{ fontSize: '1.05rem', color: 'var(--text-white)' }}>
                  {opponent ? opponent.playerName : 'Opponent'}
                </strong>
              </div>
              <span
                style={{
                  fontSize: '0.82rem',
                  fontWeight: 700,
                  color: 'var(--warning)',
                  background: 'rgba(245, 158, 11, 0.15)',
                  padding: '0.25rem 0.65rem',
                  borderRadius: '999px',
                }}
              >
                ⚔️ Coding in Arena...
              </span>
            </div>
          </div>

          <button
            className="btn btn-secondary"
            style={{ width: '100%', padding: '0.8rem' }}
            onClick={() => navigate('/')}
          >
            Leave to Arena Home
          </button>
        </div>
      </div>
    )
  }

  // State 2: Battle is COMPLETED - Final Results
  const isMeWinner = results?.winnerUsername === username
  const isDraw = results?.draw

  return (
    <div className="container">
      <div className="form-card" style={{ maxWidth: 560, textAlign: 'center' }}>
        <div
          style={{
            fontSize: '3.5rem',
            marginBottom: '0.5rem',
            animation: 'pulseGlow 2s infinite alternate',
          }}
        >
          {isDraw ? '🤝' : isMeWinner ? '👑' : '⚔️'}
        </div>

        <h2 style={{ marginBottom: '0.4rem', textAlign: 'center' }}>
          {isDraw ? 'Battle Draw!' : isMeWinner ? 'Victory is Yours!' : `${results?.winnerUsername} Wins!`}
        </h2>

        <p style={{ color: 'var(--text-muted)', marginBottom: '1.75rem', fontSize: '1rem' }}>
          {isDraw
            ? 'Both coders matched scores in this battle arena.'
            : isMeWinner
            ? 'Outstanding performance! You conquered the arena.'
            : 'Well fought battle! Review your solution and play another round.'}
        </p>

        <div className="player-list" style={{ textAlign: 'left' }}>
          {results?.players.map((p) => {
            const isMe = p.username === username
            return (
              <div
                key={p.username}
                className="player-slot"
                style={{
                  border: p.won
                    ? '1px solid var(--success)'
                    : isMe
                    ? '1px solid var(--border-medium)'
                    : undefined,
                  flexDirection: 'column',
                  alignItems: 'stretch',
                  gap: '0.65rem',
                  padding: '1.25rem',
                  background: p.won ? 'rgba(16, 185, 129, 0.08)' : 'rgba(10, 14, 32, 0.8)',
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    <span style={{ fontSize: '1.1rem', fontWeight: 800 }}>{p.username}</span>
                    {isMe && (
                      <span
                        style={{
                          fontSize: '0.72rem',
                          padding: '0.15rem 0.5rem',
                          borderRadius: '6px',
                          background: 'rgba(56, 189, 248, 0.2)',
                          color: 'var(--neon-cyan)',
                          fontWeight: 700,
                        }}
                      >
                        YOU
                      </span>
                    )}
                    {p.won && <span style={{ fontSize: '1.1rem' }}>🏆</span>}
                  </div>
                  <span
                    style={{
                      fontFamily: 'var(--font-mono)',
                      fontSize: '1.25rem',
                      fontWeight: 800,
                      color: p.won ? 'var(--neon-emerald)' : 'var(--text-white)',
                    }}
                  >
                    {p.score} <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>PTS</span>
                  </span>
                </div>

                <div
                  style={{
                    display: 'flex',
                    gap: '1.25rem',
                    color: 'var(--text-muted)',
                    fontSize: '0.85rem',
                    borderTop: '1px solid var(--border-subtle)',
                    paddingTop: '0.6rem',
                  }}
                >
                  <span>🎯 {p.questionsSolved} questions solved</span>
                  <span>⏱️ {Math.round(p.totalTimeSeconds / 60)} min time</span>
                </div>
              </div>
            )
          })}
        </div>

        <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
          <button
            className="btn btn-secondary"
            style={{ flex: 1, padding: '0.85rem' }}
            onClick={() => navigate('/')}
          >
            Arena Home
          </button>
          <button
            className="btn btn-primary"
            style={{ flex: 1, padding: '0.85rem' }}
            onClick={() => navigate('/create')}
          >
            Play Again ⚔️
          </button>
        </div>
      </div>
    </div>
  )
}
