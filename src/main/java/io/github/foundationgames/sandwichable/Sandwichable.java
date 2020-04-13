package io.github.foundationgames.sandwichable;

import io.github.foundationgames.sandwichable.blocks.BlocksRegistry;
import io.github.foundationgames.sandwichable.blocks.entity.BasinContent;
import io.github.foundationgames.sandwichable.items.CheeseType;
import io.github.foundationgames.sandwichable.items.ItemsRegistry;
import io.github.foundationgames.sandwichable.items.SandwichableGroupIconBuilder;
import io.github.foundationgames.sandwichable.recipe.CuttingRecipe;
import io.github.foundationgames.sandwichable.recipe.CuttingRecipeSerializer;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipe;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipeSerializer;
import io.github.foundationgames.sandwichable.util.CheeseRegistry;
import io.github.foundationgames.sandwichable.util.Util;
import io.github.foundationgames.sandwichable.worldgen.ShrubsFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class Sandwichable implements ModInitializer {

    public static final ItemGroup SANDWICHABLE_ITEMS = FabricItemGroupBuilder.build(Util.id("sandwichable"), SandwichableGroupIconBuilder::getIcon);

    public static final Tag<Item> BREADS = TagRegistry.item(Util.id("breads"));
    public static final Tag<Item> METAL_ITEMS = TagRegistry.item(Util.id("metal_items"));

    private static final Feature<DefaultFeatureConfig> SHRUBS = Registry.register(
            Registry.FEATURE,
            Util.id("shrubs"),
            new ShrubsFeature(DefaultFeatureConfig::deserialize)
    );

    public final CheeseRegistry cheeseRegistry = new CheeseRegistry();

    @Override
    public void onInitialize() {
        BlocksRegistry.init();
        ItemsRegistry.init();

        //CHEESE TYPES
        cheeseRegistry.register(CheeseType.NONE);
        cheeseRegistry.register(CheeseType.REGULAR);

        //BASIN CONTENTS
        cheeseRegistry.register(BasinContent.AIR);
        cheeseRegistry.register(BasinContent.MILK);
        cheeseRegistry.register(BasinContent.FERMENTING_MILK_REGULAR);

        //BASIN CONTENTS - CHEESE
        cheeseRegistry.registerCheese(BasinContent.CHEESE_REGULAR);

        Registry.register(Registry.RECIPE_SERIALIZER, CuttingRecipeSerializer.ID, CuttingRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, Util.id(CuttingRecipe.Type.ID), CuttingRecipe.Type.INSTANCE);

        Registry.register(Registry.RECIPE_SERIALIZER, ToastingRecipeSerializer.ID, ToastingRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, Util.id(ToastingRecipe.Type.ID), ToastingRecipe.Type.INSTANCE);


        Registry.BIOME.forEach(biome -> biome.addFeature(
            GenerationStep.Feature.RAW_GENERATION,
            new ShrubsFeature(DefaultFeatureConfig::deserialize).configure(
                new DefaultFeatureConfig()
            )
        ));
    }
}
