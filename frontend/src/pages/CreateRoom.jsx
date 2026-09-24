import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createRoom } from '../api/rooms'

const TOPICS = [
  { value: 'ARRAYS', label: '📊 Arrays & Two Pointers' },
  { value: 'STRINGS', label: '🔤 Strings & Parsing' },
  { value: 'LINKED_LIST', label: '🔗 Linked Lists' },
  { value: 'TREES', label: '🌳 Binary Trees & BST' },
  { value: 'GRAPHS', label: '🕸️ Graphs & Traversals' },
]

export default function CreateRoom() {
  const navigate = useNavigate()
  const [topic, setTopic] = useState('ARRAYS')
  const [numberOfQuestions, setNumberOfQuestions] = useState(3)
  const [duration, setDuration] = useState(30)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const room = await createRoom({
        topic,
        numberOfQuestions: Number(numberOfQuestions),
        duration: Number(duration),
      })
      navigate(`/lobby/${room.roomCode}`)
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
          HOST MATCH
        </div>
        <h2>Create Battle Room</h2>

        {error && <div className="error-banner">⚠️ {error}</div>}

        <div className="form-group">
          <label>DSA Category Track</label>
          <select value={topic} onChange={(e) => setTopic(e.target.value)}>
            {TOPICS.map((t) => (
              <option key={t.value} value={t.value}>
                {t.label}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Problem Count (1 - 5)</label>
          <input
            type="number"
            min={1}
            max={5}
            value={numberOfQuestions}
            onChange={(e) => setNumberOfQuestions(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Battle Duration (Minutes)</label>
          <input
            type="number"
            min={5}
            max={120}
            step={5}
            value={duration}
            onChange={(e) => setDuration(e.target.value)}
          />
        </div>

        <button
          className="btn btn-primary"
          type="submit"
          disabled={loading}
          style={{ width: '100%', marginTop: '0.5rem', padding: '0.9rem' }}
        >
          {loading ? '⚙️ Spawning Arena...' : '⚔️ Initialize Battle Room'}
        </button>
      </form>
    </div>
  )
}
