package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Boros extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Boros.class)
            .SetPower(3, CardRarity.RARE)
            .SetMultiformData(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 1;

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 4);

        SetAffinity_Fire(2);
        SetAffinity_Air(1);
        SetAffinity_Dark(2);

        SetDelayed(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetDelayed(false);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new BorosPower(p, magicNumber));
    }

    public static class BorosPower extends AnimatorClickablePower
    {
        protected boolean playPowersTwice;

        public BorosPower(AbstractCreature owner, int amount)
        {
            super(owner, Boros.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.maxAmount = 10;
            this.triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, maxAmount);
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ATTACK_MAGIC_SLOW_1, 0.65f, 0.75f, 0.85f);
            SFX.Play(SFX.ORB_LIGHTNING_EVOKE, 0.45f, 0.5f, 1.05f);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (playPowersTwice && (card.type == AbstractCard.CardType.POWER) && GameUtilities.CanPlayTwice(card))
            {
                GameActions.Top.PlayCopy(card, JUtils.SafeCast(action.target, AbstractMonster.class));
                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            for (AbstractCard c : player.hand.group)
            {
                if (c.type == CardType.POWER)
                {
                    GameActions.Bottom.SuperFlash(c);
                }
            }

            playPowersTwice = true;

            this.flash();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            playPowersTwice = false;

            GameActions.Bottom.RecoverHP(amount)
            .AddCallback(amount ->
            {
                if (amount > 0)
                {
                    this.flashWithoutSound();
                }
            });
        }
    }
}