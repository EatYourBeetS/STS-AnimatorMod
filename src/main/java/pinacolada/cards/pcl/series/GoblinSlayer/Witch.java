package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class Witch extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Witch.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Spearman(), true));
    public static final int MODIFIER = 30;


    public Witch()
    {
        super(DATA);

        Initialize(0, 8, MODIFIER, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(PCLAffinity.Dark, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        AbstractOrb orb = CheckPrimaryCondition(false) ? new Dark() : new Fire();
        PCLActions.Bottom.ChannelOrb(orb).AddCallback(() -> {
            if (upgraded) {
                PCLActions.Bottom.TriggerOrbPassive(p.orbs.size())
                        .SetFilter(o -> Dark.ORB_ID.equals(o.ID) || Fire.ORB_ID.equals(o.ID))
                        .SetSequential(true);
            }
        });

        if (TrySpendAffinity(PCLAffinity.Dark))
        {
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);

        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Red, true);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLActions.Bottom.ApplyPower(new WitchPower(player, 1));
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    public static class WitchPower extends PCLPower
    {
        public WitchPower(AbstractCreature owner, int amount)
        {
            super(owner, Witch.DATA);

            Initialize(amount, PowerType.BUFF, false);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount * Witch.MODIFIER);
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            return enabled && type == DamageInfo.DamageType.NORMAL && card instanceof PCLCard && ((PCLCard) card).attackType == PCLAttackType.Piercing ? damage * (1 + (amount * Witch.MODIFIER / 100f)) : damage;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}