package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;

public class AnimatorCardSlotEditor extends GUIElement
{
    protected DraggableHitbox hb;
    protected GUI_Image background_image;

    public AnimatorCardSlotEditor()
    {
        hb = new DraggableHitbox(0, 0, Scale(AbstractCard.RAW_W) * 0.75f, Scale(AbstractCard.RAW_H) * 0.75f, true);

        background_image = new GUI_Image(GR.Common.Images.Square.Texture(), hb)
        .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
        .SetColor(0, 0, 0, 0.85f);
    }

    @Override
    public void Update()
    {
        background_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);
    }
}