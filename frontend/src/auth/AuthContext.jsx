import { createContext, useContext, useState, useCallback } from 'react'
import { login as loginApi, register as registerApi } from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [username, setUsername] = useState(() => localStorage.getItem('codebattle_username'))
  const [token, setToken] = useState(() => localStorage.getItem('codebattle_token'))

  const persist = (data) => {
    localStorage.setItem('codebattle_token', data.token)
    localStorage.setItem('codebattle_username', data.username)
    setToken(data.token)
    setUsername(data.username)
  }

  const login = useCallback(async (credentials) => {
    const data = await loginApi(credentials)
    persist(data)
    return data
  }, [])

  const register = useCallback(async (details) => {
    const data = await registerApi(details)
    persist(data)
    return data
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('codebattle_token')
    localStorage.removeItem('codebattle_username')
    setToken(null)
    setUsername(null)
  }, [])

  const value = {
    username,
    token,
    isAuthenticated: Boolean(token),
    login,
    register,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider')
  return ctx
}
