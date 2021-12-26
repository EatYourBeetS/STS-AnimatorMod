package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.rooms.FakeEventRoom;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLCardSlot;
import pinacolada.ui.controls.GUI_CardGrid;
import pinacolada.ui.controls.GUI_TextBox;

import java.util.ArrayList;

public class PCLCardSlotSelectionEffect extends EYBEffectWithCallback<Object>
{
    private static final GUI_TextBox cardValue_text = new
    GUI_TextBox(GR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Hitbox(AbstractCard.IMG_WIDTH * 0.6f, AbstractCard.IMG_HEIGHT * 0.15f))
    .SetBackgroundTexture(GR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
    .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
    .SetAlignment(0.5f, 0.5f)
    .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

    private final PCLCardSlot slot;
    private final ArrayList<PCLCard> cards;
    private boolean draggingScreen = false;
    private PCLCard selectedCard;
    private GUI_CardGrid grid;

    public PCLCardSlotSelectionEffect(PCLCardSlot slot)
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

        this.grid = new GUI_CardGrid()
        .AddPadY(AbstractCard.IMG_HEIGHT * 0.15f)
        .SetEnlargeOnHover(false)
        .SetOnCardClick(c -> OnCardClicked((PCLCard) c))
        .SetOnCardRender((sb, c) -> OnCardRender(sb, (PCLCard) c));

        for (PCLCard card : cards)
        {
            card.current_x = InputHelper.mX;
            card.current_y = InputHelper.mY;
            grid.AddCard(card);
        }
    }

    public PCLCardSlotSelectionEffect SetStartingPosition(float x, float y)
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
            for (PCLCard card : cards)
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

        if (InputHelper.justClickedLeft && grid.hoveredCard == null)
        {
            Complete();
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

    private void OnCardClicked(PCLCard card)
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
            }

            selectedCard = card;
            CardCrawlGame.sound.play("CARD_SELECT");
            slot.Select(card.cardData, 1);
            card.beginGlowing();
            Complete();
        }
    }

    private void OnCardRender(SpriteBatch sb, PCLCard card)
    {
        for (PCLCardSlot.Item item : slot.Cards)
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