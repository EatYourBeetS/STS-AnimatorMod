package pinacolada.cards.pcl.special;

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
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.WeightedList;
import org.apache.commons.lang3.ArrayUtils;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
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
    private static final RandomizedList<OrbCore> cores0 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores1 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores2 = new RandomizedList<>();

    protected static PCLCardData RegisterOrbCore(Class<? extends PCLCard> type, PCLCardTooltip orbTooltip, PCLCardTooltip scalingTooltip, PCLCardTooltip affinityTooltip)
    {
        final PCLCardData data = Register(type);
        final CardStrings strings = GR.GetCardStrings(ID);
        final String orbEffect = PCLJUtils.Format(data.Strings.DESCRIPTION, scalingTooltip.id);
        data.Strings.DESCRIPTION = PCLJUtils.Format(strings.DESCRIPTION, orbTooltip.id, orbEffect, affinityTooltip.id);
        data.Strings.EXTENDED_DESCRIPTION = ArrayUtils.addAll(strings.EXTENDED_DESCRIPTION,data.Strings.EXTENDED_DESCRIPTION);
        data.SetSharedData(orbTooltip);
        return data;
    }

    public static SelectFromPile SelectCoreAction(String name, int amount)
    {
        return new SelectFromPile(name, amount, OrbCore.CreateCoresGroup(true, PCLGameUtilities.GetRNG()));
    }

    public static CardGroup CreateCoresGroup(boolean anyCost, Random rng)
    {
        InitializeCores();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        if (anyCost)
        {
            WeightedList<OrbCore> temp = new WeightedList<>(cores);
            group.group.add(temp.Retrieve(rng, true).makeCopy());
            group.group.add(temp.Retrieve(rng, true).makeCopy());
            group.group.add(temp.Retrieve(rng, true).makeCopy());
        }
        else
        {
            group.group.add(cores0.Retrieve(rng, false).makeCopy());
            group.group.add(cores1.Retrieve(rng, false).makeCopy());
            group.group.add(cores2.Retrieve(rng, false).makeCopy());
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
            cores0.Add(new OrbCore_Fire());
            cores0.Add(new OrbCore_Lightning());
            cores0.Add(new OrbCore_Dark());
            cores0.Add(new OrbCore_Frost());
            cores1.Add(new OrbCore_Air());
            cores1.Add(new OrbCore_Earth());
            cores2.Add(new OrbCore_Plasma());
            cores2.Add(new OrbCore_Chaos());
            cores2.Add(new OrbCore_Water());
            cores2.Add(new OrbCore_Metal());

            for (OrbCore core : cores0.GetInnerList()) {
                cores.Add(core, 11);
            }
            for (OrbCore core : cores1.GetInnerList()) {
                cores.Add(core, 7);
            }
            for (OrbCore core : cores2.GetInnerList()) {
                cores.Add(core, 1);
            }
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
        try {
            AbstractOrb o = GetOrb().getConstructor().newInstance();
            PCLActions.Bottom.ChannelOrb(o);
        } catch (Exception e) {
            throw new RuntimeException("Orb cannot be created: " + GetOrb().toString());
        }
    }

    public PCLAffinity GetAffinity() {
        return PCLJUtils.FindMax(PCLAffinity.All(), af -> affinities.GetLevel(af, false));
    }

    public PCLCardTooltip GetTooltip() {
        PCLAffinity af = GetAffinity();
        return (af == PCLAffinity.Star ? PCLAffinity.General.GetTooltip() : af.GetTooltip());
    }

    public boolean EvokeEffect(OrbCorePower power) {return false;}
    public boolean PassiveEffect(OrbCorePower power) {return false;}

    public abstract Class<? extends AbstractOrb> GetOrb();

    public static class OrbCorePower extends PCLClickablePower implements OnOrbApplyFocusSubscriber, OnOrbPassiveEffectSubscriber
    {
        protected final OrbCore card;

        public OrbCorePower(AbstractCreature owner, OrbCore card, int amount, int cost)
        {
            super(owner, card.cardData, PowerTriggerConditionType.Affinity, cost, null, null, card.GetAffinity());

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
            return FormatDescription(0, this.triggerCondition.requiredAmount, card.GetAffinity().GetTooltip(), card.cardData.GetSharedData(), appendix);
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
            if (orb != null && orb.getClass().equals(card.GetOrb())) {
                if (Plasma.class.equals(card.GetOrb())) {
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
            if (orb != null && orb.getClass().equals(card.GetOrb())) {
                if (card.PassiveEffect(this)) {
                    flash();
                }
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {
            super.onEvokeOrb(orb);
            if (orb != null && orb.getClass().equals(card.GetOrb())) {
                if (card.EvokeEffect(this)) {
                    flash();
                }
            }
        }

        private float GetScaledIncrease() {
            return PCLCombatStats.MatchingSystem.GetPowerLevel(card.GetAffinity()) * amount;
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