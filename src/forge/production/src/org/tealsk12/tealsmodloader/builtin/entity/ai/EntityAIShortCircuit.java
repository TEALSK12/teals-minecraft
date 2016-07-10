package org.tealsk12.tealsmodloader.builtin.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import org.tealsk12.tealsmodloader.builtin.entity.Robot;
import org.tealsk12.tealsmodloader.util.Sound;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityAIShortCircuit extends EntityAIBase {

    //Used to prevent entity from being damaged every tick (20 times per second).
    private int lastDamage = 0;
    private Robot robot;

    /**
     * Default short circuit constructor.
     *
     * @param robot
     */
    public EntityAIShortCircuit(Robot robot) {
        this.robot = robot;
    }

    /**
     * Called every tick as the task is running.
     */
    @Override
    public void updateTask() {
        if (lastDamage > 0) {
            --lastDamage;
            return;
        }

        robot.waterDamage();
        robot.worldObj.playSoundAtEntity(robot, Sound.MOB_IRONGOLEM_HIT.getSoundName(), 1, 1);

        //Damage set to every 20 ticks (1 second).
        lastDamage = 20;
    }

    /**
     * Only execute if the entity is inside of water.
     *
     * @return if the task should continue executing.
     */
    @Override
    public boolean continueExecuting() {
        return isInWater();
    }

    /**
     * Execute if the entity is inside of water.
     *
     * @return if the task should begin executing.
     */
    @Override
    public boolean shouldExecute() {
        return isInWater();
    }

    /**
     * Determines if the robot is inside of a form of water.
     *
     * @return wheather or not the robot is inside water.
     */
    private boolean isInWater() {
        Block at = robot.worldObj.getBlock((int) robot.posX, (int) robot.posY, (int) robot.posZ);
        return (at == Blocks.water || at == Blocks.flowing_water);
    }
}
