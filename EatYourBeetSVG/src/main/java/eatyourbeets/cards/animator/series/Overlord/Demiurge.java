package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;

public class Demiurge extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Demiurge.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Demiurge()
    {
        super(DATA);

        Initialize(0,0,4);

        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Orange(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

        if (isSynergizing && !p.hasPower(ConservePower.POWER_ID))
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