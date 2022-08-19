package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public RoyMustang()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 1);

        SetEvokeOrbCount(1);
        SetSeries(CardSeries.FullmetalAlchemist);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (CombatStats.CardsExhaustedThisTurn().size() * magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ChannelOrbs(Fire::new, Math.min(p.orbs.size(), GameUtilities.GetEnemies(true).size()));
    }
}