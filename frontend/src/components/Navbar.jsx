import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext.jsx'

export default function Navbar() {
  const { isAuthenticated, username, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  const isActive = (path) => location.pathname === path

  return (
    <nav className="navbar">
      <div style={{ display: 'flex', alignItems: 'center', gap: '1.25rem' }}>
        <Link to="/" className="logo">
          <div className="logo-icon">⚔️</div>
          Code<span>Battle</span>
        </Link>
        <span
          style={{
            fontSize: '0.72rem',
            fontWeight: 700,
            letterSpacing: '0.08em',
            padding: '0.2rem 0.6rem',
            borderRadius: '999px',
            background: 'rgba(16, 185, 129, 0.12)',
            border: '1px solid rgba(16, 185, 129, 0.3)',
            color: '#6ee7b7',
            display: 'flex',
            alignItems: 'center',
            gap: '0.35rem',
          }}
        >
          <span className="status-dot" style={{ width: 6, height: 6, margin: 0 }} />
          ARENA LIVE
        </span>
      </div>

      <div className="nav-links">
        {isAuthenticated && (
          <>
            <Link
              to="/leaderboard"
              className="nav-link"
              style={{
                color: isActive('/leaderboard') ? 'var(--neon-cyan)' : 'var(--text-muted)',
                fontWeight: isActive('/leaderboard') ? 600 : 500,
              }}
            >
              🏆 Leaderboard
            </Link>
            <Link
              to="/profile"
              className="nav-link"
              style={{
                color: isActive('/profile') ? 'var(--neon-cyan)' : 'var(--text-muted)',
                fontWeight: isActive('/profile') ? 600 : 500,
              }}
            >
              👤 Profile
            </Link>
          </>
        )}

        {isAuthenticated ? (
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.85rem' }}>
            <div className="nav-user-pill">
              <div className="nav-user-avatar">
                {username ? username.slice(0, 2).toUpperCase() : 'U'}
              </div>
              <span>{username}</span>
            </div>
            <button
              className="btn btn-secondary"
              style={{ padding: '0.45rem 1rem', fontSize: '0.85rem' }}
              onClick={handleLogout}
            >
              Log Out
            </button>
          </div>
        ) : (
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.85rem' }}>
            <Link to="/login" className="btn btn-secondary" style={{ padding: '0.5rem 1.2rem', fontSize: '0.9rem' }}>
              Log In
            </Link>
            <button className="btn btn-primary" style={{ padding: '0.5rem 1.3rem', fontSize: '0.9rem' }} onClick={() => navigate('/signup')}>
              Sign Up
            </button>
          </div>
        )}
      </div>
    </nav>
  )
}
