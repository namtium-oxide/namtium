<?php

    defined('BASEPATH') || die();

    function getUser($username)
    {
        global $authmeDatabase;
        
        $statement = $authmeDatabase->prepare('SELECT password FROM ' . DB_TABLE . ' WHERE username = :username');
        $state = $statement->execute(array('username' => $username));

        if ($state === FALSE)
        {
            die('false:errorApiDatabaseQuery');
        }

        $result = $statement->fetchAll(PDO::FETCH_ASSOC);

        if ($result === FALSE)
        {
            die('false:errorApiDatabaseFetch');
        }
        
        return $result;
    }
