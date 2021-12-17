package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yuuichirou_Asuramaru extends PCLCard
{
    public static final PCLCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 2, 10);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Dark(2);

        SetAffinityRequirement(PCLAffinity.Dark, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateLimited()) {
            PCLGameUtilities.AddAffinityPowerLevel(PCLAffinity.Red, secondaryValue);
        }

        if (TrySpendAffinity(PCLAffinity.Dark)) {
            PCLActions.Bottom.GainMight(magicNumber);
            PCLActions.Bottom.GainVelocity(magicNumber);
            PCLActions.Bottom.GainWisdom(magicNumber);
            PCLActions.Bottom.MakeCardInHand(new Status_Burn());
        }
    }
}