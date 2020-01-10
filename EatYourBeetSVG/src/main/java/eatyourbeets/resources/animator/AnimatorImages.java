package eatyourbeets.resources.animator;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

import java.util.HashMap;

public class AnimatorImages
{
    private static final HashMap<Integer, Texture> characterPortraits = new HashMap<>();

    public final String ATTACK_PNG = "images/cardui/animator/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/cardui/animator/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/cardui/animator/512/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/cardui/animator/512/card_a_orb_canvas.png";
    public final String ORB_C_PNG = "images/cardui/animator/512/card_c_orb_canvas.png";
    public final String ORB_B_PNG = "images/cardui/animator/512/card_b_orb_canvas.png";
    public final String ATTACK_P_PNG = "images/cardui/animator/1024/bg_attack_canvas.png";
    public final String SKILL_P_PNG = "images/cardui/animator/1024/bg_skill_canvas.png";
    public final String POWER_P_PNG = "images/cardui/animator/1024/bg_power_canvas.png";
    public final String CHAR_BUTTON_PNG = "images/ui/charselect/animator_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/ui/charselect/animator_portrait.jpg";

    public final String BANNER_SPECIAL_P_PNG = "images/cardui/animator/1024/banner_special.png";
    public final String BANNER_SPECIAL2_P_PNG = "images/cardui/animator/1024/banner_special2.png";

    public final String BANNER_SPECIAL_PNG = "images/cardui/animator/512/banner_special.png";
    public final String BANNER_SPECIAL2_PNG = "images/cardui/animator/512/banner_special2.png";

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

    public Textures Textures;

    public Textures InitializeTextures()
    {
        return Textures = new Textures();
    }

    public class Textures
    {
        public final Texture CARD_BG_ATTACK   = GR.GetTexture(ATTACK_PNG);
        public final Texture CARD_BG_SKILL    = GR.GetTexture(SKILL_PNG);
        public final Texture CARD_BG_POWER    = GR.GetTexture(POWER_PNG);
        public final Texture CARD_BG_ATTACK_L = GR.GetTexture(ATTACK_P_PNG);
        public final Texture CARD_BG_SKILL_L  = GR.GetTexture(SKILL_P_PNG);
        public final Texture CARD_BG_POWER_L  = GR.GetTexture(POWER_P_PNG);

        public final Texture CARD_FRAME_ATTACK_SPECIAL   = GR.GetTexture("images/cardui/animator/512/frame_attack_special.png");
        public final Texture CARD_FRAME_SKILL_SPECIAL    = GR.GetTexture("images/cardui/animator/512/frame_skill_special.png");
        public final Texture CARD_FRAME_POWER_SPECIAL    = GR.GetTexture("images/cardui/animator/512/frame_power_special.png");
        public final Texture CARD_FRAME_ATTACK_SPECIAL_L = GR.GetTexture("images/cardui/animator/1024/frame_attack_special.png");
        public final Texture CARD_FRAME_SKILL_SPECIAL_L  = GR.GetTexture("images/cardui/animator/1024/frame_skill_special.png");
        public final Texture CARD_FRAME_POWER_SPECIAL_L  = GR.GetTexture("images/cardui/animator/1024/frame_power_special.png");

        public final Texture BRONZE_TROPHY        = GR.GetTexture("images/ui/rewards/animator/Bronze.png");
        public final Texture SILVER_TROPHY        = GR.GetTexture("images/ui/rewards/animator/Silver.png");
        public final Texture GOLD_TROPHY          = GR.GetTexture("images/ui/rewards/animator/Gold.png");
        public final Texture PLATINUM_TROPHY      = GR.GetTexture("images/ui/rewards/animator/Platinum.png");
        public final Texture LOCKED_TROPHY        = GR.GetTexture("images/ui/rewards/animator/Locked.png");
        public final Texture BRONZE_TROPHY_SLOT   = GR.GetTexture("images/ui/rewards/animator/Slot1.png");
        public final Texture GOLD_TROPHY_SLOT     = GR.GetTexture("images/ui/rewards/animator/Slot2.png");
        public final Texture PLATINUM_TROPHY_SLOT = GR.GetTexture("images/ui/rewards/animator/Slot3.png");
        public final Texture SYNERGY_CARD_REWARD  = GR.GetTexture("images/ui/rewards/animator/SynergyCardsReward.png");

        public Texture GetCharacterPortrait(int id)
        {
            Texture result;
            if (!characterPortraits.containsKey(id))
            {
                result = GR.GetTexture("images/ui/charselect/animator_portrait_" + id + ".png");
                characterPortraits.put(id, result);
            }
            else
            {
                result = characterPortraits.get(id);
            }

            return result;
        }
    }
}
