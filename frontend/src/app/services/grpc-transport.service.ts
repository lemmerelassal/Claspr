import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

/**
 * Low-level gRPC-Web transport — single responsibility: send RPCs.
 * No business logic, no auth state, no URL resolution.
 */
@Injectable({ providedIn: 'root' })
export class GrpcTransport {
  private grpcUrl = environment.grpcUrl;

  async call<T>(service: string, method: string, payload: any): Promise<T> {
    const url = `${this.grpcUrl}/${service}/${method}`;

    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ message: 'Request failed' }));
      throw new Error(errorData.message || `gRPC error: ${response.status}`);
    }

    return response.json();
  }

  get baseUrl(): string {
    return this.grpcUrl.replace(/\/grpc$/, '');
  }

  resolvePhotoUrl(url: string): string {
    if (!url) return 'https://via.placeholder.com/400x500?text=No+Photo';
    if (url.startsWith('http')) return url;
    return `${this.baseUrl}${url}`;
  }
}
