CREATE USER 'mhagadmin'@'localhost' IDENTIFIED BY 'mhagadmin';
create database mhag;

GRANT ALL ON mhag.* TO 'mhagadmin'@'localhost';
FLUSH PRIVILEGES;