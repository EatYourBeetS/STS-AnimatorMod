package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Genos extends AnimatorCard
{
    public static final String ID = Register(Genos.class.getSimpleName(), EYBCardBadge.Synergy);

    public Genos()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(9, 0, 4, 4);
        SetUpgrade(3, 0, 0, 0);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.drawPile, p.discardPile)
            .SetOptions(false, true)
            .SetFilter(c -> c.costForTurn >= 3)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    cards.get(0).retain = true;
                }
            });
        }
    }
}