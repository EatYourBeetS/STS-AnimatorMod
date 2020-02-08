package eatyourbeets.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

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

        relicPreview.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        relicPreview.currentX = this.hb.x + this.hb.width * 0.9f;
        relicPreview.currentY = this.hb.y + this.hb.height * 0.5f;
        relicPreview.render(sb);

        if (this.hb.hovered)
        {
            TipHelper.queuePowerTips(
            this.hb.x + this.hb.width * 0.5f,
            this.hb.y + this.hb.height * 3f,
            relicPreview.tips);
        }
    }
}