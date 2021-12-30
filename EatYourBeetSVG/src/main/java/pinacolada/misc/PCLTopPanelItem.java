package pinacolada.misc;

import basemod.TopPanelItem;
import eatyourbeets.utilities.GenericCallback;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.ui.TextureCache;

public abstract class PCLTopPanelItem extends TopPanelItem
{
    public GenericCallback<PCLTopPanelItem> onLeftClick;
    public PCLCardTooltip tooltip;


    public static String CreateFullID(Class<? extends PCLTopPanelItem> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public PCLTopPanelItem(TextureCache tc, String id) {
        super(tc.Texture(), id);
    }

    public PCLTopPanelItem SetOnClick(GenericCallback<PCLTopPanelItem> onLeftClick) {
        this.onLeftClick = onLeftClick;
        setClickable(this.onLeftClick != null);
        return this;
    }

    public PCLTopPanelItem SetTooltip(PCLCardTooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    protected void onClick() {
        if (this.onLeftClick != null) {
            this.onLeftClick.Complete(this);
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.tooltip != null && getHitbox().hovered) {
            PCLCardTooltip.QueueTooltip(tooltip);
        }
    }
}
