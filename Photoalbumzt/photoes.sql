BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Albums" (
	"aid"	INTEGER,
	"name"	TEXT DEFAULT ' ',
	"createdate"	TEXT DEFAULT ' ',
	"path"	TEXT DEFAULT ' ',
	PRIMARY KEY("aid" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "pictures" (
	"pid"	INTEGER NOT NULL,
	"aid"	INTEGER NOT NULL,
	"name"	TEXT DEFAULT ' ',
	"note"	TEXT DEFAULT ' ',
	"path"	TEXT DEFAULT ' ',
	"date"	TEXT DEFAULT ' ',
	"category"	TEXT,
	PRIMARY KEY("pid" AUTOINCREMENT)
);
INSERT INTO "Albums" ("aid","name","createdate","path") VALUES (30,'budapest','2025-03-16','budapest');
INSERT INTO "Albums" ("aid","name","createdate","path") VALUES (31,'london','2025-03-16','london');
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (72,30,' parlament',' parlament','./photoes/budapest/budapest-parlament.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (73,30,'castle','castle','./photoes/budapest/castle.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (74,30,' chain bridge',' chain bridge','./photoes/budapest/chain_bridge.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (75,30,' heroes square',' heroes square','./photoes/budapest/heroes_square.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (76,30,'matthias church','matthias church','./photoes/budapest/matthias_church.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (77,31,' ',' ','./photoes/london/amy-leigh-barnard-TU9rJiWSkAI-unsplash.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (78,31,' ',' ','./photoes/london/arturo-ramirez-PI5CTo5xz4k-unsplash.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (79,31,' ',' ','./photoes/london/benjamin-davies-Oja2ty_9ZLM-unsplash.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (80,31,' ',' ','./photoes/london/charles-postiaux-Q6UehpkBSnQ-unsplash.png','2025-03-16',NULL);
INSERT INTO "pictures" ("pid","aid","name","note","path","date","category") VALUES (81,31,' ',' ','./photoes/london/paul-rigby--7DmLW32sF0-unsplash.png','2025-03-16',NULL);
COMMIT;
