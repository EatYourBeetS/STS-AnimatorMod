package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class EYBCombatInfo extends GUIElement
{
    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final ArrayList<EYBCombatInfo_AffinityRow> rows = new ArrayList<>();

    public EYBCombatInfo()
    {
        hb = new DraggableHitbox(ScreenW(0.024f), ScreenH(0.65f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        AffinityType[] values = AffinityType.BasicTypes();
        for (int i = 0; i < values.length; i++)
        {
            rows.add(new EYBCombatInfo_AffinityRow(values[i], hb, i, values.length + 1));
        }
        rows.add(new EYBCombatInfo_AffinityRow(AffinityType.General, hb, values.length, values.length + 1));
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
                AnimatorCard c = CombatStats.Affinities.GetLastCardPlayed();
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

        dragPanel_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (AbstractDungeon.player == null || AbstractDungeon.isScreenUp || CardCrawlGame.isPopupOpen)
        {
            return;
        }

        dragPanel_image.Render(sb);

        for (EYBCombatInfo_AffinityRow t : rows)
        {
            t.Render(sb);
        }
    }
}
