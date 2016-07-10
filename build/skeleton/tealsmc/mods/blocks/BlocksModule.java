package tealsmc.mods.blocks;

import org.tealsk12.tealsmodloader.module.Module;

/**
 * All TEALS mod blocks are registered here in order to be displayed in game.
 * All of the custom blocks you will create in the future should be registered
 * here.
 */
public class BlocksModule extends Module {

    public static final AmethystOre amethystOre = new AmethystOre();

    public void onLoad() {
        parentMod.blockRegistry.newInstance("amethyst_ore", amethystOre, "Amethyst Ore");
    }
}
