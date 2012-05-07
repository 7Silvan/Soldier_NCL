DROP TABLE Location CASCADE CONSTRAINTS 
;
DROP TABLE Soldier CASCADE CONSTRAINTS 
;
DROP TABLE Unit CASCADE CONSTRAINTS 
;
--DROP TABLE Heads CASCADE CONSTRAINTS 
--;

CREATE TABLE Location 
    ( 
     loc_id NUMBER (7)  NOT NULL , 
     name VARCHAR2 (45)  NOT NULL , 
     region VARCHAR2 (45)  NOT NULL , 
     city VARCHAR2 (45)  NOT NULL ,
     CONSTRAINT location_PK PRIMARY KEY ( loc_id ) ,
     CONSTRAINT location_name_uk UNIQUE ( name )
    ) 
;

CREATE TABLE Unit 
    ( 
     unit_id NUMBER (7)  NOT NULL , 
--  Unit's name
     name VARCHAR2 (45)  NOT NULL , 
     location NUMBER (7)  NOT NULL , 
     headOfUnit NUMBER(7) ON DELETE SET NULL,
     CONSTRAINT unit_PK PRIMARY KEY ( unit_id ),
     CONSTRAINT unit_name_uk UNIQUE ( name ) ,
     CONSTRAINT unit_Location_FK FOREIGN KEY (location) REFERENCES Location (loc_id) ON DELETE SET NULL
--     CONSTRAINT unit_head_FK FOREIGN KEY (headOfUnit) REFERENCES Soldier (soldier_id)
    ) 
;

CREATE TABLE Soldier 
    ( 
--  starts with 1000001
     soldier_id NUMBER (7)  NOT NULL , 
     name VARCHAR2 (45)  NOT NULL , 
     rank VARCHAR2 (45)  NOT NULL , 
--  ref to soldier which commands by current one
     commander NUMBER (7) , 
     unit NUMBER (7)  NOT NULL , 
     birthDate DATE  NOT NULL ,
-- like boolean (0 - false, otherwise - true)
-- headOfUnit NUMBER(1) UNIQUE,
     CONSTRAINT soldier_PK PRIMARY KEY ( soldier_id ) ,
-- CONSTRAINT soldier_name_uk UNIQUE ( name ) , 
     CONSTRAINT soldier_soldier_FK FOREIGN KEY (commander) REFERENCES Soldier (soldier_id) ON DELETE SET NULL,
     CONSTRAINT soldier_unit_FK FOREIGN KEY (unit) REFERENCES Unit (unit_id)
   ) 
;

ALTER TABLE Unit
DROP CONSTRAINT unit_headOfUnit_FK ;
TRUNCATE TABLE Unit;

ALTER TABLE Unit
ADD CONSTRAINT unit_headOfUnit_FK FOREIGN KEY (headOfUnit) REFERENCES Soldier (soldier_id) ON DELETE SET NULL;
--UNIQUE (headOfUnit);

DROP SEQUENCE Soldier_soldier_id_SEQ 
;/

commit;

CREATE SEQUENCE Soldier_soldier_id_SEQ 
START WITH 1000001 
    MINVALUE 1
    MAXVALUE 9999999 
    INCREMENT BY 1
    NOCACHE 
    ORDER 
;/

commit;

CREATE OR REPLACE TRIGGER soldier_soldier_id_TRG 
BEFORE INSERT ON Soldier 
FOR EACH ROW 
WHEN (NEW.soldier_id IS NULL) 
BEGIN 
    SELECT soldier_soldier_id_SEQ.NEXTVAL INTO :NEW.soldier_id FROM DUAL; 
END;
/

commit;