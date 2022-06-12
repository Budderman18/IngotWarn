# IngotWarn
IngotWarn is a small warn-based plugin to help track what rules players have broken, what players have done, or any reason you desire. 
You can configure a max amount of warns and in the future, execute commands on a given amount of warns.

FEATURES:

- Warn a player
- Set a limit to the amount of warns a player can have
- Configure output messages with language.yml
- Check your own warns with a command
- Check others' warns with the command above and a separate permission
- Manage player's files within the game, no need to edit any files except language.yml
- Alert players that were warned offline when they rejoin
- Execute commands on a given warn amount
- Check if player changed their username and copy over their data if they have

COMMANDS:

/warn (player) (reason) - Warn a designated player. Works for both online and Offline players
/checkwarns [player] - Check your warns. if a user is specified, it will check their warns (separate permission node for that!)
/adminwarn (command) - Admin management commands for IngotWarn
- clear (player) - Clear a given player's userfile
- delete (player) (index) - Delete a given warn for a player.
- edit (player) (index) (reason) - Edit a given warn for a player
- version - Check plugin version
- reload - Reload the plugin
- commands - Edit commands executed on a given warncount
  - add [warnNumber] [command] - Add a command to the given warncount
  - delete [warnNumber] (commandNumber) - Delete a given command or all commands ion a given warncount
  - edit [warnNumber] [commandNumber] (command) - Edit a given command
  - clear (warnNumber) - clear all commands or all commands at a specified warncount
  - list (warnNumber) - list all commands or all commands at a specifed warncount

PERMISSIONS:

- ingotwarn.warn - gives access to the /warn command
- ingotwarn.checkwarns - gives access to the /checkwarns command
- ingotwarn.checkwarns.others - gives access to checking other players within the /checkwarns command
- ingotwarn.adminwarn - gives access to the /adminwarn command
