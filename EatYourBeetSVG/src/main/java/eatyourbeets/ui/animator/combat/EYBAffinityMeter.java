package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.common.RerollAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.common.AffinityKeywordButton;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.*;

import static eatyourbeets.resources.GR.Enums.CardTags.HARMONIC;

public class EYBAffinityMeter extends GUIElement
{
    public enum Target {
        CurrentAffinity,
        NextAffinity
    }

    public static final int DEFAULT_REROLLS = 1;
    public static final float ICON_SIZE = Scale(96);
    public static final float LABEL_OFFSET = ICON_SIZE + Scale(20);
    public static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    public final DraggableHitbox hb;
    public final EYBCardAffinitySystem System;
    public final AffinityKeywordButton CurrentAffinity;
    public final AffinityKeywordButton NextAffinity;
    public RerollAffinityPower Reroll;
    protected final GUI_Image draggable_panel;
    protected final GUI_Image draggable_icon;
    protected Vector2 meterSavedPosition;

    public EYBAffinityMeter(EYBCardAffinitySystem system) {
        System = system;

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), ICON_SIZE / 2,  ICON_SIZE / 2, true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));
        draggable_panel = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
                .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), ICON_SIZE / 4, ICON_SIZE / 4, false))
                .SetColor(Colors.White(0.25f));

        CurrentAffinity = new AffinityKeywordButton(hb, Affinity.General, ICON_SIZE)
                .SetLevel(3)
                .SetOffsets(2.3f, 0.5f)
                .SetOnClick(__ -> {
                    if (Reroll != null) {
                        Reroll.OnClick(Target.CurrentAffinity);
                    }
                });
        NextAffinity = new AffinityKeywordButton(hb, Affinity.General, ICON_SIZE)
                .SetLevel(2)
                .SetOffsets(4.6f, 0.5f)
                .SetOnClick(__ -> {
                    if (Reroll != null) {
                        Reroll.OnClick(Target.NextAffinity);
                    }
                });
    }

    public void Initialize() {
        //TODO add subscribers
        RandomizeCurrentAffinity();
        RandomizeNextAffinity();
        Reroll = new RerollAffinityPower(DEFAULT_REROLLS);

        if (meterSavedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) draggable_panel.hb;
            meterSavedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            meterSavedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (meterSavedPosition.dst2(GR.Animator.Config.AffinityMeterPosition.Get()) > Mathf.Epsilon)
            {
                JUtils.LogInfo(this, "Saved affinity meter position.");
                GR.Animator.Config.AffinityMeterPosition.Set(meterSavedPosition.cpy(), true);
            }
        }
    }

    public boolean HasMatch(AbstractCard card) {
        return HasMatch(card, true);
    }

    public boolean HasMatch(AbstractCard card, boolean useHarmonic) {
        EYBCard eCard = JUtils.SafeCast(card, EYBCard.class);
        if (eCard != null) {
            if (useHarmonic && eCard.hasTag(HARMONIC)) {
                return true;
            }
            if (CurrentAffinity.Type.equals(Affinity.Star)) {
                return (eCard.affinities.GetLevel(Affinity.General, true) > 0);
            }
            return (eCard.affinities.GetLevel(CurrentAffinity.Type, true) > 0);
        }
        return false;
    }

    public Affinity OnMatch(AbstractCard card) {
        //TODO add subscribers
        CurrentAffinity.Flash();
        SetCurrentAffinity(NextAffinity.Type);
        return SetNextAffinity(card);
    }

    public void OnStartOfTurn()
    {
        if (Reroll != null) {
            Reroll.atStartOfTurn();
        }
        //TODO add subscribers
    }

    public Affinity Randomize(Target target) {
        return target == Target.CurrentAffinity ? RandomizeCurrentAffinity() : RandomizeNextAffinity();
    }

    public Affinity RandomizeCurrentAffinity() {
        return SetCurrentAffinity(GameUtilities.GetRandomElement(Affinity.Basic()));
    }

    public Affinity RandomizeNextAffinity() {
        return SetNextAffinity(GameUtilities.GetRandomElement(Affinity.Basic()));
    }

    public Affinity GetCurrentAffinity() {
        return CurrentAffinity.Type;
    }

    public Affinity GetNextAffinity() {
        return NextAffinity.Type;
    }

    public Affinity Set(Affinity affinity, Target target) {
        return target == Target.CurrentAffinity ? SetCurrentAffinity(affinity) : SetNextAffinity(affinity);
    }

    public Affinity SetCurrentAffinity(Affinity affinity) {
        CurrentAffinity.SetAffinity(affinity);
        return affinity;
    }

    public Affinity SetNextAffinity(Affinity affinity) {
        NextAffinity.SetAffinity(affinity);
        return affinity;
    }

    public Affinity SetNextAffinity(AbstractCard card) {
        EYBCard eCard = JUtils.SafeCast(card, EYBCard.class);
        if (eCard != null) {
            if (eCard.affinities.HasStar()) {
                return SetNextAffinity(Affinity.Star);
            }
            Affinity affinity = JUtils.Random(eCard.affinities.GetAffinities());
            if (affinity != null) {
                return SetNextAffinity(affinity);
            }
        }
        return RandomizeNextAffinity();
    }

    public void Flash(Target target) {
        if (target == Target.CurrentAffinity) {
            CurrentAffinity.Flash();
        }
        else {
            NextAffinity.Flash();
        }
    }

    @Override
    public void Update() {
        if (meterSavedPosition == null)
        {
            meterSavedPosition = GR.Animator.Config.AffinityMeterPosition.Get(new Vector2(0.40f, 0.7f)).cpy();
            hb.SetPosition(ScreenW(meterSavedPosition.x), ScreenH(meterSavedPosition.y));
        }
        hb.update();

        draggable_panel.TryUpdate();
        draggable_icon.TryUpdate();

        boolean isHovered = draggable_panel.hb.hovered || CurrentAffinity.background_button.hb.hovered || NextAffinity.background_button.hb.hovered;
        draggable_panel.SetColor(0.05f, 0.05f, 0.05f, isHovered ? 0.5f : 0.05f);
        draggable_icon.SetColor(Colors.White(isHovered ? 0.75f : 0.1f));

        CurrentAffinity.TryUpdate();
        NextAffinity.TryUpdate();

        if (Reroll != null)
        {
            Reroll.updateDescription();
            if (CurrentAffinity.background_button.hb.hovered || NextAffinity.background_button.hb.hovered) {
                EYBCardTooltip.QueueTooltip(Reroll.tooltip);
            }
        }

        if (CurrentAffinity.background_button.hb.hovered) {
            GameUtilities.HighlightMatchingCards(CurrentAffinity.Type == Affinity.Star ? Affinity.General : CurrentAffinity.Type);
        }

        if (NextAffinity.background_button.hb.hovered) {
            GameUtilities.HighlightMatchingCards(NextAffinity.Type == Affinity.Star ? Affinity.General : NextAffinity.Type);
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        draggable_panel.Render(sb);
        draggable_icon.Render(sb);

        CurrentAffinity.TryRender(sb);
        NextAffinity.TryRender(sb);
        hb.render(sb);

        FontHelper.renderFontCentered(sb, EYBFontHelper.CardTitleFont_Small,
                GR.Animator.Strings.Combat.Current, CurrentAffinity.background_button.hb.cX, CurrentAffinity.background_button.hb.y + LABEL_OFFSET, Colors.Cream(1f), 1f);
        FontHelper.renderFontCentered(sb, EYBFontHelper.CardTitleFont_Small,
                GR.Animator.Strings.Combat.Next, NextAffinity.background_button.hb.cX, NextAffinity.background_button.hb.y + LABEL_OFFSET, Colors.Cream(1f), 1f);

        if (Reroll != null) {
            final BitmapFont rerollFont = EYBFontHelper.CardTitleFont_Small;
            rerollFont.getData().setScale(0.8f);
            RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
                    (CurrentAffinity.background_button.hb.cX + NextAffinity.background_button.hb.cX) / 2, CurrentAffinity.background_button.hb.y - LABEL_OFFSET / 2.5f,
                    CurrentAffinity.background_button.hb.width * 1.35f, CurrentAffinity.background_button.hb.height * 0.7f, 1, 0);
            FontHelper.renderFontLeftTopAligned(sb, rerollFont,
                    GR.Animator.Strings.Combat.Rerolls + ": " + Reroll.triggerCondition.uses + "/" + Reroll.triggerCondition.baseUses,
                    CurrentAffinity.background_button.hb.cX, CurrentAffinity.background_button.hb.y - LABEL_OFFSET / 3f,
                    Reroll.triggerCondition.uses > 0 ? Settings.BLUE_TEXT_COLOR : Settings.RED_TEXT_COLOR);
            RenderHelpers.ResetFont(rerollFont);
        }
    }
}