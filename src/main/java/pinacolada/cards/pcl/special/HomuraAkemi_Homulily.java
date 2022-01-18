package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.cards.pcl.series.MadokaMagica.SayakaMiki;
import pinacolada.utilities.PCLActions;

public class HomuraAkemi_Homulily extends PCLCard
{
    public static final PCLCardData DATA = Register(HomuraAkemi_Homulily.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public HomuraAkemi_Homulily()
    {
        super(DATA);

        Initialize(0, 0, 4, 7);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Dark(1);
        SetPurge(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue);
        PCLActions.Bottom.GainEnergyNextTurn(magicNumber);
    }
}
