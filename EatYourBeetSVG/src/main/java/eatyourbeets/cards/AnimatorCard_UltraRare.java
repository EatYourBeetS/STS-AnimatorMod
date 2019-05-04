package eatyourbeets.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.AnimatorResources;
import patches.AbstractEnums;

public abstract class AnimatorCard_UltraRare extends AnimatorCard
{
    protected AnimatorCard_UltraRare(String id, int cost, CardType type, CardTarget target)
    {
        super(id, cost, type, CardColor.COLORLESS, CardRarity.SPECIAL, target);
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
    }

//    @Override
//    protected void renderAttackPortrait(SpriteBatch sb, float x, float y)
//    {
//        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
//    }
//
//    @Override
//    protected void renderPowerPortrait(SpriteBatch sb, float x, float y)
//    {
//        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
//    }
//
//    @Override
//    protected void renderSkillPortrait(SpriteBatch sb, float x, float y)
//    {
//        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
//    }
//
    private final Color RENDER_COLOR = new Color(0.3f, 0.3f, 0.3f, 1);

    @SpireOverride
    protected void renderAttackBg(SpriteBatch sb, float x, float y)
    {
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderSkillBg(SpriteBatch sb, float x, float y)
    {
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderPowerBg(SpriteBatch sb, float x, float y)
    {
        this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_POWER_BG_GRAY, x, y);
    }
}