-- Borrado lógico: agrega deleted_at (NULL = activo). No modifica ni elimina datos existentes.
-- Hibernate (ddl-auto: update) aplica el mismo cambio automáticamente; este script sirve para aplicarlo manualmente.
ALTER TABLE categories ADD COLUMN deleted_at DATETIME(6) NULL;
ALTER TABLE products   ADD COLUMN deleted_at DATETIME(6) NULL;
