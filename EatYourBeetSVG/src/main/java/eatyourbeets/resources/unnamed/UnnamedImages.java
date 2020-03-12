package eatyourbeets.resources.unnamed;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import eatyourbeets.ui.TextureCache;

public class UnnamedImages
{
    public final String ATTACK_PNG = "images/cardui/unnamed/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/cardui/unnamed/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/cardui/unnamed/512/bg_power_canvas.png";
    public final String ORB_1A_PNG = "images/cardui/unnamed/512/energy_orb1_a.png";
    public final String ORB_1B_PNG = "images/cardui/unnamed/512/energy_orb1_b.png";
    public final String ORB_1C_PNG = "images/cardui/unnamed/512/energy_orb1_c.png";
    public final String ORB_2A_PNG = "images/cardui/unnamed/512/energy_orb2_a.png";
    public final String ORB_2B_PNG = "images/cardui/unnamed/512/energy_orb2_b.png";
    public final String ORB_2C_PNG = "images/cardui/unnamed/512/energy_orb2_c.png";
    public final String ATTACK_P_PNG = "images/cardui/unnamed/1024/bg_attack_canvas.png";
    public final String SKILL_P_PNG = "images/cardui/unnamed/1024/bg_skill_canvas.png";
    public final String POWER_P_PNG = "images/cardui/unnamed/1024/bg_power_canvas.png";
    public final String CHAR_BUTTON_PNG = "images/ui/charselect/unnamed_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/ui/charselect/animator_portrait.jpg";

    public final String BANNER_SPECIAL_P_PNG = "images/cardui/unnamed/1024/banner_special.png";
    public final String BANNER_SPECIAL2_P_PNG = "images/cardui/unnamed/1024/banner_special2.png";

    public final String BANNER_SPECIAL_PNG = "images/cardui/unnamed/512/banner_special.png";
    public final String BANNER_SPECIAL2_PNG = "images/cardui/unnamed/512/banner_special2.png";

    public final String ORB_VFX_PNG = "images/characters/unnamed/energy/orbVfx.png";
    public final String[] ORB_TEXTURES =
            {
                    "images/characters/unnamed/energy/layer1.png", "images/characters/unnamed/energy/layer2.png",
                    "images/characters/unnamed/energy/layer3.png", "images/characters/unnamed/energy/layer4.png",
                    "images/characters/unnamed/energy/layer5.png", "images/characters/unnamed/energy/layer6.png",
                    "images/characters/unnamed/energy/layer1d.png", "images/characters/unnamed/energy/layer2d.png",
                    "images/characters/unnamed/energy/layer3d.png", "images/characters/unnamed/energy/layer4d.png",
                    "images/characters/unnamed/energy/layer5d.png"
            };

    public final String CHARACTER_PNG = "images/characters/unnamed/idle/unnamed.png";
    public final String SKELETON_ATLAS = "images/characters/unnamed/idle/unnamed.atlas";
    public final String SKELETON_JSON = "images/characters/unnamed/idle/unnamed.json";
    public final String SHOULDER1_PNG = "images/characters/unnamed/shoulder.png";
    public final String SHOULDER2_PNG = "images/characters/unnamed/shoulder2.png";
    public final String CORPSE_PNG = "images/characters/unnamed/corpse.png";

    public final TextureCache CARD_BG_ATTACK = new TextureCache(ATTACK_PNG);
    public final TextureCache CARD_BG_SKILL = new TextureCache(SKILL_PNG);
    public final TextureCache CARD_BG_POWER = new TextureCache(POWER_PNG);
    public final TextureCache CARD_BG_ATTACK_L = new TextureCache(ATTACK_P_PNG);
    public final TextureCache CARD_BG_SKILL_L = new TextureCache(SKILL_P_PNG);
    public final TextureCache CARD_BG_POWER_L = new TextureCache(POWER_P_PNG);
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL = new TextureCache("images/cardui/unnamed/512/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL = new TextureCache("images/cardui/unnamed/512/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL = new TextureCache("images/cardui/unnamed/512/frame_power_special.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL_L = new TextureCache("images/cardui/unnamed/1024/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL_L = new TextureCache("images/cardui/unnamed/1024/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL_L = new TextureCache("images/cardui/unnamed/1024/frame_power_special.png");
    public final TextureCache ORB_2A = new TextureCache(ORB_2A_PNG);
    public final TextureCache ORB_2B = new TextureCache(ORB_2B_PNG);
    public final TextureCache ORB_2C = new TextureCache(ORB_2C_PNG);

    public final TextureAtlas ORB_2_ATLAS = new TextureAtlas();

    public UnnamedImages()
    {
        ORB_2_ATLAS.addRegion(ORB_2A_PNG, ORB_2A.Texture(), 0, 0, ORB_2A.Texture().getWidth(), ORB_2A.Texture().getHeight());
        ORB_2_ATLAS.addRegion(ORB_2B_PNG, ORB_2B.Texture(), 0, 0, ORB_2B.Texture().getWidth(), ORB_2B.Texture().getHeight());
        ORB_2_ATLAS.addRegion(ORB_2C_PNG, ORB_2C.Texture(), 0, 0, ORB_2C.Texture().getWidth(), ORB_2C.Texture().getHeight());
    }
}
