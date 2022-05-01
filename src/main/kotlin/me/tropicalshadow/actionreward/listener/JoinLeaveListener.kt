package me.tropicalshadow.actionreward.listener

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveListener(plugin: ActionReward) : ShadowListener(plugin) {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        plugin.playerManager.addPlayer(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent){
        plugin.playerManager.removePlayer(event.player.uniqueId)
    }

}