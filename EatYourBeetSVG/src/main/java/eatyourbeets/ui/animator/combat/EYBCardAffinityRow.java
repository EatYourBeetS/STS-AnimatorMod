package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;

    public int ActivationPowerAmount;
    public int Level;

    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
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
        new RelativeHitbox(hb, 1, 1, 0, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(18f), offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void ActivateAffinityBonus(AbstractCard card, int amount)
    {
        GameActions.Bottom.StackAffinityPower(Type, amount, false);
        CombatStats.OnAffinityBonus(card, Type);
    }

    public void OnStartOfTurn()
    {
        if (Power != null)
        {
            Power.atStartOfTurn();
        }
    }

    public void Initialize()
    {
        ActivationPowerAmount = 1;

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
        }
    }

    public void Update(EYBCardAffinities handAffinities, EYBCard hoveredCard, EYBCardAffinities synergies, boolean draggingCard)
    {
        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = 0.25f;

        if (Type == Affinity.General)
        {
            Level = handAffinities.GetLevel(Type, false) + handAffinities.GetDirectLevel(Affinity.General);

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                final EYBCardAffinity best = handAffinities.Get(Affinity.General);
                final Affinity affinity = best == null ? null : best.type;
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                    if (temp == null || (temp.affinities.GetLevel(affinity) == 0 && temp.affinities.GetDirectLevel(Affinity.General) == 0))
                    {
                        c.transparency = 0.35f;
                    }
                }
            }
        }
        else
        {
            Level = handAffinities.GetLevel(Type, false);

            if (hoveredCard != null)
            {
                final EYBCardAffinity a = (synergies != null && synergies.GetLevel(Affinity.Star) == 0) ? synergies.Get(Type) : null;
                if (System.CanActivateAffinityBonus(a))
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
                }
                else if (hoveredCard.affinities.GetLevel(Type, true) > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }
            }

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                    if (temp == null || (temp.affinities.GetLevel(Type) == 0))
                    {
                        c.transparency = 0.35f;
                    }
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
        image_synergy.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        image_synergy.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }
    }
}