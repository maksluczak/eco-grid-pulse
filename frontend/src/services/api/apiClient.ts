import { clearToken, getToken, setToken } from "./tokenStorage";

const BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api/v1";
const REFRESH_ENDPOINT = "/auth/refresh";

interface RefreshResponse {
    token: string;
}

class ApiClient {
    private send(method: string, endpoint: string, data?: unknown): Promise<Response> {
        const headers: Record<string, string> = {
            "Content-Type": "application/json",
        };

        const token = getToken();
        if (token) {
            headers["Authorization"] = `Bearer ${token}`;
        }

        return fetch(`${BASE_URL}${endpoint}`, {
            method,
            headers,
            body: data ? JSON.stringify(data) : undefined,
        });
    }

    private async refreshToken(): Promise<boolean> {
        if (!getToken()) return false;

        const response = await this.send("POST", REFRESH_ENDPOINT);
        if (!response.ok) {
            clearToken();
            window.location.assign("/login");
            return false;
        }

        const body: RefreshResponse = await response.json();
        setToken(body.token);
        return true;
    }

    private async request<T>(method: string, endpoint: string, data?: unknown): Promise<T> {
        let response = await this.send(method, endpoint, data);

        if (response.status === 401 && await this.refreshToken()) {
            response = await this.send(method, endpoint, data);
        }

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message ?? `HTTP Error: ${response.status}`);
        }

        if (response.status === 204) return undefined as T;
        return response.json();
    }

    get<T>(endpoint: string) {
        return this.request<T>("GET", endpoint);
    }

    post<T>(endpoint: string, data?: unknown) {
        return this.request<T>("POST", endpoint, data);
    }

    patch<T>(endpoint: string, data?: unknown) {
        return this.request<T>("PATCH", endpoint, data);
    }
}

export const apiClient = new ApiClient();
