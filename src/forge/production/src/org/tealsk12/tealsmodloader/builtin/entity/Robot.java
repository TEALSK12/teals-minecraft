package org.tealsk12.tealsmodloader.builtin.entity;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.tealsk12.tealsmodloader.builtin.entity.ai.EntityAIShortCircuit;
import org.tealsk12.tealsmodloader.builtin.entity.ai.EntityAITargetNearest;
import org.tealsk12.tealsmodloader.entity.pathfinding.ForcePathNavigate;
import org.tealsk12.tealsmodloader.util.ReflectionUtil;
import org.tealsk12.tealsmodloader.util.Sound;

import java.lang.reflect.Field;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public abstract class Robot extends EntityMob {

    public static final float SPEED_FAST = 0.7f;
    public static final float SPEED_NORMAL = 0.5f;
    public static final float SPEED_SLOW = 0.35f;

    public Robot(World world) {
        super(world);

        try {
            //Bypass old navigator settings to force entity pathfinding to desired location
            Field navigator = ReflectionUtil.searchForField(getClass(), PathNavigate.class);
            navigator.setAccessible(true);
            navigator.set(this, new ForcePathNavigate(this, worldObj));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.tasks.addTask(0, new EntityAIShortCircuit(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAITargetNearest(this));
    }

    public boolean isAIEnabled() {
        return true;
    }

    protected String getLivingSound() {
        return rand.nextInt(10) >= 5 ? Sound.MOB_IRONGOLEM_THROW.getSoundName() : Sound.MOB_IRONGOLEM_WALK.getSoundName();
    }

    protected String getHurtSound() {
        return Sound.MOB_IRONGOLEM_HIT.getSoundName();
    }

    @Override
    protected String getDeathSound() {
        return Sound.MOB_IRONGOLEM_DEATH.getSoundName();
    }

    @Override
    protected float getSoundVolume() {
        return 0.5f;
    }

    @Override
    protected float getSoundPitch() {
        return 2f;
    }

    public void waterDamage() {
        damageEntity(new DamageSource("Water damage, malfunction indicated!"), 5f);
    }

    public void onEntityDamage(DamageSource source, float amount) {
        //Do nothing
    }
}
