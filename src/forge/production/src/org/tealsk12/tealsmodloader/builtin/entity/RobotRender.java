package org.tealsk12.tealsmodloader.builtin.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.tealsk12.tealsmodloader.Common;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class RobotRender extends RenderLiving {

    private ResourceLocation textureLocation;

    public RobotRender(String location) {
        this(new ModelRobot(16), .25f, new ResourceLocation(Common.MOD_ID, "textures/entity/" + location));
    }

    protected RobotRender(ModelBase model, float shadowSize, ResourceLocation location) {
        super(model, shadowSize);
        this.textureLocation = location;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float tickTime) {
        GL11.glScalef(2f, 3f, 2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return textureLocation;
    }
}
