package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.*;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class EYBCardAffinitySystem implements OnStartOfTurnSubscriber
{
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public ForcePower Force;
    public AgilityPower Agility;
    public IntellectPower Intellect;
    public BlessingPower Blessing;
    public CorruptionPower Corruption;

    private AbstractCard currentSynergy = null;
    private AnimatorCard lastCardPlayed = null;

    public EYBCardAffinitySystem()
    {
        Powers.add(Force = new ForcePower(null, 0));
        Powers.add(Agility = new AgilityPower(null, 0));
        Powers.add(Intellect = new IntellectPower(null, 0));
        Powers.add(Blessing = new BlessingPower(null, 0));
        Powers.add(Corruption = new CorruptionPower(null, 0));
    }

    public boolean IsSynergizing(AbstractCard card)
    {
        if (card == null || currentSynergy == null)
        {
            return false;
        }

        return currentSynergy.uuid == card.uuid;
    }

    public void Initialize()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.Initialize(AbstractDungeon.player);
        }

        CombatStats.onStartOfTurn.Subscribe(this);
    }

    public void Update()
    {
        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }
    }

    public AbstractAffinityPower GetPower(AffinityType type)
    {
        for (AbstractAffinityPower p : Powers)
        {
            if (p.affinityType.equals(type))
            {
                return p;
            }
        }

        return null;
    }

    @Override
    public void OnStartOfTurn()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.atStartOfTurn();
        }
    }

    public int GetLastAffinityLevel(AffinityType type)
    {
        return lastCardPlayed == null ? 0 : lastCardPlayed.affinities.GetLevel(type);
    }

    public AnimatorCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public boolean TrySynergize(AbstractCard card)
    {
        if (WouldSynergize(card))
        {
            currentSynergy = card;
            return true;
        }

        currentSynergy = null;
        return false;
    }

    public void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        currentSynergy = null;
    }

    public boolean WouldSynergize(AbstractCard card)
    {
        return WouldSynergize(card, lastCardPlayed);
    }

    public boolean WouldSynergize(AbstractCard card, AbstractCard other)
    {
        for (OnSynergyCheckSubscriber s : CombatStats.onSynergyCheck.GetSubscribers())
        {
            if (s.OnSynergyCheck(card, other))
            {
                return true;
            }
        }

        if (card == null || other == null)
        {
            return false;
        }

        AnimatorCard a = JUtils.SafeCast(card, AnimatorCard.class);
        AnimatorCard b = JUtils.SafeCast(other, AnimatorCard.class);

        if (a != null)
        {
            if (b != null)
            {
                return a.HasDirectSynergy(b) || b.HasDirectSynergy(a);
            }
            else
            {
                return a.HasDirectSynergy(other);
            }
        }

        if (b != null)
        {
            return b.HasDirectSynergy(card);
        }

        return HasDirectSynergy(card, other);
    }

    public boolean HasDirectSynergy(AbstractCard c1, AbstractCard c2)
    {
        if (c1.hasTag(AnimatorCard.SHAPESHIFTER) || c2.hasTag(AnimatorCard.SHAPESHIFTER))
        {
            return true;
        }

        EYBCard a = JUtils.SafeCast(c1, EYBCard.class);
        EYBCard b = JUtils.SafeCast(c2, EYBCard.class);
        if (a == null || b == null)
        {
            return false;
        }

        return a.affinities.CanSynergize(b.affinities);
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    block += card.affinities.GetScaling(p.affinityType, true) * p.amount;
                }
            }
        }

        return block;
    }

    public float ModifyDamage(float damage, EYBCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    damage += card.affinities.GetScaling(p.affinityType, true) * p.amount;
                }
            }
        }

        return damage;
    }
}