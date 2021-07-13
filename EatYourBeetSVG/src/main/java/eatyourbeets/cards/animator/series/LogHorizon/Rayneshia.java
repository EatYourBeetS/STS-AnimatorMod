package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Rayneshia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rayneshia.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

            if (CombatStats.HasActivatedLimited(cardID))
            {
                return;
            }

            GameActions.Bottom.Callback(new RefreshHandLayout(), () ->
            {
                if (GetTeamwork(null) >= secondaryValue && CombatStats.TryActivateLimited(cardID))
                {
                    final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(CardRarity.RARE, player.getCardColor());

                    while (choice.size() < 3 && pool.Size() > 0)
                    {
                        choice.addToTop(pool.Retrieve(rng).makeCopy());
                    }

                    GameActions.Bottom.SelectFromPile(name, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0))
                            .SetUpgrade(false, true);
                        }
                    });
                }
            });
        });
    }
}