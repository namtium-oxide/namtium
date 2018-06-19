<?php

    defined('BASEPATH') || die();

    try
    {
        $authmeDatabase = new PDO('mysql:dbname=' . DB_NAME . ';host=' . DB_HOST, DB_USERNAME, DB_PASSWORD);
    }
    catch (PDOException $e)
    {
        die('false:errorApiDatabaseConnect');
    }
