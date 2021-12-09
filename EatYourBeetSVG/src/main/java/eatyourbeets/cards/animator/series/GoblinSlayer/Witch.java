package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Witch extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Witch.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Spearman(), true));
    public static final int MODIFIER = 30;


    public Witch()
    {
        super(DATA);

        Initialize(0, 8, MODIFIER, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        AbstractOrb orb = CheckPrimaryCondition(false) ? new Dark() : new Fire();
        GameActions.Bottom.ChannelOrb(orb).AddCallback(() -> {
            if (upgraded) {
                GameActions.Bottom.TriggerOrbPassive(p.orbs.size())
                        .SetFilter(o -> Dark.ORB_ID.equals(o.ID) || Fire.ORB_ID.equals(o.ID))
                        .SetSequential(true);
            }
        });

        if (TrySpendAffinity(Affinity.Blue))
        {
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);

        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Red, true);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameActions.Bottom.ApplyPower(new WitchPower(player, 1));
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    public static class WitchPower extends AnimatorPower
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
            return enabled && type == DamageInfo.DamageType.NORMAL && card instanceof EYBCard && ((EYBCard) card).attackType == EYBAttackType.Piercing ? damage * (1 + (amount * Witch.MODIFIER / 100f)) : damage;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}