package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Sonic extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sonic.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public Sonic()
    {
        super(DATA);

        Initialize(0, 6, 1, 1);
        SetCostUpgrade(-1);

        SetAffinity_Green(2);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlur(secondaryValue);
        GameActions.Bottom.StackPower(new SonicPower(p, magicNumber));

        if (TryUseAffinity(Affinity.Green))
        {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }

    public static class SonicPower extends AnimatorPower
    {
        public SonicPower(AbstractCreature owner, int amount)
        {
            super(owner, Sonic.DATA);

            priority += 1;

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Delayed.DiscardFromHand(name, amount, true)
            .ShowEffect(true, true);
            RemovePower();
        }
    }
}