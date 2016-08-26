package me.libraryaddict.disguise.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.DisallowedDisguises;

public class EntityDisguiseCommand extends BaseDisguiseCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.getName().equals("CONSOLE")) {
            sender.sendMessage(ChatColor.RED + "You may not use this command from the console!");
            return true;
        }
                
        if(args.length >= 2) {
            if(args[1].contains(":")) {
                sender.sendMessage("That disguise is forbidden.");
                return true;
            }
        }
        Disguise disguise;
        try {
            disguise = parseDisguise(sender, args, getPermissions(sender));
        } catch (DisguiseParseException ex) {
            if (ex.getMessage() != null) {
                sender.sendMessage(ex.getMessage());
            }
            return true;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return true;
        }
        
       if (Arrays.toString(args).toLowerCase().contains("item_frame")) {
            sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
            return true;
        }
       
        if(Arrays.toString(args).toLowerCase().contains("itemframe")) {
            sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
            return true;
           
        }
        

        if (Arrays.toString(args).toLowerCase().contains("portal")) {
            sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
            return true;
        }
        
          if(Arrays.toString(args).toLowerCase().contains("hay_block")) {
            sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
            return true;
        }
        
           if(Arrays.toString(args).contains("fire")) {
              sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
          }
          
         if(Arrays.toString(args).contains("carrot")) {
            sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
            return true;
        }

        if (!DisallowedDisguises.disabled) {
            if (DisallowedDisguises.isAllowed(disguise)) {
                LibsDisguises.getInstance().getListener().setDisguiseEntity(sender.getName(), disguise);
            } else {
                sender.sendMessage(ChatColor.RED + "That disguise is forbidden.");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Disguises are disabled.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Right click a entity in the next " + DisguiseConfig.getDisguiseEntityExpire()
                + " seconds to disguise it as a " + disguise.getType().toReadable() + "!");
        return true;
    }

    /**
     * Send the player the information
     *
     * @param sender
     * @param map
     */
    @Override
    protected void sendCommandUsage(CommandSender sender, HashMap<DisguiseType, HashMap<ArrayList<String>, Boolean>> map) {
        ArrayList<String> allowedDisguises = getAllowedDisguises(map);
        sender.sendMessage(ChatColor.DARK_GREEN + "Choose a disguise then right click a entity to disguise it!");
        sender.sendMessage(ChatColor.DARK_GREEN + "You can use the disguises: " + ChatColor.GREEN
                + StringUtils.join(allowedDisguises, ChatColor.RED + ", " + ChatColor.GREEN));
        if (allowedDisguises.contains("player")) {
            sender.sendMessage(ChatColor.DARK_GREEN + "/disguiseentity player <Name>");
        }
        sender.sendMessage(ChatColor.DARK_GREEN + "/disguiseentity <DisguiseType> <Baby>");
        if (allowedDisguises.contains("dropped_item") || allowedDisguises.contains("falling_block")) {
            sender.sendMessage(ChatColor.DARK_GREEN + "/disguiseentity <Dropped_Item/Falling_Block> <Id> <Durability>");
        }
    }

}
