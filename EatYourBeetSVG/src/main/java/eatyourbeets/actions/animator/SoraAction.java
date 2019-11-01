package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.misc.SoraEffects.*;

import java.util.ArrayList;

public class SoraAction extends AnimatorAction
{
    //private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("ExhaustAction").TEXT;
    private static final ArrayList<SoraEffect> attackPool = new ArrayList<>();
    private static final ArrayList<SoraEffect> defendPool = new ArrayList<>();
    private static final ArrayList<SoraEffect> preparePool = new ArrayList<>();

    private final RandomizedList<SoraEffect> attackList = new RandomizedList<>();
    private final RandomizedList<SoraEffect> defendList = new RandomizedList<>();
    private final RandomizedList<SoraEffect> prepareList = new RandomizedList<>();
    private final ArrayList<SoraEffect> currentEffects = new ArrayList<>();
    private int times;

    public SoraAction(AbstractCreature target, int times)
    {
        this.times = times;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;

        InitializeRandomLists();
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
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

            if (group.size() > 0)
            {
                AbstractDungeon.gridSelectScreen.open(group, 1, false, "");
            }
            else
            {
                InitializeRandomLists();
                this.duration = Settings.ACTION_DUR_FAST;
                return;
            }
        }

        this.tickDuration();

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

            times -= 1;
            if (times > 0)
            {
                this.duration = Settings.ACTION_DUR_FAST;
            }
        }
    }

    private void InitializeRandomLists()
    {
        attackList.Clear();
        attackList.AddAll(attackPool);

        defendList.Clear();
        defendList.AddAll(defendPool);

        prepareList.Clear();
        prepareList.AddAll(preparePool);
    }

    static
    {
        //attackPool.add(new SoraEffect_GainThorns       (9 , 0));
        attackPool.add(new SoraEffect_DamageRandom     (4 , 0));
        attackPool.add(new SoraEffect_DamageAll        (5 , 0));
        attackPool.add(new SoraEffect_GainForce(7 , 0));
        attackPool.add(new SoraEffect_ApplyVulnerable  (12, 0));

        defendPool.add(new SoraEffect_GainBlock        (6 , 1));
        defendPool.add(new SoraEffect_GainAgility(8 , 1));
        defendPool.add(new SoraEffect_ApplyWeak        (11, 1));
        defendPool.add(new SoraEffect_GainTemporaryHP  (15, 1));

        preparePool.add(new SoraEffect_UpgradeCard     (3 , 2));
        preparePool.add(new SoraEffect_Motivate(10, 2));
        preparePool.add(new SoraEffect_CycleCards      (13, 2));
        preparePool.add(new SoraEffect_DrawCards       (14, 2));
    }
}
