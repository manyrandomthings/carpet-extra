package carpetextra.dispenser;

import java.util.Map;

import carpetextra.CarpetExtraSettings;
import carpetextra.dispenser.behaviors.BlazePowderDispenserBehavior;
import carpetextra.dispenser.behaviors.CarvePumpkinDispenserBehavior;
import carpetextra.dispenser.behaviors.CauldronEmptyingDispenserBehavior;
import carpetextra.dispenser.behaviors.CauldronFillingDispenserBehavior;
import carpetextra.dispenser.behaviors.CauldronWaterDispenserBehavior;
import carpetextra.dispenser.behaviors.DragonBreathDispenserBehavior;
import carpetextra.dispenser.behaviors.FeedAnimalDispenserBehavior;
import carpetextra.dispenser.behaviors.FeedMooshroomDispenserBehavior;
import carpetextra.dispenser.behaviors.FillMinecartDispenserBehavior;
import carpetextra.dispenser.behaviors.FireChargeDispenserBehavior;
import carpetextra.dispenser.behaviors.FlowerPotDispenserBehavior;
import carpetextra.dispenser.behaviors.MilkAnimalDispenserBehavior;
import carpetextra.dispenser.behaviors.MilkMooshroomDispenserBehavior;
import carpetextra.dispenser.behaviors.MusicDiscDispenserBehavior;
import carpetextra.dispenser.behaviors.ShearChickenDispenserBehavior;
import carpetextra.dispenser.behaviors.StripBlocksDispenserBehavior;
import carpetextra.dispenser.behaviors.TillSoilDispenserBehavior;
import carpetextra.dispenser.behaviors.ToggleBlockDispenserBehavior;
import carpetextra.helpers.FlowerPotHelper;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class CarpetExtraDispenserBehaviors {
    // instances of custom dispenser behaviors
    // dispensersFillMinecarts
    public static final DispenserBehavior FILL_MINECART_CHEST = new FillMinecartDispenserBehavior(AbstractMinecartEntity.Type.CHEST);
    public static final DispenserBehavior FILL_MINECART_FURNACE = new FillMinecartDispenserBehavior(AbstractMinecartEntity.Type.FURNACE);
    public static final DispenserBehavior FILL_MINECART_TNT = new FillMinecartDispenserBehavior(AbstractMinecartEntity.Type.TNT);
    public static final DispenserBehavior FILL_MINECART_HOPPER = new FillMinecartDispenserBehavior(AbstractMinecartEntity.Type.HOPPER);
    // dispensersPlayRecords
    public static final DispenserBehavior PLAY_DISC = new MusicDiscDispenserBehavior();
    // dispensersMilkAnimals
    public static final DispenserBehavior MILK_ANIMAL = new MilkAnimalDispenserBehavior();
    public static final DispenserBehavior MILK_MOOSHROOM = new MilkMooshroomDispenserBehavior();
    // dispensersFeedAnimals
    public static final DispenserBehavior FEED_ANIMAL = new FeedAnimalDispenserBehavior();
    public static final DispenserBehavior FEED_MOOSHROOM = new FeedMooshroomDispenserBehavior();
    // renewableNetherrack
    public static final DispenserBehavior FIRE_CHARGE_NETHERRACK = new FireChargeDispenserBehavior();
    // renewableEndstone
    public static final DispenserBehavior DRAGON_BREATH_ENDSTONE = new DragonBreathDispenserBehavior();
    // dispensersCarvePumpkins
    public static final DispenserBehavior CARVE_PUMPKIN = new CarvePumpkinDispenserBehavior();
    // blazeMeal
    public static final DispenserBehavior BLAZE_MEAL = new BlazePowderDispenserBehavior();
    // dispensersToggleThings
    public static final DispenserBehavior TOGGLE_BLOCK = new ToggleBlockDispenserBehavior();
    // dispensersTillSoil
    public static final DispenserBehavior TILL_SOIL = new TillSoilDispenserBehavior();
    // dispensersStripBlocks
    public static final DispenserBehavior STRIP_BLOCK = new StripBlocksDispenserBehavior();
    // dispensersUseCauldrons
    public static final DispenserBehavior CAULDRON_FILLING_BUCKET = new CauldronFillingDispenserBehavior();
    public static final DispenserBehavior CAULDRON_EMPTYING_BUCKET = new CauldronEmptyingDispenserBehavior();
    public static final DispenserBehavior CAULDRON_WATER = new CauldronWaterDispenserBehavior();
    // dispensersPotPlants
    public static final DispenserBehavior FILL_FLOWER_POT = new FlowerPotDispenserBehavior();
    // chickenShearing
    public static final DispenserBehavior SHEAR_CHICKEN = new ShearChickenDispenserBehavior();

    // get custom behavior
    // this checks conditions such as a certain block or entity being in front of the dispenser to decide which rule to return
    // if the conditions for the rule match, it return the instance of the dispenser behavior
    // returns null to use the vanilla behavoir for the given item
    public static DispenserBehavior getCustomDispenserBehavior(ServerWorld world, BlockPos pos, BlockPointer pointer, DispenserBlockEntity dispenserBlockEntity, ItemStack stack, Map<Item, DispenserBehavior> VANILLA_BEHAVIORS) {
        Item item = stack.getItem();
        Direction dispenserFacing = pointer.getBlockState().get(DispenserBlock.FACING);
        BlockPos frontBlockPos = pos.offset(dispenserFacing);
        BlockState frontBlockState = world.getBlockState(frontBlockPos);
        Block frontBlock = frontBlockState.getBlock();
        Box frontBlockBox = new Box(frontBlockPos);

        // dispensersPlayRecords
        if(CarpetExtraSettings.dispensersPlayRecords && item instanceof MusicDiscItem && frontBlock == Blocks.JUKEBOX) {
            return PLAY_DISC;
        }

        // renewableNetherrack
        if(CarpetExtraSettings.renewableNetherrack && item == Items.FIRE_CHARGE && frontBlock == Blocks.COBBLESTONE) {
            return FIRE_CHARGE_NETHERRACK;
        }

        // renewableEndstone
        if(CarpetExtraSettings.renewableEndstone && item == Items.DRAGON_BREATH && frontBlock == Blocks.COBBLESTONE) {
            return DRAGON_BREATH_ENDSTONE;
        }

        // dispensersCarvePumpkins
        if(CarpetExtraSettings.dispensersCarvePumpkins && item instanceof ShearsItem && frontBlock == Blocks.PUMPKIN) {
            return CARVE_PUMPKIN;
        }

        // blazeMeal
        if(CarpetExtraSettings.blazeMeal && item == Items.BLAZE_POWDER && frontBlock == Blocks.NETHER_WART) {
            return BLAZE_MEAL;
        }

        // dispensersToggleThings
        if(CarpetExtraSettings.dispensersToggleThings && item == Items.STICK && ToggleBlockDispenserBehavior.TOGGLEABLE_BLOCKS.contains(frontBlock)) {
            return TOGGLE_BLOCK;
        }

        // dispensersStripBlocks
        if(CarpetExtraSettings.dispensersStripBlocks && item instanceof AxeItem && (StripBlocksDispenserBehavior.canStrip(frontBlock) || StripBlocksDispenserBehavior.isStripResult(frontBlock))) {
            return STRIP_BLOCK;
        }

        // dispensersTillSoil
        if(CarpetExtraSettings.dispensersTillSoil && item instanceof HoeItem) {
            // check block in front of dispenser and one block down
            for(int i = 0; i < 2; i++) {
                BlockPos hoeBlockPos = frontBlockPos.down(i);
                Block hoeBlock = world.getBlockState(hoeBlockPos).getBlock();

                // check if block is in tilled blocks, or is farmland (to prevent hoe being dispensed when you don't want it to)
                if(TillSoilDispenserBehavior.TILLED_BLOCKS.contains(hoeBlock) || hoeBlock == Blocks.FARMLAND) {
                    return TILL_SOIL;
                }
            }
        }

        // dispensersFeedAnimals
        if(CarpetExtraSettings.dispensersFeedAnimals) {
            // check for animals that can be bred with the current item being dispensed in front of dispenser
            boolean hasFeedableAnimals = !world.getEntitiesByClass(AnimalEntity.class, frontBlockBox, EntityPredicates.VALID_LIVING_ENTITY.and((animalEntity) -> {
                return ((AnimalEntity) animalEntity).isBreedingItem(stack);
            })).isEmpty();

            if(hasFeedableAnimals) {
                return FEED_ANIMAL;
            }

            // get brown mooshrooms in front of dispenser
            boolean hasFeedableMooshrooms = !world.getEntitiesByType(EntityType.MOOSHROOM, frontBlockBox, EntityPredicates.VALID_LIVING_ENTITY.and((mooshroomEntity) -> {
                return ((MooshroomEntity) mooshroomEntity).getMooshroomType() == MooshroomEntity.Type.BROWN;
            })).isEmpty();

            // check if item is a small flower
            if(hasFeedableMooshrooms && ItemTags.SMALL_FLOWERS.contains(item)) {
                return FEED_MOOSHROOM;
            }
        }

        // dispensersMilkAnimals
        if(CarpetExtraSettings.dispensersMilkAnimals) {
            // bucket to milk
            if(item == Items.BUCKET) {
                // check for cows, mooshrooms, or goats in front of dispenser
                boolean hasMilkable = !world.getEntitiesByClass(AnimalEntity.class, frontBlockBox, EntityPredicates.VALID_LIVING_ENTITY.and((animalEntity) -> {
                    return animalEntity instanceof CowEntity || animalEntity instanceof GoatEntity;
                })).isEmpty();

                if(hasMilkable) {
                    return MILK_ANIMAL;
                }
            }
            // bowl to stew
            else if(item == Items.BOWL) {
                // check for mooshrooms in front of dispenser
                boolean hasMooshroom = !world.getEntitiesByType(EntityType.MOOSHROOM, frontBlockBox, EntityPredicates.VALID_LIVING_ENTITY).isEmpty();

                if(hasMooshroom) {
                    return MILK_MOOSHROOM;
                }
            }
        }

        // chickenShearing
        if(CarpetExtraSettings.chickenShearing && item == Items.SHEARS) {
            boolean hasShearableChickens = !world.getEntitiesByType(EntityType.CHICKEN, frontBlockBox, EntityPredicates.VALID_LIVING_ENTITY.and((chickenEntity) -> {
                return !((AnimalEntity) chickenEntity).isBaby();
            })).isEmpty();

            if(hasShearableChickens) {
                return SHEAR_CHICKEN;
            }
        }

        // dispensersFillMinecarts
        if(CarpetExtraSettings.dispensersFillMinecarts) {
            // check for minecarts with no riders in front of dispenser
            boolean hasMinecarts = !world.getEntitiesByType(EntityType.MINECART, frontBlockBox, EntityPredicates.NOT_MOUNTED).isEmpty();

            // if a minecart exist, return dispenser behavior according to item type
            if(hasMinecarts) {
                if(item == Items.CHEST) {
                    return FILL_MINECART_CHEST;
                }
                else if(item == Items.FURNACE) {
                    return FILL_MINECART_FURNACE;
                }
                else if(item == Items.TNT) {
                    return FILL_MINECART_TNT;
                }
                else if(item == Items.HOPPER) {
                    return FILL_MINECART_HOPPER;
                }
            }
        }

        // dispensersUseCauldrons
        if(CarpetExtraSettings.dispensersUseCauldrons && frontBlock instanceof AbstractCauldronBlock) {
            // empty cauldron
            if(item == Items.BUCKET) {
                return CAULDRON_EMPTYING_BUCKET;
            }
            // fill cauldron
            else if(item == Items.LAVA_BUCKET || item == Items.WATER_BUCKET || item == Items.POWDER_SNOW_BUCKET) {
                return CAULDRON_FILLING_BUCKET;
            }
            // water cauldron behaviors (leather armor, shulker boxes, banners)
            else if(CauldronWaterDispenserBehavior.isWaterCauldronItem(stack)) {
                return CAULDRON_WATER;
            }
        }

        // dispensersPotPlants
        if(CarpetExtraSettings.dispensersPotPlants && frontBlock instanceof FlowerPotBlock && FlowerPotHelper.isPottable(item)) {
            return FILL_FLOWER_POT;
        }



        // no custom behavior, return null
        return null;
    }
}
