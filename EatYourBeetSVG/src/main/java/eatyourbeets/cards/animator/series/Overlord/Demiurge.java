package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Demiurge extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Demiurge.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Demiurge()
    {
        super(DATA);

        Initialize(0,0,7);
        SetUpgrade(0,0,-2);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (misc > 0)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(misc, AttackEffects.DARK);
            GameActions.Bottom.Flash(this);
            misc = 0;
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false);
        GameActions.Bottom.GainEnergy(1);
        GameActions.Bottom.GainCorruption(1);
        GameActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c -> c.misc += magicNumber);
    }
}