package org.tealsk12.tealsmodloader.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.IAttribute;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityAttributeModifier extends BaseEntityModifier {

    private HashMap<IAttribute, Double> attributes = new HashMap<IAttribute, Double>();

    public EntityAttributeModifier(Class<? extends EntityLiving> target) {
        super(target);
    }

    public EntityAttributeModifier addAttributeModifier(IAttribute attribute, Double value) {
        attributes.put(attribute, value);
        return this;
    }

    public void inject(EntityLiving entity) {
        for (IAttribute key : attributes.keySet()) {
            Double value = attributes.get(key);

            entity.getEntityAttribute(key).removeAllModifiers();
            entity.getEntityAttribute(key).setBaseValue(value);
        }
    }
}
