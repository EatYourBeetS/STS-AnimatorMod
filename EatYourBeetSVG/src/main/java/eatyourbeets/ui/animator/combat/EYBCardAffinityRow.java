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
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
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
    public static final Color COLOR_BUTTON = new Color(0.8f, 0.8f, 0.6f, 1f);
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
    protected static boolean hasUpgradableAffinities = false;
    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_arrow;
    protected final GUI_Button button_upgrade;
    //protected final GUI_Label text_upgrade;
    protected final float offset_y;
    protected float vfxTimer;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;

        Type = affinity;
        System = system;

        offset_y = -0.5f -(index * 0.975f);

        image_background = new GUI_Image(GR.Common.Images.Panel_Rounded_Half_H.Texture(),
        new RelativeHitbox(hb, 1f, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_arrow = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(12f), (offset_y * hb.height), false));

//        text_upgrade = new GUI_Label(EYBFontHelper.CardIconFont_Small,
//        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(12f), (offset_y * hb.height) + Scale(4), false))
//        .SetAlignment(0.5f, 0.5f)
//        .SetFont(null, 0.8f);

        button_upgrade = new GUI_Button(GR.Common.Images.Tag.Texture(),
        new RelativeHitbox(hb, Scale(35), Scale(30), (hb.width * 2f) + Scale(26), offset_y * hb.height, false))
        .SetFont(null, 0.75f)
        .SetColor(COLOR_BUTTON, 0.6f)
        .UseNotInteractableColor(true);

        if (Type.ID >= 0)
        {
            button_upgrade.SetOnClick(this::UpgradeAffinity).SetText("+").SetActive(true);
            image_arrow.SetActive(true);
        }
        else
        {
            button_upgrade.SetOnClick(this::UpgradeAllAffinities).SetText("ALL").SetFont(null, 0.7f).SetActive(false);
            image_arrow.SetActive(false);
            //text_upgrade.SetActive(false);
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
        vfxTimer = 0;
        Power = JUtils.SafeCast(System.GetPower(Type), AnimatorAffinityPower.class);

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
            Power.hb = new RelativeHitbox(System.hb, 1f, 1, 1.5f, offset_y);
        }
    }

    public void GainAffinity(int amount, boolean temp)
    {
        if (Type == Affinity.Sealed)
        {
            vfxTimer = 0.75f;

            final int remainingUses = System.GetRemainingSealUses();
            if (remainingUses < 3)
            {
                amount = (Mathf.Clamp(remainingUses + amount, 0, 3));
                System.CurrentAffinities.Set(Type, amount);
                System.BaseAffinities.Set(Type, amount);
            }

            return;
        }

        if (vfxTimer <= 0f)
        {
            float volume = 0.8f;
            for (EYBCardAffinityRow row : System.Rows)
            {
                if (row.Type != Affinity.Sealed)
                {
                    if (row.vfxTimer == 0.5f)
                    {
                        volume = 0;
                        break;
                    }
                    else if (row.vfxTimer > 0)
                    {
                        volume *= 0.8f;
                    }
                }
            }
            SFX.Play(SFX.UNLOCK_PING, 2.35f, 2.55f, volume);
        }

        vfxTimer = 0.5f;

        if (temp)
        {
            System.CurrentAffinities.Add(Type, amount);
        }
        else
        {
            amount = CombatStats.OnAffinityGained(Type, amount);
            System.CurrentAffinities.Add(Type, amount);
            System.BaseAffinities.Add(Type, amount);
        }
    }

    public void Seal(EYBCardAffinities cardAffinities, boolean manual)
    {
        final int level = Mathf.Clamp(cardAffinities.GetLevel(Type, true), 0, 2);
        if (level > 0)
        {
            GameActions.Top.GainAffinity(Type, level, false);
        }
    }

    public void Update(EYBCard hoveredCard, boolean draggingCard)
    {
        RefreshLevels();

        if (vfxTimer > 0)
        {
            vfxTimer -= GR.UI.Delta();
            image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
        }
        else
        {
            image_background.SetColor(COLOR_DEFAULT);
        }
        //image_synergy.color.a = 1f;

        if (Type.ID >= 0)
        {
            //button_upgrade.SetColor(Testing.TryGetColor(button_upgrade.buttonColor), 0.6f);

            if (button_upgrade.SetInteractable(Level >= UpgradeCost && Power.CanUpgrade()).interactable)
            {
                hasUpgradableAffinities = true;
            }

            if (button_upgrade.isActive)
            {
                //text_upgrade.SetText(UpgradeCost).SetColor(Power.boost > 0 ? Colors.Green(1) : Colors.Cream(1));
                button_upgrade.SetText(UpgradeCost, false).SetTextColor(Power.boost > 0 ? Colors.Green(1) : Colors.Cream(1), 0.4f);
            }
        }
        else
        {
            button_upgrade.SetActive(hasUpgradableAffinities);
            hasUpgradableAffinities = false;
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
//            if (hoveredCard != null && vfxTimer <= 0)
//            {
//                if (hoveredCard.affinities.GetLevel(Type, true) > 0)
//                {
//                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
//                }
//            }

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
                    final String icon;
                    switch (Power.symbol)
                    {
                        case "F": icon = "[Force]"; break;
                        case "A": icon = "[Agility]"; break;
                        case "I": icon = "[Intellect]"; break;
                        case "B": icon = "[Blessing]"; break;
                        case "C": icon = "[Corruption]"; break;
                        default: throw new RuntimeException("Unsupported Affinity Power: " + Power.ID);
                    }

                    tooltip.description = GR.Animator.Strings.Affinities.AffinityStatus(UpgradeCost, icon);
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
        image_arrow.TryUpdate();
        button_upgrade.TryUpdate();
        //text_upgrade.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        text_affinity.TryRender(sb);
        image_arrow.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }

        button_upgrade.TryRender(sb);
        //text_upgrade.TryRender(sb);
    }

    protected void UpgradeAffinity()
    {
        RefreshLevels();

        if (UpgradeCost >= 0 && System.TryUseAffinity(Type, UpgradeCost))
        {
            Power.Upgrade(1);
        }
    }

    protected void UpgradeAllAffinities()
    {
        for (EYBCardAffinityRow row : System.Rows)
        {
            if (row.button_upgrade.interactable)
            {
                row.UpgradeAffinity();
            }
        }
    }

    protected void RefreshLevels()
    {
        BaseLevel = System.BaseAffinities.GetLevel(Type, true);
        Level = System.CurrentAffinities.GetLevel(Type, true);
        UpgradeCost = Power == null ? -1 : Power.GetUpgradeCost();
    }
}