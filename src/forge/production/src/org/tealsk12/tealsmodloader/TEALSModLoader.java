package org.tealsk12.tealsmodloader;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import org.tealsk12.tealsmodloader.builtin.entity.RobotRender;
import org.tealsk12.tealsmodloader.commands.SpawnEntityCommand;
import org.tealsk12.tealsmodloader.entity.BaseEntityModifier;
import org.tealsk12.tealsmodloader.entity.EntityListener;
import org.tealsk12.tealsmodloader.entity.registry.EntitySpawnInfo;
import org.tealsk12.tealsmodloader.module.Module;
import org.tealsk12.tealsmodloader.util.EntityInfo;
import org.tealsk12.tealsmodloader.util.ReflectionUtil;
import org.tealsk12.tealsmodloader.util.registry.Registry;

import java.util.*;

/**
 * @author Connor Hollasch
 *         https://github.com/CHollasch
 */
@Mod(modid = Common.MOD_ID, name = Common.MOD_NAME, version = Common.VERSION)
public class TEALSModLoader {

    //================================================//

    /**
     * Accessor to mod instance (TEALSModLoader).
     */
    @Mod.Instance
    private static TEALSModLoader modInstance;

    /**
     * Default mod loader constructor, instantiated dynamically by Forge.
     */
    public TEALSModLoader() {
        modInstance = this;
    }

    /**
     * Accessor method for mod instance.
     *
     * @return TEALSModLoader mod instance
     */
    public static TEALSModLoader getModInstance() {
        return modInstance;
    }

    //================================================//

    private HashMap<String, EntityInfo> entityLookup = new HashMap<String, EntityInfo>();

    /**
     * Block registry, automatically registers new blocks to the GameRegistry.
     * Used by calling with a block identifier name, block instance, and an optional display name.
     */
    public Registry<Block> blockRegistry = new Registry<Block>() {
        @Override
        protected void register(String key, Block type, Object... args) {
            type.setBlockName(key);
            type.setBlockTextureName(Common.MOD_ID + ":" + key);

            GameRegistry.registerBlock(type, key);

            if (args != null && args.length > 0) {
                LanguageRegistry.addName(type, args[0].toString());
            }
        }
    };

    /**
     * Item registry, automatically registers new items to the GameRegistry.
     * Used by calling with an identifier name, item instance, and an optional display name.
     */
    public Registry<Item> itemRegistry = new Registry<Item>() {
        @Override
        protected void register(String key, Item type, Object... args) {
            type.setUnlocalizedName(key);
            type.setTextureName(Common.MOD_ID + ":" + key);

            GameRegistry.registerItem(type, key);

            if (args != null && args.length > 0) {
                LanguageRegistry.addName(type, args[0].toString());
            }
        }
    };

    /**
     * Entity registry, automatically registers new entities to the GameRegistry.
     * Used by calling with an identifier name, entity class, and an optional (in any order)... texture name or spawn info.
     */
    public Registry<Class<? extends EntityLiving>> entityRegistry = new Registry<Class<? extends EntityLiving>>() {
        @Override
        protected void register(String key, Class<? extends EntityLiving> type, Object... args) {
            int uniqueId = EntityRegistry.findGlobalUniqueEntityId();
            EntityRegistry.registerGlobalEntityID(type, key, uniqueId);

            if (args != null && args.length > 0) {
                for (Object argument : args) {
                    if (argument instanceof EntitySpawnInfo) {
                        EntitySpawnInfo info = (EntitySpawnInfo) argument;

                        EntityRegistry.addSpawn(type,
                                info.getWeightedSpawnChance(),
                                info.getMinPerPack(),
                                info.getMaxPerPack(),
                                info.getCreatureType(),
                                info.getBiomes().toArray(new BiomeGenBase[0]));
                    } else if (argument instanceof String) {
                        RenderingRegistry.registerEntityRenderingHandler(
                                type,
                                new RobotRender(argument.toString() + ".png")
                        );
                    }
                }
            }

            entityLookup.put(key, new EntityInfo(type, uniqueId));
        }
    };

    /**
     * Event registry, will register listener objects to the Minecraft Forge event bus.
     */
    public Registry<Object> listenerRegistry = new Registry<Object>() {
        @Override
        protected void register(String key, Object type, Object... args) {
            MinecraftForge.EVENT_BUS.register(type);
        }
    };

    private Collection<BaseEntityModifier> entityModifiers = new ArrayList<BaseEntityModifier>();

    /**
     * Server start method.
     * Called when a local server is created (when the client joins a world).
     *
     * @param event server start event.
     */
    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new SpawnEntityCommand());
    }

    /**
     * Client pre initialization event.
     * Called during the client loading phase.
     *
     * @param event pre initialization event.
     * @throws Exception due to dynamic mod loading mechanics using reflection.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        //Initalize listeners
        listenerRegistry.newInstance("entityListener", new EntityListener());

        System.out.println("Looking for modules... ");

        //Find all modules
        List<Class<Module>> classes = ReflectionUtil.getClassNamesFromPackage("tealsmc.mods", Module.class);

        for (Class c : classes) {
            Module create = (Module) c.newInstance();

            System.out.println("Loading module: " + c.getSimpleName());

            create.parentMod = this;
            create.onLoad();
        }

        System.out.println("Found " + classes.size());

        //Keep the world time forced to mid day.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().thePlayer != null) {
                    if (Minecraft.getMinecraft().getIntegratedServer().worldServers.length > 0) {
                        WorldServer worldServer = Minecraft.getMinecraft().getIntegratedServer().worldServers[0];
                        if (worldServer != null) {
                            worldServer.setWorldTime(6000);
                        }
                    }
                }
            }
        }, 0, 200);
    }

    /**
     * Used to register an entity modifier. Can change behavior of specific entities
     * in the game without changing or overriding their class.
     *
     * @param modifier entity modifier to register.
     */
    public void registerEntityModifier(BaseEntityModifier modifier) {
        entityModifiers.add(modifier);
    }

    /**
     * Retrieve all active entity modifiers.
     *
     * @return active entity modifiers.
     */
    public Collection<BaseEntityModifier> getEntityModifiers() {
        return entityModifiers;
    }

    /**
     * Get a list of all the custom entities that were registered.
     *
     * @return list of custom entities.
     */
    public Collection<String> getEntityNames() {
        return entityLookup.keySet();
    }

    /**
     * Get the map used to find custom entities by their identifier name.
     *
     * @return entity lookup map.
     */
    public HashMap<String, EntityInfo> getEntityLookup() {
        return entityLookup;
    }

    /**
     * Can be used to display debug messages in game chat.
     * Useful for debugging code without monitoring both the game and the IDE.
     *
     * @param message log message to display.
     */
    public static void log(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GOLD + "(INFO) " + EnumChatFormatting.GRAY + message));
        }
    }
}
