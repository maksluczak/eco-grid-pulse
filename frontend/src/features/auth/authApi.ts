import { apiClient } from "../../services/api/apiClient";
import type { AuthRequest, AuthResponse } from "./types";

export const login = (data: AuthRequest) => apiClient.post<AuthResponse>("/auth/authenticate", data);

export const register = (data: AuthRequest) => apiClient.post<AuthResponse>("/auth/register", data);
