package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.SpiritPoop;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.card.HideCardEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class AnimatorCardRewardReroll extends GUIElement
{
    protected final ArrayList<RerollCardButton> buttons = new ArrayList<>();

    protected final ActionT1<AbstractCard> onCardReroll;
    protected final ActionT1<AbstractCard> onCardAdded;
    protected PurgingStone purgingStone;
    protected boolean canReroll;
    protected RewardItem rewardItem;

    public AnimatorCardRewardReroll(ActionT1<AbstractCard> onCardAdded, ActionT1<AbstractCard> onCardReroll)
    {
        this.onCardReroll = onCardReroll;
        this.onCardAdded = onCardAdded;
    }

    public void Open(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        buttons.clear();
        rewardItem = rItem;
        isActive = false;

        purgingStone = GameUtilities.GetRelic(PurgingStone.ID);
        if (purgingStone != null && purgingStone.CanActivate(rItem))
        {
            isActive = true;

            for (AbstractCard card : rItem.cards)
            {
                buttons.add(new RerollCardButton(this, card));
            }
        }
    }

    public void Close()
    {
        SetActive(false);
        buttons.clear();
    }

    public void Reroll(RerollCardButton button)
    {
        final AbstractCard removedCard = button.card;
        int cardIndex = rewardItem.cards.indexOf(removedCard);
        if (cardIndex < 0)
        {
            return;
        }

        if (removedCard.cardID.equals(HigakiRinne.DATA.ID))
        {
            GameEffects.TopLevelList.SpawnRelic(new SpiritPoop(), button.hb.cX, button.hb.cY);
        }

        button.card = purgingStone.Reroll(rewardItem);
        if (button.card != null)
        {
            SFX.Play(SFX.CARD_SELECT);
            GameEffects.TopLevelList.Add(new ExhaustCardEffect(removedCard));
            GameEffects.TopLevelList.Add(new HideCardEffect(removedCard));
            OnCardReroll(removedCard);

            GameUtilities.CopyVisualProperties(button.card, removedCard);
            rewardItem.cards.set(cardIndex, button.card);
            OnCardAdded(button.card);
        }

        SetActive(purgingStone.CanReroll());
    }

    @Override
    public void Update()
    {
        for (RerollCardButton banButton : buttons)
        {
            banButton.Update();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        for (RerollCardButton banButton : buttons)
        {
            banButton.Render(sb);
        }
    }

    private void OnCardReroll(AbstractCard card)
    {
        if (onCardReroll != null)
        {
            onCardReroll.Invoke(card);
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
