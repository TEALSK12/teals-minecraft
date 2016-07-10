package org.tealsk12.tealsmodloader.util;

import net.minecraft.entity.EntityLiving;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityInfo {

    private Class<? extends EntityLiving> entityClass;
    private int registeredId;

    public EntityInfo(Class<? extends EntityLiving> entityClass, int registeredId) {
        this.entityClass = entityClass;
        this.registeredId = registeredId;
    }

    public Class<? extends EntityLiving> getEntityClass() {
        return entityClass;
    }

    public int getRegisteredId() {
        return registeredId;
    }
}
