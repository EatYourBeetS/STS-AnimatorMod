package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.AgilityStance;
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

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(2);
        SetAffinity_Dark(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlur(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);

        int knives = 0;
        if (AgilityStance.IsActive())
        {
            knives += 1;
        }
        if (CheckAffinity(Affinity.Dark))
        {
            knives += 1;
        }

        GameActions.Bottom.CreateThrowingKnives(knives);
    }
}