package pinacolada.cards.pcl.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class MamizouFutatsuiwa extends PCLCard
{
    public static final PCLCardData DATA = Register(MamizouFutatsuiwa.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    private static final RandomizedList<PCLCard> shapeshifterPool = new RandomizedList<>();

    public MamizouFutatsuiwa()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 3);

        SetAffinity_Star(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (magicNumber > 0) ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            if (shapeshifterPool.Size() == 0)
            {
                shapeshifterPool.AddAll(PCLJUtils.Filter(CardSeries.GetNonColorlessCard(), c -> c.affinities.GetLevel(PCLAffinity.Star) > 0));
                shapeshifterPool.AddAll(PCLJUtils.Filter(CardSeries.GetColorlessCards(), c -> c.affinities.GetLevel(PCLAffinity.Star) > 0));
            }

            PCLCard shapeshifter = shapeshifterPool.Retrieve(rng, false);
            if (shapeshifter != null)
            {
                PCLActions.Bottom.MakeCardInHand(shapeshifter.makeCopy());
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .SetFilter(c -> c instanceof PCLCard && !((PCLCard)c).affinities.HasStar())
        .AddCallback(cards ->
        {
            PCLCard card = PCLJUtils.SafeCast(cards.get(0), PCLCard.class);
            if (card != null)
            {
                card.affinities.Set(PCLAffinity.Star, 2);
                card.flash();
            }
        });
    }
}
