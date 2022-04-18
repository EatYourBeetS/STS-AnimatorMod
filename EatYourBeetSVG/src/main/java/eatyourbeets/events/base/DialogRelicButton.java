package eatyourbeets.events.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.relics.EYBRelic;

public class DialogRelicButton extends LargeDialogOptionButton
{
    private final AbstractRelic relicPreview;

    public DialogRelicButton(int slot, String msg, boolean isDisabled, AbstractRelic relicPreview)
    {
        super(slot, msg, isDisabled);

        this.relicPreview = relicPreview;
    }

    @Override
    public void update(int size)
    {
        super.update(size);

        relicPreview.currentX = relicPreview.hb.x = this.hb.x + this.hb.width * 0.93f;
        relicPreview.currentY = relicPreview.hb.y = this.hb.y + this.hb.height * 0.5f;
        relicPreview.hb.width = relicPreview.hb.height = 0;
        relicPreview.update();

        if (this.hb.hovered)
        {
            if (relicPreview instanceof EYBRelic)
            {
                EYBCardTooltip.QueueTooltips((EYBRelic) relicPreview); //hb.x + hb.width * 0.5f, hb.y + hb.height * 3f, relicPreview.tips);
            }
            else
            {
                TipHelper.queuePowerTips(hb.x + hb.width * 0.5f, hb.y + hb.height * 3f, relicPreview.tips);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        relicPreview.render(sb);
    }
}