package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

public class MamizouFutatsuiwa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamizouFutatsuiwa.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    private static final RandomizedList<AnimatorCard> shapeshifterPool = new RandomizedList<>();

    public MamizouFutatsuiwa()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.TouhouProject, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            if (shapeshifterPool.Count() == 0)
            {
                shapeshifterPool.AddAll(JavaUtilities.Filter(Synergies.GetNonColorlessCard(), c -> c.anySynergy));
                shapeshifterPool.AddAll(JavaUtilities.Filter(Synergies.GetColorlessCards(), c -> c.anySynergy));
            }

            AnimatorCard shapeshifter = shapeshifterPool.Retrieve(AbstractDungeon.cardRandomRng, false);
            if (shapeshifter != null)
            {
                GameActions.Bottom.MakeCardInHand(shapeshifter.makeCopy());
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .SetFilter(c -> c instanceof AnimatorCard && !(((AnimatorCard) c).anySynergy))
        .AddCallback(cards ->
        {
            AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
            if (card != null)
            {
                card.SetSynergy(Synergies.ANY, true);
                card.flash();
            }
        });
    }
}
