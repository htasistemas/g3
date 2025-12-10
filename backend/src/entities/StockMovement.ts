import { Column, CreateDateColumn, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { StockItem } from './StockItem';

export type StockMovementType = 'Entrada' | 'Saída' | 'Ajuste';

@Entity('stock_movements')
export class StockMovement {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ type: 'date' })
  date!: string;

  @Column({ type: 'varchar', length: 10 })
  type!: StockMovementType;

  @Column({ type: 'int' })
  quantity!: number;

  @Column({ type: 'int' })
  balanceAfter!: number;

  @Column({ nullable: true })
  reference?: string;

  @Column({ nullable: true })
  responsible?: string;

  @Column({ type: 'text', nullable: true })
  notes?: string;

  @Column()
  itemCode!: string;

  @Column()
  itemDescription!: string;

  @ManyToOne(() => StockItem, (item) => item.movements, { onDelete: 'CASCADE' })
  item?: StockItem;

  @CreateDateColumn({ type: 'timestamp without time zone' })
  createdAt!: Date;
}
