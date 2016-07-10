//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraftforge.gradle.tweakers;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class AccessTransformerTweaker implements ITweaker {

      public AccessTransformerTweaker() {
      }

      public void acceptOptions(List<String> options, File f1, File f2, String str) {
      }

      public void injectIntoClassLoader(LaunchClassLoader classLoader) {
            classLoader.registerTransformer("net.minecraftforge.gradle.GradleStartCommon$AccessTransformerTransformer");
      }

      public String getLaunchTarget() {
            return null;
      }

      public String[] getLaunchArguments() {
            return new String[0];
      }
}
