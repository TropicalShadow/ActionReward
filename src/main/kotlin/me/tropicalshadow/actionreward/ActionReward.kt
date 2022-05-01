package me.tropicalshadow.actionreward

import me.tropicalshadow.actionreward.commands.utils.ShadowCommand
import me.tropicalshadow.actionreward.config.ConfigManager
import me.tropicalshadow.actionreward.listener.ShadowListener
import me.tropicalshadow.actionreward.player.PlayerManager
import me.tropicalshadow.actionreward.player.ShadowPlayer
import me.tropicalshadow.actionreward.task.ShadowTaskTimer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class ActionReward: JavaPlugin() {

    lateinit var playerManager: PlayerManager
    lateinit var configManager: ConfigManager

    var debug: Boolean = false

    override fun onEnable() {
        ShadowTaskTimer.plugin = this
        ShadowPlayer.plugin = this
        configManager = ConfigManager(this)
        playerManager = PlayerManager(this)

        registerListeners()
        registerCommands()

        ShadowTaskTimer.infinite(period = 20, onTick = {
            playerManager.players.values.forEach { it.incrementTime() }
        })

        logger.info("Plugin Enabled")
    }

    override fun onDisable() {

        logger.info("Plugin Disabled")
    }



    private fun registerListeners(){
        val packageName = javaClass.`package`.name
        for (clazz in Reflections("$packageName.listener").getSubTypesOf(
            ShadowListener::class.java
        )){
            try{
                val listener: ShadowListener = clazz.getDeclaredConstructor(this::class.java).newInstance(this)
                if(debug) logger.info("Registering ${listener.javaClass.name.split(".").last()}")
                Bukkit.getPluginManager().registerEvents(listener, this)
                if(debug) logger.info("Registered ${listener.javaClass.name.split(".").last()}")
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    private fun registerCommands() {
        val packageName = javaClass.getPackage().name
        for (clazz in Reflections("$packageName.commands").getSubTypesOf(
            ShadowCommand::class.java
        )) {
            try {
                val cmd: ShadowCommand = clazz.getDeclaredConstructor(this::class.java).newInstance(this)
                val cmdName: String = cmd.getCommandInfo().name
                if (cmdName.isEmpty()) continue
                val command = getCommand(cmdName)
                if (command == null) {
                    logger.info("Couldn't inject command: $cmdName")
                    continue
                }
                logger.info("Registering command $cmdName")
                command.setExecutor(cmd)
                command.tabCompleter = cmd
                if (cmd.getCommandInfo().permission.isEmpty()) command.permission = null else command.permission =
                    cmd.getCommandInfo().permission
                if(cmd.getCommandInfo().description.isNotBlank())
                    command.description = cmd.getCommandInfo().description
                if( cmd.getCommandInfo().usage != "/<command>")
                    command.usage = cmd.getCommandInfo().usage
                command.aliases = cmd.aliases
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}