package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Zero extends PCLCard
{
    public static final PCLCardData DATA = Register(Zero.class)
            .SetSkill(0, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GrimoireOfZero);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetDrawPileCardPreview(this::FindCards);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainWisdom(magicNumber);
        if (upgraded) {
            PCLActions.Bottom.Draw(1);
        }
        PCLActions.Bottom.PlayFromPile(name, 1, m, p.drawPile)
        .SetOptions(true, false)
        .SetFilter(c -> c.type == CardType.SKILL).AddCallback(cards -> {
                for (AbstractCard ca : cards) {
                    if (ca.rarity == CardRarity.BASIC && info.TryActivateLimited()) {
                        PCLGameUtilities.ModifyCostForCombat(ca, 0, false);
                        break;
                    }
                }
        });
    }

    protected void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.SKILL && PCLGameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE))
            {
                cards.Add(c);
            }
        }
    }
}