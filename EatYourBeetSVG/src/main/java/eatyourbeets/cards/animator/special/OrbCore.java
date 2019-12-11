package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public abstract class OrbCore extends AnimatorCard implements Hidden
{
    public static String SelectionMessage = "Select an Orb Core"; // TODO: Localize this

    private static final ArrayList<AbstractCard> cores = new ArrayList<>();
    private static final RandomizedList<AbstractCard> cores0 = new RandomizedList<>();
    private static final RandomizedList<AbstractCard> cores1 = new RandomizedList<>();
    private static final RandomizedList<AbstractCard> cores2 = new RandomizedList<>();

    public OrbCore(String ID, int cost)
    {
        super(ID, cost, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
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

    public static SelectFromPile SelectCoreAction(String sourceName, int amount)
    {
        return new SelectFromPile(sourceName, amount, OrbCore.CreateCoresGroup(true)).SetMessage(SelectionMessage);
    }

    public static CardGroup CreateCoresGroup(boolean anyCost)
    {
        InitializeCores();

        Random rng = AbstractDungeon.cardRandomRng;
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

    public static ArrayList<AbstractCard> GetAllCores()
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
}