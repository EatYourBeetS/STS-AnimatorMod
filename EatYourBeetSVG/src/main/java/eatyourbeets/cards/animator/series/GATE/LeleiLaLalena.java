package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class LeleiLaLalena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LeleiLaLalena.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.General), false));

    public LeleiLaLalena()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(1);

        SetEvokeOrbCount(1);

        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1);

        if (TryUseAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.General, upgraded);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber));
    }
}