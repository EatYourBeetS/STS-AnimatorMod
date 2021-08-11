package eatyourbeets.resources.animator;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.RotatingList;

import java.util.HashMap;

public class AnimatorImages
{
    public final String ATTACK_PNG = "images/animator/cardui/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/animator/cardui/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/animator/cardui/512/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/animator/cardui/512/energy_orb_default_a.png";
    public final String ORB_B_PNG = "images/animator/cardui/512/energy_orb_default_b.png";
    public final String ORB_C_PNG = "images/animator/cardui/512/energy_orb_default_c.png";
    public final String ORB_COLORLESS_A_PNG = "images/animator/cardui/512/energy_orb_colorless_a.png";
    public final String CHAR_BUTTON_PNG = "images/animator/ui/charselect/animator_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/animator/ui/charselect/animator_portrait.jpg";

    public final String BANNER_SPECIAL_P_PNG = "images/animator/cardui/1024/banner_special.png";
    public final String BANNER_SPECIAL2_P_PNG = "images/animator/cardui/1024/banner_special2.png";

    public final String BANNER_SPECIAL_PNG = "images/animator/cardui/512/banner_special.png";

    public final String ORB_VFX_PNG = "images/animator/ui/topPanel/canvas/orbVfx.png";
    public final String[] ORB_TEXTURES =
    {
        "images/animator/ui/topPanel/canvas/layer1.png", "images/animator/ui/topPanel/canvas/layer2.png", "images/animator/ui/topPanel/canvas/layer3.png",
        "images/animator/ui/topPanel/canvas/layer4.png", "images/animator/ui/topPanel/canvas/layer5.png", "images/animator/ui/topPanel/canvas/layer6.png",
        "images/animator/ui/topPanel/canvas/layer1d.png", "images/animator/ui/topPanel/canvas/layer2d.png", "images/animator/ui/topPanel/canvas/layer3d.png",
        "images/animator/ui/topPanel/canvas/layer4d.png", "images/animator/ui/topPanel/canvas/layer5d.png"
    };

    public final String CHARACTER_PNG = "images/animator/characters/idle/animator.png";
    public final String SKELETON_ATLAS = "images/animator/characters/idle/animator.atlas";
    public final String SKELETON_JSON = "images/animator/characters/idle/animator.json";
    public final String SHOULDER1_PNG = "images/animator/characters/shoulder.png";
    public final String SHOULDER2_PNG = "images/animator/characters/shoulder2.png";
    public final String CORPSE_PNG = "images/animator/characters/corpse.png";

    public final TextureCache CARD_ENERGY_ORB_ANIMATOR    = new TextureCache(ORB_A_PNG);
    public final TextureCache CARD_ENERGY_ORB_COLORLESS   = new TextureCache(ORB_COLORLESS_A_PNG);
    public final TextureCache CARD_BACKGROUND_ATTACK      = new TextureCache("images/animator/cardui/512/bg_attack_canvas.png");
    public final TextureCache CARD_BACKGROUND_ATTACK_UR   = new TextureCache("images/animator/cardui/512/bg_attack_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_SKILL_UR    = new TextureCache("images/animator/cardui/512/bg_skill_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_POWER_UR    = new TextureCache("images/animator/cardui/512/bg_power_ultrarare.png");
    public final TextureCache CARD_BACKGROUND_SKILL       = new TextureCache("images/animator/cardui/512/bg_skill_canvas.png");
    public final TextureCache CARD_BACKGROUND_POWER       = new TextureCache("images/animator/cardui/512/bg_power_canvas.png");
    public final TextureCache CARD_BANNER_SPECIAL         = new TextureCache("images/animator/cardui/512/banner_special.png");
    public final TextureCache CARD_BANNER_GENERIC         = new TextureCache("images/animator/cardui/512/banner_generic.png");
    public final TextureCache CARD_BANNER_ULTRARARE       = new TextureCache("images/animator/cardui/512/banner_ultrarare.png");
    public final TextureCache CARD_BORDER_INDICATOR       = new TextureCache("images/animator/cardui/512/border_indicator.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL   = new TextureCache("images/animator/cardui/512/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL    = new TextureCache("images/animator/cardui/512/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL    = new TextureCache("images/animator/cardui/512/frame_power_special.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL_L = new TextureCache("images/animator/cardui/1024/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL_L  = new TextureCache("images/animator/cardui/1024/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL_L  = new TextureCache("images/animator/cardui/1024/frame_power_special.png");

    public final TextureCache CARD_FRAME_SKILL            = new TextureCache("images/animator/cardui/512/frame_skill.png");
    public final TextureCache CARD_FRAME_POWER            = new TextureCache("images/animator/cardui/512/frame_power.png");
    public final TextureCache CARD_FRAME_ATTACK           = new TextureCache("images/animator/cardui/512/frame_attack.png");

    public final TextureCache BRONZE_TROPHY               = new TextureCache("images/animator/ui/rewards/Bronze.png");
    public final TextureCache SILVER_TROPHY               = new TextureCache("images/animator/ui/rewards/Silver.png");
    public final TextureCache GOLD_TROPHY                 = new TextureCache("images/animator/ui/rewards/Gold.png");
    public final TextureCache PLATINUM_TROPHY             = new TextureCache("images/animator/ui/rewards/Platinum.png");
    public final TextureCache LOCKED_TROPHY               = new TextureCache("images/animator/ui/rewards/Locked.png");
    public final TextureCache BRONZE_TROPHY_SLOT          = new TextureCache("images/animator/ui/rewards/Slot1.png");
    public final TextureCache GOLD_TROPHY_SLOT            = new TextureCache("images/animator/ui/rewards/Slot2.png");
    public final TextureCache PLATINUM_TROPHY_SLOT        = new TextureCache("images/animator/ui/rewards/Slot3.png");
    public final TextureCache SYNERGY_CARD_REWARD         = new TextureCache("images/animator/ui/rewards/SynergyCardsReward.png");
    public final TextureCache CHARACTER_BUTTON            = new TextureCache("images/animator/ui/charselect/animator_button.png");

    private final static HashMap<Integer, RotatingList<Texture>> portraits = new HashMap<>();

    public Texture GetCharacterPortrait(int id)
    {
        return portraits.get(id).Current(true);
    }

    public static void PreloadResources()
    {
        for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
        {
            int id = loadout.ID;
            RotatingList<Texture> images = portraits.get(id);
            if (images == null)
            {
                images = new RotatingList<>();
                portraits.put(id, images);

                Texture t;
                t = GR.GetTexture("images/animator/ui/charselect/animator_portrait_" + id + ".png");
                if (t != null)
                {
                    images.Add(t);
                }
                t = GR.GetTexture("images/animator/ui/charselect/animator_portrait_" + id + "b.png");
                if (t != null)
                {
                    images.Add(t);
                }
                t = GR.GetTexture("images/animator/ui/charselect/animator_portrait_" + id + "c.png");
                if (t != null)
                {
                    images.Add(t);
                }
            }
        }
    }
}