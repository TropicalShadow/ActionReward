package me.tropicalshadow.actionreward.commands

import me.tropicalshadow.actionreward.ActionReward
import me.tropicalshadow.actionreward.commands.utils.ShadowCommand
import me.tropicalshadow.actionreward.commands.utils.ShadowCommandInfo
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@ShadowCommandInfo("actionreward", usage = "/<command> [reload|timeplayed]")
class ActionRewardCommand(plugin: ActionReward) : ShadowCommand(plugin) {

    override fun execute(sender: CommandSender, args: Array<String>) {
        if(args.isEmpty())return sender.sendMessage(this.usage.replace("<command>", "actionreward"))
        when(args.first().lowercase()){
            "reload" -> {
                plugin.configManager.loadActions()
                sender.sendMessage("Reloaded config")
            }
            "timeplayed" -> {
                if(sender !is Player)return
                sender.sendMessage("Time played: ${plugin.playerManager.players[sender.uniqueId]?.timePlayed?:0}")
            }
            else -> sender.sendMessage("?")
        }
    }

    override fun tabComplete(sender: CommandSender, args: Array<String>): ArrayList<String> {
        val output = ArrayList<String>()
        if(args.size <= 1){
            output.addAll(listOf("reload","timeplayed"))
        }
        return output
    }

}