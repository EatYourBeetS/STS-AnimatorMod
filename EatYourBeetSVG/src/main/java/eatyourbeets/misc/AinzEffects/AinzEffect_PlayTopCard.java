package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;


public class AinzEffect_PlayTopCard extends AinzEffect
{
    public AinzEffect_PlayTopCard(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {

    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        GameActionsHelper.AddToBottom(new PlayTopCardAction(target, false));
    }
}