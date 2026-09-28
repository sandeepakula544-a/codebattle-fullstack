/**
 * Formats seconds into MM:SS format.
 */
export function formatTime(totalSeconds) {
  const clamped = Math.max(0, totalSeconds)
  const mins = Math.floor(clamped / 60)
  const secs = clamped % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

/**
 * Parses server date strings safely into a UTC timestamp.
 * Spring Boot's LocalDateTime doesn't include a timezone offset (e.g. '2026-09-28T14:40:30.651288').
 * Without 'Z', JavaScript's new Date(...) treats it as LOCAL time (e.g. IST = UTC+5:30),
 * causing the timer to immediately expire ("TIME'S UP") in non-UTC timezones.
 * Appending 'Z' forces UTC interpretation.
 */
export function parseServerDate(dateStr) {
  if (!dateStr) return Date.now()
  if (typeof dateStr === 'number') return dateStr
  if (Array.isArray(dateStr)) {
    // [year, month, day, hour, min, sec, millis]
    return Date.UTC(dateStr[0], dateStr[1] - 1, dateStr[2], dateStr[3] || 0, dateStr[4] || 0, dateStr[5] || 0)
  }

  let str = String(dateStr).trim()
  // If no timezone offset (Z or +/-HH:mm), append 'Z' so it is parsed as UTC
  if (!str.endsWith('Z') && !/[+-]\d{2}(:\d{2})?$/.test(str)) {
    str += 'Z'
  }

  const timestamp = new Date(str).getTime()
  return isNaN(timestamp) ? Date.now() : timestamp
}
