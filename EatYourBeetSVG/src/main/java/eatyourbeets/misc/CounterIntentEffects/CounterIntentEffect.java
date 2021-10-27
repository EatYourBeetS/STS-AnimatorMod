package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;

import java.util.HashMap;
import java.util.Map;

public abstract class CounterIntentEffect
{
    protected static final Map<AbstractMonster.Intent, CounterIntentEffect> effects = new HashMap<>();
    protected static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;

    private static CounterIntentEffect AddToCache(AbstractMonster.Intent intent, CounterIntentEffect effect)
    {
        effects.put(intent, effect);
        return effect;
    }

    public static CounterIntentEffect GetEffect(AbstractMonster enemy)
    {
        CounterIntentEffect effect = effects.get(enemy.intent);
        if (effect != null)
        {
            return effect;
        }

        switch (enemy.intent)
        {
            case ATTACK:
                return AddToCache(enemy.intent, new CounterIntentEffect_Attack());

            case ATTACK_BUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Attack_Buff());

            case ATTACK_DEBUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Attack_Debuff());

            case ATTACK_DEFEND:
                return AddToCache(enemy.intent, new CounterIntentEffect_Attack_Defend());

            case BUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Buff());

            case DEBUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Debuff());

            case STRONG_DEBUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Strong_Debuff());

            case DEFEND:
                return AddToCache(enemy.intent, new CounterIntentEffect_Defend());

            case DEFEND_DEBUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Defend_Debuff());

            case DEFEND_BUFF:
                return AddToCache(enemy.intent, new CounterIntentEffect_Defend_Buff());

            case ESCAPE:
                return AddToCache(enemy.intent, new CounterIntentEffect_Escape());

            case SLEEP:
                return AddToCache(enemy.intent, new CounterIntentEffect_Sleep());

            case STUN:
                return AddToCache(enemy.intent, new CounterIntentEffect_Stun());

            case UNKNOWN:
                return AddToCache(enemy.intent, new CounterIntentEffect_Unknown());

            case DEBUG:
            case NONE:
                return AddToCache(enemy.intent, new CounterIntentEffect_None());

            case MAGIC:
            default:
                return AddToCache(enemy.intent, new CounterIntentEffect_Magic());
        }
    }

    public abstract void EnqueueActions(EYBCard sourceCard, AbstractPlayer p, AbstractMonster m);
    public abstract String GetDescription(EYBCard sourceCard);

    public AbstractAttribute GetDamageInfo(EYBCard sourceCard)
    {
        int damage = GetDamage(sourceCard);
        if (damage > 0)
        {
            AbstractAttribute result = DamageAttribute.Instance.SetCard(sourceCard);
            result.mainText.text = String.valueOf(damage);
            return result;
        }

        return null;
    }

    public AbstractAttribute GetBlockInfo(EYBCard sourceCard)
    {
        int block = GetBlock(sourceCard);
        if (block > 0)
        {
            AbstractAttribute result = BlockAttribute.Instance.SetCard(sourceCard);
            result.mainText.text = String.valueOf(block);
            return result;
        }

        return null;
    }

    public AbstractAttribute GetSpecialInfo(EYBCard sourceCard)
    {
        return null;
    }

    public void OnDrag(AbstractMonster m)
    {

    }

    protected int GetDamage(EYBCard sourceCard)
    {
        return 0;
    }

    protected int GetBlock(EYBCard sourceCard)
    {
        return 0;
    }

    protected int ModifyBlock(int block, EYBCard sourceCard)
    {
        return block + (sourceCard.block - sourceCard.baseBlock);
    }

    protected int ModifyDamage(int damage, EYBCard sourceCard)
    {
        return damage + (sourceCard.damage - sourceCard.baseDamage);
    }
}
