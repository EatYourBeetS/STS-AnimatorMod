package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;

public class Sonic extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sonic.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
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

        Initialize(0, 1, 2);
        SetUpgrade(0, 1, 0);

        SetAffinity_Green(1,0,3);
        SetAffinity_Dark(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                this.AddScaling(Affinity.Green, 1);
            }
            else {
                SetHaste(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlur(magicNumber);
        GameActions.Bottom.GainVelocity(magicNumber);

        int knives = 0;
        if (VelocityStance.IsActive())
        {
            knives += 1;
        }
        if (TrySpendAffinity(Affinity.Dark))
        {
            knives += 1;
        }

        GameActions.Bottom.CreateThrowingKnives(knives);
    }
}