package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public abstract class OrbCore extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(OrbCore.class.getSimpleName());

    protected static final ArrayList<OrbCore> cores = new ArrayList<>();
    protected final FuncT0<AbstractOrb> orbConstructor;
    protected final int orbChannelAmount;

    protected static EYBCardData RegisterOrbCore(Class<? extends AnimatorCard> type, EYBCardTooltip orbTooltip)
    {
        final EYBCardData data = Register(type);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.SetSharedData(orbTooltip);
        return data;
    }

    public static SelectFromPile SelectCoreAction(String name, int amount, int size)
    {
        return SelectCoreAction(name, amount, size, false);
    }

    public static SelectFromPile SelectCoreAction(String name, int amount, int size, boolean upgraded)
    {
        return new SelectFromPile(name, amount, OrbCore.CreateCoresGroup(size, GameUtilities.GetRNG()));
    }

    public static CardGroup CreateCoresGroup(int size, Random rng)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<OrbCore> list = new RandomizedList<>(GetAllCores());
        while (list.Size() > 0 && group.size() < size)
        {
            group.group.add(list.Retrieve(rng).makeCopy());
        }

        return group;
    }

    public static ArrayList<OrbCore> GetAllCores()
    {
        if (cores.size() == 0)
        {
            cores.add(new OrbCore_Lightning());
            cores.add(new OrbCore_Frost());
            cores.add(new OrbCore_Fire());
            cores.add(new OrbCore_Dark());
            cores.add(new OrbCore_Aether());
            cores.add(new OrbCore_Plasma());
            cores.add(new OrbCore_Chaos());
        }

        return cores;
    }

    public OrbCore(EYBCardData data, FuncT0<AbstractOrb> orb, int amount)
    {
        super(data);

        Initialize(0, 0, 0);

        this.orbConstructor = orb;
        this.orbChannelAmount = amount;

        SetEvokeOrbCount(orbChannelAmount);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(orbChannelAmount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainOrbSlots(1);
        GameActions.Bottom.ChannelOrbs(orbConstructor, orbChannelAmount);
        ApplyPower();
    }

    protected abstract void ApplyPower();
}