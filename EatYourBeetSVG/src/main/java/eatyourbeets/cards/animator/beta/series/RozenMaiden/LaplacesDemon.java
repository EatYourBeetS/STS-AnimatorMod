package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnCardMovedSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LaplacesDemon extends AnimatorCard implements OnCardMovedSubscriber
{
    public static final EYBCardData DATA = Register(LaplacesDemon.class)
    		.SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage()
            .SetMaxCopies(1)
            .PostInitialize(data -> data.AddPreview(new LaplacesDemon(1), true));

    public LaplacesDemon() {
        this(0);
    }

    public LaplacesDemon(int form)
    {
        super(DATA);

        Initialize(0, 10, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(1, 1, 0);

        AfterLifeMod.Add(this);
        SetDelayed(true);
        SetExhaust(true);
        SetForm(form, timesUpgraded);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
            Initialize(0,0);
            this.type = CardType.POWER;
        }
        else {
            cardText.OverrideDescription(null, true);
            Initialize(0, 10, 2);
            this.type = CardType.SKILL;
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public int GetXValue() {
        int sum = 0;
        for (Affinity af : Affinity.Extended()) {
            final AbstractAffinityPower p = CombatStats.Affinities.GetPower(af);
            final PowerHelper ph = p.GetThresholdBonusPower();
            if (ph != null) {
                sum += GameUtilities.GetPowerAmount(player, ph.ID);
            }
        }
        return magicNumber * sum;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            GameActions.Bottom.StackPower(new LaplacesDemonPower(p, this));
        }
        else {
            GameActions.Bottom.GainBlock(block);
            int[] damageMatrix = DamageInfo.createDamageMatrix(GetXValue(), true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE);
        }
    }

    @Override
    public void OnCardMoved(AbstractCard card, CardGroup source, CardGroup destination) {
        if (card == this) {
            SetForm(destination.type.equals(CardGroup.CardGroupType.EXHAUST_PILE) ? 1 : 0, timesUpgraded);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onCardMoved.Subscribe(this);
    }

    public static class LaplacesDemonPower extends AnimatorPower
    {
        protected final LaplacesDemon laplace;

        public LaplacesDemonPower(AbstractCreature owner, LaplacesDemon laplace)
        {
            super(owner, LaplacesDemon.DATA);
            this.laplace = laplace;
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true)) {
                GameActions.Bottom.SelectFromHand(name, 999, true)
                        .SetFilter(c -> GameUtilities.IsSoul(c) || GameUtilities.IsSameSeries(c, laplace))
                        .AddCallback(cards -> {
                            for (AbstractCard card : cards) {
                                AfterLifeMod.Add(card);
                            }
                        });
            }
            else {
                GameActions.Bottom.SelectFromHand(name, 999, true)
                        .AddCallback(cards -> {
                            for (AbstractCard card : cards) {
                                GameUtilities.ModifyCostForCombat(card, MathUtils.random(0,3), false);
                                card.flash();
                            }
                        });
            }
            flash();
        }

    }
}

