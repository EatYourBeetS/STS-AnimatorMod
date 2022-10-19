package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Konayuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Konayuki.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(GetClassCard(Wound.ID), false));

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 1, 0);

        SetAffinityRequirement(Affinity.Red, 1);
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.ShowCopy(this);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1).IgnoreArtifact(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.MakeCardInDrawPile(GetCurrentClassCard(Wound.ID, false));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1).IgnoreArtifact(true);
        }
    }
}