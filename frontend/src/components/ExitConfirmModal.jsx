export default function ExitConfirmModal({ isOpen, onConfirm, onCancel }) {
  if (!isOpen) return null

  return (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        backgroundColor: 'rgba(7, 9, 19, 0.88)',
        backdropFilter: 'blur(10px)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 99999,
        padding: '1.25rem',
      }}
      onClick={onCancel}
    >
      <div
        className="form-card"
        style={{
          maxWidth: 480,
          textAlign: 'center',
          border: '1px solid rgba(244, 63, 94, 0.4)',
          boxShadow: '0 0 60px rgba(244, 63, 94, 0.25), 0 25px 50px rgba(0, 0, 0, 0.6)',
          animation: 'pulseGlow 2.5s infinite alternate',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <div style={{ fontSize: '3.2rem', marginBottom: '0.6rem' }}>⚠️</div>

        <div
          style={{
            fontSize: '0.8rem',
            fontWeight: 800,
            letterSpacing: '0.12em',
            textTransform: 'uppercase',
            color: 'var(--danger)',
            marginBottom: '0.4rem',
          }}
        >
          CONFIRM EXIT
        </div>

        <h2 style={{ marginBottom: '0.85rem', fontSize: '1.65rem', textAlign: 'center' }}>
          Exit Battle Arena?
        </h2>

        <p
          style={{
            color: 'var(--text-muted)',
            lineHeight: 1.6,
            marginBottom: '1.85rem',
            fontSize: '0.95rem',
          }}
        >
          Are you sure you want to exit from the test? Your current score and progress will be submitted.
          You will wait on the results screen for your opponent to complete their battle.
        </p>

        <div style={{ display: 'flex', gap: '0.85rem' }}>
          <button
            className="btn btn-secondary"
            style={{ flex: 1, padding: '0.85rem' }}
            onClick={onCancel}
          >
            Keep Coding ⚔️
          </button>
          <button
            className="btn btn-danger"
            style={{
              flex: 1,
              padding: '0.85rem',
              background: 'var(--danger)',
              color: 'white',
              boxShadow: '0 4px 20px rgba(244, 63, 94, 0.4)',
            }}
            onClick={onConfirm}
          >
            Yes, Exit & Submit
          </button>
        </div>
      </div>
    </div>
  )
}
