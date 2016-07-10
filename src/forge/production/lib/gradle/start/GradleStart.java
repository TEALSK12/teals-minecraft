//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.Proxy;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraftforge.gradle.GradleStartCommon;
import net.minecraftforge.gradle.OldPropertyMapSerializer;

public class GradleStart extends GradleStartCommon {

      private static final String FILE_SEPERATOR = File.separator;
      private static final Gson GSON;

      public GradleStart() {
      }

      public static void main(String... args) throws Throwable {
            hackNatives();

            GradleStart gradleStart = new GradleStart();
            gradleStart.launch(args);
      }

      protected String getBounceClass() {
            return "net.minecraft.launchwrapper.Launch";
      }

      protected String getTweakClass() {
            return "cpw.mods.fml.common.launcher.FMLTweaker";
      }

      protected void setDefaultArguments(Map<String, String> argumentMap) {
            argumentMap.put("version", "1.7.10");
            argumentMap.put("assetIndex", "1.7.10");
            argumentMap.put("assetsDir", new File("").getAbsolutePath() + FILE_SEPERATOR + "lib" + FILE_SEPERATOR + "gradle" + FILE_SEPERATOR + "assets");
            argumentMap.put("accessToken", "FML");
            argumentMap.put("userProperties", "{}");
            argumentMap.put("username", null);
            argumentMap.put("password", null);
      }

      protected void preLaunch(Map<String, String> argumentMap, List<String> extras) {
            if(!Strings.isNullOrEmpty(argumentMap.get("password"))) {
                  GradleStartCommon.LOGGER.info("Password found, attempting login");
                  this.attemptLogin(argumentMap);
            }

            if(!Strings.isNullOrEmpty(argumentMap.get("assetIndex"))) {
                  this.setupAssets(argumentMap);
            }
      }

      private static void hackNatives() {
            String libraryPath = System.getProperty("java.library.path");
            String libraryLocation = new File("").getAbsolutePath() + FILE_SEPERATOR + "lib" + FILE_SEPERATOR + "gradle" + FILE_SEPERATOR + "natives";
            if(Strings.isNullOrEmpty(libraryPath)) {
                  libraryPath = libraryLocation;
            } else {
                  libraryPath = libraryPath + File.pathSeparator + libraryLocation;
            }

            System.setProperty("java.library.path", libraryPath);

            try {
                  Field systemPath = ClassLoader.class.getDeclaredField("sys_paths");
                  systemPath.setAccessible(true);
                  systemPath.set(null, null);
            } catch (Throwable ex) {
            }

      }

      private void attemptLogin(Map<String, String> argumentMap) {
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)(new YggdrasilAuthenticationService(Proxy.NO_PROXY, "1")).createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(argumentMap.get("username"));
            auth.setPassword(argumentMap.get("password"));
            argumentMap.put("password", null);

            try {
                  auth.logIn();
            } catch (AuthenticationException ex) {
                  LOGGER.error("-- Login failed!  " + ex.getMessage());
                  Throwables.propagate(ex);
                  return;
            }

            LOGGER.info("Login Succesful!");
            argumentMap.put("accessToken", auth.getAuthenticatedToken());
            argumentMap.put("uuid", auth.getSelectedProfile().getId().toString().replace("-", ""));
            argumentMap.put("username", auth.getSelectedProfile().getName());
            argumentMap.put("userType", auth.getUserType().getName());
            argumentMap.put("userProperties", (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new OldPropertyMapSerializer()).create().toJson(auth.getUserProperties()));
      }

      private void setupAssets(Map<String, String> argumentMap) {
            if(Strings.isNullOrEmpty(argumentMap.get("assetsDir"))) {
                  throw new IllegalArgumentException("assetsDir is null when assetIndex is not! THIS IS BAD COMMAND LINE ARGUMENTS, fix them");
            } else {
                  File assetDir = new File(argumentMap.get("assetsDir"));
                  File objectDir = new File(assetDir, "objects");
                  File indexesDir = new File(new File(assetDir, "indexes"), argumentMap.get("assetIndex") + ".json");

                  try {
                        GradleStart.AssetIndex assetsIndex = this.loadAssetsIndex(indexesDir);
                        if(!assetsIndex.virtual) {
                              return;
                        }

                        File virtualAssetsDir = new File(new File(assetDir, "virtual"), argumentMap.get("assetIndex"));
                        argumentMap.put("assetsDir", virtualAssetsDir.getAbsolutePath());
                        GradleStartCommon.LOGGER.info("Setting up virtual assets in: " + virtualAssetsDir.getAbsolutePath());
                        Map virtualAssets = this.gatherFiles(virtualAssetsDir);
                        Iterator assetsIterator = assetsIndex.objects.entrySet().iterator();

                        while(assetsIterator.hasNext()) {
                              Entry assetEntry = (Entry)assetsIterator.next();
                              String assetKey = (String)assetEntry.getKey();
                              String assetValue = ((GradleStart.AssetIndex.AssetEntry)assetEntry.getValue()).hash.toLowerCase();
                              File assetLocation = new File(virtualAssetsDir, assetKey);
                              File objectLocation = new File(new File(objectDir, assetValue.substring(0, 2)), assetValue);
                              if(virtualAssets.containsKey(assetKey)) {
                                    if((virtualAssets.get(assetKey)).equals(assetValue)) {
                                          virtualAssets.remove(assetKey);
                                    } else {
                                          GradleStartCommon.LOGGER.info("  " + assetKey + ": INVALID HASH");
                                          assetLocation.delete();
                                    }
                              } else if(!objectLocation.exists()) {
                                    GradleStartCommon.LOGGER.info("  " + assetKey + ": NEW MISSING " + assetValue);
                              } else {
                                    GradleStartCommon.LOGGER.info("  " + assetKey + ": NEW ");
                                    File assetParent = assetLocation.getParentFile();
                                    if(!assetParent.exists()) {
                                          assetParent.mkdirs();
                                    }

                                    Files.copy(objectLocation, assetLocation);
                              }
                        }

                        assetsIterator = virtualAssets.keySet().iterator();

                        while(assetsIterator.hasNext()) {
                              String assetKey = (String)assetsIterator.next();
                              GradleStartCommon.LOGGER.info("  " + assetKey + ": REMOVED");
                              File assetFolder = new File(virtualAssetsDir, assetKey);
                              assetFolder.delete();
                        }
                  } catch (Throwable ex) {
                        Throwables.propagate(ex);
                  }

            }
      }

      private GradleStart.AssetIndex loadAssetsIndex(File assetFile) throws JsonSyntaxException, JsonIOException, IOException {
            FileReader reader = new FileReader(assetFile);
            GradleStart.AssetIndex assetIndex = GSON.fromJson(reader, GradleStart.AssetIndex.class);
            reader.close();
            return assetIndex;
      }

      private String getDigest(File digestFolder) {
            DigestInputStream digestInputStream = null;

            Object digestObject;
            try {
                  digestInputStream = new DigestInputStream(new FileInputStream(digestFolder), MessageDigest.getInstance("SHA"));
                  byte[] bytes = new byte[65536];

                  int position;
                  do {
                        position = digestInputStream.read(bytes);
                  } while(position > 0);

                  return String.format("%1$040x", new Object[]{new BigInteger(1, digestInputStream.getMessageDigest().digest())});
            } catch (Exception ex) {
                  digestObject = null;
            } finally {
                  if(digestInputStream != null) {
                        try {
                              digestInputStream.close();
                        } catch (Exception ex) {}
                  }

            }

            return (String)digestObject;
      }

      private Map<String, String> gatherFiles(File folder) {
            HashMap files = new HashMap();
            this.gatherDir(files, folder, folder);
            return files;
      }

      private void gatherDir(Map<String, String> files, File stub, File file) {
            if(file.exists() && file.isDirectory()) {
                  File[] filesInFolder = file.listFiles();
                  int filesLength = filesInFolder.length;

                  for(int i = 0; i < filesLength; ++i) {
                        File subFile = filesInFolder[i];
                        if(subFile.isDirectory()) {
                              this.gatherDir(files, stub, subFile);
                        } else {
                              String relative = stub.toURI().relativize(subFile.toURI()).getPath().replace("\\", "/");
                              String digest = this.getDigest(subFile).toLowerCase();
                              files.put(relative, digest);
                        }
                  }

            }
      }

      static {
            GsonBuilder gson = new GsonBuilder();
            gson.enableComplexMapKeySerialization();
            gson.setPrettyPrinting();
            GSON = gson.create();
      }

      private static class AssetIndex {
            public boolean virtual;
            public Map<String, GradleStart.AssetIndex.AssetEntry> objects;

            private AssetIndex() {
            }

            public static class AssetEntry {
                  public String hash;

                  public AssetEntry() {
                  }
            }
      }
}
