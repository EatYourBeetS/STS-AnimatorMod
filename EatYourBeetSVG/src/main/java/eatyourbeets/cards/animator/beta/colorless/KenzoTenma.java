package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KenzoTenma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KenzoTenma.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Monster);

    public KenzoTenma()
    {
        super(DATA);

        Initialize(2, 0, 3 , 0);
        SetUpgrade(1, 0, 1 , 0);

        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public int GetXValue() {
        int increase = Math.min(CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true), CombatStats.Affinities.GetAffinityLevel(Affinity.Orange,true));
        return secondaryValue + increase;
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount * GetXValue());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        GameActions.Bottom.Callback(m, (enemy, __) -> {
            if (!GameUtilities.IsDeadOrEscaped(enemy)) {
                int stacks = 0;
                for (AbstractPower po : enemy.powers) {
                    if (po.type == AbstractPower.PowerType.DEBUFF) {
                        stacks += po.amount;
                        GameActions.Bottom.RemovePower(player, po);
                    }
                }
                if (stacks > 0) {
                    GameActions.Bottom.RecoverHP(stacks * magicNumber);
                }
            }
        });
    }
}