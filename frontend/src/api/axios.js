import axios from 'axios'

const apiBaseUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Attach the JWT (if we have one) to every outgoing request.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('codebattle_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Normalizes backend error responses (see ErrorResponse.java) into a
// single readable message so pages don't need to know the response shape.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token missing/expired/invalid - clear it and force back to login.
      // A hard redirect (not React Router navigate) is deliberate here:
      // this interceptor runs outside any component, so a full reload is
      // the simplest way to reset AuthContext's in-memory state too.
      const hadToken = localStorage.getItem('codebattle_token')
      localStorage.removeItem('codebattle_token')
      localStorage.removeItem('codebattle_username')
      if (hadToken && window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }

    const data = error.response?.data
    const message =
      data?.messages?.join(', ') ||
      data?.error ||
      error.message ||
      'Something went wrong'
    return Promise.reject(new Error(message))
  }
)

export default api
