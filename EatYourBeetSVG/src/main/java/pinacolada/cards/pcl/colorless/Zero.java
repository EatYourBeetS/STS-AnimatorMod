package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RotatingList;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.replacement.Enlightenment;
import pinacolada.cards.pcl.special.Ganyu;
import pinacolada.cards.pcl.special.OrbCore_Frost;
import pinacolada.cards.pcl.special.SheerCold;
import pinacolada.cards.pcl.tokens.AffinityToken_Blue;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Zero extends PCLCard
{
    public static final PCLCardData DATA = Register(Zero.class)
            .SetSkill(0, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GrimoireOfZero);
    protected final static WeightedList<AbstractCard> blueCards = new WeightedList<>();


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
                    if ((ca.rarity == CardRarity.BASIC || PCLGameUtilities.HasSilverAffinity(ca)) && info.TryActivateLimited()) {
                        InitializeBlueCards();
                        WeightedList<AbstractCard> randomBlueCards = new WeightedList<>(blueCards);
                        final CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
                        while (options.size() < magicNumber && randomBlueCards.Size() > 0)
                        {
                            AbstractCard randomCard = randomBlueCards.Retrieve(rng, true).makeCopy();
                            PCLGameUtilities.ModifyCostForCombat(randomCard, 0, false);

                            if (upgraded)
                            {
                                randomCard.upgrade();
                            }
                            options.addToBottom(randomCard);
                        }

                        PCLActions.Top.SelectFromPile(name, 1, options)
                                .SetOptions(false, false)
                                .AddCallback(cards2 ->
                                {
                                    if (cards2.size() > 0)
                                    {
                                        PCLActions.Bottom.MakeCardInDrawPile(cards2.get(0))
                                                .SetDuration(Settings.ACTION_DUR_FASTER, true);

                                    }
                                });
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

    protected void InitializeBlueCards()
    {
        blueCards.Clear();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (PCLGameUtilities.HasBlueAffinity(c)
                    && PCLGameUtilities.IsObtainableInCombat(c)
                    && c.rarity != AbstractCard.CardRarity.BASIC)
            {
                blueCards.Add(c, c.rarity == CardRarity.RARE || c.color == CardColor.COLORLESS ? 4 : c.rarity == CardRarity.UNCOMMON ? 7 : 10);
            }

            blueCards.Add(new SheerCold(), 2);
            blueCards.Add(new OrbCore_Frost(), 2);
            blueCards.Add(new Ganyu(), 3);
            blueCards.Add(new Enlightenment(), 3);
            blueCards.Add(new AffinityToken_Blue(), 7);
        }
    }
}