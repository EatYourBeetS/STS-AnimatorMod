package pinacolada.blights.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class CardEffectBlight extends AbstractBlight
{
    protected static final FieldInfo<Float> _offsetX = PCLJUtils.GetField("offsetX", AbstractBlight.class);
    protected static final FieldInfo<Float> _rotation = PCLJUtils.GetField("rotation", AbstractBlight.class);

    protected AbstractRoom room;
    protected AbstractCard card;
    protected AbstractCard copy;
    protected float scaleModifier;
    protected float targetScaleModifier;

    public CardEffectBlight(AbstractCard card)
    {
        super(CardEffectBlight.class.getName() + ":" + card.cardID, card.name, card.rawDescription, "maze.png", true);

        this.card = card;
        this.copy = card.makeStatEquivalentCopy();
        this.targetScaleModifier = this.scaleModifier = 0.15f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        PCLJUtils.LogInfo(this, "render(SpriteBatch sb)");
        //super.render(sb);
    }

    @Override
    public void render(SpriteBatch sb, boolean renderAmount, Color outlineColor)
    {
        PCLJUtils.LogInfo(this, "render(SpriteBatch sb, boolean renderAmount, Color outlineColor)");
        //super.render(sb, renderAmount, outlineColor);
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb)
    {
        if (!Settings.hideRelics && !Settings.hideCards)
        {
            sb.setColor(Color.WHITE);

            card.drawScale = card.targetDrawScale = scaleModifier * scale / Settings.scale;
            card.setAngle(_rotation.Get(this), true);
            card.current_x = card.target_x = this.currentX + _offsetX.Get(null);
            card.current_y = card.target_y = this.currentY;
            card.render(sb);

            this.renderCounter(sb, true);

            if (copy.targetDrawScale > 0.05f)
            {
                copy.targetDrawScale = 0.01f;
                copy.current_y = copy.target_y = Settings.HEIGHT * 0.5f + AbstractCard.IMG_HEIGHT * 0.5f;
                if (currentX < Settings.WIDTH * 0.5f)
                {
                    copy.current_x = copy.target_x = currentX + AbstractCard.IMG_WIDTH * 0.7f;
                }
                else
                {
                    copy.current_x = copy.target_x = currentX - AbstractCard.IMG_WIDTH * 0.75f;
                }
            }

            this.hb.render(sb);
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        copy.targetDrawScale = 1f;

        if (copy.drawScale > 0.05f)
        {
            copy.render(sb);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (card.flashVfx != null)
        {
            card.flashVfx.update();
            if (card.flashVfx.isDone)
            {
                card.flashVfx = null;
                targetScaleModifier = 0.2f;
            }
        }

        scaleModifier = MathHelper.cardScaleLerpSnap(scaleModifier, targetScaleModifier);
        copy.update();
    }

    @Override
    public void flash()
    {
        card.superFlash();
        targetScaleModifier = 0.3f;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        PCLGameEffects.TopLevelList.WaitRealtime(0.01f).AddCallback(() -> AbstractDungeon.player.blights.remove(this));
    }
}