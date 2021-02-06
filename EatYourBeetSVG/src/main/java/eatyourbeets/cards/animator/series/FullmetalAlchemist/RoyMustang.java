package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public RoyMustang()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (CombatStats.CardsExhaustedThisTurn() * magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        int max = p.orbs.size();
        int i = 0;

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            if (i < max)
            {
                GameActions.Bottom.ChannelOrb(new Fire(), true);
            }

            i += 1;
        }
    }
}