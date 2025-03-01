package me.vagdedes.spartan.api;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.abstraction.protocol.SpartanProtocol;
import com.vagdedes.spartan.functionality.connection.Latency;
import com.vagdedes.spartan.functionality.connection.cloud.IDs;
import com.vagdedes.spartan.functionality.moderation.Wave;
import com.vagdedes.spartan.functionality.notifications.AwarenessNotifications;
import com.vagdedes.spartan.functionality.notifications.DetectionNotifications;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.Permissions;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.Enums.HackType;
import me.vagdedes.spartan.system.Enums.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BackgroundAPI {

    static String licenseID() {
        return IDs.user();
    }

    static String getVersion() {
        return Register.plugin != null ? Register.plugin.getDescription().getVersion() : "Unknown";
    }

    static String getMessage(String path) {
        return Config.messages.getColorfulString(path);
    }

    static boolean getSetting(String path) {
        return Config.settings.getBoolean(path);
    }

    @Deprecated
    static String getCategory(Player p, HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getCategory' has been removed.");
        return null;
    }

    @Deprecated
    static boolean hasVerboseEnabled(Player p) {
        return hasNotificationsEnabled(p);
    }

    static boolean hasNotificationsEnabled(Player p) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        return DetectionNotifications.isEnabled(player);
    }

    @Deprecated
    static int getViolationResetTime() {
        AwarenessNotifications.forcefullySend("The API method 'getViolationResetTime' has been removed.");
        return 0;
    }

    @Deprecated
    static void setVerbose(Player p, boolean value) {
        setNotifications(p, value);
    }

    static void setNotifications(Player p, boolean value) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;

        if (value) {
            DetectionNotifications.set(player, DetectionNotifications.defaultFrequency);
        } else {
            DetectionNotifications.remove(player);
        }
    }

    @Deprecated
    static void setVerbose(Player p, boolean value, int frequency) {
        AwarenessNotifications.forcefullySend("The API method 'setVerbose' has been removed.");
    }

    static void setNotifications(Player p, int frequency) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            DetectionNotifications.set(player, Math.abs(frequency));
        }
    }

    static int getPing(Player p) {
        return Latency.ping(p);
    }

    @Deprecated
    static double getTPS() {
        AwarenessNotifications.forcefullySend("The API method 'getTPS' has been removed.");
        return 0.0;
    }

    static boolean hasPermission(Player p, Permission Permission) {
        return Permissions.has(p, Permission);
    }

    static boolean isEnabled(HackType HackType) {
        return HackType.getCheck().isEnabled(null, null);
    }

    static boolean isSilent(HackType HackType) {
        return HackType.getCheck().isSilent(null, null);
    }

    static int getVL(Player p, HackType hackType) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        return player.getExecutor(hackType).getLevel();
    }

    @Deprecated
    static double getDecimalVL(Player p, HackType HackType) {
        AwarenessNotifications.forcefullySend("The API method 'getDecimalVL' has been removed.");
        return 0.0;
    }

    static int getVL(Player p) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        int total = 0;

        for (HackType hackType : Enums.HackType.values()) {
            total += player.getExecutor(hackType).getLevel();
        }
        return total;
    }

    @Deprecated
    static void setVL(Player p, HackType HackType, int amount) {
        AwarenessNotifications.forcefullySend("The API method 'setVL' has been removed.");
    }

    @Deprecated
    static int getCancelViolation(HackType hackType, String worldName) {
        return getCancelViolation(hackType);
    }

    @Deprecated
    static int getCancelViolation(HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getCancelViolation' has been removed.");
        return 0;
    }

    @Deprecated
    static int getViolationDivisor(Player p, HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getViolationDivisor' has been removed.");
        return 0;
    }

    static void reloadConfig() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Config.reload(null);
        }
    }

    @Deprecated
    static void reloadPermissions() {
        AwarenessNotifications.forcefullySend("The API method 'reloadPermissions' has been removed.");
    }

    @Deprecated
    static void reloadPermissions(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'reloadPermissions' has been removed.");
    }

    static void enableCheck(HackType HackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType.getCheck().setEnabled(null, true);
        }
    }

    static void disableCheck(HackType HackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType.getCheck().setEnabled(null, false);
        }
    }

    static void cancelCheck(Player p, HackType hackType, int ticks) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            player.getExecutor(hackType).addDisableCause("Developer-API", null, ticks);
        }
    }

    static void cancelCheckPerVerbose(Player p, String string, int ticks) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) { // Keep the null pointer protection to prevent the method from acting differently
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;

            for (HackType hackType : Enums.HackType.values()) {
                player.getExecutor(hackType).addDisableCause("Developer-API", string, ticks);
            }
        }
    }

    static void enableSilentChecking(HackType HackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType.getCheck().setSilent(null, true);
        }
    }

    static void disableSilentChecking(HackType HackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType.getCheck().setSilent(null, false);
        }
    }

    static void enableSilentChecking(Player p, HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            player.getExecutor(hackType).addSilentCause("Developer-API", null, 0);
        }
    }

    static void disableSilentChecking(Player p, HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            player.getExecutor(hackType).removeSilentCause();
        }
    }

    static void startCheck(Player p, HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            player.getExecutor(hackType).removeDisableCause();
        }
    }

    static void stopCheck(Player p, HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
            player.getExecutor(hackType).addSilentCause("Developer-API", null, 0);
        }
    }

    static void resetVL() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType[] hackTypes = Enums.HackType.values();

            for (SpartanProtocol protocol : SpartanBukkit.getProtocols()) {
                for (HackType hackType : hackTypes) {
                    protocol.spartanPlayer.getExecutor(hackType).resetLevel();
                }
            }
        }
    }

    static void resetVL(Player p) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;

            for (HackType hackType : Enums.HackType.values()) {
                player.getExecutor(hackType).resetLevel();
            }
        }
    }

    static boolean isBypassing(Player p) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        return Permissions.isBypassing(player, null);
    }

    static boolean isBypassing(Player p, HackType HackType) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        return Permissions.isBypassing(player, HackType);
    }

    @Deprecated
    static void banPlayer(UUID uuid, String reason) {
        AwarenessNotifications.forcefullySend("The API method 'banPlayer' has been removed.");

    }

    @Deprecated
    static boolean isBanned(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'isBanned' has been removed.");
        return false;
    }

    @Deprecated
    static void unbanPlayer(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'unbanPlayer' has been removed.");
    }

    @Deprecated
    static String getBanReason(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'getBanReason' has been removed.");
        return null;
    }

    @Deprecated
    static String getBanPunisher(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'getBanPunisher' has been removed.");
        return null;
    }

    @Deprecated
    static boolean isHacker(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'isHacker' has been removed.");
        return false;
    }

    @Deprecated
    static boolean isLegitimate(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'isLegitimate' has been removed.");
        return false;
    }

    @Deprecated
    static boolean hasMiningNotificationsEnabled(Player p) {
        return hasNotificationsEnabled(p);
    }

    @Deprecated
    static void setMiningNotifications(Player p, boolean value) {
        setNotifications(p, value);
    }

    static int getCPS(Player p) {
        SpartanPlayer player = SpartanBukkit.getProtocol(p).spartanPlayer;
        return player == null ? 0 : player.clicks.getCount();
    }

    @Deprecated
    static UUID[] getBanList() {
        AwarenessNotifications.forcefullySend("The API method 'getBanList' has been removed.");
        return new UUID[]{};
    }

    static boolean addToWave(UUID uuid, String command) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.add(uuid, command);
            return true;
        }
        return false;
    }

    static void removeFromWave(UUID uuid) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.remove(uuid);
        }
    }

    static void clearWave() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.clear();
        }
    }

    static void runWave() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.start();
        }
    }

    static UUID[] getWaveList() {
        return Wave.getWaveList();
    }

    static int getWaveSize() {
        return Wave.getWaveList().length;
    }

    static boolean isAddedToTheWave(UUID uuid) {
        return Wave.getCommand(uuid) != null;
    }

    static void warnPlayer(Player p, String reason) {
        AwarenessNotifications.forcefullySend("The API method 'warnPlayer' has been removed.");
    }

    @Deprecated
    static void addPermission(Player p, Permission permission) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            AwarenessNotifications.forcefullySend("The API method 'addPermission' has been removed.");
        }
    }

    @Deprecated
    static void sendClientSidedBlock(Player p, Location loc, Material m, byte b) {
        AwarenessNotifications.forcefullySend("The API method 'sendClientSidedBlock' has been removed.");
    }

    @Deprecated
    static void destroyClientSidedBlock(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'destroyClientSidedBlock' has been removed.");
    }

    @Deprecated
    static void removeClientSidedBlocks(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'removeClientSidedBlocks' has been removed.");
    }

    @Deprecated
    static boolean containsClientSidedBlock(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'containsClientSidedBlock' has been removed.");
        return false;
    }

    @Deprecated
    static Material getClientSidedBlockMaterial(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'getClientSidedBlockMaterial' has been removed.");
        return null;
    }

    @Deprecated
    static byte getClientSidedBlockData(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'getClientSidedBlockData' has been removed.");
        return (byte) 0;
    }

    static String getConfiguredCheckName(HackType hackType) {
        return hackType.getCheck().getName();
    }

    static void setConfiguredCheckName(HackType hackType, String name) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            hackType.getCheck().setName(name);
        }
    }

    @Deprecated
    static void disableVelocityProtection(Player p, int ticks) {
        AwarenessNotifications.forcefullySend("The API method 'disableVelocityProtection' has been removed.");
    }

    @Deprecated
    static void setOnGround(Player p, int ticks) {
        AwarenessNotifications.forcefullySend("The API method 'setOnGround' has been removed.");
    }

    @Deprecated
    static int getMaxPunishmentViolation(HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getMaxPunishmentViolation' has been removed.");
        return 0;
    }

    @Deprecated
    static int getMinPunishmentViolation(HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getMinPunishmentViolation' has been removed.");
        return 0;
    }

    @Deprecated
    static boolean mayPunishPlayer(Player p, HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'mayPunishPlayer' has been removed.");
        return false;
    }
}
