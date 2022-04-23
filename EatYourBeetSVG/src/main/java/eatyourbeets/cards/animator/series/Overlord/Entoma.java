package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Entoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Entoma.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Entoma()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Dark(1, 1, 1);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.POISON)
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.GREEN)).duration);
        GameActions.Bottom.RetainPower(Affinity.Dark);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (TryUseAffinity(Affinity.Dark) && !m.hasPower(EntomaPower.DeriveID(cardID)))
        {
            GameActions.Bottom.ApplyPower(new EntomaPower(m, p, 1));
        }
    }

    public static class EntomaPower extends AnimatorPower implements OnApplyPowerSubscriber
    {
        public EntomaPower(AbstractCreature owner, AbstractCreature source, int amount)
        {
            super(owner, source, Entoma.DATA);

            this.maxAmount = 1;

            Initialize(amount, PowerType.DEBUFF, false);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, maxAmount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onApplyPower.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onApplyPower.Unsubscribe(this);
        }

        @Override
        public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            if (target == owner && PoisonPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainTemporaryHP(amount);
                flashWithoutSound();
            }
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            if (AbstractDungeon.actionManager.currentAction instanceof PoisonLoseHpAction)
            {
                GameActions.Bottom.GainTemporaryHP(amount);
                flashWithoutSound();
            }

            super.wasHPLost(info, damageAmount);
        }
    }
}