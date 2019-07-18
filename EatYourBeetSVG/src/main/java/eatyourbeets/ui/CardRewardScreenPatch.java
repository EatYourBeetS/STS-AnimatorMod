package eatyourbeets.ui;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.cards.Synergy;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.effects.HideCardEffect;
import eatyourbeets.relics.BundledRelicContainer;
import eatyourbeets.relics.BundledRelicProvider;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.animator.PurgingStone_Cards;
import eatyourbeets.ui.buttons.BanCardButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CardRewardScreenPatch
{
    public static final Logger logger = LogManager.getLogger(CardRewardScreenPatch.class.getName());

    private static final ArrayList<BanCardButton> buttons = new ArrayList<>();
    private static BundledRelicContainer rewardBundle;
    private static PurgingStone_Cards purgingStone;
    private static RewardItem rewardItem;
    private static boolean canBan;

    public static void Open(CardRewardScreen screen, ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        canBan = false;
        buttons.clear();
        rewardItem = rItem;
        rewardBundle = null;

        if (PlayerStatistics.InBattle()) // || !(AbstractDungeon.player instanceof AnimatorCharacter))
        {
            return;
        }

        rewardBundle = BundledRelicProvider.SetupBundledRelics(rItem, cards);
        if (rewardBundle != null)
        {
            rewardBundle.Open(cards);
        }

        purgingStone = PurgingStone_Cards.GetInstance();
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
        AbstractPlayer player = AbstractDungeon.player;
        Synergy bannedSynergy = null;
        BanCardButton toBan = null;
        BanCardButton toRemove = null;
        for (BanCardButton banButton : buttons)
        {
            banButton.update();

            if (banButton.banned)
            {
                purgingStone.Ban(banButton.card);
                toBan = banButton;
            }
        }

        buttons.remove(toBan);

        while (toBan != null || toRemove != null)
        {
            buttons.remove(toRemove);

            if (toBan != null)
            {
                AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(toBan.card));
                AbstractDungeon.effectsQueue.add(new HideCardEffect(toBan.card));

                int index = rewardItem.cards.indexOf(toBan.card);
                rewardItem.cards.remove(index);

                rewardBundle.Remove(toBan.card);
                toBan.hideInstantly();

                AbstractCard replacement = null;
                boolean searchingCard = true;
                while (searchingCard)
                {
                    searchingCard = false;

                    AbstractCard temp = returnRandomCard();
                    if (temp == null)
                    {
                        break;
                    }

                    for (AbstractCard c : rewardItem.cards)
                    {
                        if (temp.cardID.equals(c.cardID))
                        {
                            searchingCard = true;
                        }
                    }

                    if (!searchingCard)
                    {
                        replacement = temp.makeCopy();
                    }
                }

                if (replacement == null)
                {
                    break;
                }

                if (replacement.type == AbstractCard.CardType.ATTACK && player.hasRelic(MoltenEgg2.ID) ||
                        (replacement.type == AbstractCard.CardType.SKILL && player.hasRelic(ToxicEgg2.ID)) ||
                        (replacement.type == AbstractCard.CardType.POWER && player.hasRelic(FrozenEgg2.ID)))
                {
                    replacement.upgrade();
                }

                replacement.drawScale = replacement.targetDrawScale = 0.75f;
                replacement.isSeen = true;
                replacement.target_x = replacement.current_x = toBan.card.target_x;
                replacement.target_y = replacement.current_y = toBan.card.target_y;
                rewardItem.cards.add(index, replacement);

                if (purgingStone.CanBan(replacement))
                {
                    BanCardButton banButton = new BanCardButton(replacement);
                    banButton.show();
                    buttons.add(banButton);
                }
            }

            toRemove = null;
            toBan = null;
            for (BanCardButton banButton : buttons)
            {
                if (purgingStone.IsBanned(banButton.card))
                {
                    toRemove = banButton;
                    toBan = banButton;
                    break;
                }
                else if (!purgingStone.CanBan(banButton.card))
                {
                    toRemove = banButton;
                    break;
                }
            }
        }
    }


    public static AbstractCard returnRandomCard()
    {
        ArrayList<AbstractCard> list;
        if (AbstractDungeon.cardRng.randomBoolean(0.4f))
        {
            list = AbstractDungeon.srcUncommonCardPool.group;
        }
        else
        {
            list = AbstractDungeon.srcCommonCardPool.group;
        }

        if (list != null && list.size() > 0)
        {
            return list.get(AbstractDungeon.cardRng.random(list.size() - 1));
        }
        else
        {
            return null;
        }
    }
}