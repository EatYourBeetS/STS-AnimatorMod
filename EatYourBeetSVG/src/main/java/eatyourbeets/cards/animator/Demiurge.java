package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.VariableDiscardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;

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

        GameActionsHelper2.GainEnergy(1);
        GameActionsHelper2.Cycle(1, name);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActionsHelper2.DiscardFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .AddCallback(cards -> ExecuteEffect(cards.isEmpty()));
        }
        else
        {
            ExecuteEffect(true);
        }
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }

    private void ExecuteEffect(boolean takeDamage)
    {
        GameActionsHelper2.GainEnergy(1);

        if (takeDamage)
        {
            GameActionsHelper2.StackPower(new SelfDamagePower(AbstractDungeon.player, magicNumber));
        }
    }
}