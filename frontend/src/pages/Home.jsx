import { useNavigate } from 'react-router-dom'

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
