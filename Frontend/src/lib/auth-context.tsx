import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from 'react';
import { login as apiLogin, getMe, type LoginResponse, type UserAccount } from './api';

interface AuthState {
  user: UserAccount | null;
  token: string | null;
  loading: boolean;
  error: string | null;
}

interface AuthContextType extends AuthState {
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<AuthState>({
    user: null,
    token: localStorage.getItem('token'),
    loading: !!localStorage.getItem('token'),
    error: null,
  });

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getMe(token)
        .then((user) => setState((s) => ({ ...s, user, loading: false })))
        .catch(() => {
          localStorage.removeItem('token');
          setState({ user: null, token: null, loading: false, error: null });
        });
    }
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    setState((s) => ({ ...s, loading: true, error: null }));
    try {
      const res: LoginResponse = await apiLogin({ email, password });
      localStorage.setItem('token', res.token);
      const user: UserAccount = await getMe(res.token);
      setState({ user, token: res.token, loading: false, error: null });
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Login failed';
      setState((s) => ({ ...s, loading: false, error: message }));
      throw err;
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setState({ user: null, token: null, loading: false, error: null });
  }, []);

  return (
    <AuthContext.Provider value={{ ...state, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
