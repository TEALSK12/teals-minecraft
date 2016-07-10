package org.tealsk12.tealsmodloader.module;

import org.tealsk12.tealsmodloader.TEALSModLoader;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public abstract class Module {

    /**
     * Direct access to the TEALS Mod Loader
     */
    public TEALSModLoader parentMod;

    /**
     * Called when the module is loaded by the parent plugin
     */
    public abstract void onLoad();
}
