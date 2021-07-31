package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class AffinityToken_Light extends AffinityToken
{
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;
    public static final EYBCardData DATA = Register(AffinityToken_Light.class);

    public AffinityToken_Light()
    {
        super(DATA, AFFINITY_TYPE);

        SetAffinityRequirement(AffinityType.Dark, secondaryValue);
        SetAffinityRequirement(AffinityType.Red, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        super.OnUse(p, m, isSynergizing);

        if (CheckAffinity(AffinityType.Dark) || CheckAffinity(AffinityType.Red))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}