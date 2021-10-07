package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AnimatorCard_Status extends AnimatorCard
{
    protected boolean playAtEndOfTurn;

    protected AnimatorCard_Status(EYBCardData data, boolean playAtEndOfTurn)
    {
        super(data);

        this.playAtEndOfTurn = playAtEndOfTurn;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        if (playAtEndOfTurn)
        {
            dontTriggerOnUseCard = true;

            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
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
    public int SetForm(Integer form, int timesUpgraded) {
        this.cardText.OverrideDescription(form > 0 && cardData.Strings.EXTENDED_DESCRIPTION != null && cardData.Strings.EXTENDED_DESCRIPTION.length >= form ? cardData.Strings.EXTENDED_DESCRIPTION[form - 1] : null, true);
        this.portraitImg.color = form != 0 ? new Color(0.5f, 0.5f, 0.5f, 1f) : Color.WHITE.cpy();
        return super.SetForm(form, timesUpgraded);
    };
}