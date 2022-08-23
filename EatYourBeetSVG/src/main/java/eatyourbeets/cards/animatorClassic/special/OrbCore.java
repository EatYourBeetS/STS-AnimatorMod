package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class OrbCore extends AnimatorClassicCard
{
    public static final String ORBCORE_ID = GR.AnimatorClassic.CreateID(OrbCore.class.getSimpleName());

    private static final ArrayList<OrbCore> cores = new ArrayList<>();
    private static final RandomizedList<OrbCore> cores0 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores1 = new RandomizedList<>();
    private static final RandomizedList<OrbCore> cores2 = new RandomizedList<>();

    protected final FuncT0<AbstractOrb> orbConstructor;
    protected final int orbChannelAmount;

    public OrbCore(EYBCardData data, FuncT0<AbstractOrb> orb, int amount)
    {
        super(data);

        this.orbConstructor = orb;
        this.orbChannelAmount = amount;

        SetEvokeOrbCount(orbChannelAmount);
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

    public static SelectFromPile SelectCoreAction(String name, int amount)
    {
        return new SelectFromPile(name, amount, OrbCore.CreateCoresGroup(true, GameUtilities.GetRNG()));
    }

    protected abstract AnimatorPower GetPower();

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(orbChannelAmount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (orbConstructor == null)
        {
            GameActions.Bottom.ChannelRandomOrb(orbChannelAmount);
        }
        else
        {
            GameActions.Bottom.ChannelOrbs(orbConstructor, orbChannelAmount);
        }

        GameActions.Bottom.StackPower(GetPower());
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    public abstract static class OrbCorePower extends AnimatorPower
    {
        protected int value;
        protected int uses;

        public OrbCorePower(EYBCardData data, AbstractCreature owner, int amount, int value)
        {
            super(owner, data);

            this.enabled = false;
            this.uses = amount;
            this.amount = amount;
            this.value = value;
        }

        protected abstract void OnSynergy(AbstractPlayer p, AbstractCard usedCard);

        @Override
        public void atStartOfTurn()
        {
            this.enabled = false;
            this.amount = uses;
            updateDescription();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            AnimatorClassicCard card = JUtils.SafeCast(usedCard, AnimatorClassicCard.class);
            if (card != null && card.HasSynergy())
            {
                if (!enabled)
                {
                    enabled = true;
                }
                else if (amount > 0)
                {
                    amount -= 1;

                    OnSynergy(player, usedCard);

                    this.flash();
                }

                updateDescription();
            }
        }

        @Override
        public void updateDescription()
        {
            if (enabled)
            {
                this.description = FormatDescription(0, amount, value);
            }
            else
            {
                this.description = GR.GetCardStrings(ORBCORE_ID).EXTENDED_DESCRIPTION[0];
            }
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);

            this.uses += stackAmount;
            this.updateDescription();
        }
    }
}