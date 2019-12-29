package eatyourbeets.relics.animator.unnamedReign;

import basemod.DevConsole;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.effects.utility.SequentialEffect;
import eatyourbeets.effects.special.UnnamedRelicEquipEffect;
import eatyourbeets.interfaces.OnEquipUnnamedReignRelicSubscriber;
import eatyourbeets.interfaces.OnRelicObtainedSubscriber;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.interfaces.OnReceiveRewardsSubscriber;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public abstract class UnnamedReignRelic extends AnimatorRelic implements OnReceiveRewardsSubscriber
{
    public UnnamedReignRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, tier, sfx);
    }

    public static boolean IsEquipped()
    {
        if (AbstractDungeon.player != null)
        {
            for (AbstractRelic r : AbstractDungeon.player.relics)
            {
                if (r instanceof UnnamedReignRelic)
                {
                    return true;
                }
            }
        }

        return false;
    }

    // TODO: Re-Enable this

    @Override
    public void update()
    {
        super.update();

        if (isObtained)
        {
            DevConsole.visible = false;
            DevConsole.commandPos = -1;
            DevConsole.currentText = "";
            DevConsole.infiniteEnergy = false;
        }
    }

    private void DisableConsole()
    {
        if (DevConsole.visible)
        {
            InputHelper.regainInputFocus();
            DevConsole.visible = false;
        }

        DevConsole.infiniteEnergy = false;
        DevConsole.enabled = false;
        DevConsole.commandPos = -1;
        DevConsole.currentText = "";
        Settings.isDebug = false;
    }

    @Override
    public void instantObtain()
    {
        super.instantObtain();

        DisableConsole();
    }

    @Override
    public void obtain()
    {
        super.obtain();

        DisableConsole();
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        super.instantObtain(p, slot, callOnEquip);

        DisableConsole();
    }

    protected abstract void OnManualEquip();

    public static void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (trigger != OnRelicObtainedSubscriber.Trigger.Equip)
        {
            return;
        }

        if (relic instanceof UnnamedReignRelic)
        {
            AbstractPlayer p = AbstractDungeon.player;

            SequentialEffect effect = new SequentialEffect();

            int goldBonus = UnnamedRelicEquipEffect.CalculateGoldBonus();

            for (AbstractRelic r : p.relics)
            {
                if (r != null && (r != relic) && !(r instanceof OnEquipUnnamedReignRelicSubscriber))
                {
                    effect.Enqueue(new RemoveRelicEffect(relic, r));
                }
            }

            effect.Enqueue(new CallbackEffect(GameActions.Bottom.Wait(0.1f), null, UnnamedReignRelic::RemoveSpecialRelics));

            AbstractRelic.relicPage = 0;
            AbstractDungeon.player.reorganizeRelics();

            ((UnnamedReignRelic) relic).OnManualEquip();

            effect.Enqueue(new UnnamedRelicEquipEffect(goldBonus));

            GameEffects.List.Add(effect);
        }
        else if (!(relic instanceof OnEquipUnnamedReignRelicSubscriber) && relic.tier != RelicTier.STARTER)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic r : p.relics)
            {
                if ((r instanceof UnnamedReignRelic) && r != relic)
                {
                    AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(r, relic));
                    r.flash();
                }
            }
        }
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards)
    {
        MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if (node != null && node.room instanceof MonsterRoom)
        {
            AddGoldToRewards(rewards, node.y);
            AddPotionToRewards(rewards);
        }
    }

    private void AddGoldToRewards(ArrayList<RewardItem> rewards, int step)
    {
        for (RewardItem rewardItem : rewards)
        {
            if (rewardItem.type == RewardItem.RewardType.GOLD)
            {
                rewardItem.goldAmt = 0;
                rewardItem.incrementGold(50 + step * 5);
                return;
            }
        }
    }

    private void AddPotionToRewards(ArrayList<RewardItem> rewards)
    {
        for (RewardItem rewardItem : rewards)
        {
            if (rewardItem.type == RewardItem.RewardType.POTION)
            {
                return;
            }
        }

        rewards.add(new RewardItem(new FalseLifePotion()));
    }

    private static void RemoveSpecialRelics(Object state, AbstractGameAction action)
    {
        try
        {
            Class<?> c = Class.forName("riskOfSpire.patches.ForUsableRelics.UsableRelicSlot");
            FieldInfo<SpireField> field = JavaUtilities.GetPrivateField("usableRelic", c);
            SpireField<?> f = field.Get(null);
            if (f != null)
            {
                f.set(AbstractDungeon.player, null);
            }
        }
        catch (ClassNotFoundException ignored) { }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }
}
