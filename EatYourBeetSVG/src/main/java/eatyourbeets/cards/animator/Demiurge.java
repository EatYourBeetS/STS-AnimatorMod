package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.DemiurgePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Demiurge extends AnimatorCard
{
    public static final String ID = Register(Demiurge.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Demiurge()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,4);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.GainEnergy(1);
        GameActionsHelper.CycleCardAction(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainEnergy(1);
        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new VariableDiscardAction(this, p, 1, this, this::OnDiscard));
        }
        else
        {
            GameActionsHelper.ApplyPowerSilently(p, p, new DemiurgePower(p, magicNumber), magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null)
        {
            if (cards.size() == 0)
            {
                AbstractPlayer p = AbstractDungeon.player;
                GameActionsHelper.ApplyPowerSilently(p, p, new DemiurgePower(p, magicNumber), magicNumber);
            }
        }
    }
}