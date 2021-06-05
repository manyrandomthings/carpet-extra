package carpetextra.mixins;

import carpetextra.CarpetExtraSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import carpetextra.utils.PlaceBlockDispenserBehavior;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin
{
    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;

    // this is old code to keep dispenserPlacesBlocks working as is
    // new behaviors should go in CarpetExtraDispenserBehaviors class
    @Inject(method = "getBehaviorForItem", at = @At("HEAD"), cancellable = true)
    private void getBehaviorForItem(ItemStack itemStack_1, CallbackInfoReturnable<DispenserBehavior> cir)
    {
        Item item = itemStack_1.getItem();
        if (CarpetExtraSettings.dispenserPlacesBlocks && !BEHAVIORS.containsKey(item) && item instanceof BlockItem)
        {
            if (PlaceBlockDispenserBehavior.canPlace(((BlockItem) item).getBlock()))
            {
                cir.setReturnValue(PlaceBlockDispenserBehavior.getInstance());
                cir.cancel();
            }
        }
    }
}
