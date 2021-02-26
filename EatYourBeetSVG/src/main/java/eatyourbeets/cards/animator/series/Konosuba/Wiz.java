package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Wiz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wiz.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None);

    public Wiz()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetPurge(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (!CombatStats.HasActivatedLimited(cardID))
        {
            SetPurge(!(Synergies.WouldSynergize(this)));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, true)
        .AddCallback(() ->
        { //
            GameActions.Top.SelectFromPile(name, 1, player.exhaustPile)
            .SetOptions(false, false)
            .SetFilter(c -> !c.cardID.equals(Wiz.DATA.ID))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.MakeCardInHand(cards.get(0).makeStatEquivalentCopy());
                }
            });
        });

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Last.ModifyAllInstances(uuid, c -> ((EYBCard)c).SetPurge(true));
        }
    }
}