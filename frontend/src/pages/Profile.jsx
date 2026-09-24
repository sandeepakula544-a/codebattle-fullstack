import { useEffect, useState } from 'react'
import { getProfile } from '../api/stats'

export default function Profile() {
  const [profile, setProfile] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    let cancelled = false
    getProfile()
      .then((data) => {
        if (!cancelled) setProfile(data)
      })
      .catch((err) => {
        if (!cancelled) setError(err.message)
      })
    return () => {
      cancelled = true
    }
  }, [])

  if (error) {
    return (
      <div className="container">
        <div className="error-banner">{error}</div>
      </div>
    )
  }

  if (!profile) {
    return (
      <div className="container">
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem' }}>
          <div className="status-dot" style={{ width: 14, height: 14 }} />
          <p style={{ color: 'var(--text-muted)' }}>Loading gladiator profile...</p>
        </div>
      </div>
    )
  }

  const initials = profile.username ? profile.username.slice(0, 2).toUpperCase() : 'U'
  const winRate =
    profile.totalBattles > 0 ? Math.round((100 * profile.wins) / profile.totalBattles) : 0

  return (
    <div className="container" style={{ alignItems: 'stretch', paddingTop: '2.5rem' }}>
      <div style={{ maxWidth: 840, width: '100%', margin: '0 auto' }}>
        {/* User Card */}
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '1.5rem',
            marginBottom: '2.5rem',
            background: 'var(--card-gradient)',
            border: '1px solid var(--border-medium)',
            padding: '1.75rem 2rem',
            borderRadius: '20px',
            backdropFilter: 'blur(16px)',
          }}
        >
          <div
            style={{
              width: 72,
              height: 72,
              borderRadius: '20px',
              background: 'var(--accent-gradient)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontWeight: 800,
              fontSize: '1.65rem',
              color: 'white',
              boxShadow: '0 0 25px rgba(124, 92, 255, 0.45)',
            }}
          >
            {initials}
          </div>
          <div style={{ textAlign: 'left' }}>
            <h2 style={{ margin: 0, fontFamily: 'var(--font-heading)', fontSize: '2rem' }}>
              {profile.username}
            </h2>
            <p style={{ margin: '0.2rem 0 0', color: 'var(--neon-cyan)', fontSize: '0.95rem' }}>
              {profile.email}
            </p>
          </div>
        </div>

        {/* 4 Stat Cards */}
        <div className="feature-grid" style={{ marginBottom: '2.75rem' }}>
          <div className="feature-card">
            <div className="feature-icon">⚔️</div>
            <h3 style={{ fontSize: '1.8rem', color: 'var(--text-white)' }}>{profile.totalBattles}</h3>
            <p>Total Battles</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🏆</div>
            <h3 style={{ fontSize: '1.8rem', color: 'var(--neon-emerald)' }}>{profile.wins}</h3>
            <p>Arena Victories</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🎯</div>
            <h3 style={{ fontSize: '1.8rem', color: 'var(--neon-cyan)' }}>{profile.accuracyPercent}%</h3>
            <p>Code Accuracy</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🔥</div>
            <h3 style={{ fontSize: '1.8rem', color: 'var(--neon-violet)' }}>{winRate}%</h3>
            <p>Win Rate</p>
          </div>
        </div>

        {/* Battle History */}
        <h3
          style={{
            textAlign: 'left',
            marginBottom: '1rem',
            fontFamily: 'var(--font-heading)',
            fontSize: '1.4rem',
          }}
        >
          Battle History
        </h3>

        {profile.history.length === 0 ? (
          <div
            style={{
              background: 'var(--card-gradient)',
              border: '1px dashed var(--border-medium)',
              borderRadius: '16px',
              padding: '2.5rem',
              textAlign: 'center',
            }}
          >
            <p style={{ color: 'var(--text-muted)' }}>No battles recorded yet. Jump into an arena match!</p>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.65rem' }}>
            {profile.history.map((h, i) => (
              <div
                key={i}
                className="player-slot"
                style={{
                  border: h.won
                    ? '1px solid rgba(16, 185, 129, 0.4)'
                    : h.draw
                    ? '1px solid var(--border-subtle)'
                    : '1px solid rgba(244, 63, 94, 0.3)',
                  background: h.won ? 'rgba(16, 185, 129, 0.05)' : 'var(--card-gradient)',
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.85rem' }}>
                  <code style={{ fontSize: '0.85rem', color: 'var(--neon-cyan)' }}>{h.roomCode}</code>
                  <span style={{ fontWeight: 600 }}>{h.topic?.replace('_', ' ')}</span>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <span
                    style={{
                      fontSize: '0.78rem',
                      fontWeight: 700,
                      padding: '0.2rem 0.65rem',
                      borderRadius: '999px',
                      background: h.won
                        ? 'rgba(16, 185, 129, 0.15)'
                        : h.draw
                        ? 'rgba(255, 255, 255, 0.08)'
                        : 'rgba(244, 63, 94, 0.15)',
                      color: h.won ? '#6ee7b7' : h.draw ? 'var(--text-muted)' : '#fda4af',
                      border: `1px solid ${
                        h.won
                          ? 'rgba(16, 185, 129, 0.3)'
                          : h.draw
                          ? 'var(--border-subtle)'
                          : 'rgba(244, 63, 94, 0.3)'
                      }`,
                    }}
                  >
                    {h.won ? 'VICTORY' : h.draw ? 'DRAW' : 'DEFEAT'}
                  </span>
                  <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                    {h.questionsSolved}/{h.numberOfQuestions} solved
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
