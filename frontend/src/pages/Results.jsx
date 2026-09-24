import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getResults } from '../api/stats'
import { useAuth } from '../auth/AuthContext.jsx'

export default function Results() {
  const { roomCode } = useParams()
  const navigate = useNavigate()
  const { username } = useAuth()

  const [results, setResults] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    let cancelled = false
    getResults(roomCode)
      .then((data) => {
        if (!cancelled) setResults(data)
      })
      .catch((err) => {
        if (!cancelled) setError(err.message)
      })
    return () => {
      cancelled = true
    }
  }, [roomCode])

  if (error) {
    return (
      <div className="container">
        <div className="error-banner">{error}</div>
      </div>
    )
  }

  if (!results) {
    return (
      <div className="container">
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem' }}>
          <div className="status-dot" style={{ width: 14, height: 14 }} />
          <p style={{ color: 'var(--text-muted)' }}>Calculating battle results...</p>
        </div>
      </div>
    )
  }

  const isMeWinner = results.winnerUsername === username
  const isDraw = results.draw

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
          {isDraw ? 'Battle Draw!' : isMeWinner ? 'Victory is Yours!' : `${results.winnerUsername} Wins!`}
        </h2>

        <p style={{ color: 'var(--text-muted)', marginBottom: '1.75rem', fontSize: '1rem' }}>
          {isDraw
            ? 'Both coders matched scores in this battle arena.'
            : isMeWinner
            ? 'Outstanding performance! You conquered the arena.'
            : 'Well fought battle! Review your solution and play another round.'}
        </p>

        <div className="player-list" style={{ textAlign: 'left' }}>
          {results.players.map((p) => {
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
