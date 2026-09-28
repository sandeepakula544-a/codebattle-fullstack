import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getRoom } from '../api/rooms'
import { subscribeToRoom } from '../ws/roomSocket'

const FEATURES = [
  {
    icon: '⚡',
    title: 'Real-Time 1v1',
    desc: 'Battle live with millisecond-synchronized timers and instantaneous room events via WebSockets.',
  },
  {
    icon: '🧩',
    title: 'Curated DSA Tracks',
    desc: 'Test your algorithm skills across Arrays, Strings, Linked Lists, Trees, and Graphs.',
  },
  {
    icon: '🖥️',
    title: 'Monaco IDE Sandbox',
    desc: 'Full VS Code editing engine in browser with Java compilation, auto-complete, and test runner.',
  },
  {
    icon: '🏆',
    title: 'Rankings & Analytics',
    desc: 'Climb the global arena leaderboard and analyze your win-rate, solving time, and accuracy.',
  },
]

export default function Home() {
  const navigate = useNavigate()
  const [lastRoom, setLastRoom] = useState(() => localStorage.getItem('codebattle_last_room'))
  const [roomInfo, setRoomInfo] = useState(null)

  useEffect(() => {
    if (!lastRoom) return

    let cancelled = false
    getRoom(lastRoom)
      .then((data) => {
        if (!cancelled) setRoomInfo(data)
      })
      .catch(() => {
        // Room not found or invalid
      })

    const unsubscribe = subscribeToRoom(lastRoom, (event) => {
      if (cancelled) return
      if (event.type === 'BATTLE_ENDED') {
        getRoom(lastRoom).then((data) => {
          if (!cancelled) setRoomInfo(data)
        }).catch(() => {})
      }
    })

    return () => {
      cancelled = true
      unsubscribe()
    }
  }, [lastRoom])

  const handleDismiss = () => {
    localStorage.removeItem('codebattle_last_room')
    setLastRoom(null)
    setRoomInfo(null)
  }

  const isCompleted = roomInfo?.status === 'COMPLETED'

  return (
    <div className="container">
      <div className="hero">
        <div className="hero-pill">
          <span>⚔️</span> Next-Gen 1v1 Competitive DSA
        </div>
        <h1>
          Code. Compete. <span className="highlight">Conquer.</span>
        </h1>
        <p>
          Step into the battle arena. Challenge opponents to live head-to-head coding battles,
          execute test cases in real time, and prove your algorithmic mastery.
        </p>
      </div>

      {lastRoom && (
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            gap: '1.25rem',
            padding: '0.85rem 1.4rem',
            borderRadius: '16px',
            background: isCompleted ? 'rgba(16, 185, 129, 0.08)' : 'rgba(56, 189, 248, 0.08)',
            border: isCompleted ? '1px solid var(--neon-emerald)' : '1px solid var(--neon-cyan)',
            marginBottom: '2rem',
            boxShadow: isCompleted
              ? '0 0 25px rgba(16, 185, 129, 0.25)'
              : '0 0 25px rgba(56, 189, 248, 0.2)',
            maxWidth: 680,
            width: '100%',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.85rem', textAlign: 'left' }}>
            <span style={{ fontSize: '1.65rem' }}>{isCompleted ? '🏆' : '⚔️'}</span>
            <div>
              <div
                style={{
                  fontWeight: 800,
                  fontSize: '0.92rem',
                  color: isCompleted ? 'var(--neon-emerald)' : 'var(--neon-cyan)',
                }}
              >
                {isCompleted ? 'MATCH COMPLETED' : 'RECENT ARENA BATTLE'} ·{' '}
                <code style={{ fontSize: '0.88rem' }}>{lastRoom}</code>
              </div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.82rem', marginTop: '0.15rem' }}>
                {isCompleted
                  ? 'All opponents have finished! Final scorecard and rankings are ready.'
                  : 'Battle in progress. Click to view the results lobby or check live status.'}
              </div>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <button
              className="btn btn-primary"
              style={{
                padding: '0.45rem 1rem',
                fontSize: '0.84rem',
                background: isCompleted ? 'var(--neon-emerald)' : undefined,
                color: isCompleted ? '#070913' : undefined,
                fontWeight: 700,
              }}
              onClick={() => navigate(`/results/${lastRoom}`)}
            >
              {isCompleted ? 'View Scorecard 📊' : 'View Results Lobby →'}
            </button>
            <button
              className="btn btn-secondary"
              style={{
                padding: '0.45rem 0.65rem',
                fontSize: '0.8rem',
                borderRadius: '8px',
                color: 'var(--text-muted)',
              }}
              title="Dismiss"
              onClick={handleDismiss}
            >
              ✕
            </button>
          </div>
        </div>
      )}

      <div className="button-row">
        <button
          className="btn btn-primary"
          style={{ padding: '0.95rem 2.2rem', fontSize: '1.05rem' }}
          onClick={() => navigate('/create')}
        >
          <span>⚔️</span> Create Battle Room
        </button>
        <button
          className="btn btn-secondary"
          style={{ padding: '0.95rem 2.2rem', fontSize: '1.05rem' }}
          onClick={() => navigate('/join')}
        >
          <span>⚡</span> Join with Room Code
        </button>
      </div>

      <div className="feature-grid">
        {FEATURES.map((f) => (
          <div className="feature-card" key={f.title}>
            <div className="feature-icon">{f.icon}</div>
            <h3>{f.title}</h3>
            <p>{f.desc}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
