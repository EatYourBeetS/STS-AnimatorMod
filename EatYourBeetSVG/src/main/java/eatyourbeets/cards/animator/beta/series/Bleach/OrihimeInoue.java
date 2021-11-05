package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.misc.GenericEffects.GenericEffect_StackPower;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.BlessingStance;
import eatyourbeets.utilities.GameActions;

public class OrihimeInoue extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 5, 1, 5);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(Affinity.Red, 4);
        SetAffinityRequirement(Affinity.Light, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.StackPower(new OrihimeInouePower(p, magicNumber));

        if (TrySpendAffinity(Affinity.Red, Affinity.Light)) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_StackPower(PowerHelper.TemporaryThorns, GR.Tooltips.Thorns, secondaryValue, true));
                choices.AddEffect(new GenericEffect_EnterStance(BlessingStance.STANCE_ID));
            }
            choices.Select(1, m);
        }
    }

    public static class OrihimeInouePower extends AnimatorPower
    {
        public OrihimeInouePower(AbstractPlayer owner, int amount)
        {
            super(owner, OrihimeInoue.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            RemovePower();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount <= 0)
            {
                return damageAmount;
            }

            this.amount--;

            GameActions.Bottom.ChannelOrb(new Fire());

            return damageAmount;
        }
    }
}