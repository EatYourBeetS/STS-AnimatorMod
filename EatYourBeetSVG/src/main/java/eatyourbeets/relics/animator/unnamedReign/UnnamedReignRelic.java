package eatyourbeets.relics.animator.unnamedReign;

import basemod.DevConsole;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.Father;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.effects.special.UnnamedRelicEquipEffect;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.SequentialEffect;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class UnnamedReignRelic extends AnimatorRelic implements OnReceiveRewardsListener, OnAddToDeckListener
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

    @Override
    public void update()
    {
        super.update();

        if (isObtained)
        {
            DisableConsole();
        }
    }

    private void DisableConsole()
    {
        if (GR.TEST_MODE)
        {
            return;
        }

        if (DevConsole.visible)
        {
            InputHelper.regainInputFocus();
            DevConsole.visible = false;
        }

        if (Gdx.input.isKeyJustPressed(DevConsole.toggleKey) && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.FTUE)
        {
            AbstractDungeon.ftue = new FtueTip(name, GR.Animator.Strings.Misc.ConsoleDisabled,
            Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, FtueTip.TipType.NO_FTUE);
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
            final AbstractPlayer p = AbstractDungeon.player;
            final SequentialEffect effect = new SequentialEffect();
            for (AbstractRelic r : p.relics)
            {
                if (r != null && (r != relic) && !(r instanceof OnEquipUnnamedReignRelicListener))
                {
                    if (r.relicId.equals(PhilosopherStone.ID) && GR.Animator.Dungeon.BannedCards.contains(Father.DATA.ID))
                    {
                        if (player.masterDeck.findCardById(Father.DATA.ID) == null)
                        {
                            player.masterDeck.addToTop(new Father());
                        }
                    }

                    effect.Enqueue(new RemoveRelicEffect(r));
                }
            }

            effect.Enqueue(new CallbackEffect(GameActions.Bottom.Wait(0.1f), null, UnnamedReignRelic::RemoveSpecialRelics));
            effect.Enqueue(new UnnamedRelicEquipEffect());
            GameEffects.List.Add(effect);

            AbstractRelic.relicPage = 0;
            player.reorganizeRelics();
            ((UnnamedReignRelic) relic).OnManualEquip();
        }
        else if (!(relic instanceof OnEquipUnnamedReignRelicListener) && relic.tier != RelicTier.STARTER)
        {
            if (relic.relicId.equals(PhilosopherStone.ID) && GR.Animator.Dungeon.BannedCards.contains(Father.DATA.ID))
            {
                return;
            }

            for (AbstractRelic r : player.relics)
            {
                //noinspection ConstantConditions
                if ((r instanceof UnnamedReignRelic) && r != relic)
                {
                    GameEffects.Queue.RemoveRelic(relic);
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

    @Override
    public boolean OnAddToDeck(AbstractCard card)
    {
        final ArrayList<String> cards = TheUnnamedReign.GetCardReplacements(card, false);
        if (cards.size() > 0)
        {
            for (String cardID : cards)
            {
                UnlockTracker.markCardAsSeen(cardID);
                GameEffects.TopLevelQueue.ShowAndObtain(CardLibrary.getCard(cardID).makeCopy());
            }

            return false;
        }

        return true;
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
            FieldInfo<SpireField> field = JUtils.GetField("usableRelic", c);
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
