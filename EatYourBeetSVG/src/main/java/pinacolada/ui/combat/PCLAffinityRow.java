package pinacolada.ui.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.effects.affinity.ChangeAffinityCountEffect;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class PCLAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);
    public static final int SYNERGY_MULTIPLIER = 1;

    public final PCLAffinity Type;
    public final PCLAffinitySystem System;
    public final AbstractPCLAffinityPower Power;

    public int ActivationPowerAmount;
    public int Level;
    public int Total;
    protected float fontScale = 1.0F;

    public final GUI_Image image_background;
    public final GUI_Image image_affinity;
    public final GUI_Label current_affinity;
    public final GUI_Image image_synergy;

    public PCLAffinityRow(PCLAffinitySystem system, PCLAffinity affinity, int index)
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

        image_background = new GUI_Image(GR.PCL.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        current_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_synergy = new GUI_Image(GR.PCL.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(18f), offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void ActivateSynergyBonus(AbstractCard card)
    {
        final PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c != null && c.cardData != null && !c.cardData.CanGrantAffinity) {
            return;
        }
        int addLevel = PCLGameUtilities.GetPCLAffinityLevel(card, Type, true);
        PCLActions.Bottom.StackAffinityPower(Type, ActivationPowerAmount * addLevel, false);
        PCLCombatStats.OnMatchBonus(card, Type);
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
        ActivationPowerAmount = SYNERGY_MULTIPLIER;

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
        }
    }

    public void Flash() {
        this.fontScale = 8.0F;
        PCLGameEffects.List.Add(new ChangeAffinityCountEffect(this, true));
    }

    public void Update(PCLAffinitySystem.AffinityCounts previewAffinities, PCLCard hoveredCard, PCLCardAffinities synergies, boolean draggingCard)
    {
        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = Power.enabled ? 1f : 0.25f;
        boolean isTriggering = false;

        if (Type == PCLAffinity.General)
        {
            int am = previewAffinities.GetAmount(PCLAffinity.General);
            Level = previewAffinities.GetAmount(Type) + am;

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                final int best = previewAffinities.GetAmount(PCLAffinity.General);
                for (PCLAffinity affinity : PCLAffinity.Extended()) {
                    if (previewAffinities.GetAmount(affinity) == best) {
                        PCLGameUtilities.HighlightMatchingCards(affinity);
                    }
                }
            }
        }
        else
        {
            Level = previewAffinities.GetAmount(Type);

            if (hoveredCard != null)
            {
                int addLevel = hoveredCard.affinities.GetLevel(Type,true);
                if (addLevel > 0 && hoveredCard.cardData.CanGrantAffinity) {
                    Level += addLevel;
                    Total += addLevel;
                }
                final PCLCardAffinity a = (synergies != null && synergies.GetLevel(PCLAffinity.Star) == 0) ? synergies.Get(Type) : null;
                if (System.CanActivateSynergyBonus(a))
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
                }
                else if (addLevel > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }


                if (hoveredCard.affinities.GetRequirement(Type) > 0 && hoveredCard.CheckAffinity(Type)) {
                    Level -= hoveredCard.affinities.GetRequirement(Type);
                    isTriggering = true;
                }
            }

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                PCLGameUtilities.HighlightMatchingCards(Type);
            }
        }

        if (this.fontScale != 1.0F) {
            this.fontScale = MathUtils.lerp(this.fontScale, 1.0F, Gdx.graphics.getDeltaTime() * 10.0F);
            if (this.fontScale - 1.0F < 0.05F) {
                this.fontScale = 1.0F;
            }
        }

        current_affinity
                .SetText(Level)
                .SetColor(isTriggering ? Colors.Green(1) : Colors.Cream(Level > 0 ? 1 : 0.6f))
                .SetFontScale(this.fontScale);

        Update();
    }

    @Override
    public void Update()
    {
        image_background.TryUpdate();
        image_affinity.TryUpdate();
        current_affinity.TryUpdate();
        image_synergy.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        current_affinity.TryRender(sb);
        image_synergy.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }
    }
}