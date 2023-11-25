package me.vagdedes.spartan.handlers.identifiers.complex.predictable;

import me.vagdedes.spartan.objects.data.Handlers;
import me.vagdedes.spartan.objects.replicates.SpartanPlayer;
import me.vagdedes.spartan.utils.gameplay.MoveUtils;

public class GroundCollision {

    public static boolean run(SpartanPlayer player) {
        if (player.isOnGroundCustom()
                && player.isOnGround()
                && player.getTicksOnAir() == 0
                && player.getTicksOnGround() > 0
                && player.getNmsVerticalDistance() == 0.0
                && player.getVehicle() == null) {
            Handlers handlers = player.getHandlers();

            if (player.getNmsHorizontalDistance() <= MoveUtils.highPrecision
                    && player.getCustomDistance() <= MoveUtils.lowPrecision) {
                handlers.removeMany(Handlers.HandlerFamily.Motion);
                handlers.removeMany(Handlers.HandlerFamily.Velocity);
            } else {
                handlers.remove(Handlers.HandlerType.BouncingBlocks);
                handlers.remove(Handlers.HandlerType.WaterElevator);
            }

            if (!Liquid.isLocation(player, player.getLocation())) {
                player.removeLastLiquidTime();
            }
            return true;
        }
        return false;
    }
}
