package com.vagdedes.spartan.functionality.command;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.abstraction.check.Check;
import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.abstraction.protocol.SpartanProtocol;
import com.vagdedes.spartan.functionality.connection.DiscordMemberCount;
import com.vagdedes.spartan.functionality.connection.cloud.CloudBase;
import com.vagdedes.spartan.functionality.connection.cloud.SpartanEdition;
import com.vagdedes.spartan.functionality.inventory.InteractiveInventory;
import com.vagdedes.spartan.functionality.moderation.Wave;
import com.vagdedes.spartan.functionality.notifications.DetectionNotifications;
import com.vagdedes.spartan.functionality.notifications.clickable.ClickableMessage;
import com.vagdedes.spartan.functionality.npc.NPCManager;
import com.vagdedes.spartan.functionality.server.Config;
import com.vagdedes.spartan.functionality.server.Permissions;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;
import com.vagdedes.spartan.functionality.server.TPS;
import com.vagdedes.spartan.utils.math.AlgebraUtils;
import com.vagdedes.spartan.utils.minecraft.server.ConfigUtils;
import com.vagdedes.spartan.utils.minecraft.server.ProxyUtils;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.Enums.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandExecution implements CommandExecutor {

    public static final int maxConnectedArgumentLength = 4096;
    public static final String support = "Click to receive plugin support.";

    private static void buildCommand(CommandSender sender, ChatColor chatColor, String command, String description) {
        ClickableMessage.sendCommand(sender, chatColor + command, description, command);
    }

    public static boolean spartanMessage(CommandSender sender,
                                         boolean isPlayer,
                                         boolean documentation) {
        if (!isPlayer || Permissions.has((Player) sender)) {
            sender.sendMessage("");
            int slots = CloudBase.getDetectionSlots();
            String command = "§2Spartan AntiCheat";
            command += "\n§8[ §7Detections Available§8: "
                    + (SpartanEdition.hasDetectionsPurchased(Check.DataType.JAVA) ? "§a" : "§c") + Check.DataType.JAVA
                    + " §8/ "
                    + (SpartanEdition.hasDetectionsPurchased(Check.DataType.BEDROCK) ? "§a" : "§c") + Check.DataType.BEDROCK
                    + " §8]";

            if (slots <= 0) {
                command += "\n§8[ §eDetection Slots§7: §6Unlimited §8]";
            } else {
                int players = SpartanBukkit.getPlayerCount();
                command += "\n§8[ §4Detection Slots§7: §c" + slots
                        + " (" + Math.max(slots - players, 0) + " remaining) §8]";
            }
            ClickableMessage.sendURL(
                    sender,
                    command,
                    "Click to learn how Detection Slots work!",
                    SpartanEdition.patreonURL
            );
            if (documentation) {
                ClickableMessage.sendURL(
                        sender,
                        "§8§l<> §7Required command argument",
                        "Click to learn how Detection Slots work!",
                        SpartanEdition.patreonURL
                );
                ClickableMessage.sendURL(
                        sender,
                        "§8§l[] §7Optional command argument",
                        "Click to learn how Detection Slots work!",
                        SpartanEdition.patreonURL
                );
            }
            return true;
        }
        sender.sendMessage(Config.messages.getColorfulString("unknown_command"));
        return false;
    }

    public static void completeMessage(CommandSender sender, String list) {
        boolean isPlayer = sender instanceof Player;
        SpartanPlayer player = isPlayer ? SpartanBukkit.getProtocol((Player) sender).spartanPlayer : null;
        isPlayer &= player != null;

        String command = Register.plugin.getName().toLowerCase();
        boolean info = !isPlayer || Permissions.has(player.getInstance(), Enums.Permission.INFO),
                manage = !isPlayer || Permissions.has(player.getInstance(), Enums.Permission.MANAGE);

        switch (list) {
            case "default":
                if (spartanMessage(sender, isPlayer, !isPlayer)) {
                    if (isPlayer) {
                        if (manage) {
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cPanic Mode §7(Click)",
                                    "Click this command to toggle silent mode and disable punishments for all checks.",
                                    "/" + command + " panic"
                            );
                        }
                        if (info || manage) {
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cInventory Menu §7(Click)",
                                    "Click this command to open the plugin's inventory menu.",
                                    "/" + command + " menu"
                            );
                        }
                        if (Permissions.has(player.getInstance(), Permission.RELOAD)) {
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cReload Plugin §7(Click)",
                                    "Click this command to reload the plugin's cache.",
                                    "/" + command + " reload"
                            );
                        }
                        if (info) {
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cPlayer Info §7(Click)",
                                    "Click this command to view useful information yourself.",
                                    "/" + command + " info"
                            );
                        }
                        if (manage) {
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cToggle Checks §7(Click)",
                                    "Click this command to toggle a check and its detections.",
                                    "/" + command + " manage-checks"
                            );
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cToggle Preventions §7(Click)",
                                    "Click this command to toggle a check's preventions.",
                                    "/" + command + " manage-checks"
                            );
                            ClickableMessage.sendCommand(
                                    sender,
                                    "§cToggle Punishments §7(Click)",
                                    "Click this command to toggle a check's punishments.",
                                    "/" + command + " manage-checks"
                            );
                        }
                    } else {
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " panic",
                                "This command can be used to enable silent mode and disable punishments for all checks.",
                                null
                        );
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " toggle <check>",
                                "This command can be used to toggle a check and its detections.",
                                null
                        );
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " toggle-prevention <check>",
                                "This command can be used to toggle a check's preventions.",
                                null
                        );
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " toggle-punishment <check>",
                                "This command can be used to toggle a check's punishments.",
                                null
                        );
                        buildCommand(
                                sender,
                                ChatColor.RED, "/" + command + " reload",
                                "Click this command to reload the plugin's cache."
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.USE_BYPASS)) {
                        ClickableMessage.sendCommand(
                                sender,
                                "§cPlayer Bypass §7(Click)",
                                "Click this command to give check bypass to a player.",
                                "/" + command + " bypass *"
                        );
                    }
                    if (!isPlayer
                            || info
                            || Permissions.has(player.getInstance(), Permission.KICK)
                            || Permissions.has(player.getInstance(), Permission.WARN)
                            || Permissions.has(player.getInstance(), Permission.USE_BYPASS)
                            || Permissions.has(player.getInstance(), Permission.WAVE)) {
                        buildCommand(
                                sender,
                                ChatColor.RED, "/" + command + " moderation",
                                "Click this command to view a list of moderation commands."
                        );
                    }
                }
                break;
            case "moderation":
                if (spartanMessage(sender, isPlayer, true)) {
                    boolean permission = false;

                    if (isPlayer && DetectionNotifications.hasPermission(player)) {
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " notifications [ticks-frequency]",
                                "This command can be used to receive chat messages whenever a player is suspected of using hack modules.", null);
                    }
                    if (isPlayer && info) {
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " info [player]",
                                "This command can be used to view useful information about a player.",
                                null
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.USE_BYPASS)) {
                        permission = true;
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " bypass <player> <check> [seconds]",
                                "This command can be used to cause a player to temporarily bypass a check and its detections.",
                                null
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.WARN)) {
                        permission = true;
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " warn <player> <reason>",
                                "This command can be used to individually warn a player about something important.",
                                null
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.KICK)) {
                        permission = true;
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " kick <player> <reason>",
                                "This command can be used to kick players from the server for a specific reason.",
                                null
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.WAVE)) {
                        permission = true;
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " wave <add/remove/clear/run/list> [player] [command]",
                                "This command can be used to add a player to a list with a command representing their punishment. " +
                                        "This list can be executed manually by a player or automatically based on the plugin's configuration, " +
                                        "and cause added players to punished all at once and in order."
                                        + "\n\n"
                                        + "Example: /" + command + " wave add playerName ban {player} You have been banned for hacking!",
                                null
                        );
                    }
                    if (!isPlayer
                            || Permissions.has(player.getInstance(), Permission.ADMIN)) {
                        permission = true;
                        ClickableMessage.sendCommand(
                                sender,
                                ChatColor.RED + "/" + command + " proxy-command <command>",
                                "This command can be used to transfer commands to the proxy/network of servers. (Example: BungeeCord)",
                                null
                        );
                    }

                    if (!permission) {
                        completeMessage(sender, "default");
                    }
                }
                break;
            case "conditions":
                if (spartanMessage(sender, isPlayer, true)) {
                    if (!isPlayer || Permissions.has(player.getInstance(), Permission.CONDITION)) {
                        sender.sendMessage(ChatColor.RED + "/" + command + " <player> if <condition> equals <result> do <command>");
                        sender.sendMessage(ChatColor.RED + "/" + command + " <player> if <condition> contains <result> do <command>");
                        sender.sendMessage(ChatColor.RED + "/" + command + " <player> if <number> is-less-than <result> do <command>");
                        sender.sendMessage(ChatColor.RED + "/" + command + " <player> if <number> is-greater-than <result> do <command>");
                    } else {
                        completeMessage(sender, "default");
                    }
                }
                break;
            default:
                break;
        }
    }

    public static int num(final String s) {
        return Integer.parseInt(s);
    }

    public static double dbl(final String s) {
        return Double.parseDouble(s);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (label.equalsIgnoreCase(Register.plugin.getName()) && (isPlayer || sender instanceof ConsoleCommandSender)) {
            SpartanPlayer player = isPlayer ? SpartanBukkit.getProtocol((Player) sender).spartanPlayer : null;

            if (isPlayer && player == null) {
                return false;
            }
            if (args.length == 0) {
                if (isPlayer) {
                    if (NPCManager.supported && Permissions.isStaff(player.getInstance())) {
                        if (Config.settings.getBoolean("Important.enable_npc")) {
                            NPCManager.create(player);
                        } else {
                            InteractiveInventory.mainMenu.open(player, false);
                        }
                    } else {
                        InteractiveInventory.mainMenu.open(player, false);
                    }
                }
                completeMessage(sender, "default");
            } else if (args.length == 1) {
                if (isPlayer && args[0].equalsIgnoreCase("Menu")) {
                    InteractiveInventory.mainMenu.open(player);
                } else if (isPlayer && args[0].equalsIgnoreCase("Manage-Checks")) {
                    InteractiveInventory.manageChecks.open(player);
                } else if (args[0].equalsIgnoreCase("Panic")) {
                    if (isPlayer && !Permissions.has(player.getInstance(), Permission.MANAGE)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("no_permission"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    Check.panic = !Check.panic;

                    if (Check.panic) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("panic_mode_enable"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                    } else {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("panic_mode_disable"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                    }
                } else if (args[0].equalsIgnoreCase("Moderation")) {
                    completeMessage(sender, args[0].toLowerCase());

                } else if (args[0].equalsIgnoreCase("Conditions")) {
                    completeMessage(sender, args[0].toLowerCase());

                } else if (args[0].equalsIgnoreCase("Reload") || args[0].equalsIgnoreCase("Rl")) {
                    if (isPlayer && !Permissions.has(player.getInstance(), Permission.RELOAD)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("no_permission"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    Config.reload(sender);

                } else if (isPlayer && args[0].equalsIgnoreCase("Info")) {
                    if (!Permissions.has(player.getInstance(), Permission.INFO)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("no_permission"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    InteractiveInventory.playerInfo.open(player, sender.getName());

                } else if (isPlayer && args[0].equalsIgnoreCase("Notifications")) {
                    if (!DetectionNotifications.hasPermission(player)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("no_permission"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    DetectionNotifications.set(player, DetectionNotifications.defaultFrequency);

                } else {
                    completeMessage(sender, "default");
                }
            } else {
                if (args[0].equalsIgnoreCase("Proxy-Command")) {
                    if (isPlayer && !Permissions.has(player.getInstance(), Permission.ADMIN)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("no_permission"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    StringBuilder argumentsToStringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        argumentsToStringBuilder.append(args[i]).append(" ");
                    }
                    String argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);

                    if (isPlayer ? argumentsToString.length() > player.getMaxChatLength() : argumentsToString.length() > maxConnectedArgumentLength) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("massive_command_reason"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    if (!ProxyUtils.executeCommand(isPlayer ? player.getInstance() : null, argumentsToString)) {
                        ClickableMessage.sendURL(
                                sender,
                                Config.messages.getColorfulString("failed_command"),
                                support,
                                DiscordMemberCount.discordURL
                        );
                        return true;
                    }
                    ClickableMessage.sendURL(
                            sender,
                            Config.messages.getColorfulString("successful_command"),
                            support,
                            DiscordMemberCount.discordURL
                    );
                } else {
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("Wave")) {
                            String command = args[1];

                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.WAVE)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (command.equalsIgnoreCase("Run")) {
                                if (Wave.getWaveList().length == 0) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("empty_wave_list"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                if (!Wave.start()) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("failed_command"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                }
                            } else if (command.equalsIgnoreCase("Clear")) {
                                Wave.clear();
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("wave_clear_message"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            } else if (command.equalsIgnoreCase("List")) {
                                sender.sendMessage(ChatColor.GRAY + "Wave Queued Players" + ChatColor.DARK_GRAY + ":");
                                sender.sendMessage(Wave.getWaveListString());
                            } else {
                                completeMessage(sender, "moderation");
                            }

                        } else if (isPlayer && args[0].equalsIgnoreCase("Info")) {
                            if (!Permissions.has(player.getInstance(), Permission.INFO)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            InteractiveInventory.playerInfo.open(player, ConfigUtils.replaceWithSyntax(args[1], null));

                        } else if (args[0].equalsIgnoreCase("Toggle")) {
                            String check = args[1];

                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.MANAGE)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            boolean exists = false;

                            for (Enums.HackType hackType : Enums.HackType.values()) {
                                if (hackType.getCheck().getName().equalsIgnoreCase(check)) {
                                    check = hackType.toString();
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                Enums.HackType type = Enums.HackType.valueOf(check);
                                Check checkObj = type.getCheck();

                                if (checkObj.isEnabled(null, null)) {
                                    checkObj.setEnabled(null, false);
                                    String message = Config.messages.getColorfulString("check_disable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                } else {
                                    checkObj.setEnabled(null, true);
                                    String message = Config.messages.getColorfulString("check_enable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                }
                            } else {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("non_existing_check"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }
                        } else if (args[0].equalsIgnoreCase("Toggle-Prevention")) {
                            String check = args[1];

                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.MANAGE)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            boolean exists = false;

                            for (Enums.HackType hackType : Enums.HackType.values()) {
                                if (hackType.getCheck().getName().equalsIgnoreCase(check)) {
                                    check = hackType.toString();
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                Enums.HackType type = Enums.HackType.valueOf(check);
                                Check checkObj = type.getCheck();

                                if (checkObj.isSilent(null, null)) {
                                    checkObj.setSilent(null, false);
                                    String message = Config.messages.getColorfulString("check_silent_disable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                } else {
                                    checkObj.setSilent(null, true);
                                    String message = Config.messages.getColorfulString("check_silent_enable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                }
                            } else {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("non_existing_check"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }
                        } else if (args[0].equalsIgnoreCase("Toggle-Punishment")) {
                            String check = args[1];

                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.MANAGE)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            boolean exists = false;

                            for (Enums.HackType hackType : Enums.HackType.values()) {
                                if (hackType.getCheck().getName().equalsIgnoreCase(check)) {
                                    check = hackType.toString();
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                Enums.HackType type = Enums.HackType.valueOf(check);
                                Check checkObj = type.getCheck();

                                if (checkObj.canPunish(null)) {
                                    checkObj.setPunish(null, false);
                                    String message = Config.messages.getColorfulString("check_punishment_disable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                } else {
                                    checkObj.setPunish(null, true);
                                    String message = Config.messages.getColorfulString("check_punishment_enable_message");
                                    message = isPlayer
                                            ? ConfigUtils.replaceWithSyntax((Player) sender, message, type)
                                            : ConfigUtils.replaceWithSyntax(message, type);
                                    sender.sendMessage(message);
                                }
                            } else {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("non_existing_check"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }

                        } else if (isPlayer && args[0].equalsIgnoreCase("Notifications")) {
                            if (!DetectionNotifications.hasPermission(player)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            String divisorString = args[1];

                            if (AlgebraUtils.validInteger(divisorString)) {
                                int frequency = Integer.parseInt(divisorString);

                                if (frequency >= 0) {
                                    DetectionNotifications.set(player, frequency);
                                } else {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("failed_command"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                }
                            } else {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("failed_command"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }

                        } else {
                            completeMessage(sender, "default");
                        }
                    } else { // 3 or more arguments
                        StringBuilder argumentsToStringBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            argumentsToStringBuilder.append(args[i]).append(" ");
                        }
                        String argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);

                        if (args[0].equalsIgnoreCase("Kick")) {
                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.KICK)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (isPlayer ? argumentsToString.length() > player.getMaxChatLength() : argumentsToString.length() > maxConnectedArgumentLength) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("massive_command_reason"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            SpartanProtocol t = SpartanBukkit.getProtocol(args[1]);

                            if (t == null) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("player_not_found_message"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (!t.spartanPlayer.punishments.kick(sender, argumentsToString)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("failed_command"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }

                        } else if (args[0].equalsIgnoreCase("Warn")) {
                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.WARN)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (isPlayer ? argumentsToString.length() > player.getMaxChatLength() : argumentsToString.length() > maxConnectedArgumentLength) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("massive_command_reason"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            SpartanProtocol t = SpartanBukkit.getProtocol(args[1]);

                            if (t == null) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("player_not_found_message"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (!t.spartanPlayer.punishments.warn(sender, argumentsToString)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("failed_command"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                            }

                        } else if (args[0].equalsIgnoreCase("Bypass")) {
                            boolean noSeconds = args.length == 3;

                            if (noSeconds || args.length == 4) {
                                Enums.HackType[] hackTypes = Enums.HackType.values();
                                int maxHackTypes = hackTypes.length;
                                String[] checks = args[2].split(",", maxHackTypes);
                                String sec = noSeconds ? null : args[3];

                                if (isPlayer && !Permissions.has(player.getInstance(), Permission.USE_BYPASS)) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("no_permission"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                SpartanProtocol t = SpartanBukkit.getProtocol(args[1]);

                                if (t == null) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("player_not_found_message"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                List<Enums.HackType> found = new ArrayList<>(maxHackTypes);

                                for (String check : checks) {
                                    for (Enums.HackType hackType : hackTypes) {
                                        if (hackType.getCheck().getName().equalsIgnoreCase(check)) {
                                            found.add(hackType);
                                            break;
                                        }
                                    }
                                }
                                if (!found.isEmpty()) {
                                    for (Enums.HackType hackType : found) {
                                        int seconds = noSeconds ? 0 : Integer.parseInt(sec);

                                        if (noSeconds) {
                                            t.spartanPlayer.getExecutor(hackType).addDisableCause("Command-" + sender.getName(), null, 0);
                                        } else {
                                            if (seconds < 1 || seconds > 3600) {
                                                sender.sendMessage(ChatColor.RED + "Seconds must be between 1 and 3600.");
                                                return true;
                                            }
                                            t.spartanPlayer.getExecutor(hackType).addDisableCause("Command-" + sender.getName(), null, seconds * ((int) TPS.maximum));
                                        }
                                        String message = ConfigUtils.replaceWithSyntax(t.spartanPlayer, Config.messages.getColorfulString("bypass_message"), hackType)
                                                .replace("{time}", noSeconds ? "infinite" : String.valueOf(seconds));
                                        sender.sendMessage(message);
                                    }
                                } else {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("non_existing_check"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                }
                            } else {
                                completeMessage(sender, "moderation");
                            }
                        } else if (args[0].equalsIgnoreCase("Wave")) {
                            String command = args[1];
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[2]);

                            if (isPlayer && !Permissions.has(player.getInstance(), Permission.WAVE)) {
                                ClickableMessage.sendURL(
                                        sender,
                                        Config.messages.getColorfulString("no_permission"),
                                        support,
                                        DiscordMemberCount.discordURL
                                );
                                return true;
                            }
                            if (command.equalsIgnoreCase("add") && args.length >= 4) {
                                if (Wave.getWaveList().length >= 100) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("full_wave_list"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                argumentsToStringBuilder = new StringBuilder();
                                for (int i = 3; i < args.length; i++) {
                                    argumentsToStringBuilder.append(args[i]).append(" ");
                                }
                                argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);

                                if (isPlayer ? argumentsToString.length() > player.getMaxChatLength() : argumentsToString.length() > maxConnectedArgumentLength) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("massive_command_reason"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                String message = Config.messages.getColorfulString("wave_add_message");
                                message = ConfigUtils.replaceWithSyntax(t, message, null);
                                sender.sendMessage(message);
                                Wave.add(t.getUniqueId(), argumentsToString); // After to allow for further messages to take palce
                            } else if (command.equalsIgnoreCase("remove")) {
                                UUID uuid = t.getUniqueId();

                                if (Wave.getCommand(uuid) == null) {
                                    String message = Config.messages.getColorfulString("wave_not_added_message");
                                    message = ConfigUtils.replaceWithSyntax(t, message, null);
                                    sender.sendMessage(message);
                                    return true;
                                }
                                Wave.remove(uuid);
                                String message = Config.messages.getColorfulString("wave_remove_message");
                                message = ConfigUtils.replaceWithSyntax(t, message, null);
                                sender.sendMessage(message);
                            } else {
                                completeMessage(sender, "moderation");
                            }

                        } else if (args.length >= 4) {
                            argumentsToStringBuilder = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                argumentsToStringBuilder.append(args[i]).append(" ");
                            }
                            argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);

                            if (args.length >= 7) {
                                if (isPlayer && !Permissions.has(player.getInstance(), Permission.CONDITION)) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("no_permission"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                SpartanProtocol t = SpartanBukkit.getProtocol(args[0]);

                                if (t == null) {
                                    ClickableMessage.sendURL(
                                            sender,
                                            Config.messages.getColorfulString("player_not_found_message"),
                                            support,
                                            DiscordMemberCount.discordURL
                                    );
                                    return true;
                                }
                                if (args[1].equalsIgnoreCase("if") && args[5].equalsIgnoreCase("do")) {
                                    final String condition = ConfigUtils.replaceWithSyntax(t.spartanPlayer, args[2], null);
                                    final String result = ConfigUtils.replaceWithSyntax(t.spartanPlayer, args[4], null);

                                    argumentsToStringBuilder = new StringBuilder();
                                    for (int i = 6; i < args.length; i++) {
                                        argumentsToStringBuilder.append(args[i]).append(" ");
                                    }
                                    final String command = ConfigUtils.replaceWithSyntax(t.spartanPlayer, argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1), null);

                                    switch (args[3].toLowerCase()) {
                                        case "equals":
                                        case "=":
                                            if (condition.equalsIgnoreCase(result)) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            }
                                            break;
                                        case "not-equals":
                                        case "/=":
                                            if (!condition.equalsIgnoreCase(result)) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            }
                                            break;
                                        case "contains":
                                            if (condition.contains(result)) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            }
                                            break;
                                        case "is-less-than":
                                        case "<":
                                            if (AlgebraUtils.validInteger(condition) && AlgebraUtils.validInteger(result) && num(condition) < num(result)
                                                    || AlgebraUtils.validDecimal(condition) && AlgebraUtils.validDecimal(result) && dbl(condition) < dbl(result)
                                                    || AlgebraUtils.validInteger(condition) && AlgebraUtils.validDecimal(result) && num(condition) < dbl(result)
                                                    || AlgebraUtils.validDecimal(condition) && AlgebraUtils.validInteger(result) && dbl(condition) < num(result)) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            }
                                            break;
                                        case "is-greater-than":
                                        case ">":
                                            if (AlgebraUtils.validInteger(condition) && AlgebraUtils.validInteger(result) && num(condition) > num(result)
                                                    || AlgebraUtils.validDecimal(condition) && AlgebraUtils.validDecimal(result) && dbl(condition) > dbl(result)
                                                    || AlgebraUtils.validInteger(condition) && AlgebraUtils.validDecimal(result) && num(condition) > dbl(result)
                                                    || AlgebraUtils.validDecimal(condition) && AlgebraUtils.validInteger(result) && dbl(condition) > num(result)) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } else {
                                completeMessage(sender, "default");
                            }
                        } else {
                            completeMessage(sender, "default");
                        }
                    }
                }
            }
        }
        return false;
    }
}
