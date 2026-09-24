import api from './axios'

export const createRoom = async ({ topic, numberOfQuestions, duration }) => {
  const { data } = await api.post('/rooms', { topic, numberOfQuestions, duration })
  return data
}

export const getRoom = async (roomCode) => {
  const { data } = await api.get(`/rooms/${roomCode}`)
  return data
}

export const joinRoom = async (roomCode) => {
  const { data } = await api.post(`/rooms/${roomCode}/join`)
  return data
}

export const startBattle = async (roomCode) => {
  const { data } = await api.post(`/rooms/${roomCode}/start`)
  return data
}

export const endBattle = async (roomCode) => {
  const { data } = await api.post(`/rooms/${roomCode}/end`)
  return data
}

export const getRoomQuestions = async (roomCode) => {
  const { data } = await api.get(`/rooms/${roomCode}/questions`)
  return data
}
