import api from './axios'

export const getResults = async (roomCode) => {
  const { data } = await api.get(`/results/${roomCode}`)
  return data
}

export const getLeaderboard = async () => {
  const { data } = await api.get('/leaderboard')
  return data
}

export const getProfile = async () => {
  const { data } = await api.get('/profile')
  return data
}
