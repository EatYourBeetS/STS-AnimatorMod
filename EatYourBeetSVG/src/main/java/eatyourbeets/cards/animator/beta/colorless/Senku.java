package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.powers.common.ShacklesPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;

import java.util.HashMap;
import java.util.Map;

public class Senku extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Senku.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.DrStone);
    public static final int CHOICES = 3;
    protected static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    protected final HashMap<String, Integer> debuffs = new HashMap<>();

    public Senku()
    {
        super(DATA);

        Initialize(3, 0, 0 , 3);
        SetUpgrade(0, 0, 1 , 0);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetUnique(true,false);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Senku other = (Senku) super.makeStatEquivalentCopy();
        other.debuffs.putAll(this.debuffs);
        other.SetAttackTarget(this.attackTarget);
        other.SetAttackType(this.attackType);
        other.refreshDescription();
        if (exhaust) {
            other.SetExhaust(true);
            other.LoadImage("_0");
        }
        return other;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.attackTarget.equals(EYBCardTarget.ALL)) {
            GameActions.Bottom.DealDamageToAll(this, damage > 15 ? AttackEffects.SLASH_HEAVY : AttackEffects.BLUNT_LIGHT);
        }
        else {
            GameActions.Bottom.DealDamage(this, m, damage > 15 ? AttackEffects.SLASH_HEAVY : AttackEffects.BLUNT_LIGHT);
        }

        if (block > 0) {
            GameActions.Bottom.GainBlock(block);
        }

        for (Map.Entry<String,Integer> debuff : debuffs.entrySet()) {
            PowerHelper helper = PowerHelper.ALL.get(debuff.getKey());
            if (helper != null) {
                GameActions.Bottom.ApplyPower(this.attackTarget.equals(EYBCardTarget.ALL) ? TargetHelper.Enemies() : TargetHelper.Normal(m), helper);
            }
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (secondaryValue >= 0) {
            makeChoice();
        }
    }

    protected void refreshDescription() {
        if (this.debuffs.size() > 0) {
            StringBuilder builder = new StringBuilder(cardData.Strings.DESCRIPTION);
            builder.append(" NL ");
            for (Map.Entry<String,Integer> debuff : debuffs.entrySet()) {
                PowerHelper helper = PowerHelper.ALL.get(debuff.getKey());
                builder.append(" NL ");
                builder.append(this.attackTarget.equals(EYBCardTarget.ALL) ? ACTIONS.ApplyToALL(debuff.getValue(), helper.Tooltip, true) : ACTIONS.Apply(debuff.getValue(), helper.Tooltip, true));
            }
            this.cardText.OverrideDescription(builder.toString(), true);
        }
    }

    private void makeChoice() {
        WeightedList<SenkuEffect> possibleEffects = new WeightedList<>();

        for (SenkuEffect effect : SenkuEffect.class.getEnumConstants()){
            if (effect.tier == secondaryValue) {
                possibleEffects.Add(effect,effect.weight);
            }
        }
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < CHOICES; i++)
        {
            group.addToTop(SenkuEffect.CreateImprovedSenku(this,possibleEffects.Retrieve(rng)));
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty())
                    {
                        GameActions.Last.ReplaceCard(uuid,cards.get(0));
                    }
                });
    }

    private enum SenkuEffect {
        ApplyBurning(BurningPower.POWER_ID, 2, 2, 7),
        ApplyFreezing(FreezingPower.POWER_ID, 2, 2, 7),
        ApplyPoison(PoisonPower.POWER_ID, 4, 3, 10),
        ApplyPoison2(PoisonPower.POWER_ID, 1, 2, 10),
        ApplyPoison3(PoisonPower.POWER_ID, 1, 1, 10),
        ApplyPoison4(PoisonPower.POWER_ID, 12, 1, 7),
        ApplyShackles(ShacklesPower.POWER_ID, 3, 2, 7),
        ApplyVulnerable(VulnerablePower.POWER_ID, 1, 3, 8),
        ApplyWeak(WeakPower.POWER_ID, 1, 3, 8),
        IncreaseDamage(null, 6, 3, 10),
        IncreaseDamage2(null, 2, 2, 10),
        IncreaseDamage3(null, 2, 1, 10),
        IncreaseDamage4(null, 18, 1, 7),
        IncreaseBlock(null, 5, 3, 10),
        IncreaseBlock2(null, 2, 2, 10),
        IncreaseBlock3(null, 2, 1, 10),
        IncreaseBlock4(null, 15, 1, 7),
        MakeAOE(null,0,2, 7);


        private final String powerID;
        private final int amount;
        private final int tier;
        private final int weight;

        SenkuEffect(String powerID, int amount, int tier, int weight) {
            this.powerID = powerID;
            this.amount = amount;
            this.tier = tier;
            this.weight = weight;
        }

        protected static Senku CreateImprovedSenku(Senku senku, SenkuEffect effect) {
            Senku copy = (Senku) senku.makeStatEquivalentCopy();

            switch (effect) {
                case ApplyPoison:
                case ApplyPoison2:
                case ApplyPoison3:{
                    copy.debuffs.merge(effect.powerID, effect.amount + senku.magicNumber, Integer::sum);
                    break;
                }
                case ApplyPoison4:{
                    copy.LoadImage("_0");
                    copy.debuffs.merge(effect.powerID, effect.amount, Integer::sum);
                    copy.SetExhaust(true);
                    break;
                }
                case IncreaseBlock:
                case IncreaseBlock2:
                case IncreaseBlock3:{
                    copy.baseBlock += effect.amount + senku.magicNumber;
                    copy.block = copy.baseBlock;
                    break;
                }
                case IncreaseBlock4:{
                    copy.LoadImage("_0");
                    copy.baseBlock += effect.amount + senku.magicNumber;
                    copy.SetExhaust(true);
                    break;
                }
                case IncreaseDamage:
                case IncreaseDamage2:
                case IncreaseDamage3:{
                    copy.baseDamage += effect.amount + senku.magicNumber;
                    break;
                }
                case IncreaseDamage4:{
                    copy.LoadImage("_0");
                    copy.baseDamage += effect.amount + senku.magicNumber;
                    copy.SetAttackType(EYBAttackType.Elemental);
                    copy.SetExhaust(true);
                    break;
                }
                case MakeAOE:{
                    copy.SetAttackTarget(EYBCardTarget.ALL);
                    break;
                }
                default: {
                    if (effect.powerID != null) {
                        copy.debuffs.merge(effect.powerID, effect.amount + senku.magicNumber, Integer::sum);
                    }
                }
            }
            GameUtilities.IncreaseSecondaryValue(copy, -1, false);
            copy.refreshDescription();

            return copy;
        }
    }


}