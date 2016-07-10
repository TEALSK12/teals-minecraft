package org.tealsk12.tealsmodloader.entity;

import net.minecraft.entity.EntityLiving;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public abstract class BaseEntityModifier {

    protected Class<? extends EntityLiving> target;

    public BaseEntityModifier(Class<? extends EntityLiving> target) {
        this.target = target;
    }

    public boolean willInject(EntityLiving entity) {
        return target.isAssignableFrom(entity.getClass());
    }

    public abstract void inject(EntityLiving entity);
}
