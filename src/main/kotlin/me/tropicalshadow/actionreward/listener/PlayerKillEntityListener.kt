package me.tropicalshadow.actionreward.listener

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerKillEntityListener(plugin: ActionReward) : ShadowListener(plugin) {


    @EventHandler
    fun onEntityKilledByPlayer(event: EntityDeathEvent){
        if(event.entityType == EntityType.PLAYER)return//Handled in onPlayerKillPlayer
        val killer = event.entity.killer?: return
        val console = Bukkit.getConsoleSender()
        val commands = plugin.configManager.playerKillMob[event.entityType]?:return
        commands.forEach { command ->
            try{
                var cmd = command
                    .replace("%player%", killer.name).replace("%displayname%", killer.displayName)
                    .replace("%other%",event.entity.name)
                    .replace("%other_displayname%", event.entity.customName?:event.entity.name )
                    .replace("%other_entitytype%",event.entityType.name.replace("_", " ").lowercase())
                    .replace("%cause%", event.entity.lastDamageCause.cause.name.lowercase().replace("_", " "))
                cmd = ChatColor.translateAlternateColorCodes('&', cmd)
                if(cmd.startsWith("<message>")){
                    killer.sendMessage(cmd.replace("<message>","").trim())
                }else{
                    Bukkit.dispatchCommand(console,cmd)
                }
            }catch (e: Exception){
                if(plugin.debug)plugin.logger.info("Failed to run command `$command` | ${e.message}")
            }
        }
    }

    @EventHandler
    fun onPlayerKillPlayer(event: PlayerDeathEvent){
        val killer = event.entity.killer?: return
        val console = Bukkit.getConsoleSender()
        plugin.configManager.playerKillPlayer.forEach {
            try {
                var cmd = it.replace("%player%", killer.name).replace("%displayname%", killer.displayName)
                    .replace("%other%", event.entity.name).replace("%other_displayname%", event.entity.displayName?:event.entity.name)
                    .replace("%cause%", event.entity.lastDamageCause.cause.name.lowercase().replace("_", " "))
                cmd = ChatColor.translateAlternateColorCodes('&', cmd)
                if(cmd.startsWith("<message>")){
                    killer.sendMessage(cmd.replace("<message>","").trim())
                }else{
                    Bukkit.dispatchCommand(console,cmd)
                }
            }catch (e: Exception){
                if(plugin.debug)
                    plugin.logger.info("Failed to execute command `$it` | ${e.message}")
            }
        }
    }

}