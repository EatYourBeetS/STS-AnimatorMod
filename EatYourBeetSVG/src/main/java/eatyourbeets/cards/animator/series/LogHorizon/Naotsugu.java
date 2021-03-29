package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Naotsugu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Naotsugu.class).SetAttack(3, CardRarity.COMMON, EYBAttackType.Normal);

    public Naotsugu()
    {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL)
        .AddCallback(c -> GameActions.Bottom.GainBlock(c.lastDamageTaken));

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.drawPile)
            .SetOptions(true, false)
            .SetFilter(c -> c.hasTag(MARTIAL_ARTIST))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameUtilities.ModifyCostForTurn(cards.get(0), 1, true);
                    GameUtilities.Retain(cards.get(0));
                }
            });
        }
    }
}