import { API_BASE_URL } from '../../config/api';

type ApiResponse<T> = {
  data: T;
};

export async function apiGet<T>(
  path: string,
  accessToken?: string,
): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: accessToken
      ? { Authorization: `Bearer ${accessToken}` }
      : {},
  });

  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }

  const json = (await response.json()) as ApiResponse<T>;

  return json.data;
}

export async function apiPost<TRequest, TResponse>(
  path: string,
  body: TRequest,
): Promise<TResponse> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }

  const json = (await response.json()) as ApiResponse<TResponse>;

  return json.data;
}
