package me.tropicalshadow.actionreward.listener

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class PlayerMineBlockListener(plugin: ActionReward) : ShadowListener(plugin) {

    @EventHandler
    fun onBlockMined(event: BlockBreakEvent){
        val miner = event.player?:return
        val block = event.block
        val commands = plugin.configManager.playerMineBlock[block.type]?: return
        val console = Bukkit.getConsoleSender()
        commands.forEach { command ->
            try {
                var cmd =  command
                    .replace("%player%", miner.name)
                    .replace("%displayname%", miner.displayName)
                    .replace("%block%", block.type.name.lowercase().replace("_"," "))
                    .replace("%x%", block.location.x.toInt().toString())
                    .replace("%y%", block.location.y.toInt().toString())
                    .replace("%z%", block.location.z.toInt().toString())
                cmd = ChatColor.translateAlternateColorCodes('&',cmd)
                if(cmd.startsWith("<message>")){
                    event.player.sendMessage(cmd.replace("<message>","").trim())
                }else{
                Bukkit.dispatchCommand(console,cmd)
                }
            }catch (e: Exception){
                if(plugin.debug)plugin.logger.info("Failed to execute command `$command` | ${e.message}")
            }
        }
    }

}