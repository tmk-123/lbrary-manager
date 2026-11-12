-- Gan ke cho cac ban chua co vi tri, theo ban cung dau tai lieu.
-- Ke 12: B2; ke 13: C1. Khong thay doi tinh trang/trang thai muon.
USE libman7;

START TRANSACTION;

UPDATE tblDocumentCopy7
SET tblShelf7id = CASE
    WHEN tblDocument7id IN (123, 124, 456) THEN 12
    WHEN tblDocument7id IN (310, 877) THEN 13
END
WHERE tblShelf7id IS NULL
  AND tblDocument7id IN (123, 124, 456, 310, 877);

COMMIT;

SELECT id, barcode, tblDocument7id, tblShelf7id
FROM tblDocumentCopy7
ORDER BY id;
