package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

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

        GameActions.Bottom.GainEnergy(1);
        GameActions.Bottom.Cycle(1, name);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActions.Bottom.DiscardFromHand(name, 1, false)
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
        GameActions.Bottom.GainEnergy(1);

        if (takeDamage)
        {
            GameActions.Bottom.StackPower(new SelfDamagePower(AbstractDungeon.player, magicNumber));
        }
    }
}