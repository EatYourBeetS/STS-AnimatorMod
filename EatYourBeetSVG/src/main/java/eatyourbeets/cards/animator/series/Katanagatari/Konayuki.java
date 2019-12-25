package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Konayuki extends AnimatorCard
{
    public static final String ID = Register(Konayuki.class.getSimpleName(), EYBCardBadge.Drawn);

    public Konayuki()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF);

        Initialize(33, 0, 3);
        SetUpgrade(11, 0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (GameUtilities.GetPowerAmount(ForcePower.POWER_ID) <= 0 && EffectHistory.TryActivateSemiLimited(cardID))
        {
            modifyCostForTurn(-1);
            this.flash();
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (GameUtilities.GetStrength() >= 10)
        {
            this.target = CardTarget.ENEMY;
        }
        else
        {
            this.target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.GetStrength(p) >= 10)
        {
            if (m == null)
            {
                m = GameUtilities.GetRandomEnemy(true);
            }

            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        else
        {
            GameActions.Bottom.GainForce(magicNumber);
        }
    }
}