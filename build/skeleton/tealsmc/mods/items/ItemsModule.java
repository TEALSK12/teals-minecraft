package tealsmc.mods.items;

import org.tealsk12.tealsmodloader.module.Module;

public class ItemsModule extends Module {

    public static final RockSifter rockSifter = new RockSifter();

    public void onLoad() {
        parentMod.itemRegistry.newInstance("rock_sifter", rockSifter, "Rock Sifter");
    }
}
