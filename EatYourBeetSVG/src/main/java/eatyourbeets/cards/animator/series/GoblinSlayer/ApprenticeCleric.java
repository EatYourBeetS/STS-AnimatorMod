package eatyourbeets.cards.animator.series.GoblinSlayer;

import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ApprenticeCleric extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Light(2);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainBlessing(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlessing(1, upgraded);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.IncreaseScaling(p.hand, BaseMod.MAX_HAND_SIZE, Affinity.Light, 1)
        .SetFilter(c -> (GameUtilities.HasRedAffinity(c) || GameUtilities.HasGreenAffinity(c)));
    }
}