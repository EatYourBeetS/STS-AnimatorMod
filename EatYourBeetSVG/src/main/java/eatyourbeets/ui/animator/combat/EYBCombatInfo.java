package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.*;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class EYBCombatInfo extends GUIElement
{
    protected final DraggableHitbox hb;
    protected final ArrayList<EYBCombatInfo_AffinityRow> rows = new ArrayList<>();

    public EYBCombatInfo()
    {
        hb = new DraggableHitbox(ScreenW(0.027f), ScreenH(0.65f), Scale(96f),  Scale(40f), true);

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

        if (player.hoveredCard != null && (player.isDraggingCard && player.isHoveringDropZone || player.inSingleTargetMode))
        {
            handAffinities = GameUtilities.GetTotalAffinity(player.hand.group, player.hoveredCard, 1);
            draggingCard = true;
        }
        else
        {
            handAffinities = GameUtilities.GetTotalAffinity(player.hand.group, null, 1);
        }

        if (player.hoveredCard instanceof EYBCard)
        {
            cardAffinities = ((EYBCard) player.hoveredCard).affinities;

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

        hb.update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (AbstractDungeon.player == null || AbstractDungeon.isScreenUp || CardCrawlGame.isPopupOpen)
        {
            return;
        }

        for (EYBCombatInfo_AffinityRow t : rows)
        {
            t.Render(sb);
        }

        hb.render(sb);
    }
}
