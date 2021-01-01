package iskallia.vault.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class ModModels {

    public static void setupRenderLayers() {
        RenderTypeLookup.setRenderLayer(ModBlocks.VAULT_PORTAL, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.ALEXANDRITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BENITOITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.LARIMAR_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.BLACK_OPAL_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.PAINITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ISKALLIUM_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.RENIUM_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.GORGINITE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.SPARKLETINE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WUTODIE_DOOR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.VAULT_ALTAR, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_1, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_2, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_3, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_4, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_5, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_6, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_7, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_8, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_9, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_10, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_11, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_12, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_13, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_14, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_15, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ARTIFACT_16, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.MVP_CROWN, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.VENDING_MACHINE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.ADVANCED_VENDING_MACHINE, RenderType.getCutout());
//        RenderTypeLookup.setRenderLayer(ModBlocks.CRYO_CHAMBER, CustomRenderType.INSTANCE);
        RenderTypeLookup.setRenderLayer(ModBlocks.CRYO_CHAMBER, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.KEY_PRESS, RenderType.getCutout());
    }

    private static class CustomRenderType extends RenderType {
        // TODO: Do dis, so Cryo Chamber renders correctly :c
        private static final RenderType INSTANCE = makeType("cutout_ignoring_normals",
                DefaultVertexFormats.BLOCK, 7, 131072,
                true, false,
                RenderType.State.getBuilder()
                        .shadeModel(SHADE_ENABLED)
                        .lightmap(LIGHTMAP_ENABLED)
                        .texture(BLOCK_SHEET)
                        .alpha(HALF_ALPHA)
                        .build(true)
        );

        public CustomRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
            super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
        }
    }

}
