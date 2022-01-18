package pinacolada.ui.cardReward;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinityStatistics;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLGameEffects;

import java.util.ArrayList;

public class CardAffinityPanel extends GUIElement
{
    public static final float ICON_SIZE = Scale(40);

    private static final PCLImages.AffinityIcons ICONS = GR.PCL.Images.Affinities;

    private final AdvancedHitbox hb;
    private final ArrayList<CardAffinityCounter> counters = new ArrayList<>();
    private final PCLCardAffinityStatistics statistics;
    private final GUI_Toggle upgrade_toggle;
    private long lastFrame;

    public CardAffinityPanel()
    {
        statistics = new PCLCardAffinityStatistics();
        hb = new DraggableHitbox(ScreenW(0.025f), ScreenH(0.65f), Scale(140), Scale(50), false);

        for (PCLAffinity t : PCLAffinity.All())
        {
            counters.add(new CardAffinityCounter(hb, t));
        }

        upgrade_toggle = new GUI_Toggle(new RelativeHitbox(hb, 1.3f, 1, 0.5f * (1 - (ICON_SIZE/hb.width)), -(0.5f + counters.size())))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
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
        Open(cards, false, null, false);
    }

    public void Open(ArrayList<AbstractCard> cards, boolean showUpgradeToggle, ActionT1<CardAffinityCounter> onClick, boolean force)
    {
        isActive = (force || GR.PCL.IsSelected()) && cards != null;

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
        statistics.RefreshStatistics(showUpgrades, false);

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

            upgrade_toggle.SetInteractable(PCLGameEffects.IsEmpty()).Update();
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
