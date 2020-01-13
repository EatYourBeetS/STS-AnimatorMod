package eatyourbeets.screens.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.resources.GR;
import eatyourbeets.screens.AbstractScreen;
import eatyourbeets.screens.controls.DraggableHitbox;
import eatyourbeets.screens.controls.GenericButton;
import eatyourbeets.screens.controls.GridLayoutControl;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class SeriesSelectionScreen extends AbstractScreen
{
    private final Random rng = new Random();
    private final SeriesSelectionBuilder seriesSelectionBuilder = new SeriesSelectionBuilder();
    private final GridLayoutControl gridLayoutControl = new GridLayoutControl();
    private final GenericButton deselectAll;
    private final GenericButton selectRandom75;
    private final GenericButton selectRandom100;
    private final GenericButton selectAll;
    private final GenericButton confirm;
    private final RenderHelpers.TextureRenderer tip1;
    private final RenderHelpers.TextureRenderer tip2;

    public SeriesSelectionScreen()
    {
        float xPos = Settings.WIDTH * 0.82f;
        float yPos = Settings.HEIGHT * 0.70f;
        float yPosDelta = -Settings.HEIGHT * 0.08f;
        float buttonWidth = Settings.WIDTH * 0.18f;
        float buttonHeight = Settings.HEIGHT * 0.07f;

        deselectAll = CreateButton(xPos, yPos + (yPosDelta*0), buttonWidth, buttonHeight)
        .SetText("Deselect All")
        .SetOnClick(this::DeselectAll)
        .SetColor(Color.FIREBRICK);

        selectRandom75 = CreateButton(xPos, yPos + (yPosDelta*1), buttonWidth, buttonHeight)
        .SetText("Random (75 cards)")
        .SetOnClick(() -> SelectRandom(75))
        .SetColor(Color.SKY);

        selectRandom100 = CreateButton(xPos, yPos + (yPosDelta*2), buttonWidth, buttonHeight)
        .SetText("Random (100 cards)")
        .SetOnClick(() -> SelectRandom(100))
        .SetColor(Color.SKY);

        selectAll = CreateButton(xPos, yPos + (yPosDelta*3), buttonWidth, buttonHeight)
        .SetText("Select All")
        .SetOnClick(this::SelectAll)
        .SetColor(Color.ROYAL);

        tip1 = RenderHelpers.ForTexture(GR.Common.Textures.Panel).SetColor(Color.DARK_GRAY)
        .SetHitbox(new DraggableHitbox(xPos, yPos + (yPosDelta*5.3f), buttonWidth, buttonHeight*2.5f, true));

        tip2 = RenderHelpers.ForTexture(GR.Common.Textures.Panel).SetColor(Color.DARK_GRAY)
        .SetHitbox(new DraggableHitbox(xPos, yPos + (yPosDelta*6f), buttonWidth, buttonHeight*0.8f, true));

        confirm = CreateButton(xPos, yPos + (yPosDelta*7), buttonWidth, buttonHeight*1.1f)
        .SetText("Proceed")
        .SetOnClick(() -> {})
        .SetColor(Color.FOREST);
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        ArrayList<AbstractCard> promoted = new ArrayList<>();
        ArrayList<AbstractCard> beta = new ArrayList<>();
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (SeriesSelectionCard c : seriesSelectionBuilder.CreateCards())
        {
            if (c.promoted)
            {
                if (GR.Animator.Database.SelectedLoadout == c.loadout)
                {
                    promoted.add(c.card);
                }
                else
                {
                    promoted.add(0, c.card);
                }
            }
            else
            {
                if (c.loadout.IsBeta)
                {
                    beta.add(c.card);
                }
                else
                {
                    group.addToBottom(c.card);
                }

                Deselect(c.card);
            }
        }

        for (AbstractCard c : promoted)
        {
            group.addToBottom(c);
        }

        for (AbstractCard c : beta)
        {
            group.addToTop(c);
        }

        gridLayoutControl.Open(group, this::OnCardClicked);
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        gridLayoutControl.Render(sb);

        deselectAll.Render(sb);
        selectRandom75.Render(sb);
        selectRandom100.Render(sb);
        selectAll.Render(sb);
        confirm.Render(sb);

        final String message = "Select #ySeries for a total of #b75 or more cards to proceed. Obtain an additional starting #yRelic if you select at least #b100 cards.";

        BitmapFont font = FontHelper.tipBodyFont;
        tip1.Draw(sb);
        float step = tip1.hb.width * 0.1f;
        FontHelper.renderSmartText(sb, font, message, tip1.hb.x + step, tip1.hb.y + (tip1.hb.height * 0.85f),
                tip1.hb.width - (step*2), font.getLineHeight(), Settings.CREAM_COLOR);
        tip1.hb.render(sb);

        tip2.Draw(sb);
        float step2 = tip2.hb.width * 0.1f;
        FontHelper.renderSmartText(sb, font, "You have selected #b" + seriesSelectionBuilder.TotalCardsInPool + " cards.", tip2.hb.x + step2, tip2.hb.y + (tip2.hb.height * 0.6f),
                tip2.hb.width - (step2*2), font.getLineHeight(), Settings.CREAM_COLOR);
        tip2.hb.render(sb);
    }

    @Override
    public void Update()
    {
        deselectAll.Update();
        selectRandom75.Update();
        selectRandom100.Update();
        selectAll.Update();
        confirm.Update();
        tip1.hb.update();
        tip2.hb.update();

        gridLayoutControl.Update();
    }

    private void OnCardClicked(AbstractCard card)
    {
        SeriesSelectionCard c = seriesSelectionBuilder.Find(card);
        if (c.promoted)
        {
            CardCrawlGame.sound.play("CARD_REJECT");
        }
        else if (seriesSelectionBuilder.selectedCards.contains(card))
        {
            Deselect(card);
        }
        else
        {
            Select(card);
            CardCrawlGame.sound.play("CARD_SELECT");
        }
    }

    private void SelectRandom(int minimum)
    {
        RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (SeriesSelectionCard c : seriesSelectionBuilder.GetAllCards())
        {
            Deselect(c.card);
            toSelect.Add(c.card);
        }

        while (seriesSelectionBuilder.TotalCardsInPool < minimum)
        {
            Select(toSelect.Retrieve(rng));
        }
    }

    private void DeselectAll()
    {
        for (SeriesSelectionCard c : seriesSelectionBuilder.GetAllCards())
        {
            Deselect(c.card);
        }
    }

    private void SelectAll()
    {
        for (SeriesSelectionCard c : seriesSelectionBuilder.GetAllCards())
        {
            Select(c.card);
        }
    }

    private void Deselect(AbstractCard card)
    {
        if (seriesSelectionBuilder.Deselect(card))
        {
            card.stopGlowing();
            card.targetTransparency = 0.75f;
        }
    }

    private void Select(AbstractCard card)
    {
        if (seriesSelectionBuilder.Select(card))
        {
            card.beginGlowing();
            card.targetTransparency = 1f;
        }
    }
}
