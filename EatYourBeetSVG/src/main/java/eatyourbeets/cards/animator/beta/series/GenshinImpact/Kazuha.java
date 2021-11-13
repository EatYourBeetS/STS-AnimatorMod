package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SwirledPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Kazuha extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuha.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage(true);

    public Kazuha()
    {
        super(DATA);

        Initialize(5, 0, 3, 0);
        SetUpgrade(2, 0, 1, 0);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                .SetDamageEffect((c, __) -> GameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 1.4f, 1.6f)));
        AbstractMonster mo = GameUtilities.GetRandomEnemy(true);
        if (mo != null) {
            GameActions.Bottom.StackPower(player, new SwirledPower(GameUtilities.GetRandomEnemy(true), magicNumber));
        }
        GameActions.Bottom.StackPower(new KazuhaPower(p, secondaryValue));
    }

    public static class KazuhaPower extends AnimatorPower
    {
        public KazuhaPower(AbstractCreature owner, int amount)
        {
            super(owner, Kazuha.DATA);

            Initialize(amount);
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            super.onAttack(info, damageAmount, target);

            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL && GameUtilities.GetPowerAmount(target, SwirledPower.POWER_ID) > 0)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true)) {
                    if (enemy != target) {
                        GameActions.Bottom.DealDamage(player, enemy, Math.max(1, damageAmount / 2), DamageInfo.DamageType.THORNS, AttackEffects.SLASH_DIAGONAL).SetDamageEffect(c -> GameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 1.4f, 1.6f).duration / 5);
                    }
                }
                this.flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}

