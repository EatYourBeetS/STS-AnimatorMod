package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final AffinityType Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;
    public int Level;

    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_synergy;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, AffinityType type, int index, int max)
    {
        Type = type;
        System = system;
        Power = system.GetPower(type);

        final Hitbox hb = system.hb;
        final float offset_y = -0.5f -(index * 0.975f);

        image_background = new GUI_Image(GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(type.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(), //type.GetSynergyEffectIcon(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(18f), offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void Update(EYBCardAffinities handAffinities, EYBCard hoveredCard, EYBCardAffinities synergies, boolean draggingCard)
    {
        final boolean synergyEffectAvailable = System.CanActivateSynergyBonus(Type);

        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = synergyEffectAvailable ? 1f : 0.25f;
        text_affinity.SetText(Level = handAffinities.GetLevel(Type, false));

        if (Type != AffinityType.General)
        {
            text_affinity.SetColor(System.BonusAffinities.GetLevel(Type) > 0 ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR);

            if (hoveredCard != null)
            {
                EYBCardAffinity a = synergies != null ? synergies.Get(Type) : null;
                if (a != null && System.CanActivateSynergyBonus(a))
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
                }
                else if (hoveredCard.affinities.GetLevel(Type, true) > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }
            }
        }

        if (!draggingCard && image_background.hb.hovered)
        {
            AffinityType type = Type;
            if (type == AffinityType.General)
            {
                EYBCardAffinity best = handAffinities.Get(AffinityType.General);
                type = best == null ? null : best.Type;
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

        if (Power != null)
        {
            Power.Render(sb, image_background.hb);
        }
    }
}