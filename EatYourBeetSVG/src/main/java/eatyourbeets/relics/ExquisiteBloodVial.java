package eatyourbeets.relics;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.effects.ExquisiteBloodVialIncreaseCounterEffect;
import eatyourbeets.monsters.KrulTepes;

import java.util.ArrayList;

public class ExquisiteBloodVial extends AnimatorRelic
{
    private static final int HEAL_AMOUNT = 2;

    public static final String ID = CreateFullID(ExquisiteBloodVial.class.getSimpleName());

    public boolean truePotential;

    private int regenAmount;
    private int maxHPAmount;

    public ExquisiteBloodVial()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public static void OnRelicReceived(AbstractRelic relic)
    {
        if (relic.relicId.equals(BloodVial.ID))
        {
            AbstractPlayer p = AbstractDungeon.player;
            ExquisiteBloodVial exquisiteBloodVial = (ExquisiteBloodVial) p.getRelic(ExquisiteBloodVial.ID);
            if (exquisiteBloodVial != null && exquisiteBloodVial.truePotential)
            {
                AbstractDungeon.player.relics.remove(relic);
                exquisiteBloodVial.IncreaseCounter();
                exquisiteBloodVial.flash();
                //AbstractDungeon.player.reorganizeRelics();
                //AbstractDungeon.topPanel.adjustRelicHbs();
            }
        }
    }

    public String getUpdatedDescription()
    {
        if (counter < 0)
        {
            return this.DESCRIPTIONS[0] + HEAL_AMOUNT + this.DESCRIPTIONS[1];
        }
        else
        {
            return this.DESCRIPTIONS[2] + regenAmount + this.DESCRIPTIONS[3] + maxHPAmount + this.DESCRIPTIONS[4];
        }
    }

    @Override
    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (truePotential)
        {
            GameActionsHelper.ApplyPower(p, p, new RegenPower(p, regenAmount), regenAmount);
            p.increaseMaxHp(maxHPAmount, true);
        }
        else
        {
            GameActionsHelper.AddToTop(new RelicAboveCreatureAction(p, this));
            GameActionsHelper.AddToTop(new HealAction(p, p, HEAL_AMOUNT));
        }

        this.flash();
    }

    @Override
    public void update()
    {
        super.update();

        if (truePotential)
        {
            return;
        }

        if (counter > 0)
        {
            UnlockPotential();
            Refresh();
        }
        else if (HitboxRightClick.rightClicked.get(this.hb))
        {
            RestRoom room = Utilities.SafeCast(AbstractDungeon.getCurrRoom(), RestRoom.class);
            if (room != null && room.event == null)
            {
                //AbstractDungeon.player.masterDeck.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);

                MapRoomNode cur = AbstractDungeon.currMapNode;

//                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom)
//                {
//                    AbstractDungeon.monsterList.add(1, KrulTepes.ID);
//                }
//                else
//                {
//                    AbstractDungeon.monsterList.add(0, KrulTepes.ID);
//                }

                MapRoomNode node = new MapRoomNode(cur.x, cur.y);
                node.room = new MonsterRoom();
                node.room.rewardAllowed = false;
                node.room.rewards.clear();
                node.room.monsters = new MonsterGroup(new KrulTepes());
                node.room.monsters.init();

                ArrayList<MapEdge> curEdges = cur.getEdges();
                for (MapEdge edge : curEdges)
                {
                    node.addEdge(edge);
                }

                AbstractDungeon.nextRoom = node;
                AbstractDungeon.nextRoomTransitionStart();

                room.cutFireSound();

                CardCrawlGame.music.unsilenceBGM();
                AbstractDungeon.scene.fadeOutAmbiance();
            }
        }
    }

    public void IncreaseCounter()
    {
        this.counter += 1;
        Refresh();
    }

    public void Refresh()
    {
        this.regenAmount = this.counter;
        this.maxHPAmount = 1 + (this.counter / 3);

        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void UnlockPotential()
    {
        if (!truePotential)
        {
            this.truePotential = true;
            if (counter < 0)
            {
                this.counter = 0;
            }

            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic relic : p.relics)
            {
                if (relic.relicId.equals(BloodVial.ID))
                {
                    AbstractDungeon.effectsQueue.add(new ExquisiteBloodVialIncreaseCounterEffect(this, (BloodVial) relic));
                }
            }
        }
    }

//    @Override
//    public Integer onSave()
//    {
//        return counter;
//    }
//
//    @Override
//    public void onLoad(Integer value)
//    {
//        logger.info("ONLOAD: " + value);
//        if (value == null || value < 0)
//        {
//            savedCounter = -1;
//            counter = savedCounter;
//            logger.info("ONLOAD 1: " + counter);
//        }
//        else
//        {
//            savedCounter = value;
//            counter = savedCounter;
//            logger.info("ONLOAD 2: " + counter);
//            UnlockPotential();
//            Refresh();
//        }
//    }
}