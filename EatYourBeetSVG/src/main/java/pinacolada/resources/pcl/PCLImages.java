package pinacolada.resources.pcl;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.utilities.RotatingList;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.ui.TextureCache;

import java.util.HashMap;

public class PCLImages
{
    public final String ATTACK_PNG = "images/pcl/cardui/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/pcl/cardui/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/pcl/cardui/512/bg_power_canvas.png";
    public final String ATTACK_PNG_L = "images/pcl/cardui/1024/bg_attack_canvas.png";
    public final String SKILL_PNG_L = "images/pcl/cardui/1024/bg_skill_canvas.png";
    public final String POWER_PNG_L = "images/pcl/cardui/1024/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/pcl/cardui/512/energy_orb_default_a.png";
    public final String ORB_B_PNG = "images/pcl/cardui/512/energy_orb_default_b.png";
    public final String ORB_C_PNG = "images/pcl/cardui/512/energy_orb_default_c.png";
    public final String CHAR_BUTTON_PNG = "images/pcl/ui/charselect/animator_button.png";
    public final String CHAR_PORTRAIT_JPG = "images/pcl/ui/charselect/animator_portrait.jpg";

    public final String BANNER_SPECIAL_P_PNG = "images/pcl/cardui/1024/banner_special.png";
    public final String BANNER_SPECIAL2_P_PNG = "images/pcl/cardui/1024/banner_special2.png";

    public final String BANNER_SPECIAL_PNG = "images/pcl/cardui/512/banner_special.png";

    public final String ORB_VFX_PNG = "images/pcl/ui/topPanel/canvas/orbVfx.png";
    public final String[] ORB_TEXTURES =
    {
        "images/pcl/ui/topPanel/canvas/layer1.png", "images/pcl/ui/topPanel/canvas/layer2.png", "images/pcl/ui/topPanel/canvas/layer3.png",
        "images/pcl/ui/topPanel/canvas/layer4.png", "images/pcl/ui/topPanel/canvas/layer5.png", "images/pcl/ui/topPanel/canvas/layer6.png",
        "images/pcl/ui/topPanel/canvas/layer1d.png", "images/pcl/ui/topPanel/canvas/layer2d.png", "images/pcl/ui/topPanel/canvas/layer3d.png",
        "images/pcl/ui/topPanel/canvas/layer4d.png", "images/pcl/ui/topPanel/canvas/layer5d.png"
    };

    public final String CHARACTER_PNG = "images/pcl/characters/idle/animator.png";
    public final String SKELETON_ATLAS = "images/pcl/characters/idle/animator.atlas";
    public final String SKELETON_JSON = "images/pcl/characters/idle/animator.json";
    public final String SHOULDER1_PNG = "images/pcl/characters/shoulder.png";
    public final String SHOULDER2_PNG = "images/pcl/characters/shoulder2.png";
    public final String CORPSE_PNG = "images/pcl/characters/corpse.png";

    public final TextureCache CARD_ENERGY_ORB_ANIMATOR    = new TextureCache(ORB_A_PNG);
    public final TextureCache CARD_ENERGY_ORB_ANIMATOR_L  = new TextureCache(ORB_B_PNG);
    public final TextureCache CARD_BACKGROUND_ATTACK      = new TextureCache("images/pcl/cardui/512/bg_attack_canvas.png");
    public final TextureCache CARD_BACKGROUND_SKILL       = new TextureCache("images/pcl/cardui/512/bg_skill_canvas.png");
    public final TextureCache CARD_BACKGROUND_POWER       = new TextureCache("images/pcl/cardui/512/bg_power_canvas.png");
    public final TextureCache CARD_BANNER                 = new TextureCache("images/pcl/cardui/512/banner.png");
    public final TextureCache CARD_BANNER_ATTRIBUTE       = new TextureCache("images/pcl/cardui/512/banner_attribute.png");
    public final TextureCache CARD_BORDER_INDICATOR       = new TextureCache("images/pcl/cardui/512/border_indicator.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL   = new TextureCache("images/pcl/cardui/512/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL    = new TextureCache("images/pcl/cardui/512/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL    = new TextureCache("images/pcl/cardui/512/frame_power_special.png");
    public final TextureCache CARD_BANNER_L               = new TextureCache("images/pcl/cardui/1024/banner.png");
    public final TextureCache CARD_BANNER_ATTRIBUTE_L     = new TextureCache("images/pcl/cardui/1024/banner_attribute.png");
    public final TextureCache CARD_FRAME_ATTACK_SPECIAL_L = new TextureCache("images/pcl/cardui/1024/frame_attack_special.png");
    public final TextureCache CARD_FRAME_SKILL_SPECIAL_L  = new TextureCache("images/pcl/cardui/1024/frame_skill_special.png");
    public final TextureCache CARD_FRAME_POWER_SPECIAL_L  = new TextureCache("images/pcl/cardui/1024/frame_power_special.png");
    public final TextureCache CARD_BACKGROUND_ATTACK_L    = new TextureCache("images/pcl/cardui/1024/bg_attack_canvas.png");
    public final TextureCache CARD_BACKGROUND_SKILL_L     = new TextureCache("images/pcl/cardui/1024/bg_skill_canvas.png");
    public final TextureCache CARD_BACKGROUND_POWER_L     = new TextureCache("images/pcl/cardui/1024/bg_power_canvas.png");

    public final TextureCache CARD_FRAME_SKILL            = new TextureCache("images/pcl/cardui/512/frame_skill.png");
    public final TextureCache CARD_FRAME_POWER            = new TextureCache("images/pcl/cardui/512/frame_power.png");
    public final TextureCache CARD_FRAME_ATTACK           = new TextureCache("images/pcl/cardui/512/frame_attack.png");
    public final TextureCache CARD_FRAME_SKILL_L            = new TextureCache("images/pcl/cardui/1024/frame_skill.png");
    public final TextureCache CARD_FRAME_POWER_L            = new TextureCache("images/pcl/cardui/1024/frame_power.png");
    public final TextureCache CARD_FRAME_ATTACK_L           = new TextureCache("images/pcl/cardui/1024/frame_attack.png");

    public final TextureCache BRONZE_TROPHY               = new TextureCache("images/pcl/ui/rewards/Bronze.png");
    public final TextureCache SILVER_TROPHY               = new TextureCache("images/pcl/ui/rewards/Silver.png");
    public final TextureCache GOLD_TROPHY                 = new TextureCache("images/pcl/ui/rewards/Gold.png");
    public final TextureCache PLATINUM_TROPHY             = new TextureCache("images/pcl/ui/rewards/Platinum.png");
    public final TextureCache LOCKED_TROPHY               = new TextureCache("images/pcl/ui/rewards/Locked.png");
    public final TextureCache BRONZE_TROPHY_SLOT          = new TextureCache("images/pcl/ui/rewards/Slot1.png");
    public final TextureCache GOLD_TROPHY_SLOT            = new TextureCache("images/pcl/ui/rewards/Slot2.png");
    public final TextureCache PLATINUM_TROPHY_SLOT        = new TextureCache("images/pcl/ui/rewards/Slot3.png");
    public final TextureCache SYNERGY_CARD_REWARD         = new TextureCache("images/pcl/ui/rewards/SynergyCardsReward.png");
    public final TextureCache CHARACTER_BUTTON            = new TextureCache("images/pcl/ui/charselect/animator_button.png");


    public final PCLImages.Badges Badges = new PCLImages.Badges();
    public final PCLImages.CardIcons Icons = new PCLImages.CardIcons();
    public final PCLImages.Tooltips Tooltips = new PCLImages.Tooltips();
    public final PCLImages.AffinityIcons Affinities = new PCLImages.AffinityIcons();
    public final PCLImages.Effects Effects = new PCLImages.Effects();
    public final PCLImages.Events Events = new PCLImages.Events();
    public final PCLImages.Orbs Orbs = new PCLImages.Orbs();

    public final TextureCache Arrow_Right                 = new TextureCache("images/pcl/ui/topPanel/Arrow_Right.png");
    public final TextureCache CampfireOption_Enchant      = new TextureCache("images/pcl/ui/campfire/Enchant.png");
    public final TextureCache CampfireOption_Kirby        = new TextureCache("images/pcl/ui/campfire/Kirby.png");
    public final TextureCache Circle                      = new TextureCache("images/pcl/ui/topPanel/Circle.png");
    public final TextureCache ControllableCardPile        = new TextureCache("images/pcl/ui/topPanel/ControllableCardPile.png");
    public final TextureCache ControllableCardPileBorder  = new TextureCache("images/pcl/ui/topPanel/ControllableCardPileBorder.png");
    public final TextureCache Discord                     = new TextureCache("images/pcl/ui/topPanel/Discord.png");
    public final TextureCache Draggable                   = new TextureCache("images/pcl/ui/topPanel/Draggable.png");
    public final TextureCache Edit                        = new TextureCache("images/pcl/ui/topPanel/Edit.png");
    public final TextureCache FullSquare                  = new TextureCache("images/pcl/ui/topPanel/FullSquare.png");
    public final TextureCache HexagonalButton             = new TextureCache("images/pcl/ui/topPanel/HexagonalButton.png");
    public final TextureCache HexagonalButtonBorder       = new TextureCache("images/pcl/ui/topPanel/HexagonalButtonBorder.png");
    public final TextureCache HexagonalButtonHover        = new TextureCache("images/pcl/ui/topPanel/HexagonalButtonHover.png");
    public final TextureCache Info                        = new TextureCache("images/pcl/ui/topPanel/Info.png");
    public final TextureCache LongButton                  = new TextureCache("images/pcl/ui/topPanel/LongButton.png");
    public final TextureCache LongButtonBorder            = new TextureCache("images/pcl/ui/topPanel/LongButtonBorder.png");
    public final TextureCache Minus                       = new TextureCache("images/pcl/ui/topPanel/Minus.png");
    public final TextureCache Panel                       = new TextureCache("images/pcl/ui/topPanel/Panel.png");
    public final TextureCache Panel_Elliptical            = new TextureCache("images/pcl/ui/topPanel/Panel_Elliptical.png");
    public final TextureCache Panel_Elliptical_Half_H     = new TextureCache("images/pcl/ui/topPanel/Panel_Elliptical_Half_H.png");
    public final TextureCache Panel_Rounded               = new TextureCache("images/pcl/ui/topPanel/Panel_Rounded.png");
    public final TextureCache Panel_Rounded_Half_H        = new TextureCache("images/pcl/ui/topPanel/Panel_Rounded_Half_H.png");
    public final TextureCache Panel_Skewed                = new TextureCache("images/pcl/ui/topPanel/Panel_Skewed.png");
    public final TextureCache Panel_Skewed_L              = new TextureCache("images/pcl/ui/topPanel/Panel_Skewed_L.png");
    public final TextureCache Plus                        = new TextureCache("images/pcl/ui/topPanel/Plus.png");
    public final TextureCache Randomize                   = new TextureCache("images/pcl/ui/topPanel/Randomize.png");
    public final TextureCache RectangularButton           = new TextureCache("images/pcl/ui/topPanel/RectangularButton.png");
    public final TextureCache SquaredButton               = new TextureCache("images/pcl/ui/topPanel/SquaredButton.png");
    public final TextureCache SquaredButton_EmptyCenter   = new TextureCache("images/pcl/ui/topPanel/SquaredButton_EmptyCenter.png");
    public final TextureCache Steam                       = new TextureCache("images/pcl/ui/topPanel/Steam.png");
    public final TextureCache SwapCards                   = new TextureCache("images/pcl/ui/topPanel/SwapCards.png");
    public final TextureCache Tag                         = new TextureCache("images/pcl/ui/topPanel/Tag.png");
    public final TextureCache UnnamedReignBoss            = new TextureCache("images/pcl/ui/map/boss/TheUnnamed.png");
    public final TextureCache UnnamedReignBossOutline     = new TextureCache("images/pcl/ui/map/bossOutline/TheUnnamed.png");
    public final TextureCache UnnamedReignEntrance        = new TextureCache("images/pcl/ui/map/act5Entrance.png");
    public final TextureCache UnnamedReignEntranceOutline = new TextureCache("images/pcl/ui/map/act5EntranceOutline.png");
    public final TextureCache X                           = new TextureCache("images/pcl/ui/topPanel/X.png");

    public static class CardIcons
    {
        public final TextureCache Agility               = new TextureCache("images/pcl/cardui/core/Agility.png");
        public final TextureCache Agility_L               = new TextureCache("images/pcl/cardui/core/1024/Agility.png");
        public final TextureCache Block                 = new TextureCache("images/pcl/cardui/core/Block.png");
        public final TextureCache BlockScaling          = new TextureCache("images/pcl/cardui/core/BlockScaling.png");
        public final TextureCache BlockScaling_L          = new TextureCache("images/pcl/cardui/core/1024/BlockScaling.png");
        public final TextureCache Block_L                 = new TextureCache("images/pcl/cardui/core/1024/Block.png");
        public final TextureCache BranchUpgrade           = new TextureCache("images/pcl/cardui/core/BranchUpgrade.png");
        public final TextureCache BranchUpgrade_L         = new TextureCache("images/pcl/cardui/core/1024/BranchUpgrade.png");
        public final TextureCache Brutal                = new TextureCache("images/pcl/cardui/core/BrutalDamage.png");
        public final TextureCache Brutal_L                = new TextureCache("images/pcl/cardui/core/1024/BrutalDamage.png");
        public final TextureCache Damage                = new TextureCache("images/pcl/cardui/core/NormalDamage.png");
        public final TextureCache Damage_L                = new TextureCache("images/pcl/cardui/core/1024/NormalDamage.png");
        public final TextureCache Elemental             = new TextureCache("images/pcl/cardui/core/ElementalDamage.png");
        public final TextureCache Elemental_L             = new TextureCache("images/pcl/cardui/core/1024/ElementalDamage.png");
        public final TextureCache Force                 = new TextureCache("images/pcl/cardui/core/Force.png");
        public final TextureCache Force_L                 = new TextureCache("images/pcl/cardui/core/1024/Force.png");
        public final TextureCache HP                    = new TextureCache("images/pcl/cardui/core/HP.png");
        public final TextureCache HP_L                    = new TextureCache("images/pcl/cardui/core/1024/HP.png");
        public final TextureCache Intellect             = new TextureCache("images/pcl/cardui/core/Intellect.png");
        public final TextureCache Intellect_L             = new TextureCache("images/pcl/cardui/core/1024/Intellect.png");
        public final TextureCache Multiform               = new TextureCache("images/pcl/cardui/core/Multiform.png");
        public final TextureCache Multiform_L             = new TextureCache("images/pcl/cardui/core/1024/Multiform.png");
        public final TextureCache Piercing              = new TextureCache("images/pcl/cardui/core/PiercingDamage.png");
        public final TextureCache Piercing_L              = new TextureCache("images/pcl/cardui/core/1024/PiercingDamage.png");
        public final TextureCache Ranged                = new TextureCache("images/pcl/cardui/core/RangedDamage.png");
        public final TextureCache Ranged_L                = new TextureCache("images/pcl/cardui/core/1024/RangedDamage.png");
        public final TextureCache TempHP                = new TextureCache("images/pcl/cardui/core/TempHP.png");
        public final TextureCache TempHP_L                = new TextureCache("images/pcl/cardui/core/1024/TempHP.png");
        public final TextureCache Unique                  = new TextureCache("images/pcl/cardui/core/Unique.png");
        public final TextureCache Unique_L                = new TextureCache("images/pcl/cardui/core/1024/Unique.png");
        public final TextureCache Willpower             = new TextureCache("images/pcl/cardui/core/Willpower.png");
        public final TextureCache Willpower_L             = new TextureCache("images/pcl/cardui/core/1024/Willpower.png");
    }

    public static class AffinityIcons
    {
        public final TextureCache General               = new TextureCache("images/pcl/cardui/affinities/General.png", true);
        public final TextureCache Green                 = new TextureCache("images/pcl/cardui/affinities/Green.png", true);
        public final TextureCache Red                   = new TextureCache("images/pcl/cardui/affinities/Red.png", true);
        public final TextureCache Blue                  = new TextureCache("images/pcl/cardui/affinities/Blue.png", true);
        public final TextureCache Orange                = new TextureCache("images/pcl/cardui/affinities/Orange.png", true);
        public final TextureCache Light                 = new TextureCache("images/pcl/cardui/affinities/Light.png", true);
        public final TextureCache Dark                  = new TextureCache("images/pcl/cardui/affinities/Dark.png", true);
        public final TextureCache Silver                = new TextureCache("images/pcl/cardui/affinities/Silver.png", true);
        public final TextureCache Star                  = new TextureCache("images/pcl/cardui/affinities/Star.png", true);
        public final TextureCache Star_BG               = new TextureCache("images/pcl/cardui/affinities/Star_BG.png", true);
        public final TextureCache Star_FG               = new TextureCache("images/pcl/cardui/affinities/Star_FG.png", true);
        public final TextureCache Border                = new TextureCache("images/pcl/cardui/affinities/Border.png", false);
        public final TextureCache BorderFG              = new TextureCache("images/pcl/cardui/affinities/Border_FG.png", false);
        public final TextureCache BorderBG              = new TextureCache("images/pcl/cardui/affinities/Border_BG.png", true);
        public final TextureCache BorderBG2              = new TextureCache("images/pcl/cardui/affinities/Border_BG2.png", true);
        public final TextureCache BorderBG3              = new TextureCache("images/pcl/cardui/affinities/Border_BG3.png", true);
        public final TextureCache Border_Strong         = new TextureCache("images/pcl/cardui/affinities/Border_Strong.png", true);
        public final TextureCache Border_Weak           = new TextureCache("images/pcl/cardui/affinities/Border_Weak.png", true);
    }

    public static class Badges
    {
        public final TextureCache Afterlife               = new TextureCache("images/pcl/cardui/badges/Afterlife.png");
        public final TextureCache Autoplay                = new TextureCache("images/pcl/cardui/badges/Autoplay.png");
        public final TextureCache Base_Badge              = new TextureCache("images/pcl/cardui/badges/Base_Badge.png");
        public final TextureCache Base_Border             = new TextureCache("images/pcl/cardui/badges/Base_Border.png");
        public final TextureCache Base_Infinite           = new TextureCache("images/pcl/cardui/badges/Base_Infinite.png");
        public final TextureCache Delayed                 = new TextureCache("images/pcl/cardui/badges/Delayed.png");
        public final TextureCache Ethereal                = new TextureCache("images/pcl/cardui/badges/Ethereal.png");
        public final TextureCache Exhaust                 = new TextureCache("images/pcl/cardui/badges/Exhaust.png");
        public final TextureCache Harmonic                = new TextureCache("images/pcl/cardui/badges/Harmonic.png");
        public final TextureCache Haste                   = new TextureCache("images/pcl/cardui/badges/Haste.png");
        public final TextureCache Innate                  = new TextureCache("images/pcl/cardui/badges/Innate.png");
        public final TextureCache Loyal                   = new TextureCache("images/pcl/cardui/badges/Loyal.png");
        public final TextureCache Purge                   = new TextureCache("images/pcl/cardui/badges/Purge.png");
        public final TextureCache Retain                  = new TextureCache("images/pcl/cardui/badges/Retain.png");
        public final TextureCache Unplayable              = new TextureCache("images/pcl/cardui/badges/Unplayable.png");
    }

    public static class Tooltips
    {
        public final TextureCache Air              = new TextureCache("images/pcl/cardui/tooltips/Air.png");
        public final TextureCache Chaos            = new TextureCache("images/pcl/cardui/tooltips/Chaos.png");
        public final TextureCache Dark             = new TextureCache("images/pcl/cardui/tooltips/Dark.png");
        public final TextureCache Earth            = new TextureCache("images/pcl/cardui/tooltips/Earth.png");
        public final TextureCache Fire             = new TextureCache("images/pcl/cardui/tooltips/Fire.png");
        public final TextureCache Frost            = new TextureCache("images/pcl/cardui/tooltips/Frost.png");
        public final TextureCache Gold             = new TextureCache("images/pcl/cardui/tooltips/Gold.png");
        public final TextureCache GriefSeed        = new TextureCache("images/pcl/cardui/tooltips/GriefSeed.png");
        public final TextureCache Lightning        = new TextureCache("images/pcl/cardui/tooltips/Lightning.png");
        public final TextureCache OrbSlot          = new TextureCache("images/pcl/cardui/tooltips/OrbSlot.png");
        public final TextureCache Plasma           = new TextureCache("images/pcl/cardui/tooltips/Plasma.png");
        public final TextureCache RandomOrb        = new TextureCache("images/pcl/cardui/tooltips/RandomOrb.png");
        public final TextureCache Regeneration     = new TextureCache("images/pcl/cardui/tooltips/Regeneration.png");
        public final TextureCache ThrowingKnife    = new TextureCache("images/pcl/cardui/tooltips/ThrowingKnife.png");
        public final TextureCache Water            = new TextureCache("images/pcl/cardui/tooltips/Water.png");
    }

    public static class Events
    {
        public final TextureCache BubuPharmacy     = new TextureCache("images/pcl/events/BubuPharmacy.png");
        public final TextureCache CursedForest     = new TextureCache("images/pcl/events/CursedForest.png");
        public final TextureCache HeroAssociation  = new TextureCache("images/pcl/events/HeroAssociation.png");
        public final TextureCache QingyunPeak      = new TextureCache("images/pcl/events/QingyunPeak.png");
        public final TextureCache PaimonsBargains  = new TextureCache("images/pcl/events/PaimonsBargains.png");
        public final TextureCache Placeholder      = new TextureCache("images/pcl/events/Placeholder.png");
        public final TextureCache SecludedHarbor      = new TextureCache("images/pcl/events/SecludedHarbor.png");
    }

    public static class Orbs
    {
        public final TextureCache AirCloud         = new TextureCache("images/pcl/orbs/AirCloud.png");
        public final TextureCache AirLeft          = new TextureCache("images/pcl/orbs/AirLeft.png");
        public final TextureCache AirRight         = new TextureCache("images/pcl/orbs/AirRight.png");
        public final TextureCache Chaos1           = new TextureCache("images/pcl/orbs/Chaos1.png");
        public final TextureCache Chaos2           = new TextureCache("images/pcl/orbs/Chaos2.png");
        public final TextureCache Chaos3           = new TextureCache("images/pcl/orbs/Chaos3.png");
        public final TextureCache Earth1           = new TextureCache("images/pcl/orbs/Earth1.png");
        public final TextureCache Earth2           = new TextureCache("images/pcl/orbs/Earth2.png");
        public final TextureCache Earth3           = new TextureCache("images/pcl/orbs/Earth3.png");
        public final TextureCache Earth4           = new TextureCache("images/pcl/orbs/Earth4.png");
        public final TextureCache FireExternal     = new TextureCache("images/pcl/orbs/FireExternal.png");
        public final TextureCache FireInternal     = new TextureCache("images/pcl/orbs/FireInternal.png");
        public final TextureCache FireParticle1     = new TextureCache("images/pcl/orbs/FireParticle1.png");
        public final TextureCache FireParticle2     = new TextureCache("images/pcl/orbs/FireParticle2.png");
        public final TextureCache FireParticle3     = new TextureCache("images/pcl/orbs/FireParticle3.png");
        public final TextureCache Water1           = new TextureCache("images/pcl/orbs/Water1.png");
        public final TextureCache Water2           = new TextureCache("images/pcl/orbs/Water2.png");
        public final TextureCache Water3           = new TextureCache("images/pcl/orbs/Water3.png");
        public final TextureCache Water4           = new TextureCache("images/pcl/orbs/Water4.png");
        public final TextureCache WaterBubble      = new TextureCache("images/pcl/orbs/WaterBubble.png");
    }

    public static class Effects
    {
        public final TextureCache AirSlice         = new TextureCache("images/pcl/effects/AirSlice.png");
        public final TextureCache AirTornado1      = new TextureCache("images/pcl/effects/AirTornado1.png");
        public final TextureCache AirTornado2      = new TextureCache("images/pcl/effects/AirTornado2.png");
        public final TextureCache AirTrail1        = new TextureCache("images/pcl/effects/AirTrail1.png");
        public final TextureCache AirTrail2        = new TextureCache("images/pcl/effects/AirTrail2.png");
        public final TextureCache AirTrail3        = new TextureCache("images/pcl/effects/AirTrail3.png");
        public final TextureCache Circle           = new TextureCache("images/pcl/effects/Circle.png");
        public final TextureCache Darkness         = new TextureCache("images/pcl/effects/Darkness.png");
        public final TextureCache Droplet          = new TextureCache("images/pcl/effects/Droplet.png");
        public final TextureCache FireBurst        = new TextureCache("images/pcl/effects/FireBurst.png");
        public final TextureCache FrostSnow1       = new TextureCache("images/pcl/effects/FrostSnow1.png");
        public final TextureCache FrostSnow2       = new TextureCache("images/pcl/effects/FrostSnow2.png");
        public final TextureCache FrostSnow3       = new TextureCache("images/pcl/effects/FrostSnow3.png");
        public final TextureCache FrostSnow4       = new TextureCache("images/pcl/effects/FrostSnow4.png");
        public final TextureCache Hexagon          = new TextureCache("images/pcl/effects/Hexagon.png");
        public final TextureCache IceImpact        = new TextureCache("images/pcl/effects/IceImpact.png");
        public final TextureCache Psi              = new TextureCache("images/pcl/effects/Psi.png");
        public final TextureCache Punch            = new TextureCache("images/pcl/effects/Punch.png");
        public final TextureCache Shot             = new TextureCache("images/pcl/effects/Shot.png");
        public final TextureCache Slice            = new TextureCache("images/pcl/effects/Slice.png");
        public final TextureCache Spark1           = new TextureCache("images/pcl/effects/Spark1.png");
        public final TextureCache Spark2           = new TextureCache("images/pcl/effects/Spark2.png");
        public final TextureCache Sparkle1         = new TextureCache("images/pcl/effects/Sparkle1.png");
        public final TextureCache Sparkle2         = new TextureCache("images/pcl/effects/Sparkle2.png");
        public final TextureCache Sparkle3         = new TextureCache("images/pcl/effects/Sparkle3.png");
        public final TextureCache Sparkle4         = new TextureCache("images/pcl/effects/Sparkle4.png");
        public final TextureCache Spear            = new TextureCache("images/pcl/effects/Spear.png");
        public final TextureCache Star             = new TextureCache("images/pcl/effects/Star.png");
        public final TextureCache WaterDome        = new TextureCache("images/pcl/effects/WaterDome.png");
        public final TextureCache WaterSplash1     = new TextureCache("images/pcl/effects/WaterSplash1.png");
        public final TextureCache WaterSplash2     = new TextureCache("images/pcl/effects/WaterSplash2.png");
        public final TextureCache WaterSplash3     = new TextureCache("images/pcl/effects/WaterSplash3.png");
        public final TextureCache Whack            = new TextureCache("images/pcl/effects/Whack.png");
    }


    private final static HashMap<Integer, RotatingList<Texture>> portraits = new HashMap<>();

    public Texture GetCharacterPortrait(int id)
    {
        return portraits.get(id).Current(true);
    }

    public static void PreloadResources()
    {
        for (PCLLoadout loadout : GR.PCL.Data.GetEveryLoadout())
        {
            int id = loadout.ID;
            RotatingList<Texture> images = portraits.get(id);
            if (images == null)
            {
                images = new RotatingList<>();
                portraits.put(id, images);

                Texture t;
                t = GR.GetTexture("images/pcl/ui/charselect/animator_portrait_" + id + ".png");
                if (t != null)
                {
                    images.Add(t);
                }
                t = GR.GetTexture("images/pcl/ui/charselect/animator_portrait_" + id + "b.png");
                if (t != null)
                {
                    images.Add(t);
                }
                t = GR.GetTexture("images/pcl/ui/charselect/animator_portrait_" + id + "c.png");
                if (t != null)
                {
                    images.Add(t);
                }
            }
        }
    }
}