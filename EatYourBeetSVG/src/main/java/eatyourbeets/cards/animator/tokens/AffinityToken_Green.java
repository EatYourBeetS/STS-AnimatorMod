package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class AffinityToken_Green extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Green;
    public static final EYBCardData DATA = Register(AffinityToken_Green.class);

    public AffinityToken_Green()
    {
        super(DATA, AFFINITY_TYPE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        super.OnUse(p, m, isSynergizing);

        if (CheckAffinity(AffinityType.Red, secondaryValue) || CheckAffinity(AffinityType.Blue, secondaryValue))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}