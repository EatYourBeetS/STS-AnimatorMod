package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shuna extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Shuna.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Drawn);

    public Shuna()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2, 3);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, 1);
        GameActionsHelper.GainTemporaryHP(p, magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainBlock(p, block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            this.applyPowers();
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, this.block);
        }
    }
}