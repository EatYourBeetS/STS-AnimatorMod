package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shion.class).SetSkill(2, CardRarity.COMMON);

    public Shion()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetUpgrade(4, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
        .SetDamageEffect(enemy ->
        {
            if (!GameUtilities.IsDeadOrEscaped(enemy) && HasSynergy() && EffectHistory.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ApplyVulnerable(player, enemy, magicNumber);
            }
        });

        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 1));
    }
}