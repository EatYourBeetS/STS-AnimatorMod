package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.common.ShacklesPower;
import pinacolada.powers.replacement.PCLConstrictedPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.Map;

public class Senku extends PCLCard
{
    public static final PCLCardData DATA = Register(Senku.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.DrStone);
    public static final int CHOICES = 3;
    protected static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    protected final HashMap<String, Integer> debuffs = new HashMap<>();
    protected boolean transformed;

    public Senku()
    {
        super(DATA);

        Initialize(4, 0, 0, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Silver(1, 0, 1);

        SetProtagonist(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Senku other = (Senku) super.makeStatEquivalentCopy();
        other.debuffs.putAll(this.debuffs);
        other.SetAttackTarget(this.attackTarget);
        other.SetAttackType(this.attackType);
        other.refreshDescription();
        if (transformed) {
            other.SetExhaust(true);
            other.transformed = true;
            other.LoadImage("_0");
        }
        return other;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractGameAction.AttackEffect attackEffect = this.transformed ? AttackEffects.SMALL_EXPLOSION : this.attackType.equals(PCLAttackType.Dark) ? AttackEffects.PSYCHOKINESIS : AttackEffects.BLUNT_LIGHT;
        if (this.attackTarget.equals(eatyourbeets.cards.base.EYBCardTarget.ALL)) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(damage, true);
            PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, attackEffect);
        }
        else {
            PCLActions.Bottom.DealCardDamage(this, m, attackEffect);
        }

        if (block > 0) {
            PCLActions.Bottom.GainBlock(block);
        }

        if (magicNumber > 0) {
            PCLActions.Bottom.GainTemporaryHP(magicNumber);
        }

        for (Map.Entry<String,Integer> debuff : debuffs.entrySet()) {
            PCLPowerHelper helper = PCLPowerHelper.ALL.get(debuff.getKey());
            if (helper != null) {
                if (m != null) {
                    PCLActions.Bottom.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, m.hb.cX, m.hb.cY), 0.2f);
                }
                else {
                    PCLActions.Bottom.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, player.hb.cX + 500 * Settings.scale, player.hb.cY), 0.2f);
                }

                PCLActions.Bottom.ApplyPower(this.attackTarget.equals(eatyourbeets.cards.base.EYBCardTarget.ALL) ? TargetHelper.Enemies() : TargetHelper.Normal(m), helper, debuff.getValue());
            }
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (secondaryValue > 0) {
            makeChoice();
        }
    }

    protected void refreshDescription() {
        if (this.debuffs.size() > 0) {
            StringBuilder builder = new StringBuilder(upgraded ? cardData.Strings.UPGRADE_DESCRIPTION : cardData.Strings.DESCRIPTION);
            builder.append(" NL ");

            if (magicNumber > 0) {
                builder.append(" NL ");
                builder.append(ACTIONS.GainAmount(magicNumber, GR.Tooltips.TempHP, true));
            }

            for (Map.Entry<String,Integer> debuff : debuffs.entrySet()) {
                PCLPowerHelper helper = PCLPowerHelper.ALL.get(debuff.getKey());
                builder.append(" NL ");
                builder.append(this.attackTarget.equals(eatyourbeets.cards.base.EYBCardTarget.ALL) ? ACTIONS.ApplyToALL(debuff.getValue(), helper.Tooltip, true) : ACTIONS.Apply(debuff.getValue(), helper.Tooltip, true));
            }
            this.cardText.OverrideDescription(builder.toString(), true);
        }
    }

    private void makeChoice() {
        WeightedList<SenkuEffect> possibleEffects = new WeightedList<>();

        for (SenkuEffect effect : SenkuEffect.class.getEnumConstants()){
            if (effect.tier == secondaryValue && (upgraded || effect.alwaysAvailable)) {
                possibleEffects.Add(effect,effect.weight);
            }
        }
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < CHOICES; i++)
        {
            group.addToTop(SenkuEffect.CreateImprovedSenku(this,possibleEffects.Retrieve(rng)));
        }

        PCLActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty())
                    {
                        PCLActions.Last.ReplaceCard(uuid,cards.get(0));
                    }
                });
    }

    private enum SenkuEffect {
        ApplyBurning(BurningPower.POWER_ID, 2, 2, 8),
        ApplyFreezing(FreezingPower.POWER_ID, 2, 2, 7),
        ApplyElectrified(ElectrifiedPower.POWER_ID, 2, 2, 7),
        ApplyConstricted(PCLConstrictedPower.POWER_ID, 1, 2, 7),
        ApplyPoison(PoisonPower.POWER_ID, 4, 3, 10),
        ApplyPoison2(PoisonPower.POWER_ID, 3, 2, 10),
        ApplyPoison3(PoisonPower.POWER_ID, 3, 1, 10),
        ApplyShackles(ShacklesPower.POWER_ID, 3, 2, 7),
        ApplyVulnerable(VulnerablePower.POWER_ID, 2, 2, 8),
        ApplyWeak(WeakPower.POWER_ID, 2, 2, 8),
        GainTempHP(null, 2, 2, 7),
        GainTempHP2(null, 2, 1, 8),
        GainTempHPPlus(PoisonPower.POWER_ID, 10, 1, 6),
        IncreaseDamage(null, 6, 3, 10),
        IncreaseDamage2(null, 4, 2, 9),
        IncreaseDamage3(null, 4, 1, 10),
        IncreaseBlock(null, 6, 3, 10),
        IncreaseBlock2(null, 3, 2, 9),
        IncreaseBlock3(null, 3, 1, 10),
        MakeAOE(null,0,2, 8),
        MultiplyEffects(PoisonPower.POWER_ID,3,1,6, false);


        private final String powerID;
        private final int amount;
        private final int tier;
        private final int weight;
        private final boolean alwaysAvailable;

        SenkuEffect(String powerID, int amount, int tier, int weight){
            this(powerID,amount,tier,weight,true);
        }

        SenkuEffect(String powerID, int amount, int tier, int weight, boolean alwaysAvailable) {
            this.powerID = powerID;
            this.amount = amount;
            this.tier = tier;
            this.weight = weight;
            this.alwaysAvailable = alwaysAvailable;
        }

        protected static Senku CreateImprovedSenku(Senku senku, SenkuEffect effect) {
            Senku copy = (Senku) senku.makeStatEquivalentCopy();

            switch (effect) {
                case ApplyPoison:
                    copy.debuffs.merge(effect.powerID, effect.amount + (senku.upgraded ? 2 : 0), Integer::sum);
                    break;
                case IncreaseBlock:{
                    copy.baseBlock += effect.amount + (senku.upgraded ? 2 : 0);
                    copy.block = copy.baseBlock;
                    break;
                }
                case IncreaseBlock2:
                case IncreaseBlock3:{
                    copy.baseBlock += effect.amount + (senku.upgraded ? 1 : 0);
                    copy.block = copy.baseBlock;
                    break;
                }
                case GainTempHP:
                case GainTempHP2: {
                    PCLGameUtilities.IncreaseMagicNumber(copy, effect.amount + (senku.upgraded ? 1 : 0), false);
                    break;
                }
                case GainTempHPPlus:{
                    copy.LoadImage("_0");
                    PCLGameUtilities.IncreaseMagicNumber(copy, effect.amount, false);
                    copy.debuffs.merge(effect.powerID, 4, Integer::sum);
                    for (String powerID : copy.debuffs.keySet()) {
                        copy.debuffs.merge(powerID, 2, Integer::sum);
                    }
                    copy.SetExhaust(true);
                    copy.cost += 2;
                    copy.costForTurn += 2;
                    copy.transformed = true;
                    break;
                }
                case IncreaseDamage:{
                    copy.baseDamage += effect.amount + (senku.upgraded ? 2 : 0);
                    break;
                }
                case IncreaseDamage2:
                case IncreaseDamage3:{
                    copy.SetAttackType(PCLAttackType.Dark);
                    copy.baseDamage += effect.amount + (senku.upgraded ? 1 : 0);
                    break;
                }
                case MakeAOE:{
                    copy.SetAttackType(PCLAttackType.Dark);
                    copy.SetAttackTarget(eatyourbeets.cards.base.EYBCardTarget.ALL);
                    break;
                }
                case MultiplyEffects:{
                    copy.LoadImage("_0");
                    copy.baseDamage *= 2;
                    copy.baseBlock *= 2;
                    copy.debuffs.merge(effect.powerID, effect.amount + copy.debuffs.getOrDefault(effect.powerID,effect.amount), Integer::sum);
                    copy.SetAttackType(PCLAttackType.Dark);
                    copy.SetExhaust(true);
                    copy.cost += 2;
                    copy.costForTurn += 2;
                    copy.transformed = true;
                    break;
                }
                default: {
                    if (effect.powerID != null) {
                        copy.debuffs.merge(effect.powerID, effect.amount + (senku.upgraded ? 1 : 0), Integer::sum);
                    }
                }
            }
            PCLGameUtilities.IncreaseSecondaryValue(copy, -1, false);
            copy.refreshDescription();

            return copy;
        }
    }


}