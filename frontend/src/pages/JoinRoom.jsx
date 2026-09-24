import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { joinRoom } from '../api/rooms'

export default function JoinRoom() {
  const navigate = useNavigate()
  const [roomCode, setRoomCode] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!roomCode.trim()) {
      setError('Please enter a 6-character room code')
      return
    }

    setLoading(true)
    try {
      const code = roomCode.trim().toUpperCase()
      await joinRoom(code)
      navigate(`/lobby/${code}`)
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
          DIRECT CHALLENGE
        </div>
        <h2>Join Battle Arena</h2>

        {error && <div className="error-banner">⚠️ {error}</div>}

        <div className="form-group">
          <label>Arena Room Code</label>
          <input
            type="text"
            value={roomCode}
            onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
            placeholder="e.g. A9B2X7"
            maxLength={10}
            style={{
              textTransform: 'uppercase',
              letterSpacing: '0.22em',
              fontFamily: 'var(--font-mono)',
              fontSize: '1.2rem',
              textAlign: 'center',
              fontWeight: 700,
            }}
          />
        </div>

        <button
          className="btn btn-primary"
          type="submit"
          disabled={loading}
          style={{ width: '100%', marginTop: '0.5rem', padding: '0.9rem' }}
        >
          {loading ? '⚡ Connecting to Arena...' : '⚔️ Enter Battle Room'}
        </button>
      </form>
    </div>
  )
}
