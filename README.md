# Julie
My personal assistant

## Setup 
Instructions are for Debian and Ubuntu based Linux distributions

### Dependencies
java

    sudo apt-get install openjdk-8-jdk

maven

    sudo apt-get install maven

python 2.7

pip

    sudo apt-get install python-pip

requests

    pip install requests

### Database

MySQL

https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-14-04

Schema name: Julie

Tables: 
- weights
    - id int(11) AI PK
    - weightInLbs double
    - datetime datetime

Stored procedures:

Name: addWeight

    CREATE DEFINER=`dev`@`%` PROCEDURE `addWeight`(IN _date DATETIME, IN _weight DOUBLE)
    BEGIN
        INSERT INTO weights (`weightInLbs`, `datetime`) VALUES (_weight, _date);
        SELECT LAST_INSERT_ID() AS `id`;
    END

Name: updateWeight

    CREATE DEFINER=`dev`@`%` PROCEDURE `updateWeight`(IN _id INT, IN _date DATETIME, IN _weight DOUBLE)
    BEGIN
        DECLARE I INT;
        SELECT COUNT(*) INTO I
        FROM weights 
        WHERE `id` = _id;
        
        IF (I != 1)
        THEN 
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'ID NOT FOUND IN TABLE.';
        END IF;
        
        UPDATE weights SET `datetime` = _date, `weightInLbs` = _weight WHERE `id` = _id;
    END
