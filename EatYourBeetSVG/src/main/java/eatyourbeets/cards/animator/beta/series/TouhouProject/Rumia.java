package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;

public class Rumia extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Rumia.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public Rumia()
    {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Dark(1);
        SetAffinity_Blue(1);

        SetProtagonist(true);
        SetHarmonic(true);

        SetAffinityRequirement(Affinity.Light, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}

