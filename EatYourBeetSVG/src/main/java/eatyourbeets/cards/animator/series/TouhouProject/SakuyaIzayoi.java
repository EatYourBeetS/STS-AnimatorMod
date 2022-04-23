package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SakuyaIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuyaIzayoi.class)
            .SetSkill(X_COST, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public SakuyaIzayoi()
    {
        super(DATA);

        Initialize(0, 2);
        SetUpgrade(0, 2);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        int x = GameUtilities.UseXCostEnergy(this);
        if (TryUseAffinity(Affinity.Green))
        {
            x += 1;
        }
        if (AgilityStance.IsActive())
        {
            x += 1;
        }

        if (x > 0)
        {
            GameActions.Bottom.GainAgility(upgraded ? x : (x - 1));
            GameActions.Bottom.StackPower(new SakuyaIzayoiPower(p, x));
        }
    }

    public static class SakuyaIzayoiPower extends AnimatorPower
    {
        public SakuyaIzayoiPower(AbstractCreature owner, int amount)
        {
            super(owner, SakuyaIzayoi.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.CreateThrowingKnives(amount);
            RemovePower();
        }
    }
}