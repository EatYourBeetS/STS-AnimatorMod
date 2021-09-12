package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.RenderHelpers;

public abstract class AnimatorCard_Curse extends AnimatorCard
{
    protected boolean playAtEndOfTurn;

    protected AnimatorCard_Curse(EYBCardData data, boolean playAtEndOfTurn)
    {
        super(data);

        this.playAtEndOfTurn = playAtEndOfTurn;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        if (playAtEndOfTurn)
        {
            dontTriggerOnUseCard = true;

            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    protected Texture GetCardBackground()
    {
        return isPopup ? IMAGES.CARD_BACKGROUND_SKILL_L.Texture() : IMAGES.CARD_BACKGROUND_SKILL.Texture();
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        RenderHelpers.DrawGrayscale(sb, () -> {
            RenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), new Color(0.2f, 0.2f, 0.2f, transparency), transparency, popUpMultiplier);
            return true;});
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);

            ColoredString costString = GetCostString();
            if (costString != null)
            {
                BitmapFont font = RenderHelpers.GetEnergyFont(this);
                RenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                RenderHelpers.ResetFont(font);
            }
        }
    }
}