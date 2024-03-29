SET DB_CLOSE_DELAY -1;         
;              
CREATE USER IF NOT EXISTS "" SALT '' HASH '' ADMIN;            
CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_CE051218_6282_4D4E_BC8B_083D4B720B25" START WITH 13 BELONGS_TO_TABLE;
CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_704587BB_DC0E_44AB_A7F0_3DE0CA44FE3F" START WITH 2 BELONGS_TO_TABLE; 
CREATE MEMORY TABLE "PUBLIC"."RANGED_WEAPON"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "TYPE" VARCHAR NOT NULL,
    "COOLDOWN" INT NOT NULL,
    "DAMAGE" VARCHAR NOT NULL,
    "ANIMATION" VARCHAR NOT NULL,
    "SOUND" VARCHAR NOT NULL,
    "RANGE" VARCHAR NOT NULL,
    "PUNCH_ANIMATION" VARCHAR NOT NULL,
    "PUNCH_SOUND" VARCHAR NOT NULL,
    "MISS_ANIMATION" VARCHAR NOT NULL,
    "MISS_SOUND" VARCHAR NOT NULL,
    "ICON" VARCHAR NOT NULL
);              
ALTER TABLE "PUBLIC"."RANGED_WEAPON" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_4" PRIMARY KEY("ID"); 
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.RANGED_WEAPON;            
INSERT INTO "PUBLIC"."RANGED_WEAPON" VALUES
('wooden_bow', 'Wooden Bow', 'bow', 1000, '1d6+1', 'Arrow', 'Arrow', '5d4', 'Punch', 'Arrow punch', 'Poof', 'Arrow punch', 'Generic,12,11'),
('iron_bow', 'Iron Bow', 'bow', 700, '2d6+2', 'Arrow', 'Arrow', '6d4', 'Punch', 'Arrow punch', 'Poof', 'Arrow punch', 'Generic,12,10');               
CREATE MEMORY TABLE "PUBLIC"."AMMUNITION"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "APPLIES_TO" VARCHAR NOT NULL,
    "DAMAGE" VARCHAR NOT NULL,
    "ICON" VARCHAR NOT NULL
);            
ALTER TABLE "PUBLIC"."AMMUNITION" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_F" PRIMARY KEY("ID");    
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.AMMUNITION;               
INSERT INTO "PUBLIC"."AMMUNITION" VALUES
('wooden_arrow', 'Wooden Arrow', 'bow', '1', 'Generic,8,10');         
CREATE MEMORY TABLE "PUBLIC"."MELEE_WEAPON"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "COOLDOWN" INT NOT NULL,
    "DAMAGE" VARCHAR NOT NULL,
    "ANIMATION" VARCHAR NOT NULL,
    "SOUND" VARCHAR,
    "ICON" VARCHAR NOT NULL
);         
ALTER TABLE "PUBLIC"."MELEE_WEAPON" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_2" PRIMARY KEY("ID");  
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.MELEE_WEAPON;             
INSERT INTO "PUBLIC"."MELEE_WEAPON" VALUES
('wooden_sword', 'Wooden Sword', 1000, '1d4+1', 'Slash', 'Sword slash', 'Generic,5,10'),
('wooden_dagger', 'Wooden Dagger', 300, '1d2', 'Slash', 'Sword slash', 'Generic,7,1');     
CREATE MEMORY TABLE "PUBLIC"."OBJECT"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "CHARSET" VARCHAR NOT NULL,
    "FRAME" SMALLINT,
    "INTERACT_SOUND" VARCHAR
);           
ALTER TABLE "PUBLIC"."OBJECT" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_C" PRIMARY KEY("ID");        
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.OBJECT;   
INSERT INTO "PUBLIC"."OBJECT" VALUES
('black_fsm_right_doors', 'Doors', 'FSM Doors', 0, 'Arrow punch'),
('enforced_chest_left', 'Enforced Chest', 'Chests', 3, 'Arrow punch'),
('plain_chest_down', 'Plain Chest', 'Chests', 0, 'Arrow punch');
CREATE MEMORY TABLE "PUBLIC"."JUNK"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "ICON" VARCHAR NOT NULL
);    
ALTER TABLE "PUBLIC"."JUNK" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_22" PRIMARY KEY("ID");         
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.JUNK;     
INSERT INTO "PUBLIC"."JUNK" VALUES
('bone', 'Bone', 'Generic,21,2'),
('eye', 'Eye', 'Generic,21,3'),
('tooth', 'Tooth', 'Generic,21,5'),
('fur', 'Fur', 'Generic,21,6');       
CREATE MEMORY TABLE "PUBLIC"."MEDICAMENTS"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "ICON" VARCHAR NOT NULL,
    "HP" VARCHAR NOT NULL,
    "ANIMATION" VARCHAR NOT NULL,
    "SOUND" VARCHAR NOT NULL
);  
ALTER TABLE "PUBLIC"."MEDICAMENTS" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_E" PRIMARY KEY("ID");   
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.MEDICAMENTS;              
INSERT INTO "PUBLIC"."MEDICAMENTS" VALUES
('small_life_potion', 'Small life potion', 'Generic,2,11', '2d4+2', 'Poof', 'Arrow punch');          
CREATE MEMORY TABLE "PUBLIC"."ENEMY_DROP"(
    "ID" INT DEFAULT NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_CE051218_6282_4D4E_BC8B_083D4B720B25" NOT NULL NULL_TO_DEFAULT SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_CE051218_6282_4D4E_BC8B_083D4B720B25",
    "ENEMY" VARCHAR NOT NULL,
    "ITEM" VARCHAR NOT NULL,
    "CHANCE" DECIMAL NOT NULL,
    "AMOUNT" VARCHAR NOT NULL
);   
ALTER TABLE "PUBLIC"."ENEMY_DROP" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_8" PRIMARY KEY("ID");    
-- 7 +/- SELECT COUNT(*) FROM PUBLIC.ENEMY_DROP;               
INSERT INTO "PUBLIC"."ENEMY_DROP" VALUES
(1, 'deku', 'throwing:deku_arrow', 0.8, '2d4+5'),
(6, 'skeleton', 'junk:bone', 0.7, '1'),
(7, 'skeleton_archer', 'junk:bone', 0.7, '1'),
(8, 'skeleton', 'melee:wooden_sword', 0.5, '1'),
(9, 'skeleton_archer', 'ranged:wooden_bow', 0.3, '1'),
(10, 'skeleton_archer', 'ammo:wooden_arrow', 0.7, '1d4+3'),
(11, 'deku', 'junk:eye', 0.7, '1d2');    
CREATE MEMORY TABLE "PUBLIC"."CONFIG"(
    "KEY" VARCHAR NOT NULL,
    "VALUE" VARCHAR
);      
ALTER TABLE "PUBLIC"."CONFIG" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_7" PRIMARY KEY("KEY");       
-- 4 +/- SELECT COUNT(*) FROM PUBLIC.CONFIG;   
INSERT INTO "PUBLIC"."CONFIG" VALUES
('start_game', 'Hero Home,Main,Start'),
('screen', '1000x800'),
('camera_scale', '2'),
('full_day_duration', '600');      
CREATE MEMORY TABLE "PUBLIC"."LEVELS"(
    "LEVEL" INT DEFAULT NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_704587BB_DC0E_44AB_A7F0_3DE0CA44FE3F" NOT NULL NULL_TO_DEFAULT SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_704587BB_DC0E_44AB_A7F0_3DE0CA44FE3F",
    "MAX_HP" VARCHAR NOT NULL
);              
ALTER TABLE "PUBLIC"."LEVELS" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_85" PRIMARY KEY("LEVEL");    
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.LEVELS;   
INSERT INTO "PUBLIC"."LEVELS" VALUES
(1, '3000');              
CREATE MEMORY TABLE "PUBLIC"."THROWING_WEAPON"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "COOLDOWN" INT NOT NULL,
    "DAMAGE" VARCHAR NOT NULL,
    "ANIMATION" VARCHAR NOT NULL,
    "SOUND" VARCHAR NOT NULL,
    "RANGE" VARCHAR NOT NULL,
    "PUNCH_ANIMATION" VARCHAR NOT NULL,
    "PUNCH_SOUND" VARCHAR NOT NULL,
    "MISS_ANIMATION" VARCHAR NOT NULL,
    "MISS_SOUND" VARCHAR NOT NULL,
    "ICON" VARCHAR NOT NULL
);         
ALTER TABLE "PUBLIC"."THROWING_WEAPON" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_40" PRIMARY KEY("ID");              
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.THROWING_WEAPON;          
INSERT INTO "PUBLIC"."THROWING_WEAPON" VALUES
('deku_arrow', 'Deku''s arrow', 500, '2d4', 'Arrow', 'Arrow', '5d4', 'Punch', 'Arrow punch', 'Poof', 'Arrow punch', 'Generic,8,10'),
('shuriken', 'Shuriken', 100, '3d6', 'Shuriken', 'Arrow', '5d4', 'Punch', 'Arrow punch', 'Poof', 'Arrow punch', 'Generic,9,2');             
CREATE MEMORY TABLE "PUBLIC"."ENEMY"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "CHARSET" VARCHAR NOT NULL,
    "DEAD_CHARSET" VARCHAR,
    "HP" VARCHAR NOT NULL,
    "SPEED" VARCHAR NOT NULL,
    "BLOCKING" BOOL NOT NULL,
    "MELEE_WEAPON" VARCHAR,
    "RANGED_WEAPON" VARCHAR,
    "THROWING_WEAPON" VARCHAR,
    "DIE_ANIMATION" VARCHAR NOT NULL,
    "DIE_SOUND" VARCHAR NOT NULL
);             
ALTER TABLE "PUBLIC"."ENEMY" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_3" PRIMARY KEY("ID");         
-- 8 +/- SELECT COUNT(*) FROM PUBLIC.ENEMY;    
INSERT INTO "PUBLIC"."ENEMY" VALUES
('deku', 'Deku', 'Deku', 'Corpse', '2d4', '10d2', TRUE, NULL, NULL, 'deku_arrow,1d4+2', 'Poof', 'Deku death'),
('garo', 'Garo', 'Garo', 'Corpse', '7d4', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('blanca', 'Blanca', 'Blanca', 'Corpse', '10d4', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('turtle', 'Turtle', 'Turtle', 'Corpse', '5d4', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('silver_bat', 'Silver Bat', 'Silver Bat', 'Corpse', '1d4+2', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('eagle', 'Eagle', 'Eagle', 'Corpse', '2d4+2', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('skeleton', 'Skeleton', 'Skeleton', 'Corpse', '2d6+2', '10d2', TRUE, 'wooden_sword', NULL, NULL, 'Poof', 'Deku death'),
('skeleton_archer', 'Skeleton Archer', 'Skeleton', 'Corpse', '2d6+2', '10d2', TRUE, 'wooden_dagger', 'wooden_bow,wooden_arrow,2d4+3', NULL, 'Poof', 'Deku death');    
CREATE MEMORY TABLE "PUBLIC"."FRIEND"(
    "ID" VARCHAR NOT NULL,
    "NAME" VARCHAR NOT NULL,
    "CHARSET" VARCHAR NOT NULL,
    "SPEED" VARCHAR NOT NULL,
    "BLOCKING" BOOLEAN DEFAULT FALSE NOT NULL,
    "DIALOG_COLOR" VARCHAR DEFAULT '0xFFFFFF' NOT NULL
);          
ALTER TABLE "PUBLIC"."FRIEND" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_7C" PRIMARY KEY("ID");       
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.FRIEND;   
INSERT INTO "PUBLIC"."FRIEND" VALUES
('turtle', 'Turtle', 'Turtle', '10d2', TRUE, 'AA00DD'),
('neko', 'Neko', 'Neko', '14', TRUE, 'AA00DD'),
('grandma', 'Grandma', 'Grandma', '7', TRUE, 'DD00AA');           
ALTER TABLE "PUBLIC"."ENEMY_DROP" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_8A" FOREIGN KEY("ENEMY") REFERENCES "PUBLIC"."ENEMY"("ID") NOCHECK;      
ALTER TABLE "PUBLIC"."ENEMY" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_3F" FOREIGN KEY("MELEE_WEAPON") REFERENCES "PUBLIC"."MELEE_WEAPON"("ID") NOCHECK;             
