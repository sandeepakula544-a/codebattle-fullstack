import { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Editor from '@monaco-editor/react'
import { getRoom, getRoomQuestions, endBattle } from '../api/rooms'
import { runCode, submitCode } from '../api/execution'
import { subscribeToRoom } from '../ws/roomSocket'

function formatTime(totalSeconds) {
  const clamped = Math.max(0, totalSeconds)
  const mins = Math.floor(clamped / 60)
  const secs = clamped % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

export default function Contest() {
  const { roomCode } = useParams()
  const navigate = useNavigate()

  const [room, setRoom] = useState(null)
  const [questions, setQuestions] = useState([])
  const [activeIndex, setActiveIndex] = useState(0)
  const [codeByQuestion, setCodeByQuestion] = useState({})
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [remainingSeconds, setRemainingSeconds] = useState(null)

  const [running, setRunning] = useState(false)
  const [runResult, setRunResult] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const [submitResult, setSubmitResult] = useState(null)
  const [solvedQuestionIds, setSolvedQuestionIds] = useState(new Set())
  const [ending, setEnding] = useState(false)

  useEffect(() => {
    let cancelled = false

    async function load() {
      try {
        const roomData = await getRoom(roomCode)
        if (cancelled) return

        if (roomData.status === 'WAITING' || roomData.status === 'READY') {
          navigate(`/lobby/${roomCode}`, { replace: true })
          return
        }
        if (roomData.status === 'COMPLETED') {
          navigate(`/results/${roomCode}`, { replace: true })
          return
        }

        setRoom(roomData)



        const qs = await getRoomQuestions(roomCode)
        if (cancelled) return
        setQuestions(qs)

        const initialCode = {}
        qs.forEach((q) => {
          initialCode[q.id] = q.starterCode
        })
        setCodeByQuestion(initialCode)
      } catch (err) {
        if (!cancelled) setError(err.message)
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    load()

    const unsubscribe = subscribeToRoom(roomCode, (event) => {
      if (cancelled) return
      if (event.type === 'BATTLE_ENDED') {
        navigate(`/results/${roomCode}`, { replace: true })
      }
    })

    return () => {
      cancelled = true
      unsubscribe()
    }
  }, [roomCode, navigate])

  useEffect(() => {
    if (!room?.startedAt) return

    const endTime = new Date(room.startedAt).getTime() + room.duration * 60 * 1000

    const tick = () => {
      const secondsLeft = Math.round((endTime - Date.now()) / 1000)
      setRemainingSeconds(secondsLeft)
    }

    tick()
    const interval = setInterval(tick, 1000)
    return () => clearInterval(interval)
  }, [room])

  const activeQuestion = questions[activeIndex]
  const timeUp = remainingSeconds !== null && remainingSeconds <= 0
  const isUrgent = remainingSeconds !== null && remainingSeconds <= 60

  const handleCodeChange = useCallback(
    (value) => {
      if (!activeQuestion) return
      setCodeByQuestion((prev) => ({ ...prev, [activeQuestion.id]: value ?? '' }))
      setRunResult(null)
      setSubmitResult(null)
    },
    [activeQuestion]
  )

  const handleRun = async () => {
    if (!activeQuestion) return
    setRunning(true)
    setRunResult(null)
    setSubmitResult(null)
    try {
      const result = await runCode({
        questionId: activeQuestion.id,
        sourceCode: codeByQuestion[activeQuestion.id] ?? '',
      })
      setRunResult(result)
    } catch (err) {
      setRunResult({ compiled: false, compileError: err.message, results: [] })
    } finally {
      setRunning(false)
    }
  }

  const handleSubmit = async () => {
    if (!activeQuestion) return
    setSubmitting(true)
    setSubmitResult(null)
    try {
      const result = await submitCode({
        roomCode,
        questionId: activeQuestion.id,
        sourceCode: codeByQuestion[activeQuestion.id] ?? '',
      })
      setSubmitResult(result)
      if (result.status === 'ACCEPTED') {
        setSolvedQuestionIds((prev) => new Set(prev).add(activeQuestion.id))
      }
    } catch (err) {
      setSubmitResult({ status: 'ERROR', errorMessage: err.message })
    } finally {
      setSubmitting(false)
    }
  }

  const handleEndBattle = async () => {
    if (!window.confirm('Are you sure you want to finish and end the battle for everyone?')) return
    setEnding(true)
    try {
      await endBattle(roomCode)
      navigate(`/results/${roomCode}`, { replace: true })
    } catch (err) {
      setError(err.message)
      setEnding(false)
    }
  }

  if (error) {
    return (
      <div className="container">
        <div className="error-banner">{error}</div>
      </div>
    )
  }

  if (loading || !activeQuestion) {
    return (
      <div className="container">
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1rem' }}>
          <div className="status-dot" style={{ width: 14, height: 14 }} />
          <p style={{ color: 'var(--text-muted)' }}>Loading battle arena problem set...</p>
        </div>
      </div>
    )
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: 'calc(100vh - 65px)', overflow: 'hidden' }}>
      {/* Contest Top HUD Bar */}
      <div className="contest-header">
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
          {questions.map((q, i) => {
            const isSolved = solvedQuestionIds.has(q.id)
            const isActive = i === activeIndex
            return (
              <button
                key={q.id}
                className={isActive ? 'btn btn-primary' : 'btn btn-secondary'}
                style={{
                  padding: '0.45rem 1rem',
                  fontSize: '0.88rem',
                  borderRadius: '10px',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '0.45rem',
                }}
                onClick={() => {
                  setActiveIndex(i)
                  setRunResult(null)
                  setSubmitResult(null)
                }}
              >
                <span>Problem {i + 1}</span>
                {isSolved && (
                  <span
                    style={{
                      background: 'var(--success)',
                      color: '#070913',
                      fontSize: '0.68rem',
                      fontWeight: 800,
                      borderRadius: '50%',
                      width: 16,
                      height: 16,
                      display: 'inline-flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    ✓
                  </span>
                )}
              </button>
            )
          })}
        </div>

        {/* HUD Match Timer */}
        <div
          className={`hud-timer ${isUrgent ? 'urgent' : ''}`}
          style={{
            color: isUrgent ? 'var(--danger)' : 'var(--neon-cyan)',
          }}
        >
          <span style={{ fontSize: '1.1rem' }}>⏱️</span>
          <span>{timeUp ? "TIME'S UP" : formatTime(remainingSeconds ?? room.duration * 60)}</span>
        </div>

        <button
          className="btn btn-danger"
          style={{ padding: '0.45rem 1.1rem', fontSize: '0.85rem' }}
          onClick={handleEndBattle}
          disabled={ending}
        >
          {ending ? 'Ending...' : 'End Battle'}
        </button>
      </div>

      {/* Main Split Layout */}
      <div style={{ display: 'flex', flex: 1, minHeight: 0 }}>
        {/* Left Pane: Problem Description */}
        <div
          style={{
            width: '42%',
            overflowY: 'auto',
            padding: '2rem 1.75rem',
            borderRight: '1px solid var(--border-subtle)',
            background: 'rgba(10, 14, 32, 0.5)',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.25rem' }}>
            <h2 style={{ margin: 0, fontFamily: 'var(--font-heading)', fontSize: '1.65rem' }}>
              {activeQuestion.title}
            </h2>
            <span
              className={`difficulty-badge ${
                activeQuestion.difficulty === 'EASY'
                  ? 'easy'
                  : activeQuestion.difficulty === 'HARD'
                  ? 'hard'
                  : 'medium'
              }`}
            >
              {activeQuestion.difficulty}
            </span>
          </div>

          <div
            style={{
              color: 'var(--text-primary)',
              lineHeight: 1.7,
              fontSize: '0.96rem',
              whiteSpace: 'pre-wrap',
              marginBottom: '1.75rem',
            }}
          >
            {activeQuestion.description}
          </div>

          {activeQuestion.examples && (
            <div style={{ marginBottom: '1.75rem' }}>
              <h4
                style={{
                  fontFamily: 'var(--font-heading)',
                  color: 'var(--neon-cyan)',
                  fontSize: '0.92rem',
                  letterSpacing: '0.04em',
                  textTransform: 'uppercase',
                  marginBottom: '0.65rem',
                }}
              >
                Sample Examples
              </h4>
              <pre
                style={{
                  background: 'rgba(7, 9, 19, 0.9)',
                  border: '1px solid var(--border-medium)',
                  borderRadius: '12px',
                  padding: '1rem',
                  whiteSpace: 'pre-wrap',
                  fontSize: '0.88rem',
                  lineHeight: 1.5,
                  color: '#bae6fd',
                }}
              >
                {activeQuestion.examples}
              </pre>
            </div>
          )}

          {activeQuestion.constraints && (
            <div>
              <h4
                style={{
                  fontFamily: 'var(--font-heading)',
                  color: 'var(--neon-violet)',
                  fontSize: '0.92rem',
                  letterSpacing: '0.04em',
                  textTransform: 'uppercase',
                  marginBottom: '0.65rem',
                }}
              >
                Constraints
              </h4>
              <div
                style={{
                  background: 'rgba(124, 92, 255, 0.06)',
                  border: '1px solid var(--border-medium)',
                  borderRadius: '12px',
                  padding: '0.85rem 1.1rem',
                  color: 'var(--text-muted)',
                  fontSize: '0.88rem',
                  lineHeight: 1.6,
                }}
              >
                {activeQuestion.constraints}
              </div>
            </div>
          )}
        </div>

        {/* Right Pane: Code Editor & Terminal */}
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', background: '#1e1e1e' }}>
          {/* Editor Header Bar */}
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '0.45rem 1.25rem',
              background: '#181818',
              borderBottom: '1px solid rgba(255, 255, 255, 0.08)',
              fontSize: '0.82rem',
              color: 'var(--text-muted)',
            }}
          >
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
              <span style={{ color: '#f59e0b', fontWeight: 600 }}>☕ Java 21</span>
              <span>•</span>
              <span>Solution.java</span>
            </div>
            <div style={{ fontSize: '0.78rem' }}>Monaco VS-Dark Engine</div>
          </div>

          {/* Monaco Editor */}
          <div style={{ flex: 1, minHeight: 0 }}>
            <Editor
              height="100%"
              language="java"
              theme="vs-dark"
              value={codeByQuestion[activeQuestion.id] ?? ''}
              onChange={handleCodeChange}
              options={{
                fontSize: 14,
                fontFamily: "'JetBrains Mono', 'Courier New', monospace",
                minimap: { enabled: false },
                scrollBeyondLastLine: false,
                lineNumbers: 'on',
                renderLineHighlight: 'all',
                automaticLayout: true,
                padding: { top: 12, bottom: 12 },
              }}
            />
          </div>

          {/* Console / Test Runner Output */}
          {(runResult || submitResult) && (
            <div
              style={{
                maxHeight: '230px',
                overflowY: 'auto',
                borderTop: '1px solid var(--border-medium)',
                background: 'rgba(7, 9, 19, 0.95)',
                padding: '1rem 1.5rem',
                fontSize: '0.88rem',
              }}
            >
              {submitResult && (
                <div
                  style={{
                    marginBottom: '0.85rem',
                    padding: '0.75rem 1rem',
                    borderRadius: '10px',
                    fontWeight: 700,
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    background:
                      submitResult.status === 'ACCEPTED'
                        ? 'rgba(16, 185, 129, 0.15)'
                        : 'rgba(244, 63, 94, 0.15)',
                    border: `1px solid ${
                      submitResult.status === 'ACCEPTED' ? 'var(--success)' : 'var(--danger)'
                    }`,
                    color: submitResult.status === 'ACCEPTED' ? '#6ee7b7' : '#fda4af',
                  }}
                >
                  {submitResult.status === 'ACCEPTED' ? (
                    <>
                      <span>🏆 Accepted!</span>
                      <span>
                        ({submitResult.testsPassed}/{submitResult.testsTotal} test cases passed)
                      </span>
                    </>
                  ) : submitResult.status === 'ERROR' ? (
                    <>
                      <span>❌ Error:</span>
                      <span>{submitResult.errorMessage}</span>
                    </>
                  ) : (
                    <>
                      <span>❌ {submitResult.status.replace(/_/g, ' ')}:</span>
                      <span>
                        {submitResult.testsPassed}/{submitResult.testsTotal} test cases passed
                      </span>
                    </>
                  )}
                </div>
              )}

              {runResult && !runResult.compiled && (
                <div
                  style={{
                    background: 'rgba(244, 63, 94, 0.1)',
                    border: '1px solid var(--danger)',
                    borderRadius: '10px',
                    padding: '0.85rem',
                  }}
                >
                  <strong style={{ color: 'var(--danger)' }}>Compilation Error:</strong>
                  <pre style={{ color: '#fecdd3', whiteSpace: 'pre-wrap', marginTop: '0.4rem', fontSize: '0.82rem' }}>
                    {runResult.compileError}
                  </pre>
                </div>
              )}

              {runResult && runResult.compiled && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.65rem' }}>
                  {runResult.results.map((r, i) => (
                    <div
                      key={i}
                      style={{
                        border: `1px solid ${r.passed ? 'rgba(16, 185, 129, 0.4)' : 'rgba(244, 63, 94, 0.4)'}`,
                        borderRadius: '10px',
                        padding: '0.75rem 1rem',
                        background: r.passed ? 'rgba(16, 185, 129, 0.06)' : 'rgba(244, 63, 94, 0.06)',
                      }}
                    >
                      <div
                        style={{
                          fontWeight: 700,
                          color: r.passed ? 'var(--success)' : 'var(--danger)',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '0.4rem',
                        }}
                      >
                        {r.passed ? '✓' : '✗'} Test Case {i + 1}:{' '}
                        {r.passed
                          ? 'Passed'
                          : r.timedOut
                          ? 'Time Limit Exceeded'
                          : r.runtimeError
                          ? 'Runtime Error'
                          : 'Wrong Answer'}
                      </div>
                      <div
                        style={{
                          color: 'var(--text-muted)',
                          marginTop: '0.45rem',
                          display: 'grid',
                          gridTemplateColumns: 'auto 1fr',
                          gap: '0.35rem 0.75rem',
                          fontSize: '0.84rem',
                        }}
                      >
                        <span>Input:</span>
                        <code>{r.input}</code>
                        <span>Expected:</span>
                        <code>{r.expectedOutput}</code>
                        <span>Actual:</span>
                        <code style={{ color: r.passed ? 'var(--success)' : 'var(--danger)' }}>
                          {r.actualOutput}
                        </code>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Action Buttons Bar */}
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'flex-end',
              gap: '0.85rem',
              padding: '0.85rem 1.5rem',
              background: '#141414',
              borderTop: '1px solid rgba(255, 255, 255, 0.08)',
            }}
          >
            <button
              className="btn btn-secondary"
              style={{ padding: '0.65rem 1.4rem' }}
              onClick={handleRun}
              disabled={running || submitting}
            >
              {running ? '⚡ Running...' : '▶ Run Code'}
            </button>
            <button
              className="btn btn-primary"
              style={{ padding: '0.65rem 1.6rem' }}
              onClick={handleSubmit}
              disabled={running || submitting}
            >
              {submitting ? '⏳ Submitting...' : '🚀 Submit Solution'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
