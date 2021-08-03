package eatyourbeets.resources.common;

import eatyourbeets.ui.TextureCache;

public class CommonImages
{
    public final Badges Badges = new Badges();
    public final CardIcons Icons = new CardIcons();
    public final Tooltips Tooltips = new Tooltips();
    public final AffinityIcons Affinities = new AffinityIcons();
    public final Effects Effects = new Effects();
    public final Events Events = new Events();
    public final Orbs Orbs = new Orbs();

    public final TextureCache UnnamedReignEntrance        = new TextureCache("images/animator/ui/map/act5Entrance.png");
    public final TextureCache UnnamedReignEntranceOutline = new TextureCache("images/animator/ui/map/act5EntranceOutline.png");
    public final TextureCache CampfireOption_Enchant      = new TextureCache("images/animator/ui/campfire/Enchant.png");
    public final TextureCache Circle                      = new TextureCache("images/eyb/ui/topPanel/Circle.png");
    public final TextureCache Tag                         = new TextureCache("images/eyb/ui/topPanel/Tag.png");
    public final TextureCache Panel                       = new TextureCache("images/eyb/ui/topPanel/Panel.png");
    public final TextureCache Panel_Skewed_Left           = new TextureCache("images/eyb/ui/topPanel/Panel_Skewed_Left.png");
    public final TextureCache Panel_Skewed_Right          = new TextureCache("images/eyb/ui/topPanel/Panel_Skewed_Right.png");
    public final TextureCache Panel_Rounded               = new TextureCache("images/eyb/ui/topPanel/Panel_Rounded.png");
    public final TextureCache Panel_Rounded_Half_H        = new TextureCache("images/eyb/ui/topPanel/Panel_Rounded_Half_H.png");
    public final TextureCache Panel_Elliptical            = new TextureCache("images/eyb/ui/topPanel/Panel_Elliptical.png");
    public final TextureCache Panel_Elliptical_Half_H     = new TextureCache("images/eyb/ui/topPanel/Panel_Elliptical_Half_H.png");
    public final TextureCache Discord                     = new TextureCache("images/eyb/ui/topPanel/Discord.png");
    public final TextureCache Steam                       = new TextureCache("images/eyb/ui/topPanel/Steam.png");
    public final TextureCache Randomize                   = new TextureCache("images/eyb/ui/topPanel/Randomize.png");
    public final TextureCache SwapCards                   = new TextureCache("images/eyb/ui/topPanel/SwapCards.png");
    public final TextureCache SquaredButton               = new TextureCache("images/eyb/ui/topPanel/SquaredButton.png");
    public final TextureCache SquaredButton_EmptyCenter   = new TextureCache("images/eyb/ui/topPanel/SquaredButton_EmptyCenter.png");
    public final TextureCache HexagonalButton             = new TextureCache("images/eyb/ui/topPanel/HexagonalButton.png");
    public final TextureCache HexagonalButtonBorder       = new TextureCache("images/eyb/ui/topPanel/HexagonalButtonBorder.png");
    public final TextureCache HexagonalButtonHover        = new TextureCache("images/eyb/ui/topPanel/HexagonalButtonHover.png");
    public final TextureCache Arrow_Right                 = new TextureCache("images/eyb/ui/topPanel/Arrow_Right.png");
    public final TextureCache Draggable                   = new TextureCache("images/eyb/ui/topPanel/Draggable.png");
    public final TextureCache FullSquare                  = new TextureCache("images/eyb/ui/topPanel/FullSquare.png");

    public static class CardIcons
    {
        public final TextureCache Damage                = new TextureCache("images/eyb/cardui/core/NormalDamage.png");
        public final TextureCache Ranged                = new TextureCache("images/eyb/cardui/core/RangedDamage.png");
        public final TextureCache Piercing              = new TextureCache("images/eyb/cardui/core/PiercingDamage.png");
        public final TextureCache Block                 = new TextureCache("images/eyb/cardui/core/Block.png");
        public final TextureCache TempHP                = new TextureCache("images/eyb/cardui/core/TempHP.png");
        public final TextureCache Elemental             = new TextureCache("images/eyb/cardui/core/ElementalDamage.png");
        public final TextureCache Intellect             = new TextureCache("images/eyb/cardui/core/Intellect.png");
        public final TextureCache Force                 = new TextureCache("images/eyb/cardui/core/Force.png");
        public final TextureCache Agility               = new TextureCache("images/eyb/cardui/core/Agility.png");
    }

    public static class AffinityIcons
    {
        public final TextureCache General               = new TextureCache("images/eyb/cardui/affinities/General.png", true);
        public final TextureCache Green                 = new TextureCache("images/eyb/cardui/affinities/Green.png", true);
        public final TextureCache Red                   = new TextureCache("images/eyb/cardui/affinities/Red.png", true);
        public final TextureCache Blue                  = new TextureCache("images/eyb/cardui/affinities/Blue.png", true);
        public final TextureCache Light                 = new TextureCache("images/eyb/cardui/affinities/Light.png", true);
        public final TextureCache Dark                  = new TextureCache("images/eyb/cardui/affinities/Dark.png", true);
        public final TextureCache Star                  = new TextureCache("images/eyb/cardui/affinities/Star.png", true);
        public final TextureCache Star_BG               = new TextureCache("images/eyb/cardui/affinities/Star_BG.png", true);
        public final TextureCache Star_FG               = new TextureCache("images/eyb/cardui/affinities/Star_FG.png", true);
        public final TextureCache Neutral               = new TextureCache("images/eyb/cardui/affinities/Neutral.png", true);
        public final TextureCache Border                = new TextureCache("images/eyb/cardui/affinities/Border.png", false);
        public final TextureCache BorderFG              = new TextureCache("images/eyb/cardui/affinities/Border_FG.png", false);
        public final TextureCache BorderBG              = new TextureCache("images/eyb/cardui/affinities/Border_BG.png", true);
        public final TextureCache Border_Weak           = new TextureCache("images/eyb/cardui/affinities/Border_Weak.png", true);
    }

    public static class Badges
    {
        public final TextureCache Ethereal                = new TextureCache("images/cardui/eyb/badges/Ethereal.png");
        public final TextureCache Exhaust                 = new TextureCache("images/cardui/eyb/badges/Exhaust.png");
        public final TextureCache Purge                   = new TextureCache("images/cardui/eyb/badges/Purge.png");
        public final TextureCache Innate                  = new TextureCache("images/cardui/eyb/badges/Innate.png");
        public final TextureCache Retain                  = new TextureCache("images/cardui/eyb/badges/Retain.png");
        public final TextureCache RetainInfinite          = new TextureCache("images/cardui/eyb/badges/RetainInfinite.png");
        public final TextureCache RetainOnce              = new TextureCache("images/cardui/eyb/badges/RetainOnce.png");
        public final TextureCache Haste                   = new TextureCache("images/cardui/eyb/badges/Haste.png");
        public final TextureCache HasteInfinite           = new TextureCache("images/cardui/eyb/badges/HasteInfinite.png");
        public final TextureCache Autoplay                = new TextureCache("images/cardui/eyb/badges/Autoplay.png");
        public final TextureCache Afterlife               = new TextureCache("images/cardui/eyb/badges/Afterlife.png");
        public final TextureCache Harmonic                = new TextureCache("images/cardui/eyb/badges/Harmonic.png");
    }

    public static class Tooltips
    {
        public final TextureCache GriefSeed        = new TextureCache("images/eyb/cardui/tooltips/GriefSeed.png");
        public final TextureCache ThrowingKnife    = new TextureCache("images/eyb/cardui/tooltips/ThrowingKnife.png");
        public final TextureCache Lightning        = new TextureCache("images/eyb/cardui/tooltips/Lightning.png");
        public final TextureCache Plasma           = new TextureCache("images/eyb/cardui/tooltips/Plasma.png");
        public final TextureCache Dark             = new TextureCache("images/eyb/cardui/tooltips/Dark.png");
        public final TextureCache Aether           = new TextureCache("images/eyb/cardui/tooltips/Aether.png");
        public final TextureCache Frost            = new TextureCache("images/eyb/cardui/tooltips/Frost.png");
        public final TextureCache Earth            = new TextureCache("images/eyb/cardui/tooltips/Earth.png");
        public final TextureCache RandomOrb        = new TextureCache("images/eyb/cardui/tooltips/RandomOrb.png");
        public final TextureCache Fire             = new TextureCache("images/eyb/cardui/tooltips/Fire.png");
        public final TextureCache Chaos            = new TextureCache("images/eyb/cardui/tooltips/Chaos.png");
        public final TextureCache Gold             = new TextureCache("images/eyb/cardui/tooltips/Gold.png");
        public final TextureCache Regeneration     = new TextureCache("images/eyb/cardui/tooltips/Regeneration.png");
    }

    public static class Events
    {
        public final TextureCache Cabin1           = new TextureCache("images/animator/events/Cabin1.png");
        public final TextureCache Cabin2           = new TextureCache("images/animator/events/Cabin2.png");
        public final TextureCache CursedForest     = new TextureCache("images/animator/events/CursedForest.png");
        public final TextureCache GoldRiver        = new TextureCache("images/animator/events/GoldRiver.png");
        public final TextureCache MaskedTraveler   = new TextureCache("images/animator/events/MaskedTraveler.png");
        public final TextureCache Merchant         = new TextureCache("images/animator/events/Merchant.png");
        public final TextureCache Portal           = new TextureCache("images/animator/events/Portal.png");
        public final TextureCache Placeholder      = new TextureCache("images/animator/events/Placeholder.png");
    }

    public static class Orbs
    {
        public final TextureCache AirLeft          = new TextureCache("images/animator/orbs/AirLeft.png");
        public final TextureCache AirRight         = new TextureCache("images/animator/orbs/AirRight.png");
        public final TextureCache Chaos1           = new TextureCache("images/animator/orbs/Chaos1.png");
        public final TextureCache Chaos2           = new TextureCache("images/animator/orbs/Chaos2.png");
        public final TextureCache Chaos3           = new TextureCache("images/animator/orbs/Chaos3.png");
        public final TextureCache Earth1           = new TextureCache("images/animator/orbs/Earth1.png");
        public final TextureCache Earth2           = new TextureCache("images/animator/orbs/Earth2.png");
        public final TextureCache Earth3           = new TextureCache("images/animator/orbs/Earth3.png");
        public final TextureCache Earth4           = new TextureCache("images/animator/orbs/Earth4.png");
        public final TextureCache FireExternal     = new TextureCache("images/animator/orbs/FireExternal.png");
        public final TextureCache FireInternal     = new TextureCache("images/animator/orbs/FireInternal.png");
    }

    public static class Effects
    {
        public final TextureCache Sparkle1         = new TextureCache("images/animator/effects/Sparkle1.png");
        public final TextureCache Sparkle2         = new TextureCache("images/animator/effects/Sparkle2.png");
        public final TextureCache Sparkle3         = new TextureCache("images/animator/effects/Sparkle3.png");
        public final TextureCache Sparkle4         = new TextureCache("images/animator/effects/Sparkle4.png");
        public final TextureCache Shot             = new TextureCache("images/animator/effects/Shot.png");
        public final TextureCache Star             = new TextureCache("images/animator/effects/Star.png");
        public final TextureCache Whack            = new TextureCache("images/animator/effects/Whack.png");
        public final TextureCache FrostSnow1       = new TextureCache("images/animator/effects/FrostSnow1.png");
        public final TextureCache FrostSnow2       = new TextureCache("images/animator/effects/FrostSnow2.png");
        public final TextureCache FrostSnow3       = new TextureCache("images/animator/effects/FrostSnow3.png");
        public final TextureCache FrostSnow4       = new TextureCache("images/animator/effects/FrostSnow4.png");
        public final TextureCache AirSlice         = new TextureCache("images/animator/effects/AirSlice.png");
        public final TextureCache AirTrail         = new TextureCache("images/animator/effects/AirTrail.png");
    }
}