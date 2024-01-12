package net.sixik.orestages.mixin.chunk;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LinearPalette;
import net.sixik.orestages.utils.BlockHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LinearPalette.class)
public class MixinLinearPalette<T> {
    @Shadow
    @Final
    private T[] values;
    @Shadow
    private int size;

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    public void onWrite(FriendlyByteBuf arg, CallbackInfo ci) {
        if (this.size > 0 && this.values[0] instanceof BlockState) {
            arg.writeVarInt(this.size);

            for (int i = 0; i < this.size; ++i) {
                arg.writeVarInt(BlockHelper.getReplacementId((BlockState) this.values[i], null));
            }

            ci.cancel();
        }
    }
}
