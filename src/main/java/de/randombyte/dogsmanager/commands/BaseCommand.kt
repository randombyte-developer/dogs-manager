package de.randombyte.dogsmanager.commands

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.entity.TameableData
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors

class BaseCommand : PlayerCommandExecutor() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        Sponge.getServiceManager().provide(PaginationService::class.java).ifPresent { paginationService ->
            paginationService.builder().contents(Sponge.getServer().worlds.mapNotNull { world ->
                world.entities.mapNotNull { entity ->
                        if (entity.supports(TameableData::class.java) &&
                                entity.get(Keys.TAMED_OWNER).orElse(null)?.orElse(null)?.equals(player.uniqueId) ?: false) {
                            entity
                        } else null
                    }
                }.flatten().map { tameable ->
                    val loc = tameable.location
                    val builder = Text.builder()
                            .append(Text.of(tameable.translation))
                            .append(Text.of(" at ${loc.blockX} ${loc.blockY} ${loc.blockZ} "))
                    if (tameable.get(Keys.IS_SITTING).orElse(false)) {
                        builder.append(Text.builder("[STAND UP]").color(TextColors.GREEN).onClick(TextActions.executeCallback {
                            tameable.offer(Keys.IS_SITTING, false)
                            executedByPlayer(player, args)
                        }).build())
                    }
                    return@map builder.build()
                }
            ).title(Text.of("Tamed animals")).sendTo(player)
        }

        return CommandResult.success()
    }
}