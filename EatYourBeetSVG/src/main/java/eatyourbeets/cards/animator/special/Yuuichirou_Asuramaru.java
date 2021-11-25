package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou_Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.IncreaseAffinityPowerLevel(Affinity.Dark, 1);
        for (int i = 0; i < CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true) / secondaryValue; i++) {
            GameActions.Bottom.GainMight(magicNumber);
            GameActions.Bottom.GainVelocity(magicNumber);
            GameActions.Bottom.GainWisdom(magicNumber);
            GameActions.Bottom.MakeCardInHand(new Status_Burn());
        }
    }
}