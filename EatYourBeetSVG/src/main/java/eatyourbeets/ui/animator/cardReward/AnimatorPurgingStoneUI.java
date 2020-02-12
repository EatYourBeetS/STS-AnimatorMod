package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.effects.card.HideCardEffect;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class AnimatorPurgingStoneUI extends GUIElement
{
    protected final ArrayList<BanCardButton> buttons = new ArrayList<>();

    protected ActionT1<AbstractCard> onCardBanned;
    protected ActionT1<AbstractCard> onCardAdded;
    protected PurgingStone purgingStone;
    protected boolean canBan;
    protected RewardItem rewardItem;

    public AnimatorPurgingStoneUI(ActionT1<AbstractCard> onCardAdded, ActionT1<AbstractCard> onCardBanned)
    {
        this.onCardBanned = onCardBanned;
        this.onCardAdded = onCardAdded;
    }

    public void Open(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        buttons.clear();
        rewardItem = rItem;
        canBan = false;

        purgingStone = GameUtilities.GetRelic(PurgingStone.ID);
        if (purgingStone != null && purgingStone.CanActivate(rItem))
        {
            for (AbstractCard card : rItem.cards)
            {
                if (TryAddButton(card))
                {
                    canBan = true;
                }
            }
        }

        isActive = canBan;
    }

    private boolean TryAddButton(AbstractCard card)
    {
        BanCardButton banButton = new BanCardButton(card);
        banButton.SetInteractable(purgingStone.CanBan(card));
        banButton.isActive = true;
        buttons.add(banButton);

        return banButton.interactable;
    }

    public void Close()
    {
        isActive = false;
        buttons.clear();
    }

    @Override
    public void Update()
    {
        BanCardButton toBan = null;
        for (BanCardButton banButton : buttons)
        {
            banButton.TryUpdate();

            if (banButton.banned && toBan == null)
            {
                toBan = banButton;
            }
        }

        if (toBan != null)
        {
            int banIndex = rewardItem.cards.indexOf(toBan.card);
            if (banIndex >= 0)
            {
                purgingStone.Ban(toBan.card);
                rewardItem.cards.remove(banIndex);
                buttons.remove(toBan);

                GameEffects.Queue.Add(new ExhaustCardEffect(toBan.card));
                GameEffects.Queue.Add(new HideCardEffect(toBan.card));
                OnCardBanned(toBan.card);

                AbstractCard replacement = FindReplacement();
                if (replacement != null)
                {
                    replacement.current_x = replacement.target_x = toBan.card.current_x;
                    replacement.current_y = replacement.target_y = toBan.card.current_y;
                    replacement.drawScale = replacement.targetDrawScale = toBan.card.drawScale;
                    rewardItem.cards.add(banIndex, replacement);
                    OnCardAdded(replacement);
                    TryAddButton(replacement);
                }
            }

            canBan = false;
            for (BanCardButton button : buttons)
            {
                button.SetInteractable(purgingStone.CanBan(button.card));
                if (button.interactable)
                {
                    canBan = true;
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
                banButton.TryRender(sb);
            }
        }
    }

    private AbstractCard FindReplacement()
    {
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

        return replacement;
    }

    private AbstractCard returnRandomCard()
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

    private void OnCardBanned(AbstractCard card)
    {
        if (onCardBanned != null)
        {
            onCardBanned.Invoke(card);
        }
    }

    private void OnCardAdded(AbstractCard card)
    {
        if (onCardAdded != null)
        {
            onCardAdded.Invoke(card);
        }
    }
}
