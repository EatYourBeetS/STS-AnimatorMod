package pinacolada.relics.pcl;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.monsters.Bosses.KrulTepes;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class ExquisiteBloodVial extends PCLRelic implements OnRelicObtainedSubscriber
{
    public static final String ID = CreateFullID(ExquisiteBloodVial.class);
    public static final int HEAL_AMOUNT = 2;

    private boolean empowered;
    private int regenAmount;
    private int maxHPAmount;

    public ExquisiteBloodVial()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (trigger == OnRelicObtainedSubscriber.Trigger.Equip && relic instanceof BloodVial && empowered)
        {
            PCLGameEffects.Queue.RemoveRelic(relic).AddCallback(() -> AddCounter(1));
        }
    }

    public String getUpdatedDescription()
    {
        if (counter < 0)
        {
            return PCLJUtils.Format(this.DESCRIPTIONS[0], HEAL_AMOUNT);
        }
        else
        {
            return PCLJUtils.Format(this.DESCRIPTIONS[1], regenAmount, maxHPAmount);
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        if (empowered)
        {
            PCLActions.Bottom.StackPower(new RegenPower(player, regenAmount));
            player.increaseMaxHp(maxHPAmount, true);
        }
        else
        {
            PCLActions.Top.Add(new RelicAboveCreatureAction(player, this));
            PCLActions.Top.Heal(HEAL_AMOUNT);
        }

        flash();
    }

    @Override
    public void update()
    {
        super.update();

        if (empowered || AbstractDungeon.currMapNode == null)
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
            RestRoom room = PCLJUtils.SafeCast(AbstractDungeon.getCurrRoom(), RestRoom.class);
            if (room != null && room.event == null)
            {
                MapRoomNode cur = AbstractDungeon.currMapNode;

                cur.room = new MonsterRoom();
                cur.room.rewardAllowed = false;
                cur.room.rewards.clear();
                cur.room.monsters = new MonsterGroup(new KrulTepes());
                cur.room.monsters.init();

                AbstractDungeon.currMapNode = new MapRoomNode(cur.x, cur.y - 1);
                AbstractDungeon.currMapNode.room = room;
                AbstractDungeon.nextRoom = cur;
                AbstractDungeon.lastCombatMetricKey = KrulTepes.ID;
                AbstractDungeon.nextRoomTransitionStart();
            }
        }
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        Refresh();
    }

    public void Refresh()
    {
        regenAmount = counter;
        maxHPAmount = 1 + (counter / 3);

        description = getUpdatedDescription();
        initializeTips();
    }

    public void UnlockPotential()
    {
        if (!empowered)
        {
            empowered = true;

            if (counter < 0)
            {
                counter = 0;
            }

            for (AbstractRelic relic : player.relics)
            {
                if (relic instanceof BloodVial)
                {
                    PCLGameEffects.Queue.RemoveRelic(relic).AddCallback(() -> AddCounter(1));
                }
            }
        }
    }
}