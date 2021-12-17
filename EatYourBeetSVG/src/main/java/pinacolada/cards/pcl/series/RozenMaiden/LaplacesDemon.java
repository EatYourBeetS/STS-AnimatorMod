package pinacolada.cards.pcl.series.RozenMaiden;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class LaplacesDemon extends PCLCard
{
    public static final PCLCardData DATA = Register(LaplacesDemon.class)
    		.SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage()
            .SetMaxCopies(1)
            .PostInitialize(data -> data.AddPreview(new LaplacesDemon(1), true));

    public LaplacesDemon() {
        this(0);
    }

    public LaplacesDemon(int form)
    {
        super(DATA);

        Initialize(0, 10, 3, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAfterlife(true);
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
        return secondaryValue * PCLJUtils.Max(PCLAffinity.Extended(), a -> PCLCombatStats.MatchingSystem.GetAffinityLevel(a, true));
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
            PCLActions.Bottom.StackPower(new LaplacesDemonPower(p, magicNumber));
        }
        else {
            PCLActions.Bottom.GainBlock(block);
            int[] damageMatrix = DamageInfo.createDamageMatrix(GetXValue(), true);
            PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE);
        }
    }

    public static class LaplacesDemonPower extends PCLPower
    {

        public LaplacesDemonPower(AbstractCreature owner, int amount)
        {
            super(owner, LaplacesDemon.DATA);
            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            PCLActions.Bottom.SelectFromHand(name, amount, false)
                    .AddCallback(cards -> {
                        if (PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true)) {
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
                                    PCLGameUtilities.ModifyCostForCombat(card, totalCost, false);
                                    card.flash();
                                }
                            }
                        }
                        else {
                            for (AbstractCard card : cards) {
                                PCLCard eC = PCLJUtils.SafeCast(card, PCLCard.class);
                                if (eC != null) {
                                    int affinities = 0;
                                    for (PCLAffinity af : PCLAffinity.Extended()) {
                                        // Increase the chance of an affinity winding up as 0
                                        int val = Math.max(0, MathUtils.random(-1,2));
                                        if (val > 0) {
                                            affinities += 1;
                                        }
                                        PCLActions.Last.ModifyAffinityLevel(eC, af, val, false);

                                        // Do not let any card have more than 4 affinities
                                        if (affinities >= 4) {
                                            break;
                                        }
                                    }
                                    if (affinities == 0) {
                                        PCLActions.Last.ModifyAffinityLevel(eC, PCLAffinity.Star, MathUtils.random(1,2), false);
                                    }
                                }
                            }
                        }
                    });
            flash();
        }

    }
}

