import api from './axios'

export const register = async ({ username, email, password }) => {
  const { data } = await api.post('/auth/register', { username, email, password })
  return data
}

export const login = async ({ usernameOrEmail, password }) => {
  const { data } = await api.post('/auth/login', { usernameOrEmail, password })
  return data
}
