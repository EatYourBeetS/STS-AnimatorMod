package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.utilities.GameActions;

public class Ainz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ainz.class)
            .SetPower(7, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int CHANNEL_AMOUNT = 3;
    public static final int POWER_ENERGY_COST = 3;

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 6);

        SetAffinity_Red(1);
        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST, CHANNEL_AMOUNT);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Motivate(this, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }

    public static class AinzPower extends AnimatorClickablePower
    {
        public AinzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Ainz.DATA, PowerTriggerConditionType.Energy, Ainz.POWER_ENERGY_COST);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, Ainz.CHANNEL_AMOUNT, amount);
        }

        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);
            if (newStance.ID.equals(CorruptionStance.STANCE_ID)) {
                EnablePowers();
            }
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE, 0.9f, 1.1f);
            GameActions.Bottom.BorderLongFlash(Color.valueOf("3d0066"));
            GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);

            if (CorruptionStance.IsActive()) {
                EnablePowers();
            }
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (Affinity a : Affinity.Basic())
            {
                if (a == Affinity.Dark && CorruptionStance.IsActive()) {
                    GameActions.Bottom.StackAffinityPower(a, amount + 1, true);
                }
                else if (a != Affinity.Light)
                {
                    GameActions.Bottom.StackAffinityPower(a, amount, true);
                }
            }

            this.flash();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.ChannelOrbs(Chaos::new, Ainz.CHANNEL_AMOUNT);
        }

        private void EnablePowers() {
            CombatStats.Affinities.GetPower(Affinity.Fire).SetEnabled(true);
            CombatStats.Affinities.GetPower(Affinity.Water).SetEnabled(true);
            CombatStats.Affinities.GetPower(Affinity.Air).SetEnabled(true);
            CombatStats.Affinities.GetPower(Affinity.Earth).SetEnabled(true);
        }
    }
}