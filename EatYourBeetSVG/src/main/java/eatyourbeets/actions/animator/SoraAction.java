package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.misc.SoraEffects.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class SoraAction extends EYBAction
{
    private static final ArrayList<SoraEffect> attackPool = new ArrayList<>();
    private static final ArrayList<SoraEffect> defendPool = new ArrayList<>();
    private static final ArrayList<SoraEffect> preparePool = new ArrayList<>();

    private final RandomizedList<SoraEffect> attackList = new RandomizedList<>();
    private final RandomizedList<SoraEffect> defendList = new RandomizedList<>();
    private final RandomizedList<SoraEffect> prepareList = new RandomizedList<>();
    private final ArrayList<SoraEffect> currentEffects = new ArrayList<>();

    protected SoraAction(SoraAction copy, int times)
    {
        super(ActionType.SPECIAL);

        message = CardRewardScreen.TEXT[1];

        InitializeRandomLists(copy);
        Initialize(times, copy.name);
    }

    public SoraAction(String sourceName, int times)
    {
        super(ActionType.SPECIAL);

        message = CardRewardScreen.TEXT[1];

        InitializeRandomLists(null);
        Initialize(times, sourceName);
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        currentEffects.clear();
        currentEffects.add(attackList.Retrieve(AbstractDungeon.cardRandomRng));
        currentEffects.add(defendList.Retrieve(AbstractDungeon.cardRandomRng));
        currentEffects.add(prepareList.Retrieve(AbstractDungeon.cardRandomRng));

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (SoraEffect e : currentEffects)
        {
            if (e != null)
            {
                e.sora.applyPowers();
                e.sora.initializeDescription();

                group.addToTop(e.sora);
            }
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, false, CreateMessage());
    }

    @Override
    protected void UpdateInternal()
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            card.untip();
            card.unhover();

            for (SoraEffect e : currentEffects)
            {
                if (e.sora == card)
                {
                    e.EnqueueAction(AbstractDungeon.player);
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();

        if (isDone && amount > 1)
        {
            GameActions.Top.Add(new SoraAction(this, amount -1));
        }
    }

    private void InitializeRandomLists(SoraAction copy)
    {
        if (copy != null)
        {
            attackList.AddAll(copy.attackList.GetInnerList());
            defendList.AddAll(copy.defendList.GetInnerList());
            prepareList.AddAll(copy.prepareList.GetInnerList());
        }

        if (attackList.Count() == 0)
        {
            attackList.AddAll(attackPool);
        }

        if (defendList.Count() == 0)
        {
            defendList.AddAll(defendPool);
        }

        if (prepareList.Count() == 0)
        {
            prepareList.AddAll(preparePool);
        }
    }

    static
    {
        //attackPool.add(new SoraEffect_GainThorns       (9 , 0));
        attackPool.add(new SoraEffect_DamageRandom     (4 , 0));
        attackPool.add(new SoraEffect_DamageAll        (5 , 0));
        attackPool.add(new SoraEffect_GainForce        (7 , 0));
        attackPool.add(new SoraEffect_ApplyVulnerable  (12, 0));

        defendPool.add(new SoraEffect_GainBlock        (6 , 1));
        defendPool.add(new SoraEffect_GainAgility      (8 , 1));
        defendPool.add(new SoraEffect_ApplyWeak        (11, 1));
        defendPool.add(new SoraEffect_GainTemporaryHP  (15, 1));

        preparePool.add(new SoraEffect_UpgradeCard     (3 , 2));
        preparePool.add(new SoraEffect_Motivate        (10, 2));
        preparePool.add(new SoraEffect_CycleCards      (13, 2));
        preparePool.add(new SoraEffect_DrawCards       (14, 2));
    }
}
