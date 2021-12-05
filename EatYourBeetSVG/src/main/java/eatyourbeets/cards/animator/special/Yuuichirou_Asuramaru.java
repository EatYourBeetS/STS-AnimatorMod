package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Yuuichirou_Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 2, 10);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Dark, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateLimited()) {
            GameUtilities.SetAffinityPowerLevel(Affinity.Red, secondaryValue, true);
        }

        if (TrySpendAffinity(Affinity.Dark)) {
            GameActions.Bottom.GainMight(magicNumber);
            GameActions.Bottom.GainVelocity(magicNumber);
            GameActions.Bottom.GainWisdom(magicNumber);
            GameActions.Bottom.MakeCardInHand(new Status_Burn());
        }
    }
}