package eatyourbeets.resources.animator;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.RotatingList;

import java.util.HashMap;

public class AnimatorImages
{
    public final String ATTACK_PNG = "images/cardui/animator/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/cardui/animator/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/cardui/animator/512/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/cardui/animator/512/card_a_orb_canvas.png";
    public final String ORB_C_PNG = "images/cardui/animator/512/card_c_orb_canvas.png";
    public final String ORB_B_PNG = "images/cardui/animator/512/card_b_orb_canvas.png";
    public final String CHAR_BUTTON_PNG = "images/ui/charselect/animator_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/ui/charselect/animator_portrait.jpg";

    public final String BANNER_SPECIAL_P_PNG = "images/cardui/animator/1024/banner_special.png";
    public final String BANNER_SPECIAL2_P_PNG = "images/cardui/animator/1024/banner_special2.png";

    public final String BANNER_SPECIAL_PNG = "images/cardui/animator/512/banner_special.png";

    public final String ORB_VFX_PNG = "images/ui/topPanel/animator_canvas/orbVfx.png";
    public final String[] ORB_TEXTURES =
    {
            "images/ui/topPanel/animator_canvas/layer1.png", "images/ui/topPanel/animator_canvas/layer2.png", "images/ui/topPanel/animator_canvas/layer3.png",
            "images/ui/topPanel/animator_canvas/layer4.png", "images/ui/topPanel/animator_canvas/layer5.png", "images/ui/topPanel/animator_canvas/layer6.png",
            "images/ui/topPanel/animator_canvas/layer1d.png", "images/ui/topPanel/animator_canvas/layer2d.png", "images/ui/topPanel/animator_canvas/layer3d.png",
            "images/ui/topPanel/animator_canvas/layer4d.png", "images/ui/topPanel/animator_canvas/layer5d.png"
    };

    public final String CHARACTER_PNG = "images/characters/animator/idle/animator.png";
    public final String SKELETON_ATLAS = "images/characters/animator/idle/animator.atlas";
    public final String SKELETON_JSON = "images/characters/animator/idle/animator.json";
    public final String SHOULDER1_PNG = "images/characters/animator/shoulder.png";
    public final String SHOULDER2_PNG = "images/characters/animator/shoulder2.png";
    public final String CORPSE_PNG = "images/characters/animator/corpse.png";

    public final TextureCache CARD_ENERGY_ORB_A           = new TextureCache("images/cardui/animator/512/card_a_orb_canvas.png");
    public final TextureCache CARD_BACKGROUND_ATTACK      = new TextureCache("images/cardui/animator/512/bg_attack_canvas.png");
    public final TextureCache CARD_BACKGROUND_ATTACK_UR   = new TextureCache("images/cardui/animator/512/bg_attack_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_SKILL_UR    = new TextureCache("images/cardui/animator/512/bg_skill_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_POWER_UR    = new TextureCache("images/cardui/animator/512/bg_power_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_SKILL       = new TextureCache("images/cardui/animator/512/bg_skill_canvas.png");
    public final TextureCache CARD_BACKGROUND_POWER       = new TextureCache("images/cardui/animator/512/bg_power_canvas.png");
    public final TextureCache CARD_BANNER_SPECIAL         = new TextureCache("images/cardui/animator/512/banner_special.png");
    public final TextureCache CARD_BANNER_ULTRARARE       = new TextureCache("images/cardui/animator/512/banner_ultrarare.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL   = new TextureCache("images/cardui/animator/512/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL    = new TextureCache("images/cardui/animator/512/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL    = new TextureCache("images/cardui/animator/512/frame_power_special.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL_L = new TextureCache("images/cardui/animator/1024/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL_L  = new TextureCache("images/cardui/animator/1024/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL_L  = new TextureCache("images/cardui/animator/1024/frame_power_special.png");
    public final TextureCache BRONZE_TROPHY               = new TextureCache("images/ui/rewards/animator/Bronze.png");
    public final TextureCache SILVER_TROPHY               = new TextureCache("images/ui/rewards/animator/Silver.png");
    public final TextureCache GOLD_TROPHY                 = new TextureCache("images/ui/rewards/animator/Gold.png");
    public final TextureCache PLATINUM_TROPHY             = new TextureCache("images/ui/rewards/animator/Platinum.png");
    public final TextureCache LOCKED_TROPHY               = new TextureCache("images/ui/rewards/animator/Locked.png");
    public final TextureCache BRONZE_TROPHY_SLOT          = new TextureCache("images/ui/rewards/animator/Slot1.png");
    public final TextureCache GOLD_TROPHY_SLOT            = new TextureCache("images/ui/rewards/animator/Slot2.png");
    public final TextureCache PLATINUM_TROPHY_SLOT        = new TextureCache("images/ui/rewards/animator/Slot3.png");
    public final TextureCache SYNERGY_CARD_REWARD         = new TextureCache("images/ui/rewards/animator/SynergyCardsReward.png");
    public final TextureCache CHARACTER_BUTTON            = new TextureCache("images/ui/charselect/animator_button.png");

    private final static HashMap<Integer, RotatingList<Texture>> portraits = new HashMap<>();

    public Texture GetCharacterPortrait(int id)
    {
        RotatingList<Texture> images = portraits.get(id);
        if (images == null)
        {
            images = new RotatingList<>();
            portraits.put(id, images);

            Texture t;
            t = GR.GetTexture("images/ui/charselect/animator_portrait_" + id + ".png");
            if (t != null)
            {
                images.Add(t);
            }
            t = GR.GetTexture("images/ui/charselect/animator_portrait_" + id + "b.png");
            if (t != null)
            {
                images.Add(t);
            }
            t = GR.GetTexture("images/ui/charselect/animator_portrait_" + id + "c.png");
            if (t != null)
            {
                images.Add(t);
            }
        }

        return images.Current(true);
    }
}
