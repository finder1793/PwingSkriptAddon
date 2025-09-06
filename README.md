# PwingSkriptAddon

PwingSkriptAddon is a Skript add-on that brings utility effects, conditions, and an event to your server scripts. It focuses on file management, cron-like scheduling, chat/books with MiniMessage, world utilities, placeholder checks, and FAWE schematic pasting.

Note: This document lists what the addon can do, the syntax you can use in your .sk scripts, and examples.

## Requirements
- Minecraft server: Spigot/Paper 1.16+ (plugin api-version: 1.16)
- Java 17 runtime
- Skript 2.10.0 or newer
- Optional/feature-specific soft dependencies:
  - PlaceholderAPI (for placeholder comparison condition)
  - Vault + a permissions plugin (for permission group condition)
  - FastAsyncWorldEdit (FAWE) compatible with your server (for schematic paste effect)

## Installation
- Download/build the jar and place PwingSkriptAddon.jar into your server's plugins folder.
- Ensure Skript is installed and loaded first.
- Install optional dependencies only if you plan to use the respective features.
- Restart the server.

## Configuration
- plugins/PwingSkriptAddon/config.yml
  - biome-brush-enabled: true (requires FAWE installed). If true and FAWE is present, enables the Biome Brush feature and command.

## Features overview
- Cron event: run Skript code on cron-like schedules.
- File and directory operations: copy, delete, rename, zip, unzip, SFTP upload.
- JSON chat messages: rich messages with hover and click actions using MiniMessage.
- Books: open interactive books for players (MiniMessage supported).
- World utilities: change weather, set time, find nearest structure.
- String manipulation: hex conversion, encoding/decoding, randomization, case conversion.
- Advanced file operations: detailed file info, attributes, permissions, ownership.
- YAML operations: read, write, and validate YAML files.
- URL operations: fetch content, check status, SSL verification, size information.
- System information: CPU, RAM, disk space, time zones, system properties.
- Server management: reload Skript, restart server, run commands.
- Encoding operations: binary, ASCII, Unicode conversions.
- Date and time operations: parsing, formatting, time zone conversions.
- Encryption and security: hash functions, encryption/decryption.
- Archiving operations: zip/unzip files and directories.
- JSON operations: parse and extract data from JSON.
- Conditions: validate JSON, check empty directory, check Vault permission group, compare PlaceholderAPI value to a number.
- FAWE schematic pasting.
- Biome Brush (requires FAWE and config enabled): Command `/biomebrush give <biome> [radius]` gives a brush item that right-click paints the biome in an area.

## Syntax reference and examples
The following are the exact Skript patterns this addon registers. Use them in your .sk files.

### Event: Cron
- Pattern: `cron %string% start`
  - This is used as an event line. The string is a cron expression.
  - Examples:
    - `cron "0 */5 * * * ?" start:`  (every 5 minutes)
    - `cron "0 0 4 * * ?" start:`    (daily at 4 AM)
- Expression format: seconds minutes hours day-of-month month day-of-week. Either day-of-week OR day-of-month must be "?".
- Example script (multiple tasks):
  - `cron "0 */5 * * * ?" start:` send "[Backup] Checking for changes..." to console
  - `cron "0 0 * * * ?" start:` send "[Cleanup] Running hourly cleanup..." to console
  - `cron "0 0 4 * * ?" start:` send "[Maintenance] Running daily maintenance..." to console

### Effects: Files and directories
- Copy directory: `copy directory %string% to %string%`
  - Example: `copy directory "plugins/MyPlugin" to "plugins/MyPlugin_backup"`
- Copy file: `copy file %string% to %string%`
  - Example: `copy file "plugins/config.yml" to "plugins/backups/config.yml"`
- Delete directory: `delete directory %string%`
  - Example: `delete directory "worlds/temp_world"`
- Delete file: `delete file %string%`
  - Example: `delete file "plugins/temp.yml"`
- Rename file or directory: `rename (file|directory) %string% to %string%`
  - Examples:
    - `rename file "config.old.yml" to "config.yml"`
    - `rename directory "plugins/OldName" to "plugins/NewName"`
- Zip: `zip file %string% to %string%`
  - Example: `zip file "worlds/world" to "backup/world.zip"`
- Unzip: `unzip file %string% to %string%`
  - Example: `unzip file "plugins/config.zip" to "plugins/config"`
- SFTP upload: `sftp transfer file %string% to %string% on host %string% with user %string% and password %string%`
  - Example: `sftp transfer file "local/path/file.txt" to "remote/path/file.txt" on host "sftp.example.com" with user "username" and password "password"`
  - Security note: Prefer using secrets management and restrict permissions; this effect sends credentials directly.

### Effect: JSON chat messages (MiniMessage)
- Pattern: `send json message %string% [with hover text %-string%] [(and|with) click command %-string%] to %players%`
- Examples:
  - `send json message "<#ff0000>This is red</#ff0000>" to player`
  - `send json message "&6&lCLICK" with hover text "&bHover!" and click command "help" to player`
  - MiniMessage supports hex, gradient, rainbow, and more.

### Effect: Open book (MiniMessage pages)
- Patterns:
  - `open book titled %string% by %string% with page %string% to %players%`
  - `open book titled %string% by %string% with page[s] %strings% to %players%`
- Examples:
  - `open book titled "&6Cool Book" by "Author" with page "Welcome!" to player`
  - `set {_pages::*} to "Page 1", "Page 2"`
    `open book titled "My Book" by "Me" with pages {_pages::*} to player`

### Effects: World utilities
- Change weather: `change weather in world %string% to %string%`
  - weather values: clear, rain, thunder
  - Example: `change weather in world "world" to "rain"`
- Set time: `set time in world %string% to %number%`
  - Example: `set time in world "world" to 6000`
- Find nearest structure: `find structure %string% near x %number%, z %number% in world %string%`
  - Structure key must match a server StructureType key. Example usage may store or use the result inside your script environment.

### Effect: Paste schematic (FAWE)
- Pattern: `paste schematic %string% at %location%`
- Example: `paste schematic "plugins/schematics/castle.schem" at location(1200, 60, 1500, world("world"))`
- Requires FAWE and compatible schematic format.

### Effects: Particle Effects (EffectLib)
PwingSkriptAddon includes a comprehensive set of particle effects powered by EffectLib. These create advanced visual effects like shapes, animations, and more. Requires EffectLib (bundled in the jar).

### Basic Shapes
- Circle: `create particle circle at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle circle at location of player with radius 5 using "FLAME" particle`
- Sphere: `create particle sphere at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle sphere at location of player with radius 3 using "HEART" particle`
- Cube: `create particle cube at %location% with size %number% using %string% particle [for %number% iterations]`
  - Example: `create particle cube at location of player with size 4 using "CRIT" particle`
- Cylinder: `create particle cylinder at %location% with radius %number% height %number% using %string% particle [for %number% iterations]`
  - Example: `create particle cylinder at location of player with radius 2 height 5 using "PORTAL" particle`
- Cloud: `create particle cloud at %location% with size %number% using %string% particle [for %number% iterations]`
  - Example: `create particle cloud at location of player with size 5 using "CLOUD" particle`
- Atom: `create particle atom at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle atom at location of player with radius 3 using "SPELL_WITCH" particle`
- Grid: `create particle grid at %location% with size %number% using %string% particle [for %number% iterations]`
  - Example: `create particle grid at location of player with size 4 using "REDSTONE" particle`

### Arcs and Lines
- Arc: `create particle arc at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle arc at location of player with radius 6 using "SPLASH" particle`
- Line: `create particle line at %location% with length %number% using %string% particle [for %number% iterations]`
  - Example: `create particle line at location of player with length 10 using "END_ROD" particle`

### Complex Shapes
- Vortex (Spiral): `create particle vortex at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle vortex at location of player with radius 4 using "WITCH" particle`
- Cone: `create particle cone at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle cone at location of player with radius 3 using "LAVA" particle`
- Pyramid: `create particle pyramid at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle pyramid at location of player with radius 5 using "TOTEM" particle`

### Animated Effects
- Star: `create particle star at %location% using %string% particle [for %number% iterations]`
  - Example: `create particle star at location of player using "FIREWORKS_SPARK" particle`
- Heart: `create particle heart at %location% with size %number% using %string% particle [for %number% iterations]`
  - Example: `create particle heart at location of player with size 2 using "HEART" particle`
- Music: `create particle music at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle music at location of player with radius 1 using "NOTE" particle`
- Tornado: `create particle tornado at %location% with height %number% radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle tornado at location of player with height 10 radius 3 using "CLOUD" particle`
- Flame: `create particle flame at %location% with particles %number% using %string% particle [for %number% iterations]`
  - Example: `create particle flame at location of player with particles 50 using "FLAME" particle`
- Smoke: `create particle smoke at %location% with particles %number% using %string% particle [for %number% iterations]`
  - Example: `create particle smoke at location of player with particles 30 using "SMOKE_NORMAL" particle`
- Fountain: `create particle fountain at %location% with radius %number% height %number% using %string% particle [for %number% iterations]`
  - Example: `create particle fountain at location of player with radius 2 height 8 using "WATER_SPLASH" particle`
- Dragon: `create particle dragon at %location% with length %number% using %string% particle [for %number% iterations]`
  - Example: `create particle dragon at location of player with length 15 using "DRAGON_BREATH" particle`
- DiscoBall: `create particle discoball at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle discoball at location of player with radius 4 using "FIREWORKS_SPARK" particle`
- Bleed: `create particle bleed at %location% with height %number% using %string% particle [for %number% iterations]`
  - Example: `create particle bleed at location of player with height 6 using "REDSTONE" particle`

### Special Effects
- Shield: `create particle shield at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle shield at location of player with radius 3 using "FLAME" particle`
- Explode: `create particle explode at %location% using %string% particle`
  - Example: `create particle explode at location of player using "EXPLOSION_NORMAL" particle`
- Donut: `create particle donut at %location% with radius %number% tube %number% using %string% particle [for %number% iterations]`
  - Example: `create particle donut at location of player with radius 4 tube 1 using "CRIT_MAGIC" particle`
- DNA: `create particle dna at %location% with radius %number% length %number% using %string% particle [for %number% iterations]`
  - Example: `create particle dna at location of player with radius 2 length 10 using "SPELL_MOB" particle`
- Earth: `create particle earth at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle earth at location of player with radius 5 using "VILLAGER_HAPPY" particle`
- BigBang: `create particle bigbang at %location% with radius %number% using %string% particle [for %number% iterations]`
  - Example: `create particle bigbang at location of player with radius 6 using "FIREWORKS_SPARK" particle`
- Hill: `create particle hill at %location% with size %number% using %string% particle [for %number% iterations]`
  - Example: `create particle hill at location of player with size 8 using "COMPOSTER" particle`
- Wave: `create particle wave at %location% with height %number% width %number% using %string% particle [for %number% iterations]`
  - Example: `create particle wave at location of player with height 3 width 10 using "DRIP_WATER" particle`
- Love: `create particle love at %location% using %string% particle [for %number% iterations]`
  - Example: `create particle love at location of player using "HEART" particle`

### Effects: String Manipulation
- Convert number to hexadecimal: `convert %number% to hexadecimal`
  - Example: `convert 255 to hexadecimal`
- Convert hexadecimal to RGB: `convert hexadecimal %string% to rgb`
  - Example: `convert hexadecimal "FF0000" to rgb`
- Encode to Base64: `encode %string% to base64`
  - Example: `encode "Hello World" to base64`
- Encode to Morse code: `encode %string% to morse`
  - Example: `encode "SOS" to morse`
- Mirror text: `mirror %string%`
  - Example: `mirror "hello"`
- Randomize string: `randomize %string%`
  - Example: `randomize "abcdef"`
- Convert to uppercase: `convert %string% to uppercase`
  - Example: `convert "hello" to uppercase`
- Convert to lowercase: `convert %string% to lowercase`
  - Example: `convert "HELLO" to lowercase`
- Clear accented characters: `clear accented characters from %string%`
  - Example: `clear accented characters from "café"`
- Get string length: `length of %string%`
  - Example: `length of "hello world"`
- Check if string starts with: `%string% starts with %string%`
  - Example: `"hello world" starts with "hello"`
- Check if string ends with: `%string% ends with %string%`
  - Example: `"hello world" ends with "world"`

### Effects: Advanced File Operations
- List directory contents: `list contents of directory %string%`
  - Example: `list contents of directory "plugins"`
- Get file name and extension: `get name and extension of file %string%`
  - Example: `get name and extension of file "config.yml"`
- Get file contents: `get contents of file %string%`
  - Example: `get contents of file "config.yml"`
- Edit file line: `edit line %number% of file %string% to %string%`
  - Example: `edit line 5 of file "config.yml" to "new value"`
- Create file: `create file %string%`
  - Example: `create file "newfile.txt"`
- Delete file: `delete file %string%`
  - Example: `delete file "oldfile.txt"`
- Download file: `download file from %string% to %string%`
  - Example: `download file from "http://example.com/file.txt" to "downloads/file.txt"`
- Insert line in file: `insert %string% at line %number% in file %string%`
  - Example: `insert "new line" at line 3 in file "config.yml"`
- Check if file exists: `file %string% exists`
  - Example: `file "config.yml" exists`
- Check if path is file: `%string% is a file`
  - Example: `"config.yml" is a file`
- Copy file: `copy file %string% to %string%`
  - Example: `copy file "source.txt" to "destination.txt"`
- Get file lines: `get lines of file %string%`
  - Example: `get lines of file "config.yml"`
- Get file time attributes: `get time attributes of file %string%`
  - Example: `get time attributes of file "config.yml"`
- Get file size: `get size of file %string%`
  - Example: `get size of file "config.yml"`
- Get file size in bytes: `get size in bytes of file %string%`
  - Example: `get size in bytes of file "config.yml"`
- Get directory size: `get size of directory %string%`
  - Example: `get size of directory "plugins"`
- Get directory size in bytes: `get size in bytes of directory %string%`
  - Example: `get size in bytes of directory "plugins"`
- Get absolute path: `get absolute path of %string%`
  - Example: `get absolute path of "config.yml"`
- Get file owner: `get owner of file %string%`
  - Example: `get owner of file "config.yml"`
- Get file attributes: `get attributes of file %string%`
  - Example: `get attributes of file "config.yml"`
- Rename/move file: `rename file %string% to %string%`
  - Example: `rename file "oldname.txt" to "newname.txt"`
- Check if directory: `%string% is a directory`
  - Example: `"plugins" is a directory`
- Check if symbolic link: `%string% is a symbolic link`
  - Example: `"link" is a symbolic link`
- Check if executable: `%string% is executable`
  - Example: `"script.sh" is executable`
- Get file creation time: `get creation time of file %string%`
  - Example: `get creation time of file "config.yml"`
- Get file deletion time: `get deletion time of file %string%`
  - Example: `get deletion time of file "config.yml"`
- Wipe file: `wipe file %string%`
  - Example: `wipe file "sensitive.txt"`
- Move file: `move file %string% to %string%`
  - Example: `move file "file.txt" to "newlocation/file.txt"`
- Write to file: `write %string% to file %string%`
  - Example: `write "Hello World" to file "output.txt"`
- Zip file: `zip file %string% to %string%`
  - Example: `zip file "config.yml" to "config.zip"`

### Effects: YAML Operations
- Set YAML value: `set yaml %string% key %string% to %string%`
  - Example: `set yaml "config.yml" key "setting.value" to "newvalue"`
- Check if YAML exists: `yaml file %string% exists`
  - Example: `yaml file "config.yml" exists`

### Effects: URL Operations
- Get URL contents: `get contents of url %string%`
  - Example: `get contents of url "https://example.com"`
- Read URL line: `read line %number% from url %string%`
  - Example: `read line 1 from url "https://example.com"`
- Get URL lines: `get lines from url %string%`
  - Example: `get lines from url "https://example.com"`
- Get URL size: `get size of url %string%`
  - Example: `get size of url "https://example.com"`
- Get URL size in bytes: `get size in bytes of url %string%`
  - Example: `get size in bytes of url "https://example.com"`
- Get URL response code: `get response code of url %string%`
  - Example: `get response code of url "https://example.com"`
- Check if URL is online: `url %string% is online`
  - Example: `url "https://example.com" is online`
- Get URL last modified: `get last modified date of url %string%`
  - Example: `get last modified date of url "https://example.com"`
- Get SSL verifier: `get ssl verifier of url %string%`
  - Example: `get ssl verifier of url "https://example.com"`
- Get SSL serial number: `get ssl serial number of url %string%`
  - Example: `get ssl serial number of url "https://example.com"`
- Get SSL issue date: `get ssl issue date of url %string%`
  - Example: `get ssl issue date of url "https://example.com"`
- Get SSL expire date: `get ssl expire date of url %string%`
  - Example: `get ssl expire date of url "https://example.com"`
- Get SSL algorithm: `get ssl algorithm of url %string%`
  - Example: `get ssl algorithm of url "https://example.com"`
- Get SSL version: `get ssl version of url %string%`
  - Example: `get ssl version of url "https://example.com"`

### Effects: System Information
- Get time zone: `get current time zone`
  - Example: `get current time zone`
- Get CPU cores: `get cpu cores count`
  - Example: `get cpu cores count`
- Get loaded list: `get loaded plugins list`
  - Example: `get loaded plugins list`
- Get RAM information: `get ram information`
  - Example: `get ram information`
- Get CPU specifications: `get cpu specifications`
  - Example: `get cpu specifications`
- Get system time: `get system time`
  - Example: `get system time`
- Get system property: `get system property %string%`
  - Example: `get system property "java.version"`
- Check if OS: `is operating system %string%`
  - Example: `is operating system "Windows"`
- Get disk space: `get disk space`
  - Example: `get disk space`

### Effects: Server Management
- Reload Skript: `reload skript`
  - Example: `reload skript`
- Restart server: `restart server`
  - Example: `restart server`
- Run OP command: `run op command %string%`
  - Example: `run op command "op player"`
- Run code: `run code %string%`
  - Example: `run code "broadcast 'Hello World'"`
- Run application: `run application %string%`
  - Example: `run application "notepad.exe"`
- Run command: `run command %string%`
  - Example: `run command "say Hello"`
- Run command with output: `run command %string% and get output`
  - Example: `run command "list" and get output`
- Reload Skript aliases: `reload skript aliases`
  - Example: `reload skript aliases`

### Effects: Encoding Operations
- Convert to binary: `convert %string% to binary`
  - Example: `convert "A" to binary`
- Convert to ASCII: `convert %string% to ascii`
  - Example: `convert "65" to ascii`
- Convert from string: `convert from string %string%`
  - Example: `convert from string "48656C6C6F"`
- Convert from binary: `convert from binary %string%`
  - Example: `convert from binary "01001000"`
- Convert from Unicode: `convert from unicode %string%`
  - Example: `convert from unicode "U+0048"`
- Convert hexadecimal to number: `convert hexadecimal %string% to number`
  - Example: `convert hexadecimal "FF" to number`
- Convert RGB to hex: `convert rgb %string% to hex`
  - Example: `convert rgb "255,0,0" to hex`

### Effects: Date and Time Operations
- Parse date: `parse date %string% with format %string%`
  - Example: `parse date "2023-01-01" with format "yyyy-MM-dd"`
- Convert date to Unix timestamp: `convert date %string% to unix timestamp`
  - Example: `convert date "2023-01-01" to unix timestamp`
- Convert Unix timestamp to date: `convert unix timestamp %number% to date`
  - Example: `convert unix timestamp 1672531200 to date`
- Format Unix timestamp: `format unix timestamp %number% as %string%`
  - Example: `format unix timestamp 1672531200 as "yyyy-MM-dd"`
- Get date inner value: `get inner value of date %string%`
  - Example: `get inner value of date "2023-01-01"`
- List time zones: `list all time zones`
  - Example: `list all time zones`
- Get time in time zone: `get time in time zone %string%`
  - Example: `get time in time zone "America/New_York"`
- Get region: `get region of time zone %string%`
  - Example: `get region of time zone "America/New_York"`
- Check if valid time zone: `%string% is a valid time zone`
  - Example: `"America/New_York" is a valid time zone`

### Effects: Encryption and Security
- Encrypt string: `encrypt %string% with algorithm %string%`
  - Example: `encrypt "secret" with algorithm "AES"`
- Hash string: `hash %string% with algorithm %string%`
  - Example: `hash "password" with algorithm "SHA-256"`

### Effects: Archiving Operations
- List zip contents: `list contents of zip file %string%`
  - Example: `list contents of zip file "archive.zip"`
- Zip files: `zip files %strings% to %string%`
  - Example: `zip files "file1.txt", "file2.txt" to "archive.zip"`
- Zip directory: `zip directory %string% to %string%`
  - Example: `zip directory "plugins" to "plugins.zip"`
- Unzip file: `unzip file %string% to %string%`
  - Example: `unzip file "archive.zip" to "extracted"`

### Effects: JSON Operations
- Get JSON value by path: `get json value %string% from %string%`
  - Example: `get json value "user.name" from "{\"user\":{\"name\":\"John\"}}"`
- Get JSON IDs: `get json ids from %string%`
  - Example: `get json ids from "{\"users\":[{\"id\":1},{\"id\":2}]}"`

### Conditions
- JSON validity:
  - `%string% is valid json`
  - `%string% is not valid json`
- Directory empty:
  - `directory %string% is empty`
  - `directory %string% is not empty`
- Player in permission group (Vault):
  - `%player% (is|has) [in] [permission] group %string%`
  - `%player% (isn't|is not|hasn't|has not) [in] [permission] group %string%`
- Placeholder comparison (PlaceholderAPI):
  - `placeholder %string% (equal|equals|greater than|less than) %number%`
  - Resolves the placeholder for the first online player and compares numerically.

## Notes and tips
- MiniMessage formatting: Supports hex colors like <#ff0000>, gradients, rainbow, hover/click events. Use MiniMessage tags for colors/formatting; legacy color codes (&a, &b, etc.) are not parsed by MiniMessage.
- Cron expressions: If your expression has 6 parts, seconds are included; with 5 parts, seconds are assumed. Ensure only one of day-of-week or day-of-month is "?".
- FAWE vs WorldEdit: This addon uses FAWE APIs. Ensure FAWE is installed and matches your server version.
- Permissions: Vault must be present and configured with a compatible permissions plugin for permission-group checks to work.
- Placeholders: PlaceholderAPI must be installed. The placeholder comparison condition expects the placeholder result to be a number.
- File paths: Paths are relative to the server working directory unless you provide absolute paths.

## Build from source
- Requirements: Java 17, Gradle (wrapper provided).
- Command: On a terminal in the project root, run `./gradlew shadowJar` (or `gradlew.bat shadowJar` on Windows). The jar will be at build/libs/PwingSkriptAddon.jar.

## Example: Multiple cron tasks
See src/main/resources/examples/multiple-crons.sk for a ready-to-use example with three cron schedules.