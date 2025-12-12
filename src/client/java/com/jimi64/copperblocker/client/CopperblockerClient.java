package com.jimi64.copperblocker.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CopperblockerClient implements ClientModInitializer {

    public static final String MOD_ID = "CopperBlocker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ArrayList<String> denyList = new ArrayList<String>();
    public static String blockerMode = "both";


    @Override
    public void onInitializeClient() {

        // generate the denyList
        String[] oxidizationPrefixes = {"minecraft:", "minecraft:exposed_", "minecraft:weathered_", "minecraft:oxidized_"};
        String[] blockVariants = {"copper", "chiseled_copper", "copper_grate", "cut_copper", "cut_copper_stairs", "cut_copper_slab", "copper_bars", "copper_door", "copper_trapdoor", "copper_bulb", "copper_chain", "copper_lantern", "lightning_rod", "copper_chest", "copper_golem_statue"};

        for (String oxidizationPrefix : oxidizationPrefixes) {
            for (String blockVariant : blockVariants) {

                denyList.add(oxidizationPrefix + blockVariant);

            }
        }

        // changes the entry for the copper block
        // because it doesn't follow the naming convention
        denyList.set(0, "minecraft:copper_block");

        LOGGER.info("Loaded the deny list!");
    }
}
