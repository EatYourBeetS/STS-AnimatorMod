package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Wiz extends AnimatorCard
{
    public static final String ID = Register_Old(Wiz.class);

    public Wiz()
    {
        super(ID, 1, CardRarity.RARE, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, true)
        .AddCallback(__ ->
        { //
            GameActions.Top.SelectFromPile(name, 1, AbstractDungeon.player.exhaustPile)
                    .SetOptions(false, false)
                    .SetFilter(c -> !c.cardID.equals(Wiz.ID))
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0).makeStatEquivalentCopy());
                        }
                    });
        });

        if (!(HasSynergy() && EffectHistory.TryActivateLimited(cardID)))
        {
            GameActions.Bottom.Purge(this);
        }
    }
}