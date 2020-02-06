package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.effects.card.HideCardEffect;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class AnimatorPurgingStoneUI extends GUIElement
{

    protected final ArrayList<BanCardButton> buttons = new ArrayList<>();

    protected ActionT1<AbstractCard> onCardBanned;
    protected PurgingStone purgingStone;
    protected boolean canBan;
    protected RewardItem rewardItem;

    public AnimatorPurgingStoneUI(ActionT1<AbstractCard> onCardBanned)
    {
        this.onCardBanned = onCardBanned;
    }

    public void Open(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        rewardItem = rItem;
        canBan = false;

        purgingStone = GameUtilities.GetRelic(PurgingStone.ID);
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

        isActive = canBan;
    }

    public void Close()
    {
        isActive = false;
        buttons.clear();
    }

    @Override
    public void Update()
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
                int index = rewardItem.cards.indexOf(toBan.card);
                if (index == -1)
                {
                    JavaUtilities.GetLogger(this).error("Could not find " + toBan.card.cardID + " in the current card reward.");
                    continue;
                }

                AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(toBan.card));
                AbstractDungeon.effectsQueue.add(new HideCardEffect(toBan.card));
                rewardItem.cards.remove(index);

                if (onCardBanned != null)
                {
                    onCardBanned.Invoke(toBan.card);
                }

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

    @Override
    public void Render(SpriteBatch sb)
    {
        if (canBan)
        {
            for (BanCardButton banButton : buttons)
            {
                banButton.render(sb);
            }
        }
    }

    public AbstractCard returnRandomCard()
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
