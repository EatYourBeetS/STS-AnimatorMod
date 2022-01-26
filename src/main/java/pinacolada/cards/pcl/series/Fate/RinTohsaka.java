package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.special.OrbCore;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RinTohsaka extends PCLCard
{
    public static final PCLCardData DATA = Register(RinTohsaka.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .AddPreviews(OrbCore.GetAllCores(), true);

    public RinTohsaka()
    {
        super(DATA);

        Initialize(0, 3, 0, 2);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (PCLJUtils.Find(PCLGameUtilities.GetPCLIntents(), i -> !i.IsDebuffing()) != null) {
            PCLActions.Bottom.GainTemporaryArtifact(secondaryValue);
        }
        else {
            PCLActions.Bottom.TriggerOrbPassive(secondaryValue,true,false);
        }
        PCLActions.Bottom.GainBlock(block);


        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLActions.Bottom.MakeCardInHand(c).SetUpgrade(upgraded, false);
                }
            }));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return PCLGameUtilities.GetUniqueOrbsCount() >= 3 && (tryUse ? CombatStats.TryActivateLimited(cardID) : CombatStats.CanActivateLimited(cardID));
    }
}