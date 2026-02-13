import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

interface RuntimeConfig {
  apiUrl?: string;
}

const DEFAULT_API_URL = '/api';
const PLACEHOLDER = '$API_BASE_URL';

@Injectable({ providedIn: 'root' })
export class RuntimeConfigService {
  private config: RuntimeConfig = {};

  constructor(private readonly http: HttpClient) {}

  load(): Promise<void> {
    return firstValueFrom(this.http.get<RuntimeConfig>('assets/config.json'))
      .then((config) => {
        this.config = config ?? {};
      })
      .catch(() => {
        this.config = {};
      });
  }

  get apiUrl(): string {
    const raw = (this.config.apiUrl ?? '').trim();
    if (!raw || raw === PLACEHOLDER) return DEFAULT_API_URL;
    return raw;
  }
}
