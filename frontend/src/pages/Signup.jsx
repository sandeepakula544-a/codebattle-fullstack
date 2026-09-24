import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext.jsx'

export default function Signup() {
  const { register } = useAuth()
  const navigate = useNavigate()

  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!username.trim() || !email.trim() || !password) {
      setError('Please fill in all required fields')
      return
    }
    if (password.length < 8) {
      setError('Password must contain at least 8 characters')
      return
    }

    setLoading(true)
    try {
      await register({ username: username.trim(), email: email.trim(), password })
      navigate('/', { replace: true })
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
          NEW GLADIATOR
        </div>
        <h2>Create Account</h2>

        {error && <div className="error-banner">⚠️ {error}</div>}

        <div className="form-group">
          <label>Gladiator Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="letters, numbers, underscores"
            maxLength={30}
            autoComplete="username"
          />
        </div>

        <div className="form-group">
          <label>Email Address</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="gladiator@arena.dev"
            autoComplete="email"
          />
        </div>

        <div className="form-group">
          <label>Password (Min. 8 characters)</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            autoComplete="new-password"
          />
        </div>

        <button
          className="btn btn-primary"
          type="submit"
          disabled={loading}
          style={{ width: '100%', marginTop: '0.5rem', padding: '0.9rem' }}
        >
          {loading ? '⚡ Enlisting...' : '⚔️ Join the Arena'}
        </button>

        <p
          style={{
            color: 'var(--text-muted)',
            fontSize: '0.9rem',
            marginTop: '1.25rem',
            textAlign: 'center',
          }}
        >
          Already an arena contender?{' '}
          <Link
            to="/login"
            style={{
              color: 'var(--neon-cyan)',
              fontWeight: 600,
              textDecoration: 'none',
            }}
          >
            Log in →
          </Link>
        </p>
      </form>
    </div>
  )
}
