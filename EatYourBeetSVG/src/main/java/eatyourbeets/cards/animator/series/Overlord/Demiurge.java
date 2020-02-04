package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;

public class Demiurge extends AnimatorCard
{
    public static final String ID = Register_Old(Demiurge.class);

    public Demiurge()
    {
        super(ID, 0, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0,0,4);

        SetSynergy(Synergies.Overlord);
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

        if (HasSynergy() && !p.hasPower(ConservePower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new ConservePower(p, 1));
        }
    }

    private void ExecuteEffect(boolean takeDamage)
    {
        GameActions.Bottom.GainEnergy(1);

        if (takeDamage)
        {
            GameActions.Bottom.StackPower(new SelfDamagePower(player, magicNumber));
        }
    }
}