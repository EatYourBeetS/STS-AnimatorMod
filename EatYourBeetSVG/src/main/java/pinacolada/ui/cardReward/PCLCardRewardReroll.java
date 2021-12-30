package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.SpiritPoop;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import pinacolada.cards.pcl.series.Katanagatari.HigakiRinne;
import pinacolada.effects.SFX;
import pinacolada.effects.card.HideCardEffect;
import pinacolada.relics.pcl.RollingCubes;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class PCLCardRewardReroll extends GUIElement
{
    protected final ArrayList<RerollCardButton> buttons = new ArrayList<>();

    protected final ActionT1<AbstractCard> onCardReroll;
    protected final ActionT1<AbstractCard> onCardAdded;
    protected RollingCubes rollingCubes;
    protected boolean canReroll;
    protected RewardItem rewardItem;

    public PCLCardRewardReroll(ActionT1<AbstractCard> onCardAdded, ActionT1<AbstractCard> onCardReroll)
    {
        this.onCardReroll = onCardReroll;
        this.onCardAdded = onCardAdded;
    }

    public void Open(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        buttons.clear();
        rewardItem = rItem;
        isActive = false;

        rollingCubes = PCLGameUtilities.GetRelic(RollingCubes.ID);
        if (rollingCubes != null && rollingCubes.CanActivate(rItem))
        {
            isActive = true;

            for (int i = 0; i < rItem.cards.size(); i++)
            {
                buttons.add(new RerollCardButton(this, i));
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
        final int cardIndex = button.GetIndex();
        final AbstractCard removedCard = button.GetCard(false);
        if (removedCard == null || cardIndex > rewardItem.cards.size())
        {
            return;
        }

        if (removedCard.cardID.equals(HigakiRinne.DATA.ID))
        {
            PCLGameEffects.TopLevelList.SpawnRelic(new SpiritPoop(), button.hb.cX, button.hb.cY);
        }

        final AbstractCard replacement = rollingCubes.Reroll(removedCard, rewardItem);
        if (replacement!= null)
        {
            SFX.Play(SFX.CARD_SELECT);
            PCLGameEffects.TopLevelList.Add(new ExhaustCardEffect(removedCard));
            PCLGameEffects.TopLevelList.Add(new HideCardEffect(removedCard));
            OnCardReroll(removedCard);

            PCLGameUtilities.CopyVisualProperties(replacement, removedCard);
            rewardItem.cards.set(cardIndex, replacement);
            OnCardAdded(replacement);
        }

        button.SetActive(false);
        SetActive(rollingCubes.CanReroll());
    }

    @Override
    public void Update()
    {
        for (RerollCardButton banButton : buttons)
        {
            banButton.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        for (RerollCardButton banButton : buttons)
        {
            banButton.TryRender(sb);
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
