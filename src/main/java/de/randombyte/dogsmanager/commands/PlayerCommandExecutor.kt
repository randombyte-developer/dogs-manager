package de.randombyte.dogsmanager.commands

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

abstract class PlayerCommandExecutor : CommandExecutor {
    companion object {
        val ERROR_TEXT = Text.of("Must be executed by a player!")
    }

    override fun execute(src: CommandSource, args: CommandContext): CommandResult = if (src !is Player) {
        throw CommandException(ERROR_TEXT)
    } else executedByPlayer(src, args)

    abstract fun executedByPlayer(player: Player, args: CommandContext): CommandResult
}