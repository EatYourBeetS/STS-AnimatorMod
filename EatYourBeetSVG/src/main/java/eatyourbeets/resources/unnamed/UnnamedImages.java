package eatyourbeets.resources.unnamed;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class UnnamedImages
{
    public static final String ATTACK_PNG = "images/cardui/unnamed/512/bg_attack_canvas.png";
    public static final String SKILL_PNG = "images/cardui/unnamed/512/bg_skill_canvas.png";
    public static final String POWER_PNG = "images/cardui/unnamed/512/bg_power_canvas.png";
    public static final String ORB_1A_PNG = "images/cardui/unnamed/512/energy_orb1_a.png";
    public static final String ORB_1B_PNG = "images/cardui/unnamed/512/energy_orb1_b.png";
    public static final String ORB_1C_PNG = "images/cardui/unnamed/512/energy_orb1_c.png";
    public static final String ORB_2A_PNG = "images/cardui/unnamed/512/energy_orb2_a.png";
    public static final String ORB_2B_PNG = "images/cardui/unnamed/512/energy_orb2_b.png";
    public static final String ORB_2C_PNG = "images/cardui/unnamed/512/energy_orb2_c.png";
    public static final String ATTACK_P_PNG = "images/cardui/unnamed/1024/bg_attack_canvas.png";
    public static final String SKILL_P_PNG = "images/cardui/unnamed/1024/bg_skill_canvas.png";
    public static final String POWER_P_PNG = "images/cardui/unnamed/1024/bg_power_canvas.png";
    public static final String CHAR_BUTTON_PNG = "images/ui/charselect/unnamed_button.png";
    public static final String CHAR_PORTRAIT_JPG = "images/ui/charselect/animator_portrait.jpg";

    public static final String BANNER_SPECIAL_P_PNG = "images/cardui/unnamed/1024/banner_special.png";
    public static final String BANNER_SPECIAL2_P_PNG = "images/cardui/unnamed/1024/banner_special2.png";

    public static final String BANNER_SPECIAL_PNG = "images/cardui/unnamed/512/banner_special.png";
    public static final String BANNER_SPECIAL2_PNG = "images/cardui/unnamed/512/banner_special2.png";

    public static final String ORB_VFX_PNG = "images/characters/unnamed/energy/orbVfx.png";
    public static final String[] ORB_TEXTURES =
    {
            "images/characters/unnamed/energy/layer1.png", "images/characters/unnamed/energy/layer2.png",
            "images/characters/unnamed/energy/layer3.png", "images/characters/unnamed/energy/layer4.png",
            "images/characters/unnamed/energy/layer5.png", "images/characters/unnamed/energy/layer6.png",
            "images/characters/unnamed/energy/layer1d.png", "images/characters/unnamed/energy/layer2d.png",
            "images/characters/unnamed/energy/layer3d.png", "images/characters/unnamed/energy/layer4d.png",
            "images/characters/unnamed/energy/layer5d.png"
    };

    public static final String CHARACTER_PNG = "images/characters/unnamed/idle/unnamed.png";
    public static final String SKELETON_ATLAS = "images/characters/unnamed/idle/unnamed.atlas";
    public static final String SKELETON_JSON = "images/characters/unnamed/idle/unnamed.json";
    public static final String SHOULDER1_PNG = "images/characters/unnamed/shoulder.png";
    public static final String SHOULDER2_PNG = "images/characters/unnamed/shoulder2.png";
    public static final String CORPSE_PNG = "images/characters/unnamed/corpse.png";

    public static final Texture CARD_BG_ATTACK = new Texture(ATTACK_PNG);
    public static final Texture CARD_BG_SKILL = new Texture(SKILL_PNG);
    public static final Texture CARD_BG_POWER = new Texture(POWER_PNG);
    public static final Texture CARD_BG_ATTACK_L = new Texture(ATTACK_P_PNG);
    public static final Texture CARD_BG_SKILL_L = new Texture(SKILL_P_PNG);
    public static final Texture CARD_BG_POWER_L = new Texture(POWER_P_PNG);

    public static final Texture CARD_FRAME_ATTACK_SPECIAL = new Texture("images/cardui/unnamed/512/frame_attack_special.png");
    public static final Texture CARD_FRAME_SKILL_SPECIAL  = new Texture("images/cardui/unnamed/512/frame_skill_special.png");
    public static final Texture CARD_FRAME_POWER_SPECIAL  = new Texture("images/cardui/unnamed/512/frame_power_special.png");
    public static final Texture CARD_FRAME_ATTACK_SPECIAL_L = new Texture("images/cardui/unnamed/1024/frame_attack_special.png");
    public static final Texture CARD_FRAME_SKILL_SPECIAL_L  = new Texture("images/cardui/unnamed/1024/frame_skill_special.png");
    public static final Texture CARD_FRAME_POWER_SPECIAL_L  = new Texture("images/cardui/unnamed/1024/frame_power_special.png");

    public static final Texture ORB_2A = UnnamedResources.GetTexture(ORB_2A_PNG);
    public static final Texture ORB_2B = UnnamedResources.GetTexture(ORB_2B_PNG);
    public static final Texture ORB_2C = UnnamedResources.GetTexture(ORB_2C_PNG);

    public static final TextureAtlas ORB_2_ATLAS = new TextureAtlas();

    static
    {
        ORB_2_ATLAS.addRegion(ORB_2A_PNG, ORB_2A, 0, 0, ORB_2A.getWidth(), ORB_2A.getHeight());
        ORB_2_ATLAS.addRegion(ORB_2B_PNG, ORB_2B, 0, 0, ORB_2B.getWidth(), ORB_2B.getHeight());
        ORB_2_ATLAS.addRegion(ORB_2C_PNG, ORB_2C, 0, 0, ORB_2C.getWidth(), ORB_2C.getHeight());
    }
}
