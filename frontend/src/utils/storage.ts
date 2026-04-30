const TOKEN_KEY = 'bytecoach_token'
const USER_KEY = 'bytecoach_user'

const guideKey = (userId: number | string) => `bytecoach_guide_seen_${userId}`

export const storage = {
  getToken: () => localStorage.getItem(TOKEN_KEY),
  setToken: (token: string) => localStorage.setItem(TOKEN_KEY, token),
  clearToken: () => localStorage.removeItem(TOKEN_KEY),
  getUser: () => {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? JSON.parse(raw) : null
  },
  setUser: (user: unknown) => localStorage.setItem(USER_KEY, JSON.stringify(user)),
  clearUser: () => localStorage.removeItem(USER_KEY),
  getGuideSeen: (userId: number | string) => localStorage.getItem(guideKey(userId)) === '1',
  setGuideSeen: (userId: number | string) => localStorage.setItem(guideKey(userId), '1')
}
