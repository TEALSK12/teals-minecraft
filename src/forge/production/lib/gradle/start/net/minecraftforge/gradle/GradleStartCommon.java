//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraftforge.gradle;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GradleStartCommon {
      protected static Logger LOGGER = LogManager.getLogger("GradleStart");
      private static final String NO_CORE_SEARCH = "noCoreSearch";
      private Map<String, String> argMap = Maps.newHashMap();
      private List<String> extras = Lists.newArrayList();

      private static final File SRG_DIR;
      private static final File SRG_NOTCH_SRG;
      private static final File SRG_NOTCH_MCP;
      private static final File SRG_SRG_MCP;
      private static final File SRG_MCP_SRG;
      private static final File SRG_MCP_NOTCH;
      private static final File CSV_DIR;

      static {
            String sep = File.separator;
            String srgDir = sep + "lib" + sep + "gradle" + sep + "srgs" + sep;

            SRG_DIR = new File("", srgDir);
            SRG_NOTCH_SRG = new File("", srgDir + "notch-srg.srg");
            SRG_NOTCH_MCP = new File("", srgDir + "notch-mcp.srg");
            SRG_SRG_MCP = new File("", srgDir + "srg-mcp.srg");
            SRG_MCP_SRG = new File("", srgDir + "mcp-srg.srg");
            SRG_MCP_NOTCH = new File("", srgDir + "mcp-notch.srg");
            CSV_DIR = new File("", sep + "lib" + sep + "gradle" + sep + "conf");
      }

      private static final String MC_VERSION = "1.7.10";
      private static final String FML_PACK_OLD = "cpw.mods";
      private static final String FML_PACK_NEW = "net.minecraftforge";
      private static final String COREMOD_VAR = "fml.coreMods.load";
      private static final String COREMOD_MF = "FMLCorePlugin";
      private static final String MOD_ATD_CLASS = "fml.common.asm.transformers.ModAccessTransformer";
      private static final String MOD_AT_METHOD = "addJar";

      public static final Map<String, File> coreMap = Maps.newHashMap();

      public GradleStartCommon() {
      }

      protected abstract void setDefaultArguments(Map<String, String> propertymap);

      protected abstract void preLaunch(Map<String, String> propertyMap, List<String> extras);

      protected abstract String getBounceClass();

      protected abstract String getTweakClass();

      protected void launch(String[] args) throws Throwable {
            System.setProperty("net.minecraftforge.gradle.GradleStart.srgDir", SRG_DIR.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg", SRG_NOTCH_SRG.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.srg.notch-mcp", SRG_NOTCH_MCP.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp", SRG_SRG_MCP.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.srg.mcp-srg", SRG_MCP_SRG.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.srg.mcp-notch", SRG_MCP_NOTCH.getCanonicalPath());
            System.setProperty("net.minecraftforge.gradle.GradleStart.csvDir", CSV_DIR.getCanonicalPath());
            this.setDefaultArguments(this.argMap);
            this.parseArgs(args);
            this.preLaunch(this.argMap, this.extras);
            System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
            if(this.argMap.get("noCoreSearch") == null) {
                  this.searchCoremods();
            } else {
                  LOGGER.info("GradleStart coremod searching disabled!");
            }

            args = this.getArgs();
            this.argMap = null;
            this.extras = null;
            System.gc();
            String bounceClass = this.getBounceClass();
            if(bounceClass.endsWith("launchwrapper.Launch")) {
                  Launch.main(args);
            } else {
                  Class.forName(this.getBounceClass()).getDeclaredMethod("main", new Class[]{String[].class}).invoke((Object)null, new Object[]{args});
            }

      }

      private String[] getArgs() {
            ArrayList argList = new ArrayList(22);
            Iterator argListIterator = this.argMap.entrySet().iterator();

            while(argListIterator.hasNext()) {
                  Entry argumentEntry = (Entry)argListIterator.next();
                  String argumentValue = (String)argumentEntry.getValue();
                  if(!Strings.isNullOrEmpty(argumentValue)) {
                        argList.add("--" + argumentEntry.getKey());
                        argList.add(argumentValue);
                  }
            }

            if(!Strings.isNullOrEmpty(this.getTweakClass())) {
                  argList.add("--tweakClass");
                  argList.add(this.getTweakClass());
            }

            if(this.extras != null) {
                  argList.addAll(this.extras);
            }

            String[] argArray = (String[])argList.toArray(new String[0]);
            StringBuilder argumentString = new StringBuilder();
            argumentString.append('[');

            for(int i = 0; i < argArray.length; ++i) {
                  argumentString.append(argArray[i]).append(", ");
                  if("--accessToken".equalsIgnoreCase(argArray[i])) {
                        argumentString.append("{REDACTED}, ");
                        ++i;
                  }
            }

            argumentString.replace(argumentString.length() - 2, argumentString.length(), "");
            argumentString.append(']');
            LOGGER.info("Running with arguments: " + argumentString.toString());
            return argArray;
      }

      private void parseArgs(String[] arguments) {
            OptionParser optionParser = new OptionParser();
            optionParser.allowsUnrecognizedOptions();
            Iterator argumentIterator = this.argMap.keySet().iterator();

            while(argumentIterator.hasNext()) {
                  String argument = (String)argumentIterator.next();
                  optionParser.accepts(argument).withRequiredArg().ofType(String.class);
            }

            optionParser.accepts("noCoreSearch");
            NonOptionArgumentSpec argumentSpec = optionParser.nonOptions();
            OptionSet optionSet = optionParser.parse(arguments);
            argumentIterator = this.argMap.keySet().iterator();

            while(argumentIterator.hasNext()) {
                  String argument = (String)argumentIterator.next();
                  if(optionSet.hasArgument(argument)) {
                        String option = (String)optionSet.valueOf(argument);
                        this.argMap.put(argument, option);
                        if(!"password".equalsIgnoreCase(argument)) {
                              LOGGER.info(argument + ": " + option);
                        }
                  }
            }

            if(optionSet.has("noCoreSearch")) {
                  this.argMap.put("noCoreSearch", "");
            }

            this.extras = Lists.newArrayList(argumentSpec.values(optionSet));
            LOGGER.info("Extra: " + this.extras);
      }

      protected static Class getFmlClass(String fmlClassName) throws ClassNotFoundException {
            return getFmlClass(fmlClassName, GradleStartCommon.class.getClassLoader());
      }

      public static Class getFmlClass(String fmlClassName, ClassLoader classLoader) throws ClassNotFoundException {
            if(!fmlClassName.startsWith("fml")) {
                  throw new IllegalArgumentException("invalid FML classname");
            } else {
                  if("1.7.10".startsWith("1.7")) {
                        fmlClassName = "cpw.mods." + fmlClassName;
                  } else {
                        fmlClassName = "net.minecraftforge." + fmlClassName;
                  }

                  return Class.forName(fmlClassName, true, classLoader);
            }
      }

      private void searchCoremods() throws Exception {
            Method method = null;

            try {
                  method = getFmlClass("fml.common.asm.transformers.ModAccessTransformer").getDeclaredMethod("addJar", new Class[]{JarFile.class});
            } catch (Throwable ex) {}

            URL[] potentialCoremods = ((URLClassLoader)GradleStartCommon.class.getClassLoader()).getURLs();
            int urlArrayLength = potentialCoremods.length;

            for(int i = 0; i < urlArrayLength; ++i) {
                  URL url = potentialCoremods[i];
                  if(url.getProtocol().startsWith("file")) {
                        File file = new File(url.toURI().getPath());
                        Manifest manifest = null;
                        if(file.exists()) {
                              if(file.isDirectory()) {
                                    File metaFile = new File(file, "META-INF/MANIFEST.MF");
                                    if(metaFile.exists()) {
                                          FileInputStream fileInputStream = new FileInputStream(metaFile);
                                          manifest = new Manifest(fileInputStream);
                                          fileInputStream.close();
                                    }
                              } else if(file.getName().endsWith("jar")) {
                                    JarFile jarFile = new JarFile(file);
                                    manifest = jarFile.getManifest();
                                    if(method != null && manifest != null) {
                                          method.invoke(null, new Object[]{jarFile});
                                    }

                                    jarFile.close();
                              }

                              if(manifest != null) {
                                    String coremodPlugin = manifest.getMainAttributes().getValue("FMLCorePlugin");
                                    if(!Strings.isNullOrEmpty(coremodPlugin)) {
                                          LOGGER.info("Found and added coremod: " + coremodPlugin);
                                          coreMap.put(coremodPlugin, file);
                                    }
                              }
                        }
                  }
            }

            HashSet coremods = Sets.newHashSet();
            if(!Strings.isNullOrEmpty(System.getProperty("fml.coreMods.load"))) {
                  coremods.addAll(Splitter.on(',').splitToList(System.getProperty("fml.coreMods.load")));
            }

            coremods.addAll(coreMap.keySet());
            System.setProperty("fml.coreMods.load", Joiner.on(',').join(coremods));
            if(!Strings.isNullOrEmpty(this.getTweakClass())) {
                  this.extras.add("--tweakClass");
                  this.extras.add("net.minecraftforge.gradle.tweakers.CoremodTweaker");
            }

      }

      public static final class AccessTransformerTransformer implements IClassTransformer {
            public AccessTransformerTransformer() {
                  this.doStuff((LaunchClassLoader)this.getClass().getClassLoader());
            }

            private void doStuff(LaunchClassLoader launchClassLoader) {
                  Class launchClass = null;
                  IClassTransformer classTransformer = null;
                  Iterator classIterator = launchClassLoader.getTransformers().iterator();

                  IClassTransformer transformer;
                  while(classIterator.hasNext()) {
                        transformer = (IClassTransformer)classIterator.next();
                        if(transformer.getClass().getCanonicalName().endsWith("fml.common.asm.transformers.ModAccessTransformer")) {
                              launchClass = transformer.getClass();
                              classTransformer = transformer;
                        }
                  }

                  if(launchClass != null && classTransformer != null) {
                        Collection launchClassValues;
                        Field field;
                        try {
                              field = launchClass.getSuperclass().getDeclaredFields()[1];
                              field.setAccessible(true);
                              launchClassValues = ((Multimap)field.get(classTransformer)).values();
                        } catch (Throwable ex) {
                              GradleStartCommon.LOGGER.log(Level.ERROR, "AccessTransformer.modifiers field was somehow not found...");
                              return;
                        }

                        if(!launchClassValues.isEmpty()) {
                              transformer = null;

                              Iterator iterator;
                              Object modifier;
                              try {
                                    Object currentModifier = null;
                                    iterator = launchClassValues.iterator();
                                    if(iterator.hasNext()) {
                                          modifier = iterator.next();
                                          currentModifier = modifier;
                                    }

                                    field = currentModifier.getClass().getFields()[0];
                                    field.setAccessible(true);
                              } catch (Throwable ex) {
                                    GradleStartCommon.LOGGER.log(Level.ERROR, "AccessTransformer.Modifier.name field was somehow not found...");
                                    return;
                              }

                              HashMap transformed = Maps.newHashMap();

                              try {
                                    this.readCsv(new File(GradleStartCommon.CSV_DIR, "fields.csv"), transformed);
                                    this.readCsv(new File(GradleStartCommon.CSV_DIR, "methods.csv"), transformed);
                              } catch (IOException ex) {
                                    GradleStartCommon.LOGGER.log(Level.ERROR, "Could not load CSV files!");
                                    ex.printStackTrace();
                                    return;
                              }

                              GradleStartCommon.LOGGER.log(Level.INFO, "Remapping AccessTransformer rules...");
                              iterator = launchClassValues.iterator();

                              while(iterator.hasNext()) {
                                    modifier = iterator.next();

                                    try {
                                          String fieldModifier = (String)field.get(modifier);
                                          String fieldValue = (String)transformed.get(fieldModifier);
                                          if(fieldValue != null) {
                                                field.set(modifier, fieldValue);
                                          }
                                    } catch (Exception ex) {
                                    }
                              }

                        }
                  } else {
                        GradleStartCommon.LOGGER.log(Level.ERROR, "ModAccessTransformer was somehow not found.");
                  }
            }

            private void readCsv(File csvFile, Map<String, String> propertyMap) throws IOException {
                  GradleStartCommon.LOGGER.log(Level.DEBUG, "Reading CSV file: {}", new Object[]{csvFile});
                  Splitter splitter = Splitter.on(',').trimResults().limit(3);
                  Iterator csvIterator = Files.readLines(csvFile, Charsets.UTF_8).iterator();

                  while(csvIterator.hasNext()) {
                        String next = (String)csvIterator.next();
                        if(!next.startsWith("searge")) {
                              List list = splitter.splitToList(next);
                              propertyMap.put((String) list.get(0), (String) list.get(1));
                        }
                  }

            }

            public byte[] transform(String s1, String s2, byte[] bytes) {
                  return bytes;
            }
      }
}
