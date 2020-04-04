package eatyourbeets.monsters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_ShuffleCard;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class EYBMovesCollection
{
    public final ArrayList<EYBAbstractMove> rotation = new ArrayList<>();
    public final EYBMoveset moveset;
    public final boolean isSpecial;

    public EYBMovesCollection(EYBMoveset moveset, boolean isSpecial)
    {
        this.isSpecial = isSpecial;
        this.moveset = moveset;
    }

    public <T extends EYBAbstractMove> T Add(T move)
    {
        if (move.attackEffect == null)
        {
            move.attackEffect = moveset.attackEffect;
        }
        if (move.attackAnimation == null)
        {
            move.attackAnimation = moveset.attackAnimation;
        }

        move.Initialize(moveset.counter, moveset.owner);
        moveset.moves.put(moveset.counter, move);
        rotation.add(move);
        moveset.counter += 1;
        return move;
    }

    public EYBMove_Attack Attack(int damage)
    {
        return Add(new EYBMove_Attack(damage));
    }

    public EYBMove_Attack Attack(int damage, int times)
    {
        return Add(new EYBMove_Attack(damage, times));
    }

    public EYBMove_AttackDebuff AttackDebuff(int damage, int times)
    {
        return Add(new EYBMove_AttackDebuff(damage, times));
    }

    public EYBMove_AttackDefend AttackDefend(int damage, int block)
    {
        return Add(new EYBMove_AttackDefend(damage, block));
    }

    public EYBMove_AttackDebuff AttackFrail(int damage, int frail)
    {
        return (EYBMove_AttackDebuff) Add(new EYBMove_AttackDebuff(damage, frail))
        .SetOnUse((m, t) -> GameActions.Bottom.ApplyFrail(moveset.owner, t, m.misc.Calculate()));
    }

    public EYBMove_AttackDebuff AttackWeak(int damage, int weak)
    {
        return (EYBMove_AttackDebuff) Add(new EYBMove_AttackDebuff(damage, weak))
        .SetOnUse((m, t) -> GameActions.Bottom.ApplyWeak(moveset.owner, t, m.misc.Calculate()));
    }

    public EYBMove_AttackDebuff AttackVulnerable(int damage, int vulnerable)
    {
        return (EYBMove_AttackDebuff) Add(new EYBMove_AttackDebuff(damage, vulnerable))
        .SetOnUse((m, t) -> GameActions.Bottom.ApplyVulnerable(moveset.owner, t, m.misc.Calculate()));
    }

    public EYBMove_AttackBuff AttackStrength(int damage, int strength)
    {
        return (EYBMove_AttackBuff) Add(new EYBMove_AttackBuff(damage, strength))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new StrengthPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_Defend Defend(int block)
    {
        return Add(new EYBMove_Defend(block));
    }

    public EYBMove_DefendBuff DefendStrength(int block, int strength)
    {
        return (EYBMove_DefendBuff) Add(new EYBMove_DefendBuff(block, strength))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new StrengthPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_DefendBuff DefendThorns(int block, int thorns)
    {
        return (EYBMove_DefendBuff) Add(new EYBMove_DefendBuff(block, thorns))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new ThornsPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_Buff GainRegen(int amount)
    {
        return (EYBMove_Buff) Add(new EYBMove_Buff(amount))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new RegenPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_Buff GainStrength(int amount)
    {
        return (EYBMove_Buff) Add(new EYBMove_Buff(amount))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new StrengthPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_Buff GainThorns(int amount)
    {
        return (EYBMove_Buff) Add(new EYBMove_Buff(amount))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new ThornsPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_Buff GainTemporaryThorns(int amount)
    {
        return (EYBMove_Buff) Add(new EYBMove_Buff(amount))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new EarthenThornsPower(moveset.owner, m.misc.Calculate())));
    }

    public EYBMove_ShuffleCard ShuffleCard(AbstractCard card, int amount)
    {
        return Add(new EYBMove_ShuffleCard(card, amount));
    }

    public EYBMove_ShuffleCard ShuffleCard(AbstractCard card, int amount, CardGroup.CardGroupType destination)
    {
        return Add(new EYBMove_ShuffleCard(card, amount)).SetDestination(destination);
    }
}
