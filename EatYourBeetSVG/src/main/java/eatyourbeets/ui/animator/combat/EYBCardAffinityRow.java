package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;
    public int BaseLevel;
    public int Level;

    protected static final EYBCardTooltip tooltip = new EYBCardTooltip("Affinities", "");
    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_synergy;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;
        final float offset_y = -0.5f -(index * 0.975f);

        Type = affinity;
        System = system;
        Power = system.GetPower(affinity);

        if (Power != null)
        {
            Power.hb = new RelativeHitbox(hb, 1, 1, 1.5f, offset_y);
        }

        image_background = new GUI_Image(GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(18f), offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void OnStartOfTurn()
    {
        RefreshLevels(true);

        if (Power != null)
        {
            Power.atStartOfTurn();
        }
    }

    public void Initialize()
    {
        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
        }
    }

    public void Seal(EYBCardAffinities cardAffinities)
    {
        final int level = Mathf.Clamp(cardAffinities.GetLevel(Type, false), 0, 2);
        final EYBCardAffinity a = System.PlayerAffinities.Get(Type, true);
        a.level += level;
        a.requirement += level;
        Power.IncreaseMinimumAmount(level);
    }

    public void Update(EYBCardAffinities affinities, EYBCard hoveredCard, boolean draggingCard)
    {
        RefreshLevels(false);

        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = 1f;

        final boolean hovering = !draggingCard && image_background.hb.hovered && !System.hb.IsDragging();
        if (Type == Affinity.Sealed)
        {
            if (hovering)
            {
                tooltip.description = GR.Animator.Strings.Affinities.SealedUses(Level);
                EYBCardTooltip.QueueTooltip(tooltip, image_background.hb, false);
            }
        }
        else
        {
            if (hoveredCard != null)
            {
                if (hoveredCard.affinities.GetLevel(Type, true) > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }
            }

            if (hovering)
            {
                if (BaseLevel > 0)
                {
                    tooltip.description = GR.Animator.Strings.Affinities.AffinityStatus(Level, BaseLevel, Type);
                    EYBCardTooltip.QueueTooltip(tooltip, image_background.hb, false);
                }

                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                    if (temp == null || temp.affinities.sealed || (temp.affinities.GetLevel(Type) == 0))
                    {
                        c.transparency = 0.35f;
                    }
                }
            }
        }

        if (Level == BaseLevel)
        {
            text_affinity.SetText(Level).SetColor(Colors.Cream(Level > 0 ? 1 : 0.6f));
        }
        else
        {
            text_affinity.SetText(Level).SetColor(Colors.LightGreen(1));
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
            Power.Render(sb);
        }
    }

    private void RefreshLevels(boolean startOfTurn)
    {
        final EYBCardAffinity a = System.PlayerAffinities.Get(Type, true);
        if (Type == Affinity.Sealed)
        {
            BaseLevel = Level = a.requirement = a.level;
        }
        else
        {
            if (startOfTurn)
            {
                a.requirement = a.level;
            }

            BaseLevel = a.level;
            Level = a.requirement;
        }
    }
}