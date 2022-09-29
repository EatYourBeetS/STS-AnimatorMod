package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.WeightedList;

public class QuestionMark extends AnimatorCard
{
    public static final EYBCardData DATA = Register(QuestionMark.class)
            .SetSkill(-2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetMaxCopies(0)
            .SetColor(CardColor.COLORLESS);

    public AnimatorCard copy = null;

    public QuestionMark()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 1, 0);
        SetUnplayable(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.Callback(() ->
        {
            Random rng = CombatStats.GetCombatData(cardID, null);
            if (rng == null)
            {
                rng = new Random(Settings.seed + (AbstractDungeon.actNum * 13) + (AbstractDungeon.floorNum * 27));
                CombatStats.SetCombatData(cardID, rng);
            }

            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final WeightedList<AbstractCard> pool = GameUtilities.GetCardsInCombatWeighted(null);
            while (pool.Size() > 0 && choice.size() < magicNumber)
            {
                final AbstractCard card = pool.Retrieve(rng, true).makeCopy();
                if (!card.cardID.equals(cardID))
                {
                    card.upgrade();
                    choice.group.add(card);
                }
            }

            GameActions.Bottom.SelectFromPile(name, 1, choice)
            .SetOptions(false, false, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.MakeCardInHand(c);
                }
            });
        });
    }
}