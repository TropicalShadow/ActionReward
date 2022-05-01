package me.tropicalshadow.actionreward.player

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class PlayerManager(private val plugin: ActionReward) {

    val players = HashMap<UUID, ShadowPlayer>()


    init {
        Bukkit.getOnlinePlayers().forEach { player ->
            addPlayer(player)
        }
    }


    fun addPlayer(player: Player){
        players[player.uniqueId] = ShadowPlayer(player.uniqueId, 0)//TODO - read from config
    }
    fun removePlayer(uniqueId: UUID){
        players.remove(uniqueId)
    }

}