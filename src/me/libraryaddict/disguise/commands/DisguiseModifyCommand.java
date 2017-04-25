package me.libraryaddict.disguise.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.utilities.DisguiseParser;
import me.libraryaddict.disguise.utilities.DisguiseParser.DisguiseParseException;
import me.libraryaddict.disguise.utilities.DisguiseParser.DisguisePerm;
import me.libraryaddict.disguise.utilities.ReflectionFlagWatchers;
import me.libraryaddict.disguise.utilities.ReflectionFlagWatchers.ParamInfo;

public class DisguiseModifyCommand extends DisguiseBaseCommand implements TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Entity)) {
            sender.sendMessage(ChatColor.RED + "You may not use this command from the console!");
            return true;
        }

        HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> map = getPermissions(sender);

        if (map.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "You are forbidden to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendCommandUsage(sender, getPermissions(sender));
            return true;
        }

        Disguise disguise = DisguiseAPI.getDisguise((Player) sender, (Entity) sender);

        if (disguise == null) {
            sender.sendMessage(ChatColor.RED + "You are not disguised!");
            return true;
        }

        if (!map.containsKey(new DisguisePerm(disguise.getType()))) {
            sender.sendMessage(ChatColor.RED + "No permission to modify your disguise!");
            return true;
        }

        try {
            DisguiseParser.callMethods(sender, disguise, getPermissions(sender).get(new DisguisePerm(disguise.getType())),
                    new ArrayList<String>(), args);
        } catch (DisguiseParseException ex) {
            if (ex.getMessage() != null) {
                sender.sendMessage(ex.getMessage());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Your disguise has been modified!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] origArgs) {
        ArrayList<String> tabs = new ArrayList<String>();

        if (!(sender instanceof Player)) {
            return tabs;
        }

        Disguise disguise = DisguiseAPI.getDisguise((Player) sender, (Entity) sender);

        if (disguise == null) {
            return tabs;
        }

        String[] args = getArgs(origArgs);

        HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> perms = getPermissions(sender);

        DisguisePerm disguiseType = new DisguisePerm(disguise.getType());

        ArrayList<String> usedOptions = new ArrayList<String>();

        for (Method method : ReflectionFlagWatchers.getDisguiseWatcherMethods(disguiseType.getWatcherClass())) {
            for (int i = disguiseType.getType() == DisguiseType.PLAYER ? 2 : 1; i < args.length; i++) {
                String arg = args[i];

                if (!method.getName().equalsIgnoreCase(arg)) {
                    continue;
                }

                usedOptions.add(arg);
            }
        }

        if (passesCheck(sender, perms.get(disguiseType), usedOptions)) {
            boolean addMethods = true;

            if (args.length > 1) {
                String prevArg = args[args.length - 1];

                ParamInfo info = ReflectionFlagWatchers.getParamInfo(disguiseType, prevArg);

                if (info != null) {
                    if (info.getParamClass() != boolean.class) {
                        addMethods = false;
                    }

                    if (info.isEnums()) {
                        for (String e : info.getEnums(origArgs[origArgs.length - 1])) {
                            tabs.add(e);
                        }
                    } else {
                        if (info.getParamClass() == String.class) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                tabs.add(player.getName());
                            }
                        }
                    }
                }
            }

            if (addMethods) {
                // If this is a method, add. Else if it can be a param of the previous argument, add.
                for (Method method : ReflectionFlagWatchers.getDisguiseWatcherMethods(disguiseType.getWatcherClass())) {
                    tabs.add(method.getName());
                }
            }
        }

        return filterTabs(tabs, origArgs);
    }

    /**
     * Send the player the information
     */
    @Override
    protected void sendCommandUsage(CommandSender sender, HashMap<DisguisePerm, HashMap<ArrayList<String>, Boolean>> map) {
        ArrayList<String> allowedDisguises = getAllowedDisguises(map);
        sender.sendMessage(ChatColor.DARK_GREEN + "Modify your own disguise as you wear it!");
        sender.sendMessage(ChatColor.DARK_GREEN + "/disguisemodify setBaby true setSprinting true");
        sender.sendMessage(ChatColor.DARK_GREEN + "You can modify the disguises: " + ChatColor.GREEN
                + StringUtils.join(allowedDisguises, ChatColor.RED + ", " + ChatColor.GREEN));
    }
}
