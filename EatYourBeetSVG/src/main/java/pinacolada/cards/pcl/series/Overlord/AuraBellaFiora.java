package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AuraBellaFiora extends PCLCard
{
    public static final PCLCardData DATA = Register(AuraBellaFiora.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new MareBelloFiore(), true);
            });

    public AuraBellaFiora()
    {
        super(DATA);

        Initialize(0, 4, 3, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1, 0, 1);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (IsStarter() ? magicNumber : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Motivate().SetFilter(PCLGameUtilities::IsHighCost);
        PCLActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, true).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                if (MareBelloFiore.DATA.ID.equals(c.cardID)) {
                    PCLActions.Last.PlayCard(c, player.discardPile, m);
                }
            }
        });
    }
}