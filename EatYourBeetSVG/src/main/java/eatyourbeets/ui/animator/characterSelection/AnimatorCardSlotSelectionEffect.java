package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorCardSlot;
import eatyourbeets.rooms.FakeEventRoom;
import eatyourbeets.ui.controls.GUI_DynamicCardGrid;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.utilities.InputManager;

import java.util.ArrayList;

public class AnimatorCardSlotSelectionEffect extends EYBEffect
{
    private static final GUI_TextBox cardValue_text = new
    GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Hitbox(AbstractCard.IMG_WIDTH * 0.6f, AbstractCard.IMG_HEIGHT * 0.15f))
    .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
    .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
    .SetAlignment(0.5f, 0.5f)
    .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

    private final AnimatorCardSlot slot;
    private final ArrayList<EYBCard> cards;
    private boolean draggingScreen = false;
    private EYBCard selectedCard;
    private GUI_DynamicCardGrid grid;

    public AnimatorCardSlotSelectionEffect(AnimatorCardSlot slot)
    {
        super(0.7f, true);

        if (AbstractDungeon.currMapNode == null)
        {
            AbstractDungeon.currMapNode = FakeEventRoom.MapRoomNode; // Because otherwise CardGlowBorder crashes in the CONSTRUCTOR
        }

        this.selectedCard = slot.GetCard(false);
        this.slot = slot;
        this.cards = slot.GetSelectableCards();

        if (cards.isEmpty())
        {
            Complete();
            return;
        }

        this.grid = new GUI_DynamicCardGrid()
        .UseScrollbar(false)
        .AddPadY(AbstractCard.IMG_HEIGHT * 0.15f)
        .SetOnCardClick(c -> OnCardClicked((EYBCard) c))
        .SetOnCardRender((sb, c) -> OnCardRender(sb, (EYBCard) c));

        for (EYBCard card : cards)
        {
            card.current_x = InputHelper.mX;
            card.current_y = InputHelper.mY;
            grid.AddCard(card);
        }
    }

    public AnimatorCardSlotSelectionEffect SetStartingPosition(float x, float y)
    {
        for (AbstractCard c : cards)
        {
            c.current_x = x - (c.hb.width * 0.5f);
            c.current_y = y - (c.hb.height * 0.5f);
        }

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (selectedCard != null)
        {
            for (EYBCard card : cards)
            {
                if (card.cardID.equals(selectedCard.cardID))
                {
                    selectedCard = card;
                    selectedCard.beginGlowing();
                    break;
                }
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        grid.TryUpdate();

        if (grid.draggingScreen)
        {
            duration = startingDuration;
            isDone = false;
            return;
        }

        if (TickDuration(deltaTime))
        {
            if (InputManager.LeftClick.IsJustReleased() && GR.UI.TryHover(null))
            {
                Complete();
                return;
            }

            isDone = false;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        grid.TryRender(sb);
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        if (AbstractDungeon.currMapNode == FakeEventRoom.MapRoomNode)
        {
            AbstractDungeon.currMapNode = null;
        }

        if (selectedCard != null && slot.GetData() != selectedCard.cardData)
        {
            slot.Select(selectedCard.cardData, 1);
        }
    }

    private void OnCardClicked(EYBCard card)
    {
        if (card.cardData.IsNotSeen())
        {
            CardCrawlGame.sound.play("CARD_REJECT");
        }
        else
        {
            if (selectedCard != null)
            {
                selectedCard.stopGlowing();

                if (selectedCard == card)
                {
                    slot.Select(null);
                    selectedCard = null;
                    return;
                }
            }

            selectedCard = card;
            CardCrawlGame.sound.play("CARD_SELECT");
            slot.Select(card.cardData, 1);
            card.beginGlowing();
        }
    }

    private void OnCardRender(SpriteBatch sb, EYBCard card)
    {
        for (AnimatorCardSlot.Item item : slot.Cards)
        {
            if (item.data == card.cardData)
            {
                cardValue_text
                .SetText(item.estimatedValue)
                .SetFontColor(item.estimatedValue < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
                .SetPosition(card.hb.cX, card.hb.cY - (card.hb.height * 0.65f))
                .Render(sb);
                return;
            }
        }
    }
}