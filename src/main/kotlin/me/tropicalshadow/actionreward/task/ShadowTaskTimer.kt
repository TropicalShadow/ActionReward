package me.tropicalshadow.actionreward.task

import me.tropicalshadow.actionreward.ActionReward
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class ShadowTaskTimer(var duration: Long, val onTick: Consumer<Long>? = null, val onEnd: Consumer<Long>? = null, val tickLast: Boolean = false, val infinite: Boolean = false): BukkitRunnable() {

    override fun run() {
        if(duration <= 0 && !infinite){
            cancel()
            if(tickLast)onTick?.accept(duration)
            onEnd?.accept(duration)
            return
        }
        onTick?.accept(duration)
        duration -= 1
    }

    companion object{
        lateinit var plugin: ActionReward


        fun infinite(delay: Long = 0, period: Long = 20, onTick: Consumer<Long>? = null, onEnd: Consumer<Long>? = null ): ShadowTaskTimer{
            val shadowTask = ShadowTaskTimer(0, onTick,onEnd,infinite = true)
            shadowTask.runTaskTimer(plugin, delay, period)
            return shadowTask
        }

        fun start(duration: Long, delay: Long = 0, period: Long = 20, onTick: Consumer<Long>? = null, onEnd: Consumer<Long>? = null, tickLast: Boolean = false): ShadowTaskTimer{
            val shadowTask = ShadowTaskTimer(duration, onTick, onEnd, tickLast)
            shadowTask.runTaskTimer(plugin, delay, period)
            return shadowTask
        }

        fun startAsync(duration: Long, delay: Long = 0, period: Long = 20, onTick: Consumer<Long>? = null, onEnd: Consumer<Long>? = null, tickLast: Boolean = false): ShadowTaskTimer{
            val shadowTask = ShadowTaskTimer(duration, onTick, onEnd, tickLast)
            shadowTask.runTaskTimerAsynchronously(plugin, delay, period)
            return shadowTask
        }

    }

}