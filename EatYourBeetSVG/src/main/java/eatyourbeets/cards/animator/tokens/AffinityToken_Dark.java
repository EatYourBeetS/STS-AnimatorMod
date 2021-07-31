package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class AffinityToken_Dark extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Dark;
    public static final EYBCardData DATA = Register(AffinityToken_Dark.class);

    public AffinityToken_Dark()
    {
        super(DATA, AFFINITY_TYPE);

        SetAffinityRequirement(AffinityType.Light, secondaryValue);
        SetAffinityRequirement(AffinityType.Green, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        super.OnUse(p, m, isSynergizing);

        if (CheckAffinity(AffinityType.Light) || CheckAffinity(AffinityType.Green))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}