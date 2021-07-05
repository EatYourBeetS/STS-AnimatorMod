package eatyourbeets.effects.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.ui.controls.GUI_CardGrid;
import eatyourbeets.utilities.InputManager;

public class ShowCardPileEffect extends EYBEffectWithCallback<CardGroup>
{
    private final CardGroup cards;
    private static final float DUR = 1.5f;
    private boolean draggingScreen = false;
    private Color screenColor;

    private GUI_CardGrid grid;

    public ShowCardPileEffect(CardGroup cards)
    {
        super(0.7f);

        this.cards = cards;
        this.isRealtime = true;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0.8f;
        AbstractDungeon.overlayMenu.proceedButton.hide();

        if (cards.isEmpty())
        {
            Complete(cards);
            return;
        }

        grid = new GUI_CardGrid()
        .CanDragScreen(false)
        .AddCards(cards.group);
    }

    public ShowCardPileEffect SetStartingPosition(float x, float y)
    {
        for (AbstractCard c : cards.group)
        {
            c.current_x = x - (c.hb.width * 0.5f);
            c.current_y = y - (c.hb.height * 0.5f);
        }

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        grid.TryUpdate();

        if (grid.scrollBar.isDragging)
        {
            duration = startingDuration;
            isDone = false;
            return;
        }

        if (TickDuration(deltaTime))
        {
            if (InputManager.LeftClick.IsJustReleased() || InputManager.RightClick.IsJustReleased())
            {
                Complete(this.cards);
                return;
            }

            isDone = false;
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        grid.TryRender(sb);
    }
}
