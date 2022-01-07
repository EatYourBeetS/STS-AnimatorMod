package pinacolada.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.resources.GR;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_CardGrid;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.utilities.PCLGameUtilities;

public class CardPoolScreen extends AbstractScreen
{
    private final GUI_Button openButton;
    private final GUI_Toggle upgradeToggle;
    private final GUI_Toggle colorlessToggle;
    public GUI_CardGrid cardGrid;

    public CardPoolScreen()
    {
        cardGrid = new GUI_CardGrid()
                .ShowScrollbar(true);

        upgradeToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.8f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
                .SetText(SingleCardViewPopup.TEXT[6])
                .SetOnToggle(CardPoolScreen::ToggleViewUpgrades);

        colorlessToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.75f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
                .SetText(GR.PCL.Strings.SeriesSelectionButtons.ShowColorless)
                .SetOnToggle(val -> {
                    GR.UI.CardFilters.ColorsDropdown.ToggleSelection(AbstractCard.CardColor.COLORLESS, val, true);
                    GR.UI.CardFilters.ColorsDropdown.ToggleSelection(AbstractCard.CardColor.CURSE, val, true);
                });

        openButton = new GUI_Button(GR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                .SetBorder(GR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(GR.PCL.Strings.Misc.Filters)
                .SetOnClick(() -> {
                    if (GR.UI.CardFilters.isActive) {
                        GR.UI.CardFilters.Close();
                    }
                    else {
                        GR.UI.CardFilters.Open();
                    }
                })
                .SetColor(Color.GRAY);
    }

    public void Open(CardGroup cards)
    {
        super.Open();

        cardGrid.Clear();
        colorlessToggle.SetToggle(false);
        if (cards.isEmpty())
        {
            AbstractDungeon.closeCurrentScreen();
            return;
        }

        cardGrid.SetCardGroup(cards);
        GR.UI.CustomHeader.setGroup(cards);
        GR.UI.CustomHeader.SetupButtons(!PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass));
        GR.UI.CardFilters.Initialize(__ -> {
            GR.UI.CustomHeader.UpdateForFilters();
            GR.UI.CardAffinities.Open(GR.UI.CustomHeader.group.group);
        }, GR.UI.CustomHeader.originalGroup, GR.UI.CustomHeader.IsColorless());
        GR.UI.CustomHeader.UpdateForFilters();

        if (PCLGameUtilities.InGame())
        {
            AbstractDungeon.overlayMenu.cancelButton.show(MasterDeckViewScreen.TEXT[1]);
        }

        GR.UI.CardAffinities.SetActive(true);
        GR.UI.CardAffinities.Open(cardGrid.cards.group);
    }

    @Override
    public void Update()
    {
        cardGrid.TryUpdate();
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
        colorlessToggle.Update();
        GR.UI.CustomHeader.update();
        GR.UI.CardFilters.TryUpdate();
        if (!GR.UI.CardFilters.isActive) {
            openButton.TryUpdate();
        }

        GR.UI.CardAffinities.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        cardGrid.TryRender(sb);
        upgradeToggle.Render(sb);
        colorlessToggle.Render(sb);
        GR.UI.CustomHeader.render(sb);
        GR.UI.CardFilters.TryRender(sb);
        if (!GR.UI.CardFilters.isActive) {
            openButton.TryRender(sb);
        }
        GR.UI.CardAffinities.TryRender(sb);
    }

    private static void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }
}