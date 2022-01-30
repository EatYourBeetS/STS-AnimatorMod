package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ShidoItsuka extends PCLCard
{
    public static final PCLCardData DATA = Register(ShidoItsuka.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    protected final static WeightedList<AbstractCard> dateALiveCards = new WeightedList<>();

    public ShidoItsuka()
    {
        super(DATA);

        Initialize(0, 6, 3, 4);
        SetUpgrade(0, 0);
        SetAffinity_Blue(1, 0, 1);

        SetExhaust(true);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        boolean giveHarmonic = CheckSpecialCondition(true) && info.TryActivateLimited();

        WeightedList<AbstractCard> randomizedDALCards = new WeightedList<>(GetSynergicCards());
        final CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        for (int i = 0; i < magicNumber; i++)
        {
            if (randomizedDALCards.Size() == 0)
            {
                break;
            }

            AbstractCard randomCard = randomizedDALCards.Retrieve(rng, true).makeCopy();

            if (upgraded)
            {
                randomCard.upgrade();
            }
            if (giveHarmonic)
            {
                PCLGameUtilities.ModifyAffinityLevel(randomCard, PCLAffinity.Star, 1, false);
            }

            options.addToBottom(randomCard);
        }

        PCLActions.Top.SelectFromPile(name, 1, options)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        PCLActions.Bottom.MakeCardInDrawPile(cards.get(0))
                                .SetDuration(Settings.ACTION_DUR_FASTER, true);

                    }
                });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLGameUtilities.GetCurrentMatchCombo() >= secondaryValue;
    }

    private WeightedList<AbstractCard> GetSynergicCards()
    {
        if (dateALiveCards.GetInnerList().isEmpty()) {
            for (AbstractCard c : CardLibrary.getAllCards())
            {
                if (PCLGameUtilities.IsSameSeries(this, c)
                        && !c.hasTag(AbstractCard.CardTags.HEALING)
                        && c.rarity != AbstractCard.CardRarity.SPECIAL
                        && c.rarity != AbstractCard.CardRarity.BASIC)
                {
                    dateALiveCards.Add(c, c.rarity == CardRarity.COMMON ? 10 : c.rarity == CardRarity.UNCOMMON ? 7 : 4);
                }
            }
        }
        return dateALiveCards;
    }
}