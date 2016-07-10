package org.tealsk12.tealsmodloader.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.tealsk12.tealsmodloader.TEALSModLoader;
import org.tealsk12.tealsmodloader.util.EntityInfo;

import java.util.*;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class SpawnEntityCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "spawnentity";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "spawnentity <name>";
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList(new String[]{"spawnmob", "createentity", "createentity", "robot", "spawnrobot"});
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            sendMessage(sender, "&cMust be a player to use this command.");
            return;
        }

        EntityPlayer player = (EntityPlayer) sender;

        if (args.length == 0) {
            sendMessage(player, "&cNo entity specified... Format: /spawnentity <name>.");
            return;
        }

        Map<String, EntityInfo> entityRegistry = TEALSModLoader.getModInstance().getEntityLookup();
        EntityInfo found = entityRegistry.get(args[0]);

        if (found == null) {
            sendMessage(player, "&cNo such custom entity \"" + args[0] + "\"");
            sendMessage(player, "&cPossible entities: ");

            if (entityRegistry.size() > 0) {
                for (Map.Entry<String, EntityInfo> infoEntry : entityRegistry.entrySet()) {
                    String name = infoEntry.getKey();
                    EntityInfo info = infoEntry.getValue();

                    sendMessage(player, " &f- &a" + name + " &7(&eID " + info.getRegisteredId() + "&7)");
                }
            } else {
                sendMessage(player, " &f- &aNo custom entities available.");
            }

            return;
        }

        Entity entity = EntityList.createEntityByID(found.getRegisteredId(), player.worldObj);

        if (entity == null) {
            sendMessage(player, "&cCould not spawn entity \"" + args[0] + "\" at your location.");
            return;
        }

        entity.setLocationAndAngles(player.posX, player.posY, player.posZ, 0f, 0f);

        player.worldObj.spawnEntityInWorld(entity);
        sendMessage(player, "&aSpawned entity at your location");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        if (args.length == 0) {

            Collection<String> names = TEALSModLoader.getModInstance().getEntityNames();
            List<String> list = new ArrayList<String>();
            list.addAll(names);

            return list;
        }

        return null;
    }

    private void sendMessage(ICommandSender sender, String message) {
        sender.addChatMessage(new ChatComponentText(message.replace("&", "§")));
    }
}
