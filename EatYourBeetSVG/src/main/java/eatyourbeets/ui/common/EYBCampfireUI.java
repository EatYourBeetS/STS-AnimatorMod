package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class EYBCampfireUI extends GUIElement
{
    private static GUI_TextBox availableActions_message;
    private static ArrayList<EYBCard> cardsCache = new ArrayList<>();
    private static ArrayList<GUIElement> availableActions = new ArrayList<>();

    public EYBCampfireUI()
    {
        final Hitbox hb = new DraggableHitbox(ScreenW(0.87f), ScreenH(0.79f), ScreenW(0.12f), ScreenH(0.05f));
        availableActions_message = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), hb)
        .SetColors(new Color(0.1f, 0.1f, 0.1f, 0.5f), Colors.Gold(1))
        .SetText("Available Actions:") // TODO: Localization
        .SetAlignment(0.5f, 0.5f)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1);
    }

    public void Reset()
    {
        availableActions_message.SetActive(false);
        availableActions.clear();
        cardsCache.clear();
    }

    public void Update()
    {
        if (GR.UI.Elapsed50())
        {
            final ArrayList<String> cardIDs = new ArrayList<>();
            final ArrayList<EYBCard> cards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                final EYBCard card = JUtils.SafeCast(c, EYBCard.class);
                if (card != null && card.cardData.popupActions.size() > 0 && !cardIDs.contains(card.cardID))
                {
                    for (EYBCardPopupAction action : card.cardData.popupActions)
                    {
                        action.Refresh();

                        if (action.CanExecute(card))
                        {
                            cardIDs.add(card.cardID);
                            cards.add(card);
                            break;
                        }
                    }
                }
            }

            for (AnimatorCard card : EYBCardPopupActions.PermanentActions)
            {
                if (card != null && card.cardData.popupActions.size() > 0 && !cardIDs.contains(card.cardID))
                {
                    for (EYBCardPopupAction action : card.cardData.popupActions)
                    {
                        action.Refresh();

                        if (action.CanExecute(card))
                        {
                            cardIDs.add(card.cardID);
                            cards.add(card);
                            break;
                        }
                    }
                }
            }

            boolean rebuildActions = cards.size() != cardsCache.size();
            if (!rebuildActions)
            {
                for (int i = 0; i < cards.size(); i++)
                {
                    if (cards.get(i) != cardsCache.get(i))
                    {
                        rebuildActions = true;
                        break;
                    }
                }
            }

            if (rebuildActions)
            {
                availableActions.clear();
                cardsCache.clear();

                for (EYBCard c : cards)
                {
                    cardsCache.add(c);
                    AddAvailableAction(c);
                }
            }
        }

        if (availableActions_message.SetActive(!AbstractDungeon.isScreenUp && !CardCrawlGame.isPopupOpen && availableActions.size() > 0).TryUpdate())
        {
            for (GUIElement t : availableActions)
            {
                t.Update();
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (availableActions_message.TryRender(sb))
        {
            for (GUIElement t : availableActions)
            {
                t.Render(sb);
            }
        }
    }

    private void AddAvailableAction(EYBCard card)
    {
        final float cY = -0.65f * (0.5f + availableActions.size());
        final Hitbox hb = new RelativeHitbox(availableActions_message.hb, 0.9f, 0.6f, 0.5f, cY);
        final GUI_Button textBox = new GUI_Button(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(new Color(0.1f, 0.1f, 0.1f, 0.5f))
        .SetTextColor(Colors.Cream(1))
        .SetText(card.name)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 0.9f)
        .SetOnClick(card, (c, __) ->
        {
            if (availableActions_message.isActive)
            {
                if (AbstractDungeon.player.masterDeck.contains(c))
                {
                    GR.UI.CardPopup.Open(c, AbstractDungeon.player.masterDeck);
                }
                else
                {
                    GR.UI.CardPopup.Open(c, null);
                }
            }
        });
        availableActions.add(textBox);
    }
}
