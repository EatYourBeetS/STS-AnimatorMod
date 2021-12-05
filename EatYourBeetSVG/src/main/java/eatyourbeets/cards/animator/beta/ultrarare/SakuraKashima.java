package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class SakuraKashima extends AnimatorCard_UltraRare {
    public static final EYBCardData DATA = Register(SakuraKashima.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.Rewrite);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public SakuraKashima() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(2, 0, 0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle) {
            GameEffects.List.ShowCopy(this);
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(WisdomStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(EnduranceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(SuperchargeStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(DesecrationStance.STANCE_ID));
            }

            choices.Select(1, null);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new SakuraKashimaPower(p, magicNumber));
    }

    public static class SakuraKashimaPower extends AnimatorPower
    {
        public SakuraKashimaPower(AbstractCreature owner, int amount)
        {
            super(owner, SakuraKashima.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            EnablePowers();
        }


        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);

            EYBStance eOldStance = JUtils.SafeCast(newStance, EYBStance.class);
            EYBStance eNewStance = JUtils.SafeCast(newStance, EYBStance.class);

            if (eOldStance != null) {
                final AbstractAffinityPower p = CombatStats.Affinities.GetPower(eOldStance.affinity);
                if (p != null) {
                    GameActions.Bottom.StackAffinityPower(eOldStance.affinity, p.maxAmount);
                    p.SetEffectMultiplier(p.effectMultiplier - 1);
                }
            }
            if (eNewStance != null) {
                final AbstractAffinityPower p = CombatStats.Affinities.GetPower(eNewStance.affinity);
                if (p != null) {
                    p.SetEffectMultiplier(p.effectMultiplier + 1);
                }
            }

            EnablePowers();
        }

        private void EnablePowers() {
            for (Affinity affinity : Affinity.Extended()) {
                CombatStats.Affinities.GetPower(affinity).SetEnabled(true);
            }
        }
    }
}