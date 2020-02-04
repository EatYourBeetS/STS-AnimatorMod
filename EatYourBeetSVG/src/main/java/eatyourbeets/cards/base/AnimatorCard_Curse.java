package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RenderHelpers;

public abstract class AnimatorCard_Curse extends AnimatorCard
{
    protected AnimatorCard_Curse(String id, int cost, CardRarity rarity, CardTarget target)
    {
        super(id, cost, CardType.CURSE, CardColor.CURSE, rarity, target);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        RenderHelpers.DrawOnCardCentered(sb, this, new Color(0.3f, 0.3f, 0.3f, transparency), GR.Animator.Images.CARD_BACKGROUND_SKILL_UR.Texture(), x, y);
    }
}