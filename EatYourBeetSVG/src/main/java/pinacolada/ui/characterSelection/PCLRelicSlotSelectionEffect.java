package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.rooms.FakeEventRoom;
import pinacolada.effects.PCLEffect;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLRelicSlot;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_Relic;
import pinacolada.ui.controls.GUI_TextBox;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLInputManager;

import java.util.ArrayList;

public class PCLRelicSlotSelectionEffect extends PCLEffect
{
    public static final float TARGET_X = Settings.WIDTH * 0.25f;
    public static final float START_XY = Settings.WIDTH * 0.5f;

    private static final GUI_TextBox cardValue_text = new
    GUI_TextBox(GR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Hitbox(AbstractCard.IMG_WIDTH * 0.15f, AbstractCard.IMG_HEIGHT * 0.15f))
    .SetBackgroundTexture(GR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
    .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
    .SetAlignment(0.5f, 0.5f)
    .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

    private final PCLRelicSlot slot;
    private final ArrayList<RenderItem> relics = new ArrayList<>();
    //private boolean draggingScreen = false;
    private PCLRelic selectedRelic;

    public PCLRelicSlotSelectionEffect(PCLRelicSlot slot)
    {
        super(0.7f, true);

        if (AbstractDungeon.currMapNode == null)
        {
            AbstractDungeon.currMapNode = FakeEventRoom.MapRoomNode; // Because otherwise CardGlowBorder crashes in the CONSTRUCTOR
        }

        this.selectedRelic = slot.GetRelic();
        this.slot = slot;
        final ArrayList<PCLRelicSlot.Item> slotItems = slot.GetSelectableRelics();
        for (int i = 0; i < slotItems.size(); i++) {
            this.relics.add(new RenderItem(slotItems.get(i), i));
        }

        if (relics.isEmpty())
        {
            Complete();
        }
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (selectedRelic != null)
        {
            for (RenderItem item : relics)
            {
                if (item.relic.relicId.equals(selectedRelic.relicId))
                {
                    selectedRelic = item.relic;
                    selectedRelic.beginLongPulse();
                    break;
                }
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        for (RenderItem item : relics)
        {
            item.Update(deltaTime);
            if (item.relicImage.hb.hovered || item.relicName_text.hb.hovered) {
                item.relicName_text.SetColor(Color.WHITE);
            }
            else {
                item.relicName_text.SetColor(Color.GOLD);
            }
        }

        if (TickDuration(deltaTime))
        {
            if (PCLInputManager.LeftClick.IsJustReleased() && GR.UI.TryHover(null))
            {
                for (RenderItem item : relics)
                {
                    if (item.relicImage.hb.hovered || item.relicName_text.hb.hovered) {
                        OnRelicClicked(item.relic);
                    }
                }
                Complete();
                return;
            }

            isDone = false;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {

        for (RenderItem item : relics)
        {
            item.relicImage.TryRender(sb);
            cardValue_text
                    .SetText(item.estimatedValue)
                    .SetFontColor(item.estimatedValue < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
                    .SetPosition(item.relicImage.hb.x - 40 * Settings.scale, item.relicImage.hb.cY)
                    .Render(sb);
            item.relicName_text.Render(sb);
        }
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        if (AbstractDungeon.currMapNode == FakeEventRoom.MapRoomNode)
        {
            AbstractDungeon.currMapNode = null;
        }

        if (selectedRelic != null && slot.GetRelic().relicId != selectedRelic.relicId)
        {
            slot.Select(selectedRelic);
        }
    }

    private void OnRelicClicked(PCLRelic relic)
    {
        if (selectedRelic != null)
        {
            selectedRelic.stopPulse();

            if (selectedRelic == relic)
            {
                slot.Select((PCLRelic) null);
                selectedRelic = null;
                return;
            }
        }

        selectedRelic = relic;
        CardCrawlGame.sound.play("CARD_SELECT");
        slot.Select(relic);
        relic.beginLongPulse();
    }


    public class RenderItem
    {
        public final float targetX;
        public final float targetY;
        public final int estimatedValue;
        public final GUI_Relic relicImage;
        public final PCLRelic relic;
        public float animTimer;
        public float duration = 0.2f;
        public final GUI_Label relicName_text = new GUI_Label(FontHelper.cardTitleFont, new Hitbox(AbstractCard.IMG_WIDTH, AbstractCard.IMG_HEIGHT * 0.15f))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.01f);

        public RenderItem(PCLRelicSlot.Item item, int index)
        {
            this.relic = item.relic;
            this.estimatedValue = item.estimatedValue;
            this.relicImage = new GUI_Relic(relic, new AdvancedHitbox(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, item.relic.hb.width, item.relic.hb.height));
            this.targetX = TARGET_X;
            this.targetY = Settings.HEIGHT * (0.8f - (index * 0.05f));
            this.relicName_text.SetText(item.relic.name);
        }

        public void Update(float deltaTime) {
            this.animTimer += deltaTime;
            if (this.animTimer <= duration) {
                float newX = Interpolation.pow2.apply(START_XY, targetX, this.animTimer / duration);
                float newY = Interpolation.pow2.apply(START_XY, targetY, this.animTimer / duration);
                this.relicImage.Translate(newX, newY);
                this.relicName_text.SetPosition(relicImage.hb.x + 256 * Settings.scale, relicImage.hb.cY);
            }
            this.relicName_text.TryUpdate();
            this.relicImage.TryUpdate();
        }
    }
}