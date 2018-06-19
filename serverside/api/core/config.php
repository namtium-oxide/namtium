<?php

    defined('BASEPATH') || die();

    // ---- CONFIGURATION -------------------------------

    define('DB_HOST', '127.0.0.1');
    define('DB_USERNAME', 'root');
    define('DB_PASSWORD', '');
    define('DB_NAME', 'minecraft');
    define('DB_TABLE', 'authme');

    // ---- PASSWORD COMPARISON FUNCTION ----------------

    function passwordCompare($rawHash, $password)
    {
        $parts = explode('$', $rawHash);

        if (count($parts) === 4)
        {
            return hash('sha256', hash('sha256', $password) . $parts[2]) === $parts[3];
        }

        return FALSE;
    }

    // --------------------------------------------------
