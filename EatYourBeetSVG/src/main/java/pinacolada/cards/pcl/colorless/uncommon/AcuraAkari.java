package pinacolada.cards.pcl.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.powers.replacement.TemporaryEnvenomPower;
import pinacolada.utilities.PCLActions;

public class AcuraAkari extends PCLCard
{
    public static final PCLCardData DATA = Register(AcuraAkari.class)
            .SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(3)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraAkari()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .AddCallback(() -> PCLActions.Bottom.CreateThrowingKnives(magicNumber).SetUpgrade(upgraded));

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}