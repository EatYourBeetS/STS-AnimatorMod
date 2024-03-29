package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class MamizouFutatsuiwa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamizouFutatsuiwa.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    private static final RandomizedList<AnimatorCard> shapeshifterPool = new RandomizedList<>();

    public MamizouFutatsuiwa()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 3);

        SetExhaust(true);
        SetSynergy(Synergies.TouhouProject);
        SetShapeshifter();
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
                shapeshifterPool.AddAll(JUtils.Filter(Synergies.GetNonColorlessCard(), c -> c.hasTag(SHAPESHIFTER)));
                shapeshifterPool.AddAll(JUtils.Filter(Synergies.GetColorlessCards(), c -> c.hasTag(SHAPESHIFTER)));
            }

            AnimatorCard shapeshifter = shapeshifterPool.Retrieve(rng, false);
            if (shapeshifter != null)
            {
                GameActions.Bottom.MakeCardInHand(shapeshifter.makeCopy());
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .SetFilter(c -> c instanceof AnimatorCard && !c.hasTag(SHAPESHIFTER))
        .AddCallback(cards ->
        {
            AnimatorCard card = JUtils.SafeCast(cards.get(0), AnimatorCard.class);
            if (card != null)
            {
                card.SetSynergy(Synergies.ANY);
                card.SetShapeshifter();
                card.flash();
            }
        });
    }
}
