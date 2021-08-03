package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class RerollCardButton extends GUI_Button
{
    public AnimatorCardRewardReroll container;
    public AdvancedHitbox hitbox;
    public AbstractCard card;
    public boolean used;

    public RerollCardButton(AnimatorCardRewardReroll container, AbstractCard card)
    {
        super(GR.Common.Images.SquaredButton.Texture(), 0, 0);

        this.container = container;
        this.hitbox = (AdvancedHitbox) hb;
        this.card = card;

        SetDimensions(AbstractCard.IMG_WIDTH * 0.85f, AbstractCard.IMG_HEIGHT * 0.175f);
        SetColor(new Color(0.8f, 0.2f, 0.2f, 1f));
        SetText(GR.Animator.Strings.Rewards.Reroll);
        SetOnClick(() -> this.container.Reroll(this));
        SetPosition(GetTargetCX(), GetTargetCY());
    }

    @Override
    public void Update()
    {
        hitbox.target_cX = GetTargetCX();
        hitbox.target_cY = GetTargetCY();
        SetInteractable(!card.hb.hovered);

        super.Update();
    }

    private float GetTargetCX()
    {
        return card.current_x;
    }

    private float GetTargetCY()
    {
        return card.current_y + (AbstractCard.IMG_HEIGHT * 0.515f);
    }
}