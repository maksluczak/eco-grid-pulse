class ApiClient {
    private readonly baseUrl = "http://localhost:8080/api/v1";

    private async getHeaders(customHeaders?: HeadersInit): Promise<HeadersInit> {
        const headers: Record<string, string> = {
            "Content-Type": "application/json",
        };

        if (typeof window !== "undefined") {
            const token = getCookie("access_token");
            if (token) {
                headers["Authorization"] = `Bearer ${token}`;
            }
        }

        return { ...headers, ...customHeaders };
    }

    private async handleResponse<T>(response: Response): Promise<T> {
        if (!response.ok) {
            const err = await response.json().catch(() => ({ message: "Unknown error" }));
            throw new Error(err.message || `HTTP Error: ${response.status}`);
        }

        if (response.status === 204) return undefined as T;
        return response.json();
    }

    private async request<T>(method: string, endpoint: string, options: { data?: unknown; headers?: HeadersInit } = {}): Promise<T> {
        const url = `${this.baseUrl}${endpoint}`;
        const headers = await this.getHeaders(options.headers);

        const response = await fetch(url, {
            method,
            headers,
            body: options.data ? JSON.stringify(options.data) : undefined,
            credentials: "include"
        });

        return this.handleResponse<T>(response);
    }

    get<T>(endpoint: string, headers?: HeadersInit) {
        return this.request<T>("GET", endpoint, { headers });
    }

    post<T>(endpoint: string, data?: unknown, headers?: HeadersInit) {
        return this.request<T>("POST", endpoint, { data, headers });
    }

    put<T>(endpoint: string, data?: unknown, headers?: HeadersInit) {
        return this.request<T>("PUT", endpoint, { data, headers });
    }

    patch<T>(endpoint: string, data?: unknown, headers?: HeadersInit) {
        return this.request<T>("PATCH", endpoint, { data, headers });
    }

    delete<T>(endpoint: string, headers?: HeadersInit) {
        return this.request<T>("DELETE", endpoint, { headers });
    }
}

export const apiClient = new ApiClient();