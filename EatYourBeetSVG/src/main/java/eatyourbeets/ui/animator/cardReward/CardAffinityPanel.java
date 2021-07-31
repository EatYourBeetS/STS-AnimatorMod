package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardAffinityStatistics;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class CardAffinityPanel extends GUIElement
{
    public static final float ICON_SIZE = Scale(40);

    private static final CommonImages.AffinityIcons ICONS = GR.Common.Images.Affinities;

    private final AdvancedHitbox hb;
    private final ArrayList<CardAffinityCounter> counters = new ArrayList<>();
    private final EYBCardAffinityStatistics statistics;
    private final GUI_Toggle upgrade_toggle;
    private final GUI_Image LV1_image;
    private final GUI_Image LV2_image;
    private long lastFrame;

    public CardAffinityPanel()
    {
        statistics = new EYBCardAffinityStatistics();
        hb = new DraggableHitbox(ScreenW(0.025f), ScreenH(0.65f), Scale(140), Scale(50), false);

        LV1_image = RenderHelpers.ForTexture(ICONS.Border_Weak.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.15f, 1f, true));

        LV2_image = RenderHelpers.ForTexture(ICONS.Border.Texture())
        .SetBackgroundTexture(ICONS.BorderBG.Texture())
        .SetForegroundTexture(ICONS.BorderFG.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.45f, 1f, true));

        for (AffinityType t : AffinityType.AllTypes())
        {
            counters.add(new CardAffinityCounter(hb, t));
        }

        upgrade_toggle = new GUI_Toggle(new RelativeHitbox(hb, 1.3f, 1, 0.5f * (1 - (ICON_SIZE/hb.width)), -(0.5f + counters.size())))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.4f)
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);
    }

    public void Close()
    {
        SetActive(false);
    }

    public void Open(ArrayList<AbstractCard> cards)
    {
        Open(cards, false, null);
    }

    public void Open(ArrayList<AbstractCard> cards, boolean showUpgradeToggle, ActionT1<CardAffinityCounter> onClick)
    {
        isActive = GR.Animator.IsSelected() && cards != null;

        if (!isActive)
        {
            return;
        }

        upgrade_toggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).SetActive(showUpgradeToggle);
        statistics.Reset();
        statistics.AddCards(cards);

        for (CardAffinityCounter c : counters)
        {
            c.SetOnClick(onClick).Initialize(statistics);
        }

        Refresh(showUpgradeToggle && upgrade_toggle.toggled);
    }

    public void Refresh(boolean showUpgrades)
    {
        statistics.RefreshStatistics(showUpgrades);

        counters.sort((a, b) -> (int)(1000 * (b.AffinityGroup.GetPercentage(0) - a.AffinityGroup.GetPercentage(0))));

        int index = 0;
        for (CardAffinityCounter c : counters)
        {
            if (c.isActive)
            {
                c.SetIndex(index);
                index += 1;
            }
        }
    }

    @Override
    public void Update()
    {
        hb.update();
        LV1_image.TryUpdate();
        LV2_image.TryUpdate();

        for (CardAffinityCounter c : counters)
        {
            c.TryUpdate();
        }

        if (upgrade_toggle.isActive)
        {
            if (upgrade_toggle.toggled != SingleCardViewPopup.isViewingUpgrade)
            {
                upgrade_toggle.Toggle(SingleCardViewPopup.isViewingUpgrade);
            }

            upgrade_toggle.SetInteractable(GameEffects.IsEmpty()).Update();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        long frame = Gdx.graphics.getFrameId();
        if (frame == lastFrame)
        {
            return;
        }

        lastFrame = frame;
        LV1_image.TryRender(sb);
        LV2_image.TryRender(sb);
        for (CardAffinityCounter c : counters)
        {
            c.TryRender(sb);
        }
        upgrade_toggle.TryRender(sb);
        hb.render(sb);
    }

    protected void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
        Refresh(value);
    }
}
