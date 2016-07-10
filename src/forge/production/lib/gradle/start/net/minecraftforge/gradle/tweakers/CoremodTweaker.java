//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraftforge.gradle.tweakers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.gradle.GradleStartCommon;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoremodTweaker implements ITweaker {
      protected static final Logger LOGGER = LogManager.getLogger("GradleStart");
      private static final String COREMOD_CLASS = "fml.relauncher.CoreModManager";
      private static final String TWEAKER_SORT_FIELD = "tweakSorting";

      public CoremodTweaker() {
      }

      public void acceptOptions(List<String> options, File f1, File f2, String str) {
      }

      public void injectIntoClassLoader(LaunchClassLoader classLoader) {
            try {
                  Field loadPlugins = GradleStartCommon.getFmlClass("fml.relauncher.CoreModManager", classLoader).getDeclaredField("loadPlugins");
                  loadPlugins.setAccessible(true);
                  Class pluginWrapper = GradleStartCommon.getFmlClass("fml.relauncher.CoreModManager$FMLPluginWrapper", classLoader);
                  Constructor constructor = pluginWrapper.getConstructors()[0];
                  constructor.setAccessible(true);
                  Field[] declaredFields = pluginWrapper.getDeclaredFields();
                  Field f1 = declaredFields[1];
                  Field f2 = declaredFields[3];
                  Field f3 = declaredFields[2];
                  Field.setAccessible(pluginWrapper.getConstructors(), true);
                  Field.setAccessible(declaredFields, true);
                  List pluginList = (List)loadPlugins.get(null);

                  for(int i = 0; i < pluginList.size(); ++i) {
                        ITweaker tweaker = (ITweaker)pluginList.get(i);
                        if(pluginWrapper.isInstance(tweaker)) {
                              Object obj1 = f1.get(tweaker);
                              Object obj2 = f2.get(tweaker);
                              File coremodFile = GradleStartCommon.coreMap.get(obj1.getClass().getCanonicalName());
                              LOGGER.info("Injecting location in coremod {}", new Object[]{obj1.getClass().getCanonicalName()});
                              if(coremodFile != null && obj2 == null) {
                                    pluginList.set(i, constructor.newInstance(new Object[]{declaredFields[0].get(tweaker), obj1, coremodFile, Integer.valueOf(declaredFields[4].getInt(tweaker)), ((List)f3.get(tweaker)).toArray(new String[0])}));
                              }
                        }
                  }
            } catch (Throwable ex) {
                  LOGGER.log(Level.ERROR, "Something went wrong with the coremod adding.");
                  ex.printStackTrace();
            }

            String accessTransformerClassName = "net.minecraftforge.gradle.tweakers.AccessTransformerTweaker";
            ((List)Launch.blackboard.get("TweakClasses")).add(accessTransformerClassName);

            try {
                  Field tweakSorting = GradleStartCommon.getFmlClass("fml.relauncher.CoreModManager", classLoader).getDeclaredField("tweakSorting");
                  tweakSorting.setAccessible(true);
                  ((Map)tweakSorting.get(null)).put(accessTransformerClassName, Integer.valueOf(1001));
            } catch (Throwable ex) {
                  LOGGER.log(Level.ERROR, "Something went wrong with the adding the AT tweaker adding.");
                  ex.printStackTrace();
            }

      }

      public String getLaunchTarget() {
            return null;
      }

      public String[] getLaunchArguments() {
            return new String[0];
      }
}
