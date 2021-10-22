package eatyourbeets.cards.animator.special;

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
import eatyourbeets.actions.animator.QuestionMarkAction;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

import static eatyourbeets.ui.animator.combat.EYBCardAffinitySystem.SCALING_DIVISION;

public abstract class OrbCore extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(OrbCore.class.getSimpleName());

    private static final ArrayList<OrbCore> cores = new ArrayList<>();
    private static final RandomizedList<OrbCore> cores0 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores1 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores2 = new RandomizedList<>();

    protected static EYBCardData RegisterOrbCore(Class<? extends AnimatorCard> type, EYBCardTooltip orbTooltip, EYBCardTooltip scalingTooltip, EYBCardTooltip affinityTooltip)
    {
        final EYBCardData data = Register(type);
        final CardStrings strings = GR.GetCardStrings(ID);
        final String orbEffect = JUtils.Format(data.Strings.DESCRIPTION, scalingTooltip.id);
        data.Strings.DESCRIPTION = JUtils.Format(strings.DESCRIPTION, orbTooltip.id, orbEffect, affinityTooltip.id);
        data.Strings.EXTENDED_DESCRIPTION = ArrayUtils.addAll(strings.EXTENDED_DESCRIPTION,data.Strings.EXTENDED_DESCRIPTION);
        data.SetSharedData(orbTooltip);
        return data;
    }

    public static SelectFromPile SelectCoreAction(String name, int amount)
    {
        return new SelectFromPile(name, amount, OrbCore.CreateCoresGroup(true, GameUtilities.GetRNG()));
    }

    public static CardGroup CreateCoresGroup(boolean anyCost, Random rng)
    {
        InitializeCores();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        if (anyCost)
        {
            RandomizedList<AbstractCard> temp = new RandomizedList<>(cores);
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

        return new ArrayList<>(cores);
    }

    private static void InitializeCores()
    {
        if (cores.size() == 0)
        {
            cores0.Add(new OrbCore_Fire());
            cores0.Add(new OrbCore_Lightning());
            cores1.Add(new OrbCore_Dark());
            cores1.Add(new OrbCore_Frost());
            cores2.Add(new OrbCore_Plasma());
            cores2.Add(new OrbCore_Chaos());
            cores2.Add(new OrbCore_Air());
            cores2.Add(new OrbCore_Earth());
            cores2.Add(new OrbCore_Water());

            cores.addAll(cores0.GetInnerList());
            cores.addAll(cores1.GetInnerList());
            cores.addAll(cores2.GetInnerList());
        }
    }

    public OrbCore(EYBCardData data, int modifier, int affinityCost)
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
        GameActions.Bottom.ApplyPower(new OrbCorePower(p, this, magicNumber, secondaryValue));
    }

    public void ChannelOrb() {
        try {
            AbstractOrb o = GetOrb().getConstructor().newInstance();
            GameActions.Bottom.ChannelOrb(o);
        } catch (Exception e) {
            throw new RuntimeException("Orb cannot be created: " + GetOrb().toString());
        }
    }

    public Affinity GetAffinity() {
        return JUtils.FindMax(Affinity.All(), af -> affinities.GetLevel(af, false));
    }

    public EYBCardTooltip GetTooltip() {
        Affinity af = GetAffinity();
        return (af == Affinity.Star ? Affinity.General.GetTooltip() : af.GetTooltip());
    }

    public abstract Class<? extends AbstractOrb> GetOrb();

    public static class OrbCorePower extends AnimatorClickablePower implements OnOrbApplyFocusSubscriber, OnOrbPassiveEffectSubscriber
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

            CombatStats.onOrbApplyFocus.Subscribe(this);
            CombatStats.onOrbPassiveEffect.Subscribe(this);
        }


        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbApplyFocus.Unsubscribe(this);
            CombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            String appendix = JUtils.Format(powerStrings.DESCRIPTIONS[1], amount);
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
                else if (!Chaos.class.equals(card.GetOrb())){
                    orb.passiveAmount += GetScaledIncrease();
                    orb.evokeAmount += GetScaledIncrease();
                }
            }
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Chaos.class.equals(card.GetOrb()) && Chaos.ORB_ID.equals(orb.ID)) {
                AnimatorCard c = QuestionMarkAction.GetRandomCard();
                if (c != null)
                {
                    GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
                }
                flash();
            }
        }

        private float GetScaledIncrease() {
            return CombatStats.Affinities.GetPowerAmount(card.GetAffinity()) * amount / (float)SCALING_DIVISION;
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