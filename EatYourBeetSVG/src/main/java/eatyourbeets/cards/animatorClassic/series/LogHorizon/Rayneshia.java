package eatyourbeets.cards.animatorClassic.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Rayneshia extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Rayneshia.class).SetSeriesFromClassPackage().SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);


    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(selected ->
        {
            for (AbstractCard c : selected)
            {
                GameActions.Top.MoveCard(c, player.hand, player.drawPile);
            }
//
//            if (CombatStats.HasActivatedLimited(cardID))
//            {
//                return;
//            }

//            GameActions.Bottom.Callback(new RefreshHandLayout(), () ->
//            {
//                if (HasTeamwork(secondaryValue) && CombatStats.TryActivateLimited(cardID))
//                {
//                    final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
//                    final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(CardRarity.RARE, player.getCardColor());
//
//                    while (choice.size() < 3 && pool.Size() > 0)
//                    {
//                        choice.addToTop(pool.Retrieve(rng).makeCopy());
//                    }
//
//                    GameActions.Bottom.SelectFromPile(name, 1, choice)
//                    .SetOptions(false, true)
//                    .AddCallback(cards ->
//                    {
//                        if (cards != null && cards.size() > 0)
//                        {
//                            GameActions.Bottom.MakeCardInHand(cards.get(0))
//                            .SetUpgrade(false, true);
//                        }
//                    });
//                }
//            });
        });
    }
}