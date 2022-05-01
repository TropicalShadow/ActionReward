package me.tropicalshadow.actionreward.player

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.lang.Exception
import java.util.*

class ShadowPlayer(val uuid: UUID, var timePlayed: Long = 0){

    fun incrementTime(){
        timePlayed += 1
        val commands = plugin.configManager.playerTimePlayed[timePlayed]?: return
        val console = Bukkit.getConsoleSender()
        commands.forEach {
            try {
                var cmd =  it
                    .replace("%player%", Bukkit.getOfflinePlayer(uuid).name?: "Unknown")
                    .replace("%time%", timePlayed.toString())
                cmd = ChatColor.translateAlternateColorCodes('&',cmd)
                if(cmd.startsWith("<message>")){
                    Bukkit.getPlayer(uuid)?.sendMessage(cmd.replace("<message>","").trim())
                }else{
                    Bukkit.dispatchCommand(console,cmd)
                }
            }catch (e: Exception){
                if(plugin.debug)plugin.logger.info("Failed to execute command $it | ${e.message}")
            }
        }
    }

    companion object{
        lateinit var plugin: ActionReward
    }
}