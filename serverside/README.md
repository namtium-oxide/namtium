# namtium-serverside

Serverside of Natium launcher. Designed to use on Windows with PHP and Apache.

## File Description

### `version`

For use with [hydra-updater](https://github.com/lion328/hydra-updater)

### `whitelist`

Whitelisted files (i.e. tell launcher to not delete them).

### `filelist.gz`

GZIP'd file list. Generate by `tools/regenerate_files.bat`.

### `files`

GZIP'd game files. Generate by `tools/regenerate_files.bat`.

### `api/core/config.php`

Configuration of authentication system.

### `tools/original_files`

Files required to launch the game.

### `tools/regenerate_files.bat`

Generate file list and compress files from `tools/original_files`.
