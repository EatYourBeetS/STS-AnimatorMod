package eatyourbeets.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.animator.*;

import java.util.HashMap;

public abstract class AnimatorCard_UltraRare extends AnimatorCard implements AnimatorResources.Hidden
{
    private static final float IM_LAZY = 0.4f;
    public static final Color RENDER_COLOR = new Color(IM_LAZY, IM_LAZY, IM_LAZY, 1);

    private static final float IM_LAZY2 = 0.4f;
    public static final Color RENDER_COLOR2 = new Color(IM_LAZY2, IM_LAZY2, IM_LAZY2, 1);

    protected AnimatorCard_UltraRare(String id, int cost, CardType type, CardTarget target)
    {
        //super(id, cost, type, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.SPECIAL, target);
        super(id, cost, type, CardColor.COLORLESS, CardRarity.SPECIAL, target);

        setOrbTexture(AnimatorResources.ORB_A_PNG, AnimatorResources.ORB_B_PNG);
    }

    private static HashMap<String, AbstractCard> Cards = null;

    public static HashMap<String, AbstractCard> GetCards()
    {
        if (Cards == null)
        {
            Cards = new HashMap<>();
            Cards.put(Chomusuke.ID, new Chomusuke());
            Cards.put(Giselle.ID, new Giselle());
            Cards.put(Veldora.ID, new Veldora());
            Cards.put(Rose.ID, new Rose());
            Cards.put(Truth.ID, new Truth());
            Cards.put(Azriel.ID, new Azriel());
            Cards.put(Hero.ID, new Hero());
            Cards.put(SirTouchMe.ID, new SirTouchMe());
            Cards.put(ShikizakiKiki.ID, new ShikizakiKiki());
            Cards.put(HiiragiTenri.ID, new HiiragiTenri());
            Cards.put(JeanneDArc.ID, new JeanneDArc());
            Cards.put(NivaLada.ID, new NivaLada());
        }

        return Cards;
    }

//    @SpireOverride
//    protected void renderAttackPortrait(SpriteBatch sb, float x, float y)
//    {
//        switch (this.rarity)
//        {
//            case BASIC:
//            case CURSE:
//            case COMMON:
//                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
//                return;
//
//            case SPECIAL:
//                this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_FRAME_ATTACK_SPECIAL, x, y);
//                return;
//
//            case UNCOMMON:
//                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
//                return;
//
//            case RARE:
//                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
//        }
//    }

    @SpireOverride
    protected void renderAttackBg(SpriteBatch sb, float x, float y)
    {
        //this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_BG_ATTACK, x, y);
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderSkillBg(SpriteBatch sb, float x, float y)
    {
        //this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_BG_SKILL, x, y);
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderPowerBg(SpriteBatch sb, float x, float y)
    {
        //this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_BG_POWER, x, y);
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_POWER_BG_GRAY, x, y);
    }
}