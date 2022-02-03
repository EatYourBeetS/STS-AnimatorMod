package pinacolada.ui.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.Mathf;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.affinity.AffinityGlowEffect;
import pinacolada.powers.special.RerollAffinityPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.ui.common.AffinityKeywordButton;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.resources.GR.Enums.CardTags.HARMONIC;

public class PCLAffinityMeter extends GUIElement
{
    public enum Target {
        CurrentAffinity,
        NextAffinity
    }

    public static final int DEFAULT_REROLLS = 1;
    public static final float ICON_SIZE = Scale(96);
    public static final float LABEL_OFFSET = ICON_SIZE + Scale(30);
    public static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    public final DraggableHitbox hb;
    public final PCLAffinitySystem System;
    public RerollAffinityPower Reroll;
    protected final AffinityKeywordButton CurrentAffinity;
    protected final AffinityKeywordButton NextAffinity;
    protected int matchesThisCombat;
    protected int currentMatchCombo;
    protected int longestMatchCombo;
    protected final GUI_Image draggable_panel;
    protected final GUI_Image draggable_icon;
    protected float fontScale = 1.0F;
    protected boolean isGlowing;
    protected Texture glowImg;
    protected Vector2 meterSavedPosition;

    public PCLAffinityMeter(PCLAffinitySystem system) {
        System = system;

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), ICON_SIZE / 2,  ICON_SIZE / 2, true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));
        draggable_panel = new GUI_Image(GR.PCL.Images.Panel_Rounded.Texture(), hb)
                .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(GR.PCL.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), ICON_SIZE / 4, ICON_SIZE / 4, false))
                .SetColor(Colors.White(0.25f));

        CurrentAffinity = new AffinityKeywordButton(hb, PCLAffinity.General, ICON_SIZE)
                .SetLevel(3)
                .SetOffsets(2.3f, 0.5f)
                .SetOnClick(__ -> {
                    if (Reroll != null) {
                        Reroll.OnClick();
                    }
                });
        NextAffinity = new AffinityKeywordButton(hb, PCLAffinity.General, ICON_SIZE)
                .SetLevel(2)
                .SetOffsets(4.6f, 0.5f)
                .SetOnClick(__ -> {
                    if (Reroll != null) {
                        Reroll.OnClick();
                    }
                });
        glowImg = GR.PCL.Images.Affinities.Border_Silhouette.Texture();
    }

    public void Initialize() {
        //TODO add subscribers
        RandomizeCurrentAffinity();
        RandomizeNextAffinity();
        Reroll = new RerollAffinityPower(DEFAULT_REROLLS);
        matchesThisCombat = 0;
        currentMatchCombo = 0;
        longestMatchCombo = 0;

        if (meterSavedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) draggable_panel.hb;
            meterSavedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            meterSavedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (meterSavedPosition.dst2(GR.PCL.Config.AffinityMeterPosition.Get()) > Mathf.Epsilon)
            {
                PCLJUtils.LogInfo(this, "Saved affinity meter position.");
                GR.PCL.Config.AffinityMeterPosition.Set(meterSavedPosition.cpy(), true);
            }
        }
    }

    public boolean HasMatch(AbstractCard card) {
        return HasMatch(card, true);
    }

    public boolean HasMatch(AbstractCard card, boolean useHarmonic) {
        PCLCard eCard = PCLJUtils.SafeCast(card, PCLCard.class);
        if (eCard != null) {
            if (useHarmonic && eCard.hasTag(HARMONIC)) {
                return true;
            }
            if (CurrentAffinity.Type.equals(PCLAffinity.Star)) {
                return (eCard.affinities.GetLevel(PCLAffinity.General, true) > 0);
            }
            return (eCard.affinities.GetLevel(CurrentAffinity.Type, true) > 0);
        }
        return false;
    }

    public PCLAffinity OnMatch(AbstractCard card) {
        //TODO add subscribers

        matchesThisCombat += 1;
        currentMatchCombo += 1;
        longestMatchCombo = Math.max(longestMatchCombo, currentMatchCombo);
        this.fontScale = 4.0F;

        CurrentAffinity.Flash();
        if (card.hasTag(HARMONIC) && PCLGameUtilities.HasMulticolorAffinity(card)) {
            SetCurrentAffinity(PCLAffinity.Star);
        }
        else {
            SetCurrentAffinity(NextAffinity.Type);
        }
        return SetNextAffinity(card);
    }

    public PCLAffinity OnNotMatch(AbstractCard card) {
        //TODO add subscribers

        currentMatchCombo = 0;
        this.fontScale = 1.5F;

        return CurrentAffinity.Type;
    }

    public void OnStartOfTurn()
    {
        if (Reroll != null) {
            Reroll.atStartOfTurn();
        }
        //TODO add subscribers
    }

    public PCLAffinity Randomize(Target target) {
        return target == Target.CurrentAffinity ? RandomizeCurrentAffinity() : RandomizeNextAffinity();
    }

    public PCLAffinity RandomizeCurrentAffinity() {
        return SetCurrentAffinity(PCLGameUtilities.GetRandomElement(PCLAffinity.Basic()));
    }

    public PCLAffinity RandomizeNextAffinity() {
        return SetNextAffinity(PCLGameUtilities.GetRandomElement(PCLAffinity.Basic()));
    }

    public PCLAffinity GetCurrentAffinity() {
        return CurrentAffinity.Type;
    }

    public PCLAffinity GetNextAffinity() {
        return NextAffinity.Type;
    }

    public int GetMatchesThisCombat() {return matchesThisCombat;}

    public int GetCurrentMatchCombo() {return currentMatchCombo;}

    public int GetLongestMatchCombo() {return longestMatchCombo;}

    public PCLAffinity Set(PCLAffinity affinity, Target target) {
        return target == Target.CurrentAffinity ? SetCurrentAffinity(affinity) : SetNextAffinity(affinity);
    }

    public PCLAffinity SetCurrentAffinity(PCLAffinity affinity) {
        CurrentAffinity.SetAffinity(affinity);
        return affinity;
    }

    public PCLAffinity SetNextAffinity(PCLAffinity affinity) {
        NextAffinity.SetAffinity(affinity);
        return affinity;
    }

    public PCLAffinity SetNextAffinity(AbstractCard card) {
        PCLCard eCard = PCLJUtils.SafeCast(card, PCLCard.class);
        if (eCard != null) {
            if (eCard.affinities.HasStar()) {
                return SetNextAffinity(PCLAffinity.Star);
            }
            PCLAffinity affinity = PCLGameUtilities.GetRandomElement(eCard.affinities.GetAffinities());
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
            meterSavedPosition = GR.PCL.Config.AffinityMeterPosition.Get(new Vector2(0.40f, 0.7f)).cpy();
            hb.SetPosition(ScreenW(meterSavedPosition.x), ScreenH(meterSavedPosition.y));
        }
        hb.update();

        draggable_panel.TryUpdate();
        draggable_icon.TryUpdate();

        boolean isHovered = draggable_panel.hb.hovered || CurrentAffinity.background_button.hb.hovered || NextAffinity.background_button.hb.hovered;
        draggable_panel.SetColor(0.05f, 0.05f, 0.05f, isHovered ? 0.5f : 0.05f);
        draggable_icon.SetColor(Colors.White(isHovered ? 0.75f : 0.1f));

        if (this.fontScale != 1.0F) {
            this.fontScale = MathUtils.lerp(this.fontScale, 1.0F, Gdx.graphics.getDeltaTime() * 10.0F);
            if (this.fontScale - 1.0F < 0.05F) {
                this.fontScale = 1.0F;
            }
        }

        CurrentAffinity.TryUpdate();
        NextAffinity.TryUpdate();

        if (Reroll != null)
        {
            Reroll.updateDescription();
            if (CurrentAffinity.background_button.hb.hovered || NextAffinity.background_button.hb.hovered) {
                PCLCardTooltip.QueueTooltip(Reroll.tooltip);
            }

            if (PCLHotkeys.rerollCurrent.isJustPressed()) {
                Reroll.OnClick();
            }

            isGlowing = GR.PCL.Config.FlashForReroll.Get()
                            && PCLGameUtilities.InBattle()
                            && PCLGameUtilities.IsPlayerTurn()
                            && AbstractDungeon.getCurrMapNode() != null
                            && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER
                            && PCLJUtils.Find(AbstractDungeon.player.hand.group, c -> HasMatch(c, true) && PCLGameUtilities.IsPlayable(c)) == null
                            && Reroll.triggerCondition.uses > 0;
            if (isGlowing && GR.UI.Elapsed(1.3f)) {
                PCLGameEffects.Queue.Add(new AffinityGlowEffect(CurrentAffinity));
            }
        }

        if (CurrentAffinity.background_button.hb.hovered) {
            PCLGameUtilities.HighlightMatchingCards(CurrentAffinity.Type == PCLAffinity.Star ? PCLAffinity.General : CurrentAffinity.Type);
        }

        if (NextAffinity.background_button.hb.hovered) {
            PCLGameUtilities.HighlightMatchingCards(NextAffinity.Type == PCLAffinity.Star ? PCLAffinity.General : NextAffinity.Type);
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        draggable_panel.Render(sb);
        draggable_icon.Render(sb);

        if (Reroll != null) {
            final BitmapFont rerollFont = EYBFontHelper.CardTitleFont_Small;
            rerollFont.getData().setScale(0.8f);

            //if (isGlowing) {
            //    PCLRenderHelpers.DrawCentered(sb, AffinityGlowEffect.FALLBACK_COLOR, glowImg, CurrentAffinity.background_button.hb.cX, CurrentAffinity.background_button.hb.cY, CurrentAffinity.background_button.hb.width, CurrentAffinity.background_button.hb.height, 1.2f, 0, false, false);
            //}

            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Colors.Black(0.5f), GR.PCL.Images.Panel_Elliptical_Half_H.Texture(),
                    (CurrentAffinity.background_button.hb.cX + NextAffinity.background_button.hb.cX) / 2, CurrentAffinity.background_button.hb.y - LABEL_OFFSET / 3.2f,
                    CurrentAffinity.background_button.hb.width * 1.25f, CurrentAffinity.background_button.hb.height * 0.7f, 1, 0);
            FontHelper.renderFontCentered(sb, rerollFont,
                    GR.PCL.Strings.Combat.Rerolls + ": " + Reroll.triggerCondition.uses + "/" + Reroll.triggerCondition.baseUses,
                    NextAffinity.background_button.hb.x, CurrentAffinity.background_button.hb.y - LABEL_OFFSET / 3.2f,
                    Reroll.triggerCondition.uses > 0 ? Settings.BLUE_TEXT_COLOR : Settings.RED_TEXT_COLOR);
            pinacolada.utilities.PCLRenderHelpers.ResetFont(rerollFont);
        }

        final BitmapFont comboFont = EYBFontHelper.CardTitleFont_Large;
        comboFont.getData().setScale(fontScale);
        FontHelper.renderFontCentered(sb, comboFont,
                "×" + currentMatchCombo,
                NextAffinity.background_button.hb.cX + NextAffinity.background_button.hb.width * 1.25f, NextAffinity.background_button.hb.cY,
                currentMatchCombo > 0 ? Colors.White(1f) : Colors.Cream(0.6f));
        pinacolada.utilities.PCLRenderHelpers.ResetFont(comboFont);

        CurrentAffinity.TryRender(sb);
        NextAffinity.TryRender(sb);
        hb.render(sb);



        FontHelper.renderFontCentered(sb, EYBFontHelper.CardTitleFont_Small,
                GR.PCL.Strings.Combat.Current, CurrentAffinity.background_button.hb.cX, CurrentAffinity.background_button.hb.y + LABEL_OFFSET, Colors.Cream(1f), 1f);
        FontHelper.renderFontCentered(sb, EYBFontHelper.CardTitleFont_Small,
                GR.PCL.Strings.Combat.Next, NextAffinity.background_button.hb.cX, NextAffinity.background_button.hb.y + LABEL_OFFSET, Colors.Cream(1f), 1f);
        FontHelper.renderFontCentered(sb, EYBFontHelper.CardTitleFont_Small,
                GR.PCL.Strings.Combat.CurrentMatchCombo, NextAffinity.background_button.hb.cX + NextAffinity.background_button.hb.width * 1.25f, NextAffinity.background_button.hb.y + LABEL_OFFSET, Colors.Cream(1f), 1f);
    }
}