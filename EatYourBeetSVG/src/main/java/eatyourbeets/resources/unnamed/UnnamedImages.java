package eatyourbeets.resources.unnamed;

import eatyourbeets.ui.TextureCache;

public class UnnamedImages
{
    public final String ATTACK_PNG = "images/unnamed/cardui/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/unnamed/cardui/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/unnamed/cardui/512/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/unnamed/cardui/512/energy_orb_default_a.png";
    public final String ORB_B_PNG = "images/unnamed/cardui/512/energy_orb_default_b.png";
    public final String ORB_C_PNG = "images/unnamed/cardui/512/energy_orb_default_c.png";
    public final String CHAR_BUTTON_PNG = "images/unnamed/ui/charselect/unnamed_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/unnamed/ui/charselect/unnamed_portrait.jpg";

    // TODO: Custom energy orb VFX
    public final String ORB_VFX_PNG = "images/animator/ui/topPanel/canvas/orbVfx.png";
    public final String[] ORB_TEXTURES =
    {
        "images/animator/ui/topPanel/canvas/layer1.png", "images/animator/ui/topPanel/canvas/layer2.png", "images/animator/ui/topPanel/canvas/layer3.png",
        "images/animator/ui/topPanel/canvas/layer4.png", "images/animator/ui/topPanel/canvas/layer5.png", "images/animator/ui/topPanel/canvas/layer6.png",
        "images/animator/ui/topPanel/canvas/layer1d.png", "images/animator/ui/topPanel/canvas/layer2d.png", "images/animator/ui/topPanel/canvas/layer3d.png",
        "images/animator/ui/topPanel/canvas/layer4d.png", "images/animator/ui/topPanel/canvas/layer5d.png"
    };

    public final String CHARACTER_PNG = "images/unnamed/characters/idle/unnamed.png";
    public final String SKELETON_ATLAS = "images/unnamed/characters/idle/unnamed.atlas";
    public final String SKELETON_JSON = "images/unnamed/characters/idle/unnamed.json";
    public final String SHOULDER1_PNG = "images/unnamed/characters/shoulder.png";
    public final String SHOULDER2_PNG = "images/unnamed/characters/shoulder2.png";
    public final String CORPSE_PNG = "images/unnamed/characters/corpse.png";

    public final TextureCache CARD_ENERGY_ORB_UNNAMED     = new TextureCache(ORB_A_PNG);
    public final TextureCache CARD_BACKGROUND_ATTACK      = new TextureCache("images/unnamed/cardui/512/bg_attack_canvas.png");
    public final TextureCache CARD_BACKGROUND_SKILL       = new TextureCache("images/unnamed/cardui/512/bg_skill_canvas.png");
    public final TextureCache CARD_BACKGROUND_POWER       = new TextureCache("images/unnamed/cardui/512/bg_power_canvas.png");
    public final TextureCache CARD_BANNER_GENERIC         = new TextureCache("images/unnamed/cardui/512/banner_generic.png");
    public final TextureCache CARD_BORDER_INDICATOR       = new TextureCache("images/unnamed/cardui/512/border_indicator.png");
    public final TextureCache CARD_FRAME_SKILL            = new TextureCache("images/unnamed/cardui/512/frame_skill.png");
    public final TextureCache CARD_FRAME_POWER            = new TextureCache("images/unnamed/cardui/512/frame_power.png");
    public final TextureCache CARD_FRAME_ATTACK           = new TextureCache("images/unnamed/cardui/512/frame_attack.png");
    public final TextureCache CHARACTER_BUTTON            = new TextureCache("images/unnamed/ui/charselect/unnamed_button.png");
}