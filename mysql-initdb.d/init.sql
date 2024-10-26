CREATE DATABASE IF NOT EXISTS blogdb;
CREATE DATABASE IF NOT EXISTS keycloak;

CREATE USER IF NOT EXISTS 'dbuser'@'%' IDENTIFIED BY 'dbuserpassword';

GRANT ALL PRIVILEGES ON blogdb.* TO 'dbuser'@'%';
GRANT ALL PRIVILEGES ON keycloak.* TO 'dbuser'@'%';
ALTER USER 'dbuser'@'%' IDENTIFIED WITH mysql_native_password BY 'dbuserpassword';


FLUSH PRIVILEGES;
