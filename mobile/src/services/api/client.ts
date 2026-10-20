import { API_BASE_URL } from '../../config/api';
import { getAccessToken } from '../auth/tokenStorage';

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

export async function apiPostAuthenticated<TRequest, TResponse>(
  path: string,
  body: TRequest,
): Promise<TResponse> {
  const accessToken = await getAccessToken();

  if (!accessToken) {
    throw new Error('توکن ورود پیدا نشد؛ لطفاً دوباره وارد شو.');
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(body),
  });

 
if (!response.ok) {
  const errorBody = await response.text();

  console.error('API request failed:', {
    url: `${API_BASE_URL}${path}`,
    status: response.status,
    body: errorBody,
  });

  throw new Error(
    `API request failed: ${response.status} - ${errorBody}`,
  );
}

 const responseText = await response.text();

console.log('Calorie API status:', response.status);
console.log('Calorie API response:', responseText);

const json = JSON.parse(responseText) as ApiResponse<TResponse> & TResponse;

return json.data ?? json;
}
