import { useEffect, useState } from 'react'
import { getLeaderboard } from '../api/stats'
import { useAuth } from '../auth/AuthContext.jsx'

export default function Leaderboard() {
  const [entries, setEntries] = useState(null)
  const [error, setError] = useState(null)
  const { username } = useAuth()

  useEffect(() => {
    let cancelled = false
    getLeaderboard()
      .then((data) => {
        if (!cancelled) setEntries(data)
      })
      .catch((err) => {
        if (!cancelled) setError(err.message)
      })
    return () => {
      cancelled = true
    }
  }, [])

  const getRankBadge = (rank) => {
    if (rank === 1) return { label: '🥇 #1', color: '#fbbf24', bg: 'rgba(251, 191, 36, 0.15)' }
    if (rank === 2) return { label: '🥈 #2', color: '#cbd5e1', bg: 'rgba(203, 213, 225, 0.15)' }
    if (rank === 3) return { label: '🥉 #3', color: '#fb923c', bg: 'rgba(251, 146, 60, 0.15)' }
    return { label: `#${rank}`, color: 'var(--text-muted)', bg: 'rgba(255, 255, 255, 0.04)' }
  }

  return (
    <div className="container" style={{ alignItems: 'stretch', paddingTop: '2.5rem' }}>
      <div style={{ maxWidth: 840, width: '100%', margin: '0 auto' }}>
        <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
          <div
            style={{
              fontSize: '0.8rem',
              fontWeight: 700,
              letterSpacing: '0.12em',
              textTransform: 'uppercase',
              color: 'var(--neon-cyan)',
              marginBottom: '0.5rem',
            }}
          >
            GLOBAL RANKINGS
          </div>
          <h2 style={{ fontFamily: 'var(--font-heading)', fontSize: '2.4rem', fontWeight: 800 }}>
            Arena Leaderboard
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '1rem' }}>
            Top algorithmic gladiators ranked by victories, precision, and speed.
          </p>
        </div>

        {error && <div className="error-banner">{error}</div>}

        {!entries && !error && (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem', padding: '3rem 0' }}>
            <div className="status-dot" style={{ width: 14, height: 14 }} />
            <p style={{ color: 'var(--text-muted)' }}>Retrieving global leaderboard...</p>
          </div>
        )}

        {entries && entries.length === 0 && (
          <div
            style={{
              background: 'var(--card-gradient)',
              border: '1px dashed var(--border-medium)',
              borderRadius: '16px',
              padding: '3rem',
              textAlign: 'center',
            }}
          >
            <p style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>⚔️</p>
            <p style={{ color: 'var(--text-muted)' }}>No completed battles yet. Be the first to claim rank #1!</p>
          </div>
        )}

        {entries && entries.length > 0 && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            {entries.map((e) => {
              const badge = getRankBadge(e.rank)
              const isCurrentUser = e.username === username
              return (
                <div
                  key={e.username}
                  className="player-slot"
                  style={{
                    border: isCurrentUser
                      ? '1px solid var(--neon-cyan)'
                      : e.rank <= 3
                      ? '1px solid var(--border-strong)'
                      : '1px solid var(--border-subtle)',
                    background: isCurrentUser
                      ? 'rgba(56, 189, 248, 0.08)'
                      : 'var(--card-gradient)',
                    padding: '1.1rem 1.4rem',
                    boxShadow: isCurrentUser ? '0 0 20px rgba(56, 189, 248, 0.15)' : undefined,
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                    <span
                      style={{
                        padding: '0.3rem 0.65rem',
                        borderRadius: '8px',
                        background: badge.bg,
                        color: badge.color,
                        fontFamily: 'var(--font-mono)',
                        fontWeight: 700,
                        fontSize: '0.85rem',
                      }}
                    >
                      {badge.label}
                    </span>
                    <span style={{ fontWeight: 700, fontSize: '1.05rem', color: 'var(--text-white)' }}>
                      {e.username}
                    </span>
                    {isCurrentUser && (
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
                  </div>

                  <div
                    style={{
                      display: 'flex',
                      gap: '1.75rem',
                      fontSize: '0.9rem',
                      color: 'var(--text-muted)',
                      alignItems: 'center',
                    }}
                  >
                    <span>
                      <strong style={{ color: 'var(--text-white)' }}>{e.battlesPlayed}</strong> battles
                    </span>
                    <span>
                      <strong style={{ color: 'var(--neon-emerald)' }}>{e.battlesWon}</strong> won
                    </span>
                    <span>
                      <strong style={{ color: 'var(--neon-cyan)' }}>{e.accuracyPercent}%</strong> acc
                    </span>
                    <span style={{ fontFamily: 'var(--font-mono)' }}>
                      {Math.round(e.avgSolvingTimeSeconds / 60)}m avg
                    </span>
                  </div>
                </div>
              )
            })}
          </div>
        )}
      </div>
    </div>
  )
}
