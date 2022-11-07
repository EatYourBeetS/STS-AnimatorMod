package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;

import java.util.HashMap;
import java.util.Map;

public abstract class NanamiEffect
{
    protected static final Map<AbstractMonster.Intent, NanamiEffect> effects = new HashMap<>();
    protected static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;

    private static NanamiEffect AddToCache(AbstractMonster.Intent intent, NanamiEffect effect)
    {
        effects.put(intent, effect);
        return effect;
    }

    public static NanamiEffect GetEffect(AbstractMonster enemy)
    {
        NanamiEffect effect = effects.get(enemy.intent);
        if (effect != null)
        {
            return effect;
        }

        switch (enemy.intent)
        {
            case ATTACK:
                return AddToCache(enemy.intent, new NanamiEffect_Attack());

            case ATTACK_BUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Attack_Buff());

            case ATTACK_DEBUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Attack_Debuff());

            case ATTACK_DEFEND:
                return AddToCache(enemy.intent, new NanamiEffect_Attack_Defend());

            case BUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Buff());

            case DEBUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Debuff());

            case STRONG_DEBUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Strong_Debuff());

            case DEFEND:
                return AddToCache(enemy.intent, new NanamiEffect_Defend());

            case DEFEND_DEBUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Defend_Debuff());

            case DEFEND_BUFF:
                return AddToCache(enemy.intent, new NanamiEffect_Defend_Buff());

            case ESCAPE:
                return AddToCache(enemy.intent, new NanamiEffect_Escape());

            case SLEEP:
                return AddToCache(enemy.intent, new NanamiEffect_Sleep());

            case STUN:
                return AddToCache(enemy.intent, new NanamiEffect_Stun());

            case UNKNOWN:
                return AddToCache(enemy.intent, new NanamiEffect_Unknown());

            case DEBUG:
            case NONE:
                return AddToCache(enemy.intent, new NanamiEffect_None());

            case MAGIC:
            default:
                return AddToCache(enemy.intent, new NanamiEffect_Magic());
        }
    }

    public abstract void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m);
    public abstract String GetDescription(EYBCard nanami);

    public AbstractAttribute GetDamageInfo(EYBCard nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            AbstractAttribute result = DamageAttribute.Instance.SetCard(nanami);
            result.mainText.text = String.valueOf(damage);
            return result;
        }

        return null;
    }

    public AbstractAttribute GetBlockInfo(EYBCard nanami)
    {
        int block = GetBlock(nanami);
        if (block > 0)
        {
            AbstractAttribute result = BlockAttribute.Instance.SetCard(nanami);
            result.mainText.text = String.valueOf(block);
            return result;
        }

        return null;
    }

    public AbstractAttribute GetSpecialInfo(EYBCard nanami)
    {
        return null;
    }

    public void OnDrag(AbstractMonster m)
    {

    }

    protected int GetDamage(EYBCard nanami)
    {
        return 0;
    }

    protected int GetBlock(EYBCard nanami)
    {
        return 0;
    }

    protected int ModifyBlock(int block, EYBCard nanami)
    {
        return block + (nanami.block - nanami.baseBlock);
    }

    protected int ModifyDamage(int damage, EYBCard nanami)
    {
        return damage + (nanami.damage - nanami.baseDamage);
    }

    protected EYBCardTooltip GetForceTooltip(EYBCard nanami)
    {
        return nanami instanceof AnimatorClassicCard ? GR.Tooltips.Force : GR.Tooltips.Affinity_Red;
    }
}
