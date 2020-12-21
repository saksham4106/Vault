package iskallia.vault.block.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.block.RelicStatueBlock;
import iskallia.vault.block.entity.RelicStatueTileEntity;
import iskallia.vault.item.RelicItem;
import iskallia.vault.util.RelicSet;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;

public class RelicStatueRenderer extends TileEntityRenderer<RelicStatueTileEntity> {

    public RelicStatueRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(RelicStatueTileEntity statue, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RelicSet relicSet = RelicSet.REGISTRY.get(statue.getRelicSet());
        BlockState state = statue.getBlockState();

        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        float horizontalAngle = state.get(RelicStatueBlock.FACING).getHorizontalAngle();
        matrixStack.rotate(Vector3f.YN.rotationDegrees(180 + horizontalAngle));

        if(relicSet == RelicSet.DRAGON) {
            matrixStack.translate(0, 0, 0.15);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 0.7f, 7f, Registry.ITEM.getOrDefault(Vault.id("statue_dragon")));
        } else if(relicSet == RelicSet.MINER) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2f, RelicItem.withCustomModelData(0));
        } else if(relicSet == RelicSet.WARRIOR) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2f, RelicItem.withCustomModelData(1));
        } else if(relicSet == RelicSet.RICHITY) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2f, RelicItem.withCustomModelData(2));
        } else if(relicSet == RelicSet.TWITCH) {
            renderItem(matrixStack, buffer, combinedLight, combinedOverlay, 1.2f, 2f, RelicItem.withCustomModelData(3));
        }

        matrixStack.pop();
    }

    private void renderItem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, int overlay,
                            float yOffset, float scale, Item item) {
        renderItem(matrixStack, buffer, lightLevel, overlay, yOffset, scale, new ItemStack(item));
    }

    private void renderItem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int lightLevel, int overlay,
                            float yOffset, float scale, ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        matrixStack.push();
        matrixStack.translate(0, yOffset, 0);
        matrixStack.scale(scale, scale, scale);
        IBakedModel ibakedmodel = minecraft
                .getItemRenderer().getItemModelWithOverrides(itemStack, null, null);
        minecraft.getItemRenderer()
                .renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND, true,
                        matrixStack, buffer, lightLevel, overlay, ibakedmodel);
        matrixStack.pop();
    }

}
