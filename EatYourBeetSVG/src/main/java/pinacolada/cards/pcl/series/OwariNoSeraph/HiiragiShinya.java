package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HiiragiShinya extends PCLCard
{
    public static final PCLCardData DATA = Register(HiiragiShinya.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public HiiragiShinya()
    {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Green,3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetMessage(MoveCardsAction.TEXT[0])
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                PCLGameUtilities.ModifyCostForTurn(card, 1, true);
                PCLGameUtilities.Retain(card);
                PCLActions.Bottom.Add(new RefreshHandLayout());
            }
        });

        if (TrySpendAffinity(PCLAffinity.Green))
        {
            PCLActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber));
        }
    }
}