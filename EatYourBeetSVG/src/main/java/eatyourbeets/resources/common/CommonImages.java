package eatyourbeets.resources.common;

import eatyourbeets.ui.TextureCache;

public class CommonImages
{
    public final Badges Badges = new Badges();
    public final CardIcons Icons = new CardIcons();
    public final Tooltips Tooltips = new Tooltips();
    public final AlignmentsIcons Alignments = new AlignmentsIcons();

    public final TextureCache UnnamedReignEntrance        = new TextureCache("images/ui/map/act5Entrance.png");
    public final TextureCache UnnamedReignEntranceOutline = new TextureCache("images/ui/map/act5EntranceOutline.png");
    public final TextureCache Circle                      = new TextureCache("images/ui/topPanel/eyb/Circle.png");
    public final TextureCache Panel                       = new TextureCache("images/ui/topPanel/eyb/Panel.png");
    public final TextureCache Panel_Skewed_Left           = new TextureCache("images/ui/topPanel/eyb/Panel_Skewed_Left.png");
    public final TextureCache Panel_Skewed_Right          = new TextureCache("images/ui/topPanel/eyb/Panel_Skewed_Right.png");
    public final TextureCache Panel_Rounded               = new TextureCache("images/ui/topPanel/eyb/Panel_Rounded.png");
    public final TextureCache Panel_Rounded_Half_H        = new TextureCache("images/ui/topPanel/eyb/Panel_Rounded_Half_H.png");
    public final TextureCache Discord                     = new TextureCache("images/ui/topPanel/eyb/Discord.png");
    public final TextureCache Steam                       = new TextureCache("images/ui/topPanel/eyb/Steam.png");
    public final TextureCache Randomize                   = new TextureCache("images/ui/topPanel/eyb/Randomize.png");
    public final TextureCache HexagonalButton             = new TextureCache("images/ui/topPanel/eyb/HexagonalButton.png");
    public final TextureCache HexagonalButtonBorder       = new TextureCache("images/ui/topPanel/eyb/HexagonalButtonBorder.png");
    public final TextureCache HexagonalButtonHover        = new TextureCache("images/ui/topPanel/eyb/HexagonalButtonHover.png");
    public final TextureCache CardBadgeLegend             = new TextureCache("images/cardui/eyb/badges/_Legend.png");

    public static class CardIcons
    {
        public final TextureCache Damage                = new TextureCache("images/cardui/eyb/core/NormalDamage.png");
        public final TextureCache Ranged                = new TextureCache("images/cardui/eyb/core/RangedDamage.png");
        public final TextureCache Piercing              = new TextureCache("images/cardui/eyb/core/PiercingDamage.png");
        public final TextureCache Block                 = new TextureCache("images/cardui/eyb/core/Block.png");
        public final TextureCache TempHP                = new TextureCache("images/cardui/eyb/core/TempHP.png");
        public final TextureCache Elemental             = new TextureCache("images/cardui/eyb/core/ElementalDamage.png");
        public final TextureCache Intellect             = new TextureCache("images/cardui/eyb/core/Intellect.png");
        public final TextureCache Force                 = new TextureCache("images/cardui/eyb/core/Force.png");
        public final TextureCache Agility               = new TextureCache("images/cardui/eyb/core/Agility.png");
    }

    public static class AlignmentsIcons
    {
        public final TextureCache Green                 = new TextureCache("images/cardui/eyb/alignments/Green.png", true);
        public final TextureCache Red                   = new TextureCache("images/cardui/eyb/alignments/Red.png", true);
        public final TextureCache Blue                  = new TextureCache("images/cardui/eyb/alignments/Blue.png", true);
        public final TextureCache Light                 = new TextureCache("images/cardui/eyb/alignments/Light.png", true);
        public final TextureCache Dark                  = new TextureCache("images/cardui/eyb/alignments/Dark.png", true);
        public final TextureCache Star                  = new TextureCache("images/cardui/eyb/alignments/Star.png", true);
        public final TextureCache Border                = new TextureCache("images/cardui/eyb/alignments/Border.png", true);
        public final TextureCache Border_Weak           = new TextureCache("images/cardui/eyb/alignments/Border_Weak.png", true);
    }

    public static class Badges
    {
        public final TextureCache Ethereal                = new TextureCache("images/cardui/eyb/badges/Ethereal.png");
        public final TextureCache Exhaust                 = new TextureCache("images/cardui/eyb/badges/Exhaust.png");
        public final TextureCache Purge                   = new TextureCache("images/cardui/eyb/badges/Purge.png");
        public final TextureCache Innate                  = new TextureCache("images/cardui/eyb/badges/Innate.png");
        public final TextureCache Retain                  = new TextureCache("images/cardui/eyb/badges/Retain.png");
        public final TextureCache Haste                   = new TextureCache("images/cardui/eyb/badges/Haste.png");
    }

    public static class Tooltips
    {
        public final TextureCache GriefSeed        = new TextureCache("images/cardui/eyb/tooltips/GriefSeed.png");
        public final TextureCache ThrowingKnife    = new TextureCache("images/cardui/eyb/tooltips/ThrowingKnife.png");
        public final TextureCache Lightning        = new TextureCache("images/cardui/eyb/tooltips/Lightning.png");
        public final TextureCache Plasma           = new TextureCache("images/cardui/eyb/tooltips/Plasma.png");
        public final TextureCache Dark             = new TextureCache("images/cardui/eyb/tooltips/Dark.png");
        public final TextureCache Aether           = new TextureCache("images/cardui/eyb/tooltips/Aether.png");
        public final TextureCache Frost            = new TextureCache("images/cardui/eyb/tooltips/Frost.png");
        public final TextureCache Earth            = new TextureCache("images/cardui/eyb/tooltips/Earth.png");
        public final TextureCache RandomOrb        = new TextureCache("images/cardui/eyb/tooltips/RandomOrb.png");
        public final TextureCache Fire             = new TextureCache("images/cardui/eyb/tooltips/Fire.png");
        public final TextureCache Gold             = new TextureCache("images/cardui/eyb/tooltips/Gold.png");
    }
}