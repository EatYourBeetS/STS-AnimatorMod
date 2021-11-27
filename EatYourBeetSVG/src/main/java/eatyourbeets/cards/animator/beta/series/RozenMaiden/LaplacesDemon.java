package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class LaplacesDemon extends AnimatorCard
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

        Initialize(0, 10, 1);
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
        return magicNumber * JUtils.Max(CombatStats.Affinities.Powers, p -> p.amount);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player != null) {
            SetForm(player.exhaustPile.contains(this) ? 1 : 0, timesUpgraded);
        }

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
                GameActions.Bottom.SelectFromHand(name, player.hand.size(), true)
                        .SetFilter(c -> GameUtilities.IsSoul(c) || GameUtilities.IsSameSeries(c, laplace))
                        .AddCallback(cards -> {
                            for (AbstractCard card : cards) {
                                AfterLifeMod.Add(card);
                            }
                        });
            }
            else {
                GameActions.Bottom.SelectFromHand(name, player.hand.size(), true)
                        .AddCallback(cards -> {
                            int totalCost = 0;
                            for (AbstractCard card : cards) {
                                if (card.costForTurn >= 0) {
                                    totalCost += card.costForTurn;
                                }
                            }
                            for (AbstractCard card : cards) {
                                if (card.costForTurn >= 0) {
                                    int newCost = MathUtils.random(0, Math.min(totalCost, 3));
                                    totalCost -= newCost;
                                    GameUtilities.ModifyCostForCombat(card, totalCost, false);
                                    card.flash();
                                }
                            }
                        });
            }
            flash();
        }

    }
}

