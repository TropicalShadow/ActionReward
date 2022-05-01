package me.tropicalshadow.actionreward.config

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.event.block.Action
import java.io.File
import java.lang.Exception

class ConfigManager(private val plugin: ActionReward) {

    private val actionFile = File(plugin.dataFolder, "actions.yml")

    var playerKillPlayer = listOf<String>()
    val playerKillMob = HashMap<EntityType, List<String>>()
    val playerTimePlayed = HashMap<Long, List<String>>()
    val playerMineBlock = HashMap<Material, List<String>>()

    init {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
        plugin.debug = plugin.config["debug"] as Boolean
        if(!actionFile.exists())plugin.saveResource("actions.yml",false)

        loadActions()
    }

    fun loadActions(){
        if(!actionFile.exists())plugin.saveResource("actions.yml",false)
        val config = YamlConfiguration.loadConfiguration(actionFile)
        playerKillMob.clear()
        playerKillPlayer = config.getStringList("player_kill_player")

        val killMobs = config.getConfigurationSection("player_kill_mob")
        playerKillMob.clear()
        killMobs.getKeys(false).forEach {
            try{
                val entity = EntityType.fromName(it)?: return@forEach plugin.logger.info("Unknown player_kill_mob.${it} entity was not found.")
                playerKillMob[entity] = killMobs.getStringList(it)
                if(plugin.debug)
                    plugin.logger.info("Loaded $entity into player_kill_mob")
            }catch (e: Exception){
                plugin.logger.info("Failed to load player_kill_mob.${it} : ${e.message}")
            }
        }
        val playerTime = config.getConfigurationSection("player_time_played")
        playerTimePlayed.clear()
        playerTime.getKeys(false).forEach { key ->
            val duration = key.toLongOrNull()?: return@forEach plugin.logger.info("Unknown duration player_time_played.${key}")
            playerTimePlayed[duration] = playerTime.getStringList(key)
            if(plugin.debug)
                plugin.logger.info("Loaded duration $duration into player_time_played")
        }
        val blockMined = config.getConfigurationSection("player_mined_block")
        playerMineBlock.clear()
        blockMined.getKeys(false).forEach { key ->
            val material = Material.matchMaterial(key)?: return@forEach plugin.logger.info("Unknown block type player_mined_block.${key}")
            playerMineBlock[material] = blockMined.getStringList(key)
            if(plugin.debug)
                plugin.logger.info("Loaded block type $key into player_mined_block")
        }

    }

}