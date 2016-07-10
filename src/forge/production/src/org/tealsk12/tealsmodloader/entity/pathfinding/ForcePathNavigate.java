package org.tealsk12.tealsmodloader.entity.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class ForcePathNavigate extends PathNavigate {

    public ForcePathNavigate(EntityLiving navigator, World world) {
        super(navigator, world);
    }

    private boolean forcePath = false;

    @Override
    public void clearPathEntity() {
        if (forcePath)
            return;

        super.clearPathEntity();
    }

    @Override
    public PathEntity getPath() {
        PathEntity entity = super.getPath();

        if (forcePath || entity == null || entity.isFinished()) {
            forcePath = false;
        }

        return entity;
    }

    public void setAvoidSun(boolean avoidsSun) {
        super.setAvoidSun(avoidsSun);
    }

    public void setAvoidsWater(boolean avoidsWater) {
        super.setAvoidsWater(avoidsWater);
    }

    public void setBreakDoors(boolean breakDoors) {
        super.setBreakDoors(breakDoors);
    }

    public void setCanSwim(boolean canSwim) {
        super.setCanSwim(canSwim);
    }

    public void setEnterDoors(boolean enterDoors) {
        super.setEnterDoors(enterDoors);
    }

    public void setSpeed(double speed) {
        super.setSpeed(speed);
    }

    public boolean tryMoveToEntityLiving(Entity toMoveTo, double speed) {
        if (isForced()) {
            return false;
        }

        forcePath = true;
        return super.tryMoveToEntityLiving(toMoveTo, speed);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speed) {
        if (isForced()) {
            return false;
        }

        forcePath = true;
        return super.tryMoveToXYZ(x, y, z, speed);
    }

    public boolean setPath(PathEntity p_setPath_1_, double p_setPath_2_) {
        forcePath = super.setPath(p_setPath_1_, p_setPath_2_);
        return forcePath;
    }

    public PathEntity getPathToEntityLiving(Entity entityToGoTo) {
        return super.getPathToEntityLiving(entityToGoTo);
    }

    public PathEntity getPathToXYZ(double x, double y, double z) {
        return super.getPathToXYZ(x, y, z);
    }

    private boolean isForced() {
        getPath();
        return forcePath;
    }
}
