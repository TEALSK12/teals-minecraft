package org.tealsk12.tealsmodloader.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import org.tealsk12.tealsmodloader.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntityAIModifier extends BaseEntityModifier {

    private Collection<AIModifier> aiModifiers = new ArrayList<AIModifier>();
    private Collection<Class<? extends EntityAIBase>> removedModifiers = new ArrayList<Class<? extends EntityAIBase>>();

    public EntityAIModifier(Class<? extends EntityLiving> target) {
        super(target);
    }

    public EntityAIModifier injectAITask(int priority, Class<? extends EntityAIBase> ai, Object... args) {
        aiModifiers.add(new AIModifier(priority, ai, args));
        return this;
    }

    public EntityAIModifier removeAITask(Class<? extends EntityAIBase> ai) {
        removedModifiers.add(ai);
        return this;
    }

    public void inject(EntityLiving entity) {
        removeTasks(entity);
        for (AIModifier modifier : aiModifiers) {
            Object[] args;
            if (modifier.args == null || modifier.args.length == 0)
                args = new Object[1];
            else {
                args = new Object[modifier.args.length + 1];
                for (int i = 1; i < args.length; i++) {
                    args[i] = modifier.args[i - 1];
                }
            }
            args[0] = entity;

            Class<? extends EntityAIBase> aiClass = modifier.ai;
            Constructor<?> matchingConstructor = ReflectionUtil.getConstructor(aiClass, args);

            if (matchingConstructor == null)
                continue;

            EntityAIBase ai = null;
            try {
                ai = (EntityAIBase) matchingConstructor.newInstance(args);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            entity.tasks.addTask(modifier.priority, ai);
        }
    }

    private void removeTasks(EntityLiving entity) {
        List globalTaskEntries = entity.tasks.taskEntries;
        globalTaskEntries.addAll(entity.targetTasks.taskEntries);

        List<EntityAITasks.EntityAITaskEntry> toRemove = new ArrayList<EntityAITasks.EntityAITaskEntry>();
        allTasks:
        for (Object remove : globalTaskEntries) {
            EntityAITasks.EntityAITaskEntry ai = (EntityAITasks.EntityAITaskEntry) remove;
            for (Class<? extends EntityAIBase> removed : removedModifiers) {
                if (ai.action.getClass().isAssignableFrom(removed)
                        || removed.isAssignableFrom(ai.action.getClass())) {
                    toRemove.add(ai);
                    continue allTasks;
                }
            }
        }

        //Iterate again afterwards because the list isn't a concurrent list
        for (EntityAITasks.EntityAITaskEntry ai : toRemove) {
            if (entity.tasks.taskEntries.contains(ai))
                entity.tasks.taskEntries.remove(ai);
        }

        //Make sure we try attack tasks too
        for (EntityAITasks.EntityAITaskEntry ai : toRemove) {
            if (entity.targetTasks.taskEntries.contains(ai))
                entity.targetTasks.taskEntries.remove(ai);
        }
    }

    protected class AIModifier {

        protected Class<? extends EntityAIBase> ai;
        protected int priority;
        protected Object[] args;

        public AIModifier(int priority, Class<? extends EntityAIBase> ai, Object[] args) {
            this.ai = ai;
            this.priority = priority;
            this.args = args;
        }
    }
}
