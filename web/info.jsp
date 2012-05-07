<%--
  Created by IntelliJ IDEA.
  User: gural
  Date: 07.05.12
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>

<p>

<h1>Use-case</h1> <br/>
Пользователь заходит на стартовую страницу, и выбирает действие которое он хочет осуществить из списка возможных: добавление нового солдата, просмотр перечня (солдат, подразделений, локаций), осуществление поиска солдат, или просмотр справки.

На странице просмотра он видит список всех солдат, нажимая на конкретного солдата страница отображает всех его подчиненных включая отображение текущего пути в иерархии, задав параметры фильтра пользователь может суживать выборку солдат на текущей странице отображания.

На странице поиска он может отмечать по каким критериям выбирать солдат в результирующее множество, в этом множестве пользователь может отметить солдат которых он хочет удалить или может выбрать любого из них (конкретно) открыв перечень его характеристик, а также перечень подчиненных ему солдат, с этой страницы он может перейти на страницу редактирования для изменения его характеристик.

Если пользователь хочет добавить солдата - он может перейти на страницу добавления, и ввести все необходимые данные. Если пользователь вводит данные в неправильном формате он получает уведомление об этом.

Пользователь может просматривать список подразделений или локаций, а также редактировать их.

В списке локаций, выбрав конкретную из них ему отображаются параметры данной локации и список подразделений размещенных в этой локации.

В списке подразделений, выбрав конкретную из них ему отображаются параметры данного подразделения и список солдат в этом подразделении.

Любому солдату можно поменять коммандира, нажав кнопку "move" напротив него в списке, чем вызывается диалог с отображением всех остальных солдат с возможностью суживания выборки.

Для любого солдата в списке отображения можно вызвать диалог редактирования нажав кнопку "edit" напротив него в списке, чем вызывается диалог с отображением его параметров с возможностью изменения, процес применения изменений идентичен добавлению нового солдата.

При отображении подчиненных выбранного солдата, над списком выводится уровень иерархии. В котором отображается путь к текущему солдату от самого главного.

</p>
<p>

<h1>DDL</h1>

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
loc_id NUMBER (7) NOT NULL ,
name VARCHAR2 (45) NOT NULL ,
region VARCHAR2 (45) NOT NULL ,
city VARCHAR2 (45) NOT NULL ,
CONSTRAINT location_PK PRIMARY KEY ( loc_id ) ,
CONSTRAINT location_name_uk UNIQUE ( name )
)
;

CREATE TABLE Unit
(
unit_id NUMBER (7) NOT NULL ,
-- Unit's name
name VARCHAR2 (45) NOT NULL ,
location NUMBER (7) NOT NULL ,
headOfUnit NUMBER(7) ON DELETE SET NULL,
CONSTRAINT unit_PK PRIMARY KEY ( unit_id ),
CONSTRAINT unit_name_uk UNIQUE ( name ) ,
CONSTRAINT unit_Location_FK FOREIGN KEY (location) REFERENCES Location (loc_id) ON DELETE SET NULL
-- CONSTRAINT unit_head_FK FOREIGN KEY (headOfUnit) REFERENCES Soldier (soldier_id)
)
;

CREATE TABLE Soldier
(
-- starts with 1000001
soldier_id NUMBER (7) NOT NULL ,
name VARCHAR2 (45) NOT NULL ,
rank VARCHAR2 (45) NOT NULL ,
-- ref to soldier which commands by current one
commander NUMBER (7) ,
unit NUMBER (7) NOT NULL ,
birthDate DATE NOT NULL ,
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

</p>

