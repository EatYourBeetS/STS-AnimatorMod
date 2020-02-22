package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.utilities.RenderHelpers;

public class BanCardButton extends GUI_Button
{
    public AdvancedHitbox hitbox;
    public boolean banned;
    public AbstractCard card;

    public BanCardButton(AbstractCard card)
    {
        super(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, 0, 0);

        this.hitbox = (AdvancedHitbox) hb;
        this.card = card;

        SetDimensions(AbstractCard.IMG_WIDTH, AbstractCard.IMG_HEIGHT * 0.4f);
        SetColor(Color.RED);
        SetText(GR.Animator.Strings.Rewards.Banish);
        SetOnClick(() -> banned = true);
        SetPosition(GetTargetX(), GetTargetY());
    }

    @Override
    public void Update()
    {
        targetAlpha = (interactable ? 1 : 0.5f);

        hitbox.target_cX = GetTargetX();
        hitbox.target_cY = GetTargetY();

        super.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (!interactable)
        {
            final String text = GR.Animator.Strings.Misc.NotEnoughCards;
            final BitmapFont font = FontHelper.buttonLabelFont;
            RenderHelpers.WriteCentered(sb, font, text, hb, Color.WHITE.cpy(), 0.6f);
            RenderHelpers.ResetFont(font);
        }
        else
        {
            super.Render(sb);
        }
    }

    private float GetTargetX()
    {
        return card.current_x;
    }

    private float GetTargetY()
    {
        return card.current_y + (AbstractCard.IMG_HEIGHT * 0.5f);
    }
}