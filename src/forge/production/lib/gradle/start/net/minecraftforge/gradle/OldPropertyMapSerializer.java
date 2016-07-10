//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraftforge.gradle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Type;
import java.util.Iterator;

public class OldPropertyMapSerializer implements JsonSerializer<PropertyMap> {
      public OldPropertyMapSerializer() {
      }

      public JsonElement serialize(PropertyMap propertyMap, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            Iterator iterator = propertyMap.keySet().iterator();

            while(iterator.hasNext()) {
                  String jsonKey = (String)iterator.next();
                  JsonArray jsonArray = new JsonArray();
                  Iterator propertyIterator = propertyMap.get(jsonKey).iterator();

                  while(propertyIterator.hasNext()) {
                        Property property = (Property)propertyIterator.next();
                        jsonArray.add(new JsonPrimitive(property.getValue()));
                  }

                  jsonObject.add(jsonKey, jsonArray);
            }

            return jsonObject;
      }
}
