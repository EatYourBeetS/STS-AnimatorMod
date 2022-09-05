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
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.*;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_BUTTON = new Color(0.13f, 0.13f, 0.13f, 1f);
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;
    public int BaseLevel;
    public int Level;
    public int ThresholdCost;

    protected static final EYBCardTooltip tooltip = new EYBCardTooltip("Affinities", "");
    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_synergy;
    protected final GUI_Button threshold_button;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;
        final float offset_y = -0.5f -(index * 0.975f);

        Type = affinity;
        System = system;
        Power = system.GetPower(affinity);

        threshold_button = new GUI_Button(GR.Common.Images.Tag.Texture(),
                new RelativeHitbox(hb, 1f, 0.92f, 1.6f, offset_y))
                .SetColor(COLOR_BUTTON);

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

        if (Power != null)
        {
            // TODO Only set the hitbox when playing as Classic Animator
            //Power.hb = new RelativeHitbox(hb, 1, 1, 1.5f, offset_y);
            threshold_button.SetOnClick(this::SpendCost).SetFont(null, 0.5f);
        }
        else
        {
            image_synergy.SetActive(false);
            threshold_button.SetActive(false);
        }
    }

    public void OnStartOfTurn()
    {
        RefreshLevels();

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

    public void Seal(EYBCardAffinities cardAffinities, boolean manual)
    {
        final int level = Mathf.Clamp(cardAffinities.GetLevel(Type, true), 0, 2);
        if (level > 0)
        {
            System.BaseAffinities.Add(Type, level);
            System.CurrentAffinities.Add(Type, level);

            if (manual)
            {
                GameActions.Bottom.StackAffinityPower(Type, 1, level > 1);
            }
        }
    }

    public void Update(EYBCard hoveredCard, boolean draggingCard)
    {
        RefreshLevels();

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
                    tooltip.description = GR.Animator.Strings.Affinities.AffinityStatus(Level, BaseLevel);
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
            else if (threshold_button.hb.hovered && Power != null)
            {
                tooltip.description = GR.Animator.Strings.Affinities.Threshold(ThresholdCost, Type.GetTooltip(), Type.GetPowerTooltip(false));
                EYBCardTooltip.QueueTooltip(tooltip, false);
            }
        }

        if (Type == Affinity.Sealed || Level == BaseLevel)
        {
            text_affinity.SetText(Level).SetColor(Colors.Cream(Level > 0 ? 1 : 0.6f));
        }
        else
        {
            text_affinity.SetText(Level).SetColor(Colors.LightGreen(1));
        }

        if (Power != null)
        {
            threshold_button
                    .SetText("+1 " + Type.GetPowerTooltip(false) + " (" + ThresholdCost + ") ", true, true)
                    .SetTextColor(Level >= ThresholdCost ? Colors.LightGreen(1) : Colors.Cream(0.6f));
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
        threshold_button.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        text_affinity.TryRender(sb);
        image_synergy.TryRender(sb);
        threshold_button.TryRender(sb);

        // TODO Render power for Classic Animator
/*        if (Power != null)
        {
            Power.Render(sb);
        }*/
    }

    protected void SpendCost()
    {
        if (Power != null && System.TryUseAffinity(Type, ThresholdCost))
        {
            GameActions.Bottom.StackPower(TargetHelper.Player(), Power.GetThresholdBonusPower(), 1);
            System.BaseAffinities.SetRequirement(Type, ThresholdCost + 1);
            RefreshLevels();
        }
    }

    private void RefreshLevels()
    {
        BaseLevel = System.BaseAffinities.GetLevel(Type, true);
        Level = System.CurrentAffinities.GetLevel(Type, true);
        ThresholdCost = System.BaseAffinities.GetRequirement(Type);
    }
}