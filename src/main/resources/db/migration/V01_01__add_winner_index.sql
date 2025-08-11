-- Improves performance for winner endpoint: SELECT ... WHERE apuesta_item_id=? ORDER BY apuesta_monto DESC LIMIT 1
CREATE INDEX IF NOT EXISTS idx_subasta_apuesta_item_monto
  ON subasta_apuesta (apuesta_item_id, apuesta_monto);
