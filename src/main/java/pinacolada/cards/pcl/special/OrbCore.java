package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.WeightedList;
import org.apache.commons.lang3.ArrayUtils;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public abstract class OrbCore extends PCLCard
{
    public static final String ID = GR.PCL.CreateID(OrbCore.class.getSimpleName());

    private static final WeightedList<OrbCore> cores = new WeightedList<>();

    protected static PCLCardData RegisterOrbCore(Class<? extends PCLCard> type, PCLOrbHelper orbHelper)
    {
        final PCLCardData data = Register(type);
        final CardStrings strings = GR.GetCardStrings(ID);

        final String orbEffect = PCLJUtils.Format(data.Strings.DESCRIPTION, orbHelper.Affinity.GetPowerTooltip().id);
        data.Strings.DESCRIPTION = PCLJUtils.Format(strings.DESCRIPTION, orbHelper.Tooltip.id, orbEffect, orbHelper.Affinity.GetTooltip().id);
        data.Strings.EXTENDED_DESCRIPTION = ArrayUtils.addAll(strings.EXTENDED_DESCRIPTION,data.Strings.EXTENDED_DESCRIPTION);
        data.SetSharedData(orbHelper);
        return data;
    }

    public static SelectFromPile SelectCoreAction(String name, int amount)
    {
        return SelectCoreAction(name, amount, 3, false);
    }

    public static SelectFromPile SelectCoreAction(String name, int amount, int choices) {
        return SelectCoreAction(name, amount, choices, false);
    }

    public static SelectFromPile SelectCoreAction(String name, int amount, int choices, boolean upgraded) {
        return new SelectFromPile(name, amount, CreateCoresGroup(choices, GameUtilities.GetRNG(), upgraded));
    }

    public static CardGroup CreateCoresGroup(int size, Random rng, boolean upgraded)
    {
        InitializeCores();
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        WeightedList<OrbCore> temp = new WeightedList<>(cores);
        while(temp.Size() > 0 && group.size() < size) {
            AbstractCard copy = temp.Retrieve(rng, true).makeCopy();
            if (upgraded) {
                copy.upgrade();
            }
            group.group.add(copy);
        }

        return group;
    }

    public static ArrayList<OrbCore> GetAllCores()
    {
        InitializeCores();

        return new ArrayList<>(cores.GetInnerList());
    }

    private static void InitializeCores()
    {
        if (cores.Size() == 0)
        {
            cores.Add(new OrbCore_Fire(), 11);
            cores.Add(new OrbCore_Lightning(), 11);
            cores.Add(new OrbCore_Dark(), 11);
            cores.Add(new OrbCore_Frost(), 11);
            cores.Add(new OrbCore_Air(), 7);
            cores.Add(new OrbCore_Earth(), 7);
            cores.Add(new OrbCore_Plasma(), 1);
            cores.Add(new OrbCore_Chaos(), 1);
            cores.Add(new OrbCore_Water(), 1);
            cores.Add(new OrbCore_Metal(), 1);
        }
    }

    public OrbCore(PCLCardData data, int modifier, int affinityCost)
    {
        super(data);

        Initialize(0, 0, modifier, affinityCost);

        SetEvokeOrbCount(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        ChannelOrb();
        PCLActions.Bottom.ApplyPower(new OrbCorePower(p, this, magicNumber, secondaryValue));
    }

    public void ChannelOrb() {
        PCLActions.Bottom.ChannelOrbs(GetHelper(), 1);
    }

    public PCLAffinity GetRequiredAffinity() {
        PCLAffinity affinity = GetHelper().Affinity;
        return affinity != null && affinity != PCLAffinity.Star ? affinity : PCLAffinity.General;
    }
    public PCLOrbHelper GetHelper() {return cardData.GetSharedData();}
    public boolean EvokeEffect(OrbCorePower power) {return false;}
    public boolean PassiveEffect(OrbCorePower power) {return false;}

    public static class OrbCorePower extends PCLClickablePower implements OnOrbApplyFocusSubscriber, OnOrbPassiveEffectSubscriber
    {
        protected final OrbCore card;

        public OrbCorePower(AbstractCreature owner, OrbCore card, int amount, int cost)
        {
            super(owner, card.cardData, PowerTriggerConditionType.Affinity, cost, null, null, card.GetRequiredAffinity());

            this.card = card;
            this.canBeZero = true;

            Initialize(amount);
            refreshOrbs();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onOrbApplyFocus.Subscribe(this);
            PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
        }


        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbApplyFocus.Unsubscribe(this);
            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            String appendix = PCLJUtils.Format(powerStrings.DESCRIPTIONS[1], amount);
            return FormatDescription(0, this.triggerCondition.requiredAmount, card.GetRequiredAffinity().GetTooltip(), card.GetHelper().Tooltip, appendix);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            refreshOrbs();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            card.ChannelOrb();
            this.flash();
        }

        @Override
        public void OnApplyFocus(AbstractOrb orb)
        {
            if (orb != null && card.GetHelper().ID.equals(orb.ID)) {
                if (Plasma.ORB_ID.equals(orb.ID)) {
                    orb.passiveAmount += 1;
                    orb.evokeAmount += 1;
                }
                else {
                    PCLGameUtilities.ModifyOrbTemporaryFocus(orb, (int) GetScaledIncrease(), true, false);
                }
            }
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (orb != null && card.GetHelper().ID.equals(orb.ID)) {
                if (card.PassiveEffect(this)) {
                    flash();
                }
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {
            super.onEvokeOrb(orb);
            if (orb != null && card.GetHelper().ID.equals(orb.ID)) {
                if (card.EvokeEffect(this)) {
                    flash();
                }
            }
        }

        private float GetScaledIncrease() {
            return PCLCombatStats.MatchingSystem.GetPowerLevel(card.GetRequiredAffinity()) * amount;
        }

        private void refreshOrbs() {
            for (AbstractOrb orb : player.orbs) {
                if (orb != null && !(orb instanceof EmptyOrbSlot)) {
                    orb.applyFocus();
                }
            }
        }
    }
}