package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;

public class SpellScroll extends AnimatorRelic
{
    public static final String ID = CreateFullID(SpellScroll.class);

    public SpellScroll()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

//    @Override
//    public void update()
//    {
//        super.update();
//
//        if (hb.hovered && InputManager.RightClick.IsJustPressed() && GameUtilities.InBattle(true))
//        {
//            final RandomizedList<AbstractCard> possibleCards = GameUtilities.GetCardsInCombat(c -> GameUtilities.HasAffinity(c, Affinity.Blue, false));
//            final CardGroup group = GameUtilities.CreateCardGroup(possibleCards.GetInnerList());
//            GameEffects.List.Add(new ShowCardPileEffect(group));
//        }
//    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        if (CombatStats.TurnCount(true) == 0)
        {
            flash();
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.SFX(SFX.RELIC_DROP_MAGICAL);
            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetOptions(false, false, false)
            .AddCallback(cards ->
            {
                if (cards == null || cards.isEmpty())
                {
                    return;
                }

                final RandomizedList<AbstractCard> possibleCards = GameUtilities.GetCardsInCombat(GenericCondition.FromT1(c -> GameUtilities.HasAffinity(c, Affinity.Blue, false)));
                for (AbstractCard c : cards)
                {
                    if (possibleCards.Size() > 0)
                    {
                        final AbstractCard replacement = possibleCards.Retrieve(rng, true).makeCopy();
                        replacement.upgrade();
                        replacement.retain = replacement.selfRetain = false;
                        replacement.isEthereal = true;
                        GameActions.Top.ReplaceCard(c.uuid, replacement);
                    }
                }
            });
        }
    }
}