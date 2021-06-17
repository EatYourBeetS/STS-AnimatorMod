package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class Rayneshia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rayneshia.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.SelectFromHand(name, 2, false)
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
                int synergies = 0;
                HashSet<AbstractCard> cards = GameUtilities.GetOtherCardsInHand(this);
                for (AbstractCard c1 : cards)
                {
                    for (AbstractCard c2 : cards)
                    {
                        if (c1 != c2 && Synergies.WouldSynergize(c1, c2))
                        {
                            synergies += 1;
                            break;
                        }
                    }
                }

                if (synergies >= secondaryValue && CombatStats.TryActivateLimited(cardID))
                {
                    GameActions.Bottom.WaitRealtime(0.3f);
                    GameActions.Bottom.Motivate(2);
                }
            });
        });
    }
}