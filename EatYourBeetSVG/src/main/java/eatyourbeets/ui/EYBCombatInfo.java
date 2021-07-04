package eatyourbeets.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.DraggableHitbox;

public class EYBCombatInfo extends GUIElement
{
    protected final DraggableHitbox hb;
    protected final GUI_Image background;
    protected final GUI_Label teamworkInfo;

    public EYBCombatInfo()
    {
        hb = new DraggableHitbox(ScreenW(0.5f), ScreenH(0.5f), ScreenW(0.1f), ScreenH(0.05f), true);

        background = new GUI_Image(GR.Common.Images.Panel.Texture(), hb);
        teamworkInfo = new GUI_Label(FontHelper.topPanelAmountFont, hb)
        .SetText("Teamwork: 3");
    }

    @Override
    public void Update()
    {
        background.Update();
        teamworkInfo.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background.Render(sb);
        teamworkInfo.Render(sb);
    }
}
