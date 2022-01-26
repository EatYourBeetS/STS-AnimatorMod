package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Geryuganshoop extends PCLCard
{
    public static final PCLCardData DATA = Register(Geryuganshoop.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Geryuganshoop()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,0,1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Silver(1);

        SetDrawPileCardPreview(PCLGameUtilities::HasDarkAffinity);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        drawPileCardPreview.SetEnabled(IsStarter());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IsStarter())
        {
            PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile)
            .SetFilter(PCLGameUtilities::HasDarkAffinity);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!IsStarter())
        {
            PCLActions.Bottom.Cycle(name, magicNumber);
        }

        PCLActions.Delayed.PurgeFromPile(name, secondaryValue, player.exhaustPile)
        .SetMessage(PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.GainEnergyNextTurn(cards.size());
            }
        });
    }
}