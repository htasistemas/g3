import { MigrationInterface, QueryRunner, Table } from 'typeorm';

export class CreateStockModule1731000000000 implements MigrationInterface {
  name = 'CreateStockModule1731000000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(
      new Table({
        name: 'stock_items',
        columns: [
          { name: 'id', type: 'uuid', isPrimary: true, isGenerated: true, generationStrategy: 'uuid' },
          { name: 'code', type: 'varchar', isUnique: true },
          { name: 'description', type: 'varchar' },
          { name: 'category', type: 'varchar', isNullable: true },
          { name: 'unit', type: 'varchar', isNullable: true },
          { name: 'location', type: 'varchar', isNullable: true },
          { name: 'locationDetail', type: 'varchar', isNullable: true },
          { name: 'currentStock', type: 'int', default: 0 },
          { name: 'minStock', type: 'int', default: 0 },
          { name: 'unitValue', type: 'decimal', precision: 12, scale: 2, default: 0 },
          { name: 'status', type: 'varchar', length: '20', default: "'Ativo'" },
          { name: 'notes', type: 'text', isNullable: true },
          { name: 'createdAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updatedAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'stock_movements',
        columns: [
          { name: 'id', type: 'uuid', isPrimary: true, isGenerated: true, generationStrategy: 'uuid' },
          { name: 'date', type: 'date' },
          { name: 'type', type: 'varchar', length: '10' },
          { name: 'quantity', type: 'int' },
          { name: 'balanceAfter', type: 'int' },
          { name: 'reference', type: 'varchar', isNullable: true },
          { name: 'responsible', type: 'varchar', isNullable: true },
          { name: 'notes', type: 'text', isNullable: true },
          { name: 'itemCode', type: 'varchar' },
          { name: 'itemDescription', type: 'varchar' },
          { name: 'itemId', type: 'uuid' },
          { name: 'createdAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ],
        foreignKeys: [
          {
            columnNames: ['itemId'],
            referencedTableName: 'stock_items',
            referencedColumnNames: ['id'],
            onDelete: 'CASCADE'
          }
        ]
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('stock_movements');
    await queryRunner.dropTable('stock_items');
  }
}
