ALTER TABLE saved_sets ADD MH_VERSION INT;

ALTER TABLE saved_sets ADD NUM_VOTES INT DEFAULT 0;

-- execute after import of tri before import of p3
UPDATE saved_sets SET MH_VERSION = 0;
