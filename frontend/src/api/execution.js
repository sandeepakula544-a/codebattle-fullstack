import api from './axios'

export const runCode = async ({ questionId, sourceCode }) => {
  const { data } = await api.post('/execute', { questionId, sourceCode })
  return data
}

export const submitCode = async ({ roomCode, questionId, sourceCode }) => {
  const { data } = await api.post('/submissions', { roomCode, questionId, sourceCode })
  return data
}
