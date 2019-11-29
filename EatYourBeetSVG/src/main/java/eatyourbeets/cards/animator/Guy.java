package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.DiscardTopCardsAction;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper;

public class Guy extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Guy.class.getSimpleName(), EYBCardBadge.Synergy);

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 1, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.Discard(this.magicNumber, false);

        if (HasActiveSynergy())
        {
            GameActionsHelper.DelayedAction(this);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        AbstractPlayer p = AbstractDungeon.player;
        int max = Math.min(p.drawPile.size(), secondaryValue);
        for (int i = 0; i < max; i++)
        {
            AbstractCard card = p.drawPile.getNCardFromTop(i);
            card.target_x = Settings.WIDTH * (0.3f + (i * 0.02f));
            card.target_y = Settings.HEIGHT * (0.4f + (i * 0.02f));
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(card, p.discardPile, p.drawPile, true));
            GameActionsHelper.Wait(0.2f);
        }
    }
}