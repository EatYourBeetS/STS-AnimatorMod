package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.ArrayUtils;

public class EYBCombatInfo_AffinityRow extends GUIElement
{
    public final AffinityType Type;

    protected static final Color COLOR_LV2_SYNERGY = new Color(0.75f, 0.75f, 0.35f, 0.75f);
    protected static final Color COLOR_CARD_AFFINITY = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    protected static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);

    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_synergy;

    public EYBCombatInfo_AffinityRow(AffinityType type, Hitbox hb, int index, int max)
    {
        Type = type;

        float offset_y = -0.5f -(index * 0.975f);

        image_background = new GUI_Image(GR.Common.Images.Panel_Rounded_Half_H.Texture(),
                new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
                .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(type.GetIcon(),
                new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
                .SetAlignment(0.5f, 0.5f)
                .SetText("-");

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(), //type.GetSynergyEffectIcon(),
                new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(10f), offset_y * hb.height, false));

        image_synergy.SetActive(ArrayUtils.contains(AffinityType.BasicTypes(), type));
    }

    public void Update(EYBCardAffinities handAffinities, EYBCardAffinities cardAffinities, EYBCardAffinities strongSynergies, boolean draggingCard)
    {
        boolean synergyEffectAvailable = !CombatStats.HasActivatedSemiLimited(Type.name());
        int total = handAffinities.GetLevel(Type, false);
        text_affinity.SetText(total);// > 0 ? total : "-");
        image_synergy.color.a = synergyEffectAvailable ? 1f : 0.35f;

        int level = strongSynergies == null ? 0 : strongSynergies.GetLevel(Type, false);
        if (level == 2 && synergyEffectAvailable && CombatStats.Affinities.GetLastAffinityLevel(Type) > 0)
        {
            image_background.SetColor(COLOR_LV2_SYNERGY);
        }
        else
        {
            level = cardAffinities == null ? 0 : cardAffinities.GetLevel(Type, false);
            if (level == 0)
            {
                image_background.SetColor(COLOR_DEFAULT);
            }
            else
            {
                image_background.SetColor(COLOR_CARD_AFFINITY);
            }
        }

        if (!draggingCard && image_background.hb.hovered)
        {
            AffinityType type = Type;
            if (type == AffinityType.General)
            {
                EYBCardAffinity a = JUtils.FindMax(handAffinities.List, t -> t.level);
                if (a != null)
                {
                    type = a.Type;
                }
            }

            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                EYBCard t = JUtils.SafeCast(c, EYBCard.class);
                if (t == null || t.affinities.GetLevel(type) == 0)
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
        image_background.TryUpdate();
        image_affinity.TryUpdate();
        text_affinity.TryUpdate();
        image_synergy.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        text_affinity.TryRender(sb);
        image_synergy.TryRender(sb);

        if (image_synergy.isActive)
        {
            CombatStats.Affinities.GetPower(Type).Render(sb, image_background.hb);
        }
    }
}