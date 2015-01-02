-- export this table
-- mysqldump -uroot mhag armour_skills > armour_skills.sql
-- import the table
-- mysql -umhagadmin -pmhagadmin -Dmhag < armour_skills.sql
drop table if exists armour_skills;
CREATE TABLE armour_skills (
         ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         ARMOUR_NAME VARCHAR(1024) NOT NULL,
         ARMOUR_PIECE INT NOT NULL,
         ARMOUR_ID INT NOT NULL,
         SKILL_NAME VARCHAR(1024) NOT NULL,
         SKILL_VALUE INT NOT NULL,
         MH_VERSION INT NOT NULL,
         RANK INT NOT NULL,
         NUM_SLOTS INT DEFAULT 0,
         GENDER VARCHAR(1),
         BLADE_OR_GUNNER VARCHAR(3)
       );