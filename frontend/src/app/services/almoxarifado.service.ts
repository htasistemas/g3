import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type MovementType = 'Entrada' | 'Saída' | 'Ajuste';
export type AdjustmentDirection = 'increase' | 'decrease';
export type StockItemStatus = 'Ativo' | 'Inativo';

export interface StockItem {
  id: string;
  code: string;
  description: string;
  category?: string;
  unit?: string;
  location?: string;
  locationDetail?: string;
  currentStock: number;
  minStock: number;
  unitValue: number;
  status: StockItemStatus;
  notes?: string;
}

export interface StockItemPayload {
  code: string;
  description: string;
  category: string;
  unit: string;
  location?: string;
  locationDetail?: string;
  currentStock?: number;
  minStock: number;
  unitValue?: number;
  status: StockItemStatus;
  notes?: string;
}

export interface StockMovement {
  id: string;
  date: string;
  type: MovementType;
  itemCode: string;
  itemDescription: string;
  quantity: number;
  balanceAfter: number;
  reference?: string;
  responsible?: string;
  notes?: string;
}

@Injectable({ providedIn: 'root' })
export class AlmoxarifadoService {
  private readonly baseUrl = `${environment.apiUrl}/api/almoxarifado`;

  constructor(private readonly http: HttpClient) {}

  listItems(): Observable<StockItem[]> {
    return this.http
      .get<{ items: StockItem[] }>(`${this.baseUrl}/items`)
      .pipe(map((response) => response.items));
  }

  listMovements(): Observable<StockMovement[]> {
    return this.http
      .get<{ movements: StockMovement[] }>(`${this.baseUrl}/movements`)
      .pipe(map((response) => response.movements));
  }

  createItem(payload: StockItemPayload): Observable<StockItem> {
    return this.http
      .post<{ item: StockItem }>(`${this.baseUrl}/items`, payload)
      .pipe(map((response) => response.item));
  }

  updateItem(id: string, payload: StockItemPayload): Observable<StockItem> {
    return this.http
      .put<{ item: StockItem }>(`${this.baseUrl}/items/${id}`, payload)
      .pipe(map((response) => response.item));
  }

  registerMovement(payload: {
    date: string;
    type: MovementType;
    itemCode: string;
    quantity: number;
    reference?: string;
    responsible?: string;
    notes?: string;
    adjustmentDirection?: AdjustmentDirection;
  }): Observable<{ movement: StockMovement; item: StockItem }> {
    return this.http
      .post<{ movement: StockMovement; item: StockItem }>(`${this.baseUrl}/movements`, payload)
      .pipe(map((response) => response));
  }
}
