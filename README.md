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