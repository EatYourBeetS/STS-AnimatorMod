package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Kuroyukihime_BlackLotus;
import pinacolada.utilities.PCLActions;

public class Kuroyukihime extends PCLCard
{
    public static final PCLCardData DATA = Register(Kuroyukihime.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.AccelWorld)
            .PostInitialize(data -> data.AddPreview(new Kuroyukihime_BlackLotus(), true));

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 1, 2, 1);
        SetCostUpgrade(-1);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Silver(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.General, 8);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainBlur(secondaryValue);
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            PCLActions.Bottom.MakeCardInHand(new Kuroyukihime_BlackLotus())
            .SetUpgrade(CheckAffinity(PCLAffinity.General), false);
        });
    }
}