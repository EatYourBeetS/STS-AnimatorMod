package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public abstract class OrbCore extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(OrbCore.class.getSimpleName());

    private static final ArrayList<OrbCore> cores = new ArrayList<>();
    private static final RandomizedList<OrbCore> cores0 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores1 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores2 = new RandomizedList<>();

    protected static EYBCardData RegisterOrbCore(Class<? extends AnimatorCard> type, EYBCardTooltip orbTooltip)
    {
        final EYBCardData data = Register(type);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.Strings.DESCRIPTION = JUtils.Format(strings.DESCRIPTION, orbTooltip.id);
        data.Strings.EXTENDED_DESCRIPTION = strings.EXTENDED_DESCRIPTION;
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
            cores2.Add(new OrbCore_Aether());

            cores.addAll(cores0.GetInnerList());
            cores.addAll(cores1.GetInnerList());
            cores.addAll(cores2.GetInnerList());
        }
    }

    public OrbCore(EYBCardData data, int synergiesRequired)
    {
        super(data);

        Initialize(0, 0, 0, synergiesRequired);

        SetEvokeOrbCount(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        ChannelOrb();
        GameActions.Bottom.ApplyPower(new OrbCorePower(p, this, secondaryValue));
    }

    public abstract void ChannelOrb();

    public static class OrbCorePower extends AnimatorPower implements OnSynergySubscriber
    {
        protected final OrbCore card;

        public OrbCorePower(AbstractCreature owner, OrbCore card, int amount)
        {
            super(owner, card.cardData);

            this.card = card;
            this.maxAmount = amount;
            this.canBeZero = true;

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, baseAmount, card.cardData.GetSharedData());
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            amount = 0;
            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            if ((amount + 1) < baseAmount)
            {
                stackPower(1, false);
            }
            else
            {
                reducePower(amount);
                this.card.ChannelOrb();
                this.flash();
            }
        }
    }
}