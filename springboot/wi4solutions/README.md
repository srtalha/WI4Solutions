Mariadb Install
-------------------
apt install  default-mysql-server/kali-rolling
apt install default-mysql-client/kali-rolling
mysql_secure_installation

Asterisk Install
-------------------
#apt update && sudo apt upgrade
#apt install wget build-essential subversion
#cd /usr/src/
#wget http://downloads.asterisk.org/pub/telephony/asterisk/asterisk-16-current.tar.gz
#tar -zxf asterisk-16-current.tar.gz 
#mv asterisk-16.1.0/ asterisk
#cd asterisk/
#contrib/scripts/get_mp3_source.sh
#contrib/scripts/install_prereq install
#./configure
#make
#make menuselect
#make install
#make menuselect
#make samples

Clone project source code
#apt install git
#mkdir /var/www/projects
#mkdir -p /var/www/projects
#cd /var/www/projects
Clone repository with your custom git access: (After that you will have project source code in server to install it)
---------------------------------------------
#git clone https://guatellama.visualstudio.com/wi4solutions/_git/wi4solutions
Username for 'https://guatellama.visualstudio.com':srtalha@outlook.com
Password for 'https://srtalha@outlook.com@guatellama.visualstudio.com':pob7xagadyksz7ajshipjxil33cosnrikdvfmivzjf6vy4sgxaya

Install java and maven
-----------------------------
#apt install openjdk-8-jdk/kali-rolling
Select number associated to openjdk 1.8 version. By default server has 11 installed.
#update-alternatives  --config java
#apt install maven
Create user and password for database.
---------------------------------------
Log into mariadb
#mysql -u root -h localhost -p
#password XXXX

mariadb> create database wi4solutions;
mariadb> create database wi4solutions;
mariadb> GRANT ALL PRIVILEGES ON .* to 'wi4solutions'@'localhost' IDENTIFIED BY 'w14s0l_1';
mariadb> FLUSH PRIVILEGES;

Import database wi4solutions

mariadb>source /var/www/projects/wi4solutions/springboot/wi4solutions/database/wi4solutions.sql;











Import databse
-------------------------
Connect to mysql server.
#mysql -u root -h localhost
MariaDB()>source 
