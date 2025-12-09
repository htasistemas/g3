import { Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn, UpdateDateColumn } from 'typeorm';
import { StockMovement } from './StockMovement';

export type StockItemStatus = 'Ativo' | 'Inativo';

@Entity('stock_items')
export class StockItem {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  code!: string;

  @Column()
  description!: string;

  @Column({ nullable: true })
  category?: string;

  @Column({ nullable: true })
  unit?: string;

  @Column({ nullable: true })
  location?: string;

  @Column({ nullable: true })
  locationDetail?: string;

  @Column({ type: 'int', default: 0 })
  currentStock!: number;

  @Column({ type: 'int', default: 0 })
  minStock!: number;

  @Column({
    type: 'decimal',
    precision: 12,
    scale: 2,
    default: 0,
    transformer: { to: (value: number) => value, from: (value: string | number) => Number(value) }
  })
  unitValue!: number;

  @Column({ type: 'varchar', length: 20, default: 'Ativo' })
  status!: StockItemStatus;

  @Column({ type: 'text', nullable: true })
  notes?: string;

  @OneToMany(() => StockMovement, (movement) => movement.item)
  movements?: StockMovement[];

  @CreateDateColumn({ type: 'datetime' })
  createdAt!: Date;

  @UpdateDateColumn({ type: 'datetime' })
  updatedAt!: Date;
}
