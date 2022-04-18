package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.UnnamedClickablePower;
import eatyourbeets.utilities.GameActions;

public class InfinitePower extends UnnamedCard
{
    public static final int POWER_COST = 3;
    public static final EYBCardData DATA = Register(InfinitePower.class)
            .SetPower(3, CardRarity.RARE);

    public InfinitePower()
    {
        super(DATA);

        Initialize(0, 0, POWER_COST);

        SetEthereal(true);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new InfinitePowerPower(p, 1));
    }

    public static class InfinitePowerPower extends UnnamedClickablePower
    {
        public InfinitePowerPower(AbstractCreature owner, int amount)
        {
            super(owner, InfinitePower.DATA, PowerTriggerConditionType.Energy, POWER_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ATTACK_MAGIC_SLOW_1, 0.65f, 0.75f, 0.85f);
            SFX.Play(SFX.ORB_LIGHTNING_EVOKE, 0.45f, 0.5f, 1.05f);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            triggerCondition.requiredAmount += 1;
            stackPower(1);
            playApplyPowerSfx();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.GainEnergy(amount);
            GameActions.Bottom.GainStrength(amount);
            GameActions.Bottom.GainBlock(amount);
            flashWithoutSound();
        }
    }
}