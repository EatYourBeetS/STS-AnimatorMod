package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HaruhiSuzumiya extends PCLCard
{
    public static final PCLCardData DATA = Register(HaruhiSuzumiya.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HaruhiSuzumiya);
    private CardType cardType;

    public HaruhiSuzumiya()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetCostUpgrade(-1);
        SetEthereal(true);
        SetExhaust(true);

        SetAffinity_Star(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.General, 18);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup[] groups = CheckAffinity(PCLAffinity.General) && info.TryActivateLimited() ? (new CardGroup[] {p.hand, p.discardPile, p.drawPile}) : (new CardGroup[] {p.hand});
        PCLActions.Bottom.ExhaustFromPile(name, 999, groups)
                .SetOptions(true, true)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        AbstractCard newCard = PCLGameUtilities.GetRandomCard();
                        if (newCard != null)
                        {
                            PCLActions.Bottom.MakeCardInHand(newCard).AddCallback(ca -> {
                                PCLActions.Bottom.Motivate(ca, 1);
                            });
                        }
                    }
                });
    }
}