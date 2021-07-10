package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class EYBCombatInfo extends GUIElement
{
    protected final DraggableHitbox hb;
    protected final GUI_Image drag_panel;
    protected final ArrayList<EYBCombatInfo_AffinityRow> rows = new ArrayList<>();

    public EYBCombatInfo()
    {
        hb = new DraggableHitbox(ScreenW(0.027f), ScreenH(0.65f), Scale(96f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.75f));

        drag_panel = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        AffinityType[] values = AffinityType.values();
        for (int i = 0; i < values.length; i++)
        {
            rows.add(new EYBCombatInfo_AffinityRow(values[i], hb, i, values.length));
        }
    }

    @Override
    public void Update()
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || AbstractDungeon.isScreenUp || CardCrawlGame.isPopupOpen)
        {
            return;
        }

        EYBCardAffinities handAffinities;
        EYBCardAffinities cardAffinities = null;
        EYBCardAffinities strongSynergies = null;
        boolean draggingCard = false;

        AbstractCard card = player.hoveredCard;
        if (player.hoveredCard != null && (player.isDraggingCard && player.isHoveringDropZone || player.inSingleTargetMode))
        {
            draggingCard = true;
        }
        handAffinities = GameUtilities.GetTotalAffinity(player.hand.group, card, 1);

        if (card instanceof EYBCard)
        {
            cardAffinities = ((EYBCard) card).affinities;

            if (cardAffinities != null)
            {
                AnimatorCard c = Synergies.GetLastCardPlayed();
                if (c != null)
                {
                    strongSynergies = cardAffinities.GetSynergies(c.affinities);
                }
            }
        }

        for (EYBCombatInfo_AffinityRow t : rows)
        {
            t.Update(handAffinities, cardAffinities, strongSynergies, draggingCard);
        }

        drag_panel.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (AbstractDungeon.player == null || AbstractDungeon.isScreenUp || CardCrawlGame.isPopupOpen)
        {
            return;
        }

        drag_panel.Render(sb);

        for (EYBCombatInfo_AffinityRow t : rows)
        {
            t.Render(sb);
        }
    }
}
