package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class TsurugaMeisai extends PCLCard
{
    public static final PCLCardData DATA = Register(TsurugaMeisai.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 2, 5, 2);
        SetUpgrade(0, 2, 0);

        SetAffinity_Light(1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, magicNumber));
        PCLActions.Bottom.CreateThrowingKnives(secondaryValue, player.drawPile).AddCallback(c -> {
            if (upgraded) {
                PCLGameUtilities.SetCardTag(c, HASTE, true);
            }
        });
        PCLActions.Bottom.DiscardFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .AddCallback(cards -> {
                   if (cards.size() > 0) {
                       PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, secondaryValue));
                   }
                });
    }
}