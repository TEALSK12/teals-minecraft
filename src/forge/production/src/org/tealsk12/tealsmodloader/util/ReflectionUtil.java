package org.tealsk12.tealsmodloader.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class ReflectionUtil {

    public static Constructor<?> getConstructor(Class<?> clazz, Object[] args) {
        Class<?>[] argTypes = PrimitiveTypes.convertToPrimitive(toClassArray(args));

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (arraySimilar(argTypes, PrimitiveTypes.convertToPrimitive(constructor.getParameterTypes())))
                return constructor;
        }

        return null;
    }

    private static Class<?>[] toClassArray(Object[] objects) {
        Class<?>[] array = new Class<?>[objects.length];

        for (int i = 0; i < objects.length; i++) {
            array[i] = objects[i].getClass();
        }

        return array;
    }

    private static boolean arraySimilar(Class<?>[] first, Class<?>[] second) {
        if (first == null || second == null) return false;
        if (first.length != second.length) return false;

        for (int i = 0; i < first.length; i++) {
            if (first[i].equals(second[i]) || second[i].isAssignableFrom(first[i]))
                continue;

            return false;
        }

        return true;
    }

    public static Field searchForField(Class<?> clazz, Class<?> search) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(search))
                return field;
        }

        return searchForField(clazz.getSuperclass(), search);
    }

    public enum PrimitiveTypes {
        BYTE(byte.class, Byte.class),
        SHORT(short.class, Short.class),
        INT(int.class, Integer.class),
        LONG(long.class, Long.class),
        CHAR(char.class, Character.class),
        FLOAT(float.class, Float.class),
        DOUBLE(double.class, Double.class),
        BOOLEAN(boolean.class, Boolean.class);

        private Class<?> primitive;
        private Class<?> wrapper;

        private PrimitiveTypes(Class<?> primitive, Class<?> wrapper) {
            this.primitive = primitive;
            this.wrapper = wrapper;
        }

        public Class<?> getPrimitive() {
            return primitive;
        }

        public Class<?> getWrapper() {
            return wrapper;
        }

        public static Class<?>[] convertToPrimitive(Class<?>[] wrappers) {
            Class<?>[] primitive = new Class<?>[wrappers.length];

            for (int i = 0; i < wrappers.length; i++) {
                primitive[i] = convertToPrimitive(wrappers[i]);
            }

            return primitive;
        }

        public static Class<?> convertToPrimitive(Class<?> wrapper) {
            for (PrimitiveTypes t : values()) {
                if (t.wrapper.equals(wrapper))
                    return t.primitive;
            }
            return wrapper;
        }
    }

    public static <T extends Class> ArrayList<T> getClassNamesFromPackage(String packageName, T clazz) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL = classLoader.getResource(packageName.replace(".", "/"));
        if (packageURL == null)
            return new ArrayList<T>();

        if (packageURL.getProtocol().equals("jar")) {
            String jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));

            JarFile jf = new JarFile(jarFileName);
            Enumeration<JarEntry> jarEntries = jf.entries();
            return loadClassesFromJar(packageName, jarEntries, clazz);
        } else {
            URI uri = new URI(packageURL.toString());
            File folder = new File(uri.getPath());
            return loadClassesFromArchive(folder, packageName, clazz);
        }
    }

    private static <T extends Class> ArrayList<T> loadClassesFromArchive(File current_folder, String package_name, T clazz) throws ClassNotFoundException {
        ArrayList<T> all = new ArrayList<T>();

        if (current_folder == null || !current_folder.exists() || current_folder.listFiles().length == 0)
            return all;

        package_name = package_name.replace("/", ".");

        File[] files = current_folder.listFiles();
        for (File actual : files) {
            if (actual.getName().endsWith(".class")) {
                Class<?> check = Class.forName(package_name + "." + actual.getName().replace(".class", ""));

                if (clazz.isAssignableFrom(check)) {
                    all.add((T) check);
                }

            } else if (actual.isDirectory()) {
                all.addAll(loadClassesFromArchive(actual, package_name + "." + actual.getName(), clazz));
            }
        }

        return all;
    }

    public static <T extends Class> ArrayList<T> loadClassesFromJar(String package_name, Enumeration<JarEntry> entries, T clazz) {
        ArrayList<T> modules = new ArrayList<T>();

        String relPath = package_name.replace('.', '/');

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length()) && !(entryName.contains("$"))) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> c = null;
                try {
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (c != null && clazz != null) {
                    if (c.getSuperclass() == null && clazz.isAssignableFrom(c))
                        modules.add((T) c);
                    else if (c.getSuperclass() != null && clazz.isAssignableFrom(c.getSuperclass()))
                        modules.add((T) c);
                }
            }
        }

        return modules;
    }
}
