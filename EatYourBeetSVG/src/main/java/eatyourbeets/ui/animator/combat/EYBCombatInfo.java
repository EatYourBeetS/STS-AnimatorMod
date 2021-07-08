package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_ImageRegion;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class EYBCombatInfo extends GUIElement
{
    protected final DraggableHitbox hb;
    protected final ArrayList<AffinityRow> rows = new ArrayList<>();

    public EYBCombatInfo()
    {
        hb = new DraggableHitbox(Scale(0.025f), ScreenH(0.65f), Scale(96f),  Scale(40f), true);

        AffinityType[] values = AffinityType.values();
        for (int i = 0; i < values.length; i++)
        {
            rows.add(new AffinityRow(values[i], hb, i, values.length));
        }
    }

    @Override
    public void Update()
    {
        EYBCardAffinities affinities;
        AbstractPlayer player = AbstractDungeon.player;
        AbstractCard card = null;
        if (player.hoveredCard != null && (player.isDraggingCard && player.isHoveringDropZone || player.inSingleTargetMode))
        {
            card = player.hoveredCard;
        }
        affinities = GameUtilities.GetTotalAffinity(AbstractDungeon.player.hand.group, card, 1);
        for (AffinityRow t : rows)
        {
            t.Update(affinities, card != null);
        }

        hb.update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        for (AffinityRow t : rows)
        {
            t.Render(sb);
        }

        hb.render(sb);
    }

    public class AffinityRow extends GUIElement
    {
        public final AffinityType Type;

        protected final GUI_Image background_image;
        protected final GUI_Image affinity_image;
        protected final GUI_Label affinity_text;
        protected final GUI_ImageRegion synergy_icon;

        public AffinityRow(AffinityType type, Hitbox hb, int index, int max)
        {
            Type = type;

            float offset_y = -0.5f -(index * 0.95f);

            background_image = new GUI_Image(GR.Common.Images.Panel_Rounded_Half_H.Texture(),
                    new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
            .SetColor(0.05f, 0.05f, 0.05f, 1f);

            affinity_image = new GUI_Image(type.GetIcon(),
                    new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

            affinity_text = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                    new RelativeHitbox(hb, 0.5f, 1f, 0.5f, offset_y))
            .SetAlignment(0.5f, 0.5f)
            .SetText("-");

            synergy_icon = new GUI_ImageRegion(type.GetSynergyEffectIcon(),
                    new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(10f), offset_y * hb.height, false));
        }

        public void Update(EYBCardAffinities affinities, boolean draggingCard)
        {
            int total = affinities.GetLevel(Type);
            affinity_text.SetText(total > 0 ? total : "-");

            if (CombatStats.HasActivatedSemiLimited(Type.name()))
            {
                synergy_icon.color.a = 0.6f;
            }
            else
            {
                synergy_icon.color.a = 1f;
            }

            if (!draggingCard && background_image.hb.hovered)
            {
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    EYBCard card = JUtils.SafeCast(c, EYBCard.class);
                    if (card == null || card.affinities.GetLevel(Type) == 0)
                    {
                        c.transparency = 0.35f;
                    }
                }
            }

            Update();
        }

        @Override
        public void Update()
        {
            background_image.Update();
            affinity_image.Update();
            affinity_text.Update();
            synergy_icon.Update();
        }

        @Override
        public void Render(SpriteBatch sb)
        {
            background_image.Render(sb);
            affinity_image.Render(sb);
            affinity_text.Render(sb);
            synergy_icon.Render(sb);
        }
    }
}
