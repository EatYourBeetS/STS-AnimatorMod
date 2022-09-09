package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.affinity.AnimatorAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.*;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_BUTTON = new Color(0.62f, 0.58f, 0.36f, 1f);
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public AnimatorAffinityPower Power;
    public int BaseLevel;
    public int Level;
    public int UpgradeCost;

    protected static final EYBCardTooltip tooltip = new EYBCardTooltip("Affinities", "");
    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_upgrade;
    protected final GUI_Button button_upgrade;
    protected final GUI_Label text_upgrade;
    protected final float offset_y;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;

        Type = affinity;
        System = system;

        offset_y = -0.5f -(index * 0.975f);

        image_background = new GUI_Image(GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_upgrade = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(15f), offset_y * hb.height, false));

        button_upgrade = new GUI_Button(GR.Common.Images.Tag.Texture(),
        new RelativeHitbox(hb, Scale(35), Scale(30), hb.width * 1.95f, offset_y * hb.height, false))
        .SetFont(null, 0.75f)
        .SetText("+")
        .SetColor(COLOR_BUTTON);

        text_upgrade = new GUI_Label(EYBFontHelper.CardIconFont_Small,
        new RelativeHitbox(hb, Scale(20), Scale(20), (hb.width * 1.95f) + Scale(10), (offset_y * hb.height) - Scale(5), false))
        .SetAlignment(0.5f, 0.5f);

        if (Type.ID >= 0)
        {
            button_upgrade.SetOnClick(this::UpgradeAffinity);
        }
        else
        {
            image_upgrade.SetActive(false);
            text_upgrade.SetActive(false);
            button_upgrade.SetActive(false);
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
        Power = JUtils.SafeCast(System.GetPower(Type), AnimatorAffinityPower.class);

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
            Power.hb = new RelativeHitbox(System.hb, 1f, 1, 1.5f, offset_y);
        }
    }

    public void Seal(EYBCardAffinities cardAffinities, boolean manual)
    {
        final int level = Mathf.Clamp(cardAffinities.GetLevel(Type, true), 0, 2);
        if (level > 0)
        {
            GameActions.Bottom.StackAffinityPower(Type, level, false);
        }
    }

    public void Update(EYBCard hoveredCard, boolean draggingCard)
    {
        RefreshLevels();

        image_background.SetColor(COLOR_DEFAULT);
        //image_synergy.color.a = 1f;

        if (Type.ID >= 0)
        {
            button_upgrade.SetActive(Level >= UpgradeCost && Power.CanUpgrade());
            if (text_upgrade.SetActive(button_upgrade.isActive).isActive)
            {
                text_upgrade.SetText(UpgradeCost).SetColor(Power.retainedTurns > 0 ? Colors.Green(1) : Colors.Cream(1));
            }
        }

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

            if (button_upgrade.isActive && button_upgrade.hb.hovered && Power != null)
            {
                tooltip.description = GR.Animator.Strings.Affinities.UpgradeAffinityPower(UpgradeCost,
                        Type.GetTooltip().GetTitleOrIcon(),
                        Type.GetPowerTooltip(false).GetTitleOrIcon());
                EYBCardTooltip.QueueTooltip(tooltip, false);
            }
            else if (hovering)
            {
                if (Power != null)
                {
                    tooltip.description = GR.Animator.Strings.Affinities.AffinityStatus(UpgradeCost, "[" + Power.symbol + "]");
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

        if (Type == Affinity.Sealed || Level == BaseLevel)
        {
            text_affinity.SetText(Level).SetColor(Colors.Cream(Level > 0 ? 1 : 0.6f));
        }
        else
        {
            text_affinity.SetText(Level).SetColor(Level < BaseLevel ? Colors.LightOrange(1) : Colors.LightGreen(1));
        }

        Update();
    }

    @Override
    public void Update()
    {
        image_background.TryUpdate();
        image_affinity.TryUpdate();
        text_affinity.TryUpdate();
        image_upgrade.TryUpdate();
        button_upgrade.TryUpdate();
        text_upgrade.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        text_affinity.TryRender(sb);
        image_upgrade.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }

        button_upgrade.TryRender(sb);
        text_upgrade.TryRender(sb);
    }

    protected void UpgradeAffinity()
    {
        RefreshLevels();

        if (UpgradeCost >= 0 && System.TryUseAffinity(Type, UpgradeCost))
        {
            Power.Upgrade(1);
        }
    }

    protected void RefreshLevels()
    {
        BaseLevel = System.BaseAffinities.GetLevel(Type, true);
        Level = System.CurrentAffinities.GetLevel(Type, true);
        UpgradeCost = Power == null ? -1 : Power.GetUpgradeCost();
    }
}