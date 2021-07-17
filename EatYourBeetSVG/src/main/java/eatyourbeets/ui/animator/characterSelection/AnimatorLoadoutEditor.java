package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;

public class AnimatorLoadoutEditor extends AbstractScreen
{
    protected GUI_Image background_image;
    protected GUI_Button cancel_button;
    protected GUI_Button confirm_button;
    protected GUI_TextBox cardsCount_text;
    protected GUI_TextBox cardsPower_text;

    public AnimatorLoadoutEditor()
    {
        final float buttonHeight = ScreenH(0.07f);
        final float buttonWidth = ScreenW(0.18f);
        final float button_cY = buttonHeight * 1.5f;

        background_image = new GUI_Image(GR.Common.Images.Square.Texture(), new Hitbox(ScreenW(1), ScreenH(1)))
        .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
        .SetColor(0, 0, 0, 0.85f);

        cancel_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(buttonWidth * 0.75f, button_cY)
        .SetColor(Color.FIREBRICK)
        .SetText("Cancel")
        .SetOnClick(AbstractDungeon::closeCurrentScreen);

        confirm_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(ScreenW(1) - (buttonWidth * 0.75f), button_cY)
        .SetColor(Color.FOREST)
        .SetText("Confirm")
        .SetInteractable(false)
        .SetOnClick(AbstractDungeon::closeCurrentScreen);

        cardsCount_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Hitbox(buttonWidth, buttonHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.6f, 0.2f, true)
        .SetPosition(confirm_button.hb.cX, button_cY * 2.5f)
        .SetFont(FontHelper.charDescFont, 1)
        .SetText("Cards: #g" + 12 + " /15");

        cardsPower_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Hitbox(buttonWidth, buttonHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.6f, 0.2f, true)
        .SetPosition(confirm_button.hb.cX, button_cY * 1.75f)
        .SetFont(FontHelper.charDescFont, 1)
        .SetText("Power: #r" + 12 + " /10");
    }

    public void Open(AnimatorLoadout loadout)
    {
        super.Open();
    }

    @Override
    public void Update()
    {
        super.Update();

        background_image.Update();
        cancel_button.Update();
        confirm_button.Update();
        cardsCount_text.Update();
        cardsPower_text.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        super.Render(sb);

        background_image.Render(sb);
        cancel_button.Render(sb);
        confirm_button.Render(sb);
        cardsCount_text.Render(sb);
        cardsPower_text.Render(sb);
    }
}