package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yusa extends PCLCard
{
    public static final PCLCardData DATA = Register(Yusa.class).SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    public Yusa()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Top.Scry(secondaryValue).AddCallback(() -> {
            PCLActions.Top.ExhaustFromPile(name, 1, p.discardPile)
                    .SetOptions(false,true)
                    .AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (PCLGameUtilities.HasLightAffinity(c)) {
                        PCLActions.Bottom.GainTemporaryHP(magicNumber);
                    }
                }
            });
        });
    }
}