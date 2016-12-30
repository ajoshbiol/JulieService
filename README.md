# JulieService
The web service portion of my personal assistant - Julie.

## Setup 
Instructions are for Debian and Ubuntu based Linux distributions

### Dependencies
java

    sudo apt-get install openjdk-8-jdk

maven

    sudo apt-get install maven

### Database

MySQL

https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-14-04

Schema name: Julie

Tables: 
- weights
    - id int(11) AI PK
    - weightInLbs double
    - datetime datetime

- todos
    - id int(11) AI PK
    - task varchar(256)
    - creationDate timestamp
    - completionDate timestamp

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

Name: addTodo

    CREATE DEFINER=`dev`@`%` PROCEDURE `addTodo`(IN _task VARCHAR(256))
    BEGIN
        SET @creationDate = UTC_TIMESTAMP();
        INSERT INTO todos (`task`, `creationDate`) VALUES (_task, @creationDate);
        SELECT LAST_INSERT_ID() AS `id`, @creationDate AS `creationDate`;
    END

Name: updateTodo

    CREATE DEFINER=`dev`@`%` PROCEDURE `updateTodo`(IN _id INT, IN _task VARCHAR(256), IN _creationDate DATETIME, IN _completionDate DATETIME)
    BEGIN
        DECLARE I INT;
        SELECT COUNT(*) INTO I
        FROM todos 
        WHERE `id` = _id;
        
        IF (I != 1)
        THEN 
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'ID NOT FOUND IN TABLE.';
        END IF;
        
        UPDATE todos SET `task` = _task, `creationDate` = _creationDate, `completionDate` = _completionDate WHERE `id` = _id;
    END
