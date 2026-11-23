const API_URL = (import.meta.env.VITE_API_URL ?? "http://localhost:8080").replace(/\/$/, "");

export class ApiError extends Error {
  status: number;
  constructor(message: string, status: number) { super(message); this.name = "ApiError"; this.status = status; }
}

export async function api<T>(path: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers);
  if (init.body && !headers.has("Content-Type") && !(init.body instanceof FormData)) headers.set("Content-Type", "application/json");
  const response = await fetch(`${API_URL}${path}`, { ...init, headers });
  const text = await response.text();
  let data: unknown = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!response.ok) {
    const message = typeof data === "object" && data && "message" in data ? String((data as {message?: unknown}).message) : `Erro ${response.status} na API`;
    throw new ApiError(message, response.status);
  }
  return data as T;
}

export const apiBaseUrl = API_URL;
