package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Shion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shion.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Shion()
    {
        super(DATA);

        Initialize(15, 0, 2, 9);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 1, 2);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        for (AbstractPower p : player.powers)
        {
            amount = p.atDamageGive(amount, damageTypeForTurn, this);
        }
        for (AbstractPower p : enemy.powers)
        {
            amount = p.atDamageReceive(amount, damageTypeForTurn, this);
        }
        for (AbstractPower p : player.powers)
        {
            amount = p.atDamageFinalGive(amount, damageTypeForTurn, this);
        }
        for (AbstractPower p : enemy.powers)
        {
            amount = p.atDamageFinalReceive(amount, damageTypeForTurn, this);
        }
        amount = CombatStats.OnDamageOverride(enemy, damageTypeForTurn, amount, this);
        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}