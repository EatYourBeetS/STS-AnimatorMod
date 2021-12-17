package pinacolada.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GUI_Relic extends GUI_Image
{
    private AbstractRelic relic;

    public GUI_Relic(AbstractRelic relic, Hitbox hb)
    {
        super(relic.img, Color.WHITE);

        SetHitbox(hb);

        this.relic = relic;
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        super.Render(sb);

        if (hb.hovered)
        {
            relic.renderTip(sb);
        }
    }
}
