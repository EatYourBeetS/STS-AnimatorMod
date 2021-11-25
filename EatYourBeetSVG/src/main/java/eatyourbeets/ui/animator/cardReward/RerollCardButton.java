package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class RerollCardButton extends GUI_Button
{
    public AnimatorCardRewardReroll container;
    public AdvancedHitbox hitbox;
    public boolean used;

    private final int cardIndex;
    private AbstractCard card;

    public RerollCardButton(AnimatorCardRewardReroll container, int cardIndex)
    {
        super(GR.Common.Images.LongButton.Texture(), 0, 0);

        this.container = container;
        this.hitbox = (AdvancedHitbox) hb;
        this.cardIndex = cardIndex;
        this.card = GetCard(true);

        SetDimensions(AbstractCard.IMG_WIDTH * 0.85f, AbstractCard.IMG_HEIGHT * 0.175f);
        SetColor(new Color(0.8f, 0.2f, 0.2f, 1f));
        SetBorder(GR.Common.Images.LongButtonBorder.Texture(), Settings.GOLD_COLOR);
        SetText(GR.Animator.Strings.Rewards.Reroll);
        SetOnClick(() -> this.container.Reroll(this));
        SetPosition(GetTargetCX(card), GetTargetCY(card));
    }

    @Override
    public void Update()
    {
        if ((card = GetCard(false)) != null)
        {
            hitbox.target_cX = GetTargetCX(card);
            hitbox.target_cY = GetTargetCY(card);
        }

        SetInteractable(card != null && !card.hb.hovered);

        super.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (card != null)
        {
            super.Render(sb);
        }
    }

    private static float GetTargetCX(AbstractCard card)
    {
        return card.current_x;
    }

    private static float GetTargetCY(AbstractCard card)
    {
        return card.current_y + (AbstractCard.IMG_HEIGHT * 0.515f);
    }

    public AbstractCard GetCard(boolean includeColorless)
    {
        if (cardIndex < container.rewardItem.cards.size())
        {
            final AbstractCard card = container.rewardItem.cards.get(cardIndex);
            return (!includeColorless && card.color == AbstractCard.CardColor.COLORLESS) ? null : card;
        }

        return null;
    }

    public int GetIndex()
    {
        return cardIndex;
    }
}