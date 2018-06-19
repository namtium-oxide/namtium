<?php

    define('BASEPATH', dirname(__FILE__));
    
    require_once('core/config.php');

    header('Content-Type: text/plain');

    $username = isset($_POST['username']) ? $_POST['username'] : NULL;
    $password = isset($_POST['password']) ? $_POST['password'] : NULL;

    if (empty($username) || empty($password))
    {
        die('false:errorApiEmptyRequest');
    }

    require_once('core/dbconn.php');
    require_once('core/shared.php');

    $result = getUser($username);

    if (count($result) === 0)
    {
        die('false');
    }

    if (passwordCompare($result[0]['password'], $password) === TRUE)
    {
        die('true');
    }

    die('false');
