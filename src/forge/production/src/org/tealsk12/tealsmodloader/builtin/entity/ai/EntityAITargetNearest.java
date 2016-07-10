package org.tealsk12.tealsmodloader.builtin.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import org.tealsk12.tealsmodloader.builtin.entity.Robot;

import java.util.List;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityAITargetNearest extends EntityAITarget {

    private EntityLivingBase target;

    public EntityAITargetNearest(Robot robot) {
        super(robot, false);
    }

    @Override
    public boolean shouldExecute() {
        double distance = this.getTargetDistance();
        List<EntityLivingBase> near = this.taskOwner.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class,
                this.taskOwner.boundingBox.expand(distance, 4.0D, distance), null);

        if (near.isEmpty()) {
            return false;
        } else {
            for (EntityLivingBase contained : near) {
                if (contained instanceof Robot) {
                    continue;
                }

                if (contained instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) contained;
                    if (player.capabilities.isCreativeMode) {
                        continue;
                    }
                }

                this.target = contained;
                return true;
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        taskOwner.setAttackTarget(target);
        super.startExecuting();
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }

    @Override
    public void resetTask() {
        target = null;
    }
}
