package com.meo209.betterrailplacement.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailPlacementHelper.class)
public abstract class MixinRailPlacementHelper {

    @Shadow @Final private World world;

    @Shadow @Final private BlockPos pos;

    @Shadow public abstract BlockState getBlockState();

    @Shadow private BlockState state;

    @Inject(method = "updateBlockState", at = @At("HEAD"), cancellable = true)
    private void updateBlockState(boolean poweredpa, boolean forceUpdate, RailShape railShape, CallbackInfoReturnable<RailPlacementHelper> cir) {
        PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
        if (player != null && player.isSneaking()) {
            BlockState state = getBlockState();
            AbstractRailBlock block = (AbstractRailBlock) state.getBlock();

            state = state.with(block.getShapeProperty(), railShape);
            world.setBlockState(pos, state, AbstractRailBlock.NOTIFY_ALL);

            cir.setReturnValue((RailPlacementHelper) (Object) this);
        }
    }

}
