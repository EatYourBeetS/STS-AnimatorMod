package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.common.AffinityKeywordButton;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

public class EYBAffinityMeter extends GUIElement
{
    public static final int DEFAULT_REROLLS = 1;
    public static final float ICON_SIZE = Scale(48);
    public static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    public final EYBCardAffinitySystem System;
    public final DraggableHitbox hb;
    protected final AffinityKeywordButton CurrentAffinity;
    protected final AffinityKeywordButton NextAffinity;
    protected final GUI_Image dragMeter_image;
    protected final GUI_Image draggable_icon;
    protected Vector2 meterSavedPosition;
    protected int currentRerolls;

    public EYBAffinityMeter(EYBCardAffinitySystem system) {
        System = system;

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(40f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));
        dragMeter_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
                .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(20f), Scale(20f), false))
                .SetColor(Colors.White(0.75f));

        //TODO add reroll functionality
        CurrentAffinity = new AffinityKeywordButton(hb, Affinity.General, 96f)
                .SetLevel(3)
                .SetOffsets(2f, 0);
        NextAffinity = new AffinityKeywordButton(hb, Affinity.General, 96f)
                .SetLevel(2)
                .SetOffsets(4f, 0);
    }

    public void Initialize() {
        //TODO add subscribers
        RandomizeCurrentAffinity();
        RandomizeNextAffinity();

        if (meterSavedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) dragMeter_image.hb;
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
        EYBCard eCard = JUtils.SafeCast(card, EYBCard.class);
        if (eCard != null) {
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
        //TODO add subscribers
        currentRerolls = DEFAULT_REROLLS;
    }

    public Affinity RandomizeCurrentAffinity() {
        return SetCurrentAffinity(JUtils.Random(Affinity.Basic()));
    }

    public Affinity RandomizeNextAffinity() {
        return SetNextAffinity(JUtils.Random(Affinity.Basic()));
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
            Affinity affinity = JUtils.Random(eCard.affinities.GetAffinities());
            if (affinity != null) {
                return SetNextAffinity(affinity);
            }
        }
        return RandomizeNextAffinity();
    }

    @Override
    public void Update() {
        if (meterSavedPosition == null)
        {
            meterSavedPosition = GR.Animator.Config.AffinityMeterPosition.Get(new Vector2(0.40f, 0.7f)).cpy();
            hb.SetPosition(ScreenW(meterSavedPosition.x), ScreenH(meterSavedPosition.y));
        }
        hb.update();

        dragMeter_image.TryUpdate();
        draggable_icon.TryUpdate();

        CurrentAffinity.TryUpdate();
        NextAffinity.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb) {
        dragMeter_image.Render(sb);
        draggable_icon.Render(sb);

        CurrentAffinity.TryRender(sb);
        NextAffinity.TryRender(sb);
        hb.render(sb);
    }
}