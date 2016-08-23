CREATE TABLE user_votes (
         ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         IP_ADDRESS VARCHAR(1024) NOT NULL,
         USER_ID BIGINT,
         ACTION VARCHAR(12) NOT NULL,
         SET_ID BIGINT NOT NULL
       );

ALTER TABLE user_votes ADD FOREIGN KEY (USER_ID) REFERENCES users(ID);
ALTER TABLE user_votes ADD FOREIGN KEY (SET_ID) REFERENCES saved_sets(ID);

-- todo add in the following table after some consieration
CREATE TABLE armour_skills (
         ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         ARMOUR_NAME VARCHAR(1024) NOT NULL,
         ARMOUR_PIECE INT NOT NULL,
         SKILL_NAME VARCHAR(1024) NOT NULL,
         SKILL_VALUE INT NOT NULL,
         MH_VERSION INT NOT NULL,
         RANK INT NOT NULL
       );

ALTER TABLE saved_sets DROP NUM_VOTES;
ALTER TABLE saved_sets ADD NUM_UP_VOTES INT DEFAULT 0;
ALTER TABLE saved_sets ADD NUM_DOWN_VOTES INT DEFAULT 0;
