package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.BlockAttribute;
import pinacolada.cards.base.attributes.DamageAttribute;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;

import java.util.HashMap;
import java.util.Map;

public abstract class CounterIntentEffect
{
    protected static final Map<AbstractMonster.Intent, CounterIntentEffect> effects = new HashMap<>();
    protected static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;

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

    public abstract void EnqueueActions(PCLCard sourceCard, AbstractPlayer p, AbstractMonster m);
    public abstract String GetDescription(PCLCard sourceCard);

    public AbstractAttribute GetDamageInfo(PCLCard sourceCard)
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

    public AbstractAttribute GetBlockInfo(PCLCard sourceCard)
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

    public AbstractAttribute GetSpecialInfo(PCLCard sourceCard)
    {
        return null;
    }

    public void OnDrag(AbstractMonster m)
    {

    }

    protected int GetDamage(PCLCard sourceCard)
    {
        return 0;
    }

    protected int GetBlock(PCLCard sourceCard)
    {
        return 0;
    }

    protected int ModifyBlock(int block, PCLCard sourceCard)
    {
        return block + (sourceCard.block - sourceCard.baseBlock);
    }

    protected int ModifyDamage(int damage, PCLCard sourceCard)
    {
        return damage + (sourceCard.damage - sourceCard.baseDamage);
    }
}
