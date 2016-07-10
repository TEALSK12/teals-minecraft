package org.tealsk12.tealsmodloader.entity.registry;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Connor Hollasch
 * https://github.com/CHollasch
 */
public class EntitySpawnInfo {

    private int weightedSpawnChance = 5;

    private int minPerPack;
    private int maxPerPack;

    private EnumCreatureType creatureType;

    private List<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();

    public EntitySpawnInfo setWeightedSpawnChance(int weightedSpawnChance) {
        this.weightedSpawnChance = weightedSpawnChance;
        return this;
    }

    public EntitySpawnInfo setMinimumPerPack(int minPerPack) {
        this.minPerPack = minPerPack;
        return this;
    }

    public EntitySpawnInfo setMaximumPerPack(int maxPerPack) {
        this.maxPerPack = maxPerPack;
        return this;
    }

    public EntitySpawnInfo setCreatureType(EnumCreatureType type) {
        this.creatureType = type;
        return this;
    }

    public EntitySpawnInfo addBiomesToSpawnIn(BiomeGenBase... biomes) {
        this.biomes.addAll(Arrays.asList(biomes));
        return this;
    }

    public int getWeightedSpawnChance() {
        return weightedSpawnChance;
    }

    public int getMinPerPack() {
        return minPerPack;
    }

    public int getMaxPerPack() {
        return maxPerPack;
    }

    public EnumCreatureType getCreatureType() {
        return creatureType;
    }

    public List<BiomeGenBase> getBiomes() {
        return biomes;
    }
}
