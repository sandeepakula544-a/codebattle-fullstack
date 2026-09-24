import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext.jsx'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const [usernameOrEmail, setUsernameOrEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const redirectTo = location.state?.from?.pathname || '/'

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!usernameOrEmail.trim() || !password) {
      setError('Please enter your username/email and password')
      return
    }

    setLoading(true)
    try {
      await login({ usernameOrEmail: usernameOrEmail.trim(), password })
      navigate(redirectTo, { replace: true })
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container">
      <form className="form-card" onSubmit={handleSubmit}>
        <div
          style={{
            fontSize: '0.78rem',
            fontWeight: 700,
            letterSpacing: '0.12em',
            textTransform: 'uppercase',
            color: 'var(--neon-cyan)',
            marginBottom: '0.5rem',
          }}
        >
          GLADIATOR ACCESS
        </div>
        <h2>Sign In to Arena</h2>

        {error && <div className="error-banner">⚠️ {error}</div>}

        <div className="form-group">
          <label>Username or Email Address</label>
          <input
            type="text"
            value={usernameOrEmail}
            onChange={(e) => setUsernameOrEmail(e.target.value)}
            placeholder="gladiator / you@example.com"
            autoComplete="username"
          />
        </div>

        <div className="form-group">
          <label>Arena Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            autoComplete="current-password"
          />
        </div>

        <button
          className="btn btn-primary"
          type="submit"
          disabled={loading}
          style={{ width: '100%', marginTop: '0.5rem', padding: '0.9rem' }}
        >
          {loading ? '🔐 Authenticating...' : '🚀 Enter CodeBattle'}
        </button>

        <p
          style={{
            color: 'var(--text-muted)',
            fontSize: '0.9rem',
            marginTop: '1.25rem',
            textAlign: 'center',
          }}
        >
          New challenger?{' '}
          <Link
            to="/signup"
            style={{
              color: 'var(--neon-cyan)',
              fontWeight: 600,
              textDecoration: 'none',
            }}
          >
            Create an Account →
          </Link>
        </p>
      </form>
    </div>
  )
}
