import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type MovementType = 'Entrada' | 'Saida' | 'Ajuste';
export type AdjustmentDirection = 'increase' | 'decrease';
export type StockItemStatus = 'Ativo' | 'Inativo';

export interface StockItem {
  id: string;
  code: string;
  barcode?: string;
  description: string;
  category?: string;
  unit?: string;
  location?: string;
  locationDetail?: string;
  currentStock: number;
  minStock: number;
  unitValue: number;
  status: StockItemStatus;
  validity?: string;
  ignoreValidity?: boolean;
  notes?: string;
}

export interface StockItemPayload {
  code: string;
  barcode?: string;
  description: string;
  category: string;
  unit: string;
  location?: string;
  locationDetail?: string;
  currentStock?: number;
  minStock: number;
  unitValue?: number;
  status: StockItemStatus;
  validity?: string;
  ignoreValidity?: boolean;
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
      .get<{ itens: AlmoxarifadoItemApi[] }>(`${this.baseUrl}/items`)
      .pipe(map((response) => (response.itens ?? []).map((item) => this.mapApiToItem(item))));
  }

  getNextItemCode(): Observable<string> {
    return this.http
      .get<{ codigo: string }>(`${this.baseUrl}/items/next-code`)
      .pipe(map((response) => response.codigo));
  }

  listMovements(): Observable<StockMovement[]> {
    return this.http
      .get<{ movimentações: AlmoxarifadoMovimentacaoApi[] }>(`${this.baseUrl}/movements`)
      .pipe(map((response) => (response.movimentações ?? []).map((item) => this.mapApiToMovement(item))));
  }

  createItem(payload: StockItemPayload): Observable<StockItem> {
    return this.http
      .post<AlmoxarifadoItemApi>(`${this.baseUrl}/items`, this.mapPayloadToApi(payload))
      .pipe(map((response) => this.mapApiToItem(response)));
  }

  updateItem(id: string, payload: StockItemPayload): Observable<StockItem> {
    return this.http
      .put<AlmoxarifadoItemApi>(`${this.baseUrl}/items/${id}`, this.mapPayloadToApi(payload))
      .pipe(map((response) => this.mapApiToItem(response)));
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
      .post<{ movimentacao: AlmoxarifadoMovimentacaoApi; item: AlmoxarifadoItemApi }>(
        `${this.baseUrl}/movements`,
        this.mapMovementPayloadToApi(payload)
      )
      .pipe(
        map((response) => ({
          movement: this.mapApiToMovement(response.movimentacao),
          item: this.mapApiToItem(response.item)
        }))
      );
  }

  private mapPayloadToApi(payload: StockItemPayload): AlmoxarifadoItemApiRequest {
    return {
      codigo: payload.code,
      codigo_barras: payload.barcode ?? null,
      descricao: payload.description,
      categoria: payload.category,
      unidade: payload.unit,
      localizacao: payload.location ?? null,
      localizacao_interna: payload.locationDetail ?? null,
      estoque_atual: payload.currentStock ?? 0,
      estoque_minimo: payload.minStock,
      valor_unitario: payload.unitValue ?? 0,
      situacao: payload.status,
      validade: payload.validity ?? null,
      ignorar_validade: payload.ignoreValidity ?? false,
      observacoes: payload.notes ?? null
    };
  }

  private mapMovementPayloadToApi(payload: {
    date: string;
    type: MovementType;
    itemCode: string;
    quantity: number;
    reference?: string;
    responsible?: string;
    notes?: string;
    adjustmentDirection?: AdjustmentDirection;
  }): AlmoxarifadoMovimentacaoApiRequest {
    return {
      data_movimentacao: payload.date,
      tipo: payload.type,
      codigo_item: payload.itemCode,
      quantidade: payload.quantity,
      referencia: payload.reference ?? null,
      responsavel: payload.responsible ?? null,
      observacoes: payload.notes ?? null,
      direcao_ajuste: payload.adjustmentDirection ?? null
    };
  }

  private mapApiToItem(item: AlmoxarifadoItemApi): StockItem {
    return {
      id: String(item.id_item ?? ''),
      code: item.codigo,
      barcode: item.codigo_barras ?? undefined,
      description: item.descricao,
      category: item.categoria ?? '',
      unit: item.unidade ?? '',
      location: item.localizacao ?? '',
      locationDetail: item.localizacao_interna ?? '',
      currentStock: item.estoque_atual ?? 0,
      minStock: item.estoque_minimo ?? 0,
      unitValue: item.valor_unitario ?? 0,
      status: (item.situacao as StockItemStatus) ?? 'Ativo',
      validity: item.validade ?? undefined,
      ignoreValidity: item.ignorar_validade ?? false,
      notes: item.observacoes ?? ''
    };
  }

  private mapApiToMovement(item: AlmoxarifadoMovimentacaoApi): StockMovement {
    return {
      id: String(item.id_movimentacao ?? ''),
      date: item.data_movimentacao,
      type: item.tipo as MovementType,
      itemCode: item.codigo_item,
      itemDescription: item.descricao_item ?? '',
      quantity: item.quantidade ?? 0,
      balanceAfter: item.saldo_apos ?? 0,
      reference: item.referencia ?? '',
      responsible: item.responsavel ?? '',
      notes: item.observacoes ?? ''
    };
  }
}

type AlmoxarifadoItemApiRequest = {
  codigo: string;
  codigo_barras?: string | null;
  descricao: string;
  categoria: string;
  unidade: string;
  localizacao?: string | null;
  localizacao_interna?: string | null;
  estoque_atual: number;
  estoque_minimo: number;
  valor_unitario: number;
  situacao: string;
  validade?: string | null;
  ignorar_validade?: boolean;
  observacoes?: string | null;
};

type AlmoxarifadoItemApi = AlmoxarifadoItemApiRequest & {
  id_item?: number | string;
};

type AlmoxarifadoMovimentacaoApiRequest = {
  data_movimentacao: string;
  tipo: string;
  codigo_item: string;
  quantidade: number;
  referencia?: string | null;
  responsavel?: string | null;
  observacoes?: string | null;
  direcao_ajuste?: string | null;
};

type AlmoxarifadoMovimentacaoApi = {
  id_movimentacao?: number | string;
  data_movimentacao: string;
  tipo: string;
  codigo_item: string;
  descricao_item?: string;
  quantidade?: number;
  saldo_apos?: number;
  referencia?: string | null;
  responsavel?: string | null;
  observacoes?: string | null;
};
