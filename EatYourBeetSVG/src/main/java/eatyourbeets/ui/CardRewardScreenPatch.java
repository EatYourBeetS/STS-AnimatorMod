package eatyourbeets.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.effects.HideCardEffect;
import eatyourbeets.misc.BundledRelicContainer;
import eatyourbeets.misc.BundledRelicProvider;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.PurgingStone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CardRewardScreenPatch
{
    public static final Logger logger = LogManager.getLogger(CardRewardScreenPatch.class.getName());

    private static final ArrayList<BanCardButton> buttons = new ArrayList<>();
    private static BundledRelicContainer rewardBundle;
    private static PurgingStone purgingStone;
    private static RewardItem rewardItem;
    private static boolean canBan;

    public static void Open(CardRewardScreen screen, ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        canBan = false;
        buttons.clear();
        rewardItem = rItem;
        rewardBundle = null;

        if (PlayerStatistics.InBattle())
        {
            return;
        }

        rewardBundle = BundledRelicProvider.SetupBundledRelics(rItem, cards);
        if (rewardBundle != null)
        {
            rewardBundle.Open(cards);
        }

        purgingStone = PurgingStone.GetInstance();
        if (purgingStone != null && purgingStone.CanActivate(rItem))
        {
            for (AbstractCard card : cards)
            {
                if (purgingStone.CanBan(card))
                {
                    BanCardButton banButton = new BanCardButton(card);
                    banButton.show();
                    buttons.add(banButton);
                    canBan = true;
                }
            }
        }
    }

    public static void OnClose(CardRewardScreen screen)
    {
        rewardBundle = null;
        buttons.clear();
        canBan = false;
    }

    public static void Update(CardRewardScreen screen)
    {
        if (canBan)
        {
            UpdateBanButtons();
        }

        if (rewardBundle != null)
        {
            rewardBundle.Update(screen);
        }
    }

    public static void PreRender(CardRewardScreen screen, SpriteBatch sb)
    {
        if (canBan)
        {
            for (BanCardButton banButton : buttons)
            {
                banButton.render(sb);
            }
        }
    }

    public static void PostRender(CardRewardScreen screen, SpriteBatch sb)
    {
        if (rewardBundle != null)
        {
            rewardBundle.Render(screen, sb);
        }
    }

    public static void AcquireCard(CardRewardScreen screen, AbstractCard hoveredCard)
    {
        if (rewardBundle != null)
        {
            rewardBundle.Acquired(hoveredCard);
        }
    }

    private static void UpdateBanButtons()
    {
        BanCardButton toRemove = null;
        for (BanCardButton banButton : buttons)
        {
            banButton.update();
            if (banButton.banned)
            {
                AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(banButton.card));
                AbstractDungeon.effectsQueue.add(new HideCardEffect(banButton.card));
                rewardItem.cards.remove(banButton.card);

                rewardBundle.Remove(banButton.card);

                purgingStone.Ban(banButton.card);
                banButton.hideInstantly();
                toRemove = banButton;

                if (rewardItem.cards.size() == 0)
                {
                    AbstractDungeon.combatRewardScreen.rewards.remove(rewardItem);
                    AbstractDungeon.combatRewardScreen.positionRewards();
                    if (AbstractDungeon.combatRewardScreen.rewards.isEmpty())
                    {
                        AbstractDungeon.combatRewardScreen.hasTakenAll = true;
                        AbstractDungeon.overlayMenu.proceedButton.show();
                    }
                }
            }
        }

        while (toRemove != null)
        {
            buttons.remove(toRemove);
            toRemove = null;

            for (BanCardButton banButton : buttons)
            {
                if (!purgingStone.CanBan(banButton.card))
                {
                    toRemove = banButton;
                    break;
                }
            }
        }
    }
}