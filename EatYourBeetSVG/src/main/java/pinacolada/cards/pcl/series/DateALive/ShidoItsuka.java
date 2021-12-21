package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ShidoItsuka extends PCLCard
{
    public static final PCLCardData DATA = Register(ShidoItsuka.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    protected final static WeightedList<AbstractCard> dateALiveCards = new WeightedList<>();

    public ShidoItsuka()
    {
        super(DATA);

        Initialize(0, 6, 3);
        SetUpgrade(0, 0);
        SetAffinity_Blue(1, 0, 1);

        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        InitializeSynergicCards();
        boolean giveHarmonic = info.IsSynergizing && PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber && info.TryActivateLimited();

        WeightedList<AbstractCard> randomizedDALCards = new WeightedList<>(dateALiveCards);
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
                PCLGameUtilities.ModifyCardTag(randomCard, HARMONIC, true);
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
        return PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber;
    }

    private void InitializeSynergicCards()
    {
        dateALiveCards.Clear();

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
}