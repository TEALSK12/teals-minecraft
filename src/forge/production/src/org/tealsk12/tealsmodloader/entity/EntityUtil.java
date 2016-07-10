package org.tealsk12.tealsmodloader.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import org.tealsk12.tealsmodloader.entity.pathfinding.ForcePathNavigate;
import org.tealsk12.tealsmodloader.util.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityUtil {

    public static ForcePathNavigate getEntityNavigation(EntityLiving entity) {
        if (entity.getNavigator() instanceof ForcePathNavigate)
            return ((ForcePathNavigate) entity.getNavigator());

        ForcePathNavigate nav = new ForcePathNavigate(entity, entity.worldObj);

        Field field = ReflectionUtil.searchForField(entity.getClass(), PathNavigate.class);
        field.setAccessible(true);
        try {
            field.set(entity, nav);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return nav;
    }
}
