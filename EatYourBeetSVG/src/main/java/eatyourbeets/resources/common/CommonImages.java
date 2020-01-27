package eatyourbeets.resources.common;

import eatyourbeets.ui.TextureCache;

public class CommonImages
{
    public final Badges Badges = new Badges();

    public final TextureCache UnnamedReignEntrance        = new TextureCache("images/ui/map/act5Entrance.png");
    public final TextureCache UnnamedReignEntranceOutline = new TextureCache("images/ui/map/act5EntranceOutline.png");
    public final TextureCache Panel                       = new TextureCache("images/ui/topPanel/eyb/Panel.png");
    public final TextureCache HexagonalButton             = new TextureCache("images/ui/topPanel/eyb/HexagonalButton.png");
    public final TextureCache HexagonalButtonBorder       = new TextureCache("images/ui/topPanel/eyb/HexagonalButtonBorder.png");
    public final TextureCache HexagonalButtonHover        = new TextureCache("images/ui/topPanel/eyb/HexagonalButtonHover.png");
    public final TextureCache CardBadgeLegend             = new TextureCache("images/cardui/eyb/badges/_Legend.png");
    public final TextureCache NormalDamage                = new TextureCache("images/cardui/eyb/core/NormalDamage.png");
    public final TextureCache RangedDamage                = new TextureCache("images/cardui/eyb/core/RangedDamage.png");
    public final TextureCache PiercingDamage              = new TextureCache("images/cardui/eyb/core/PiercingDamage.png");
    public final TextureCache Block                       = new TextureCache("images/cardui/eyb/core/Block.png");
    public final TextureCache TempHP                      = new TextureCache("images/cardui/eyb/core/TempHP.png");
    public final TextureCache ElementalDamage             = new TextureCache("images/cardui/eyb/core/ElementalDamage.png");

    public static class Badges
    {
        public final TextureCache Ethereal                = new TextureCache("images/cardui/eyb/badges/Ethereal.png");
        public final TextureCache Exhaust                 = new TextureCache("images/cardui/eyb/badges/Exhaust.png");
        public final TextureCache Innate                  = new TextureCache("images/cardui/eyb/badges/Innate.png");
        public final TextureCache Retain                  = new TextureCache("images/cardui/eyb/badges/Retain.png");
    }
}
