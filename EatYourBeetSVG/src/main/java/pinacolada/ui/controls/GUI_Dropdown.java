package pinacolada.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.Mathf;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class GUI_Dropdown<T> extends GUIElement
{
    private static final int DEFAULT_MAX_ROWS = 15;
    private static final float ARROW_ICON_W = 30.0F * Settings.scale;
    private static final float ARROW_ICON_H = 30.0F * Settings.scale;
    private static final float BORDER_SIZE = Settings.scale * 10.0F;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float ICON_WIDTH = 72.0F * Settings.scale;
    private static final float SCROLLBAR_WIDTH = 24.0F * Settings.scale;
    private static final float SCROLLBAR_PADDING = 8.0F * Settings.scale;
    private static final float TOGGLE_OFFSET = 5f;

    protected ActionT1<Boolean> onOpenOrClose;
    protected ActionT1<List<T>> onChange;
    protected BitmapFont font;
    protected FuncT1<Color, List<T>> colorFunctionButton;
    protected FuncT1<String, List<T>> labelFunctionButton;
    protected FuncT1<String, T> labelFunction;
    protected GUI_Label header; // TODO
    protected GUI_VerticalScrollBar scrollBar;
    protected boolean isOpen;
    protected boolean rowsHaveBeenPositioned;
    protected boolean shouldSnapCursorToSelectedIndex;
    protected final GUI_Button button;
    protected final GUI_Button clearButton;
    protected final TreeSet<Integer> currentIndices = new TreeSet<>();
    protected float fontScale;
    protected float rowHeight;
    protected float rowWidth;
    protected int maxRows;
    protected int topVisibleRowIndex;
    public boolean canAutosizeButton;
    public boolean isMultiSelect;
    public final AdvancedHitbox hb;
    public final ArrayList<DropdownRow<T>> rows = new ArrayList<>();

    public GUI_Dropdown(AdvancedHitbox hb) {
        this(hb, Object::toString, new ArrayList<>(), EYBFontHelper.CardTooltipFont, DEFAULT_MAX_ROWS, false);
    }

    public GUI_Dropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction) {
        this(hb, labelFunction, new ArrayList<>(), EYBFontHelper.CardTooltipFont, DEFAULT_MAX_ROWS, false);
    }

    public GUI_Dropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction, ArrayList<T> options) {
        this(hb, labelFunction, options, EYBFontHelper.CardTooltipFont, DEFAULT_MAX_ROWS, false);
    }

    public GUI_Dropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction, ArrayList <T> options, BitmapFont font, int maxRows, boolean canAutosizeButton) {
        this.hb = hb;
        this.hb.SetIsPopupCompatible(true);
        this.font = font;
        this.fontScale = 1;
        this.maxRows = maxRows;
        this.labelFunction = labelFunction;
        this.canAutosizeButton = canAutosizeButton;

        this.rowHeight = CalculateRowHeight();
        for (int i = 0; i < options.size(); i++) {
            rows.add(new DropdownRow<>(new RelativeHitbox(hb, hb.width, this.rowHeight, 0f, 0, false).SetIsPopupCompatible(true), options.get(i), labelFunction, font, fontScale, i).SetIsMultiSelect(isMultiSelect));
        }
        this.scrollBar = new GUI_VerticalScrollBar(
                new AdvancedHitbox(hb.x + hb.width - SCROLLBAR_PADDING, hb.y + CalculateScrollbarOffset(), SCROLLBAR_WIDTH,rowHeight * this.visibleRowCount())
                .SetIsPopupCompatible(true))
        .SetOnScroll(this::OnScroll);
        this.button = new GUI_Button(GR.PCL.Images.RectangularButton.Texture(), this.hb)
                .SetColor(Color.GRAY)
                .SetFont(font, fontScale)
                .SetText(currentIndices.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected)
                .SetOnClick(this::OpenOrCloseMenu);
        this.clearButton = new GUI_Button(GR.PCL.Images.X.Texture(), new AdvancedHitbox(hb.x + hb.width, hb.y, hb.height, hb.height).SetIsPopupCompatible(true))
                .SetOnClick(() -> {SetSelectionIndices(new int[] {}, true);});
        this.header = new GUI_Label(EYBFontHelper.CardTitleFont_Small, new AdvancedHitbox(hb.x, hb.y + hb.height, hb.width, hb.height)).SetAlignment(0.5f,0.0f,false);
        this.header.SetActive(false);
        Autosize();
    }

    public GUI_Dropdown<T> SetCanAutosizeButton(boolean value) {
        this.canAutosizeButton = value;
        if (this.canAutosizeButton) {
            Autosize();
        }

        return this;
    }

    public GUI_Dropdown<T> SetHeader(BitmapFont font, float fontScale, Color textColor, String text) {
        return SetHeader(font,fontScale,textColor,text,false);
    }

    public GUI_Dropdown<T> SetHeader(BitmapFont font, float fontScale, Color textColor, String text, boolean smartText) {
        this.header.SetFont(font, fontScale).SetColor(textColor).SetText(text).SetSmartText(smartText).SetActive(true);

        return this;
    }

    public GUI_Dropdown<T> SetIsMultiSelect(boolean value) {
        this.isMultiSelect = value;
        for (DropdownRow<T> row : rows) {
            row.SetIsMultiSelect(value);
        }

        return this;
    }

    public GUI_Dropdown<T> SetItems(T[] options) {
        return SetItems(Arrays.asList(options));
    }

    public GUI_Dropdown<T> SetItems(List<T> options) {
        this.currentIndices.clear();
        this.rows.clear();
        for (int i = 0; i < options.size(); i++) {
            rows.add(new DropdownRow<>(new RelativeHitbox(hb, hb.width, this.rowHeight, 0f, 0, false).SetIsPopupCompatible(true), options.get(i), labelFunction, font, fontScale, i).SetIsMultiSelect(isMultiSelect));
        }
        Autosize();

        return this;
    }

    public GUI_Dropdown<T> SetLabelFunctionForButton(FuncT1<String, List<T>> labelFunctionButton, FuncT1<Color, List<T>> colorFunctionButton, boolean isSmartText) {
        this.button.SetSmartText(isSmartText);
        this.labelFunctionButton = labelFunctionButton;
        this.colorFunctionButton = colorFunctionButton;
        if (labelFunctionButton != null) {
            this.button.SetText(labelFunctionButton.Invoke(GetCurrentItems()));
        }
        if (colorFunctionButton != null) {
            this.button.SetTextColor(colorFunctionButton.Invoke(GetCurrentItems()));
        }
        return this;
    }

    public GUI_Dropdown<T> SetLabelFunctionForOption(FuncT1<String, T> labelFunction, boolean isSmartText) {
        this.labelFunction = labelFunction;
        for (DropdownRow<T> row : rows) {
            row.SetLabelFunction(labelFunction, isSmartText);
        }
        return this;
    }

    public GUI_Dropdown<T> SetFontForButton(BitmapFont font, float fontScale)
    {
        button.SetFont(font, fontScale);
        Autosize();

        return this;
    }

    public GUI_Dropdown<T> SetFontForRows(BitmapFont font, float fontScale)
    {
        this.font = font;
        this.fontScale = fontScale;

        for (DropdownRow<T> row : rows) {
            row.label.SetFont(font, fontScale);
        }
        Autosize();

        return this;
    }

    public GUI_Dropdown<T> SetOnChange(ActionT1<List<T>> onChange) {
        this.onChange = onChange;
        return this;
    }

    // If you're using this dropdown on a pop-up menu, you need to have this action set CardCrawlGame.isOpen or your pop-up menu won't work properly
    public GUI_Dropdown<T> SetOnOpenOrClose(ActionT1<Boolean> onOpenOrClose) {
        this.onOpenOrClose = onOpenOrClose;

        return this;
    }

    public GUI_Dropdown<T> SetPosition(float x, float y) {
        this.hb.translate(x, y);
        this.button.hb.translate(x, y);
        this.scrollBar.hb.translate(x + hb.width - SCROLLBAR_PADDING, y + CalculateScrollbarOffset());
        this.header.hb.translate(x, y + hb.height);
        this.clearButton.hb.translate(x + hb.width, y);
        return this;
    }

    public GUI_Dropdown<T> SetSelectionIndices(int[] selection, boolean shouldInvoke) {
        this.currentIndices.clear();
        for (Integer i : selection) {
            if (i < rows.size() && i >= 0) {
                currentIndices.add(i);
            }
        }
        updateForSelection(shouldInvoke);
        return this;
    }

    public GUI_Dropdown<T> SetSelection(T selection, boolean shouldInvoke) {
        this.currentIndices.clear();
        for (int i = 0; i < rows.size(); i++) {
            if (selection.equals(rows.get(i).item)) {
                currentIndices.add(i);
                break;
            }
        }
        updateForSelection(shouldInvoke);
        return this;
    }


    public GUI_Dropdown<T> SetSelection(ArrayList<T> selection, boolean shouldInvoke) {
        this.currentIndices.clear();
        for (int i = 0; i < rows.size(); i++) {
            if (selection.contains(rows.get(i).item)) {
                currentIndices.add(i);
            }
        }
        updateForSelection(shouldInvoke);
        return this;
    }

    public void Autosize() {
        this.rowWidth = CalculateRowWidth();
        this.rowHeight = CalculateRowHeight();
        if (canAutosizeButton) {
            hb.resize(rowWidth, hb.height);
            button.hb.resize(rowWidth, hb.height);
            this.header.hb.translate(hb.x, hb.y + hb.height);
            this.clearButton.hb.translate(hb.x + hb.width, hb.y);
        }
        for (DropdownRow<T> row : rows) {
            row.hb.resize(rowWidth,rowHeight);
        }
        this.scrollBar.hb.resize(SCROLLBAR_WIDTH, rowHeight * (this.visibleRowCount() - 1));
        this.scrollBar.hb.translate(hb.x + rowWidth - SCROLLBAR_PADDING, hb.y + CalculateScrollbarOffset());
    }

    public boolean AreAnyItemsHovered() {
        if (this.hb.hovered || this.clearButton.hb.hovered) {
            return true;
        }
        for(int i = 0; i < this.visibleRowCount(); ++i) {
            if (this.rows.get(i + this.topVisibleRowIndex).hb.hovered) {
                return true;
            }
        }
        return false;
    }

    private float CalculateRowWidth() {
        float w = 0;
        for (DropdownRow<T> row : rows) {
            w = Math.max(w, FontHelper.getSmartWidth(this.font, row.label.text, 3.4028235E38F, 3.4028235E38F) + ICON_WIDTH);
        }
        return w;
    }

    public float CalculateRowHeight() {
        float scaledHeight = this.font.getCapHeight() * this.fontScale;
        float extraSpace = Math.min(Math.max(scaledHeight, 15.0F) * Settings.scale, 15.0F);
        return scaledHeight + extraSpace;
    }

    public float CalculateScrollbarOffset() {
        return -this.visibleRowCount() * this.rowHeight;
    }

    public ArrayList<T> GetAllItems() {
        return PCLJUtils.Map(this.rows, row -> row.item);
    }

    public ArrayList<T> GetCurrentItems() {
        ArrayList<T> items = new ArrayList<>();
        for (Integer i : currentIndices) {
            items.add(this.rows.get(i).item);
        }
        return items;
    }

    public int GetCurrentIndex() {
        return currentIndices.isEmpty() ? 0 : currentIndices.first();
    }

    public void OpenOrCloseMenu() {
        if (this.isOpen) {
            CardCrawlGame.isPopupOpen = false;
            this.isOpen = false;
        }
        else {
            CardCrawlGame.isPopupOpen = true;
            this.isOpen = true;
            this.updateNonMouseStartPosition();
        }

        if (this.onOpenOrClose != null) {
            this.onOpenOrClose.Invoke(this.isOpen);
        }
    }

    protected void OnScroll(float newPercent)
    {
        int approximateRow = (int) (newPercent * (this.rows.size() - this.visibleRowCount()));
        this.topVisibleRowIndex = Mathf.Clamp(approximateRow, 0, this.rows.size() - this.visibleRowCount());
    }

    @Override
    public void Update() {
        this.hb.update();
        this.button.Update();
        this.header.TryUpdate();
        if (this.isMultiSelect && currentIndices.size() != 0) {
            this.clearButton.Update();
        }
        if (this.rows.size() != 0 && this.isOpen) {
                boolean isHoveringOver = this.hb.hovered;
                this.updateNonMouseInput();

                if (this.isMultiSelect) {
                    isHoveringOver = isHoveringOver | this.clearButton.hb.hovered;
                }

                for(int i = 0; i < rows.size(); ++i) {
                    if (this.rows.get(i).update(i >= topVisibleRowIndex && i < topVisibleRowIndex + visibleRowCount(), currentIndices.contains(i))) {
                        this.setSelectedIndex(i);
                        isHoveringOver = true;
                        CardCrawlGame.sound.play("UI_CLICK_2");
                        if (!this.isMultiSelect) {
                            OpenOrCloseMenu();
                        }
                    }
                    else if (this.rows.get(i).hb.hovered) {
                        isHoveringOver = true;
                    }
                }

                if (this.shouldShowSlider()) {
                    this.scrollBar.TryUpdate();
                    isHoveringOver = isHoveringOver | this.scrollBar.hb.hovered;
                }

                if (InputHelper.scrolledDown) {
                    this.topVisibleRowIndex = Integer.min(this.topVisibleRowIndex + 1, this.rows.size() - this.visibleRowCount());
                    this.scrollBar.Scroll(this.scrollPercentForTopVisibleRowIndex(this.topVisibleRowIndex), false);
                } else if (InputHelper.scrolledUp) {
                    this.topVisibleRowIndex = Integer.max(0, this.topVisibleRowIndex - 1);
                    this.scrollBar.Scroll(this.scrollPercentForTopVisibleRowIndex(this.topVisibleRowIndex), false);
                }

                boolean shouldCloseMenu = InputHelper.justClickedLeft && !isHoveringOver || InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed();
                if (shouldCloseMenu) {
                    if (Settings.isControllerMode) {
                        CInputActionSet.cancel.unpress();
                        CInputHelper.setCursor(this.hb);
                    }

                    OpenOrCloseMenu();
                }
        }

    }

    private boolean isUsingNonMouseControl() {
        return Settings.isControllerMode || InputActionSet.up.isJustPressed() || InputActionSet.down.isJustPressed();
    }

    private void updateNonMouseStartPosition() {
        if (this.isUsingNonMouseControl()) {
            this.shouldSnapCursorToSelectedIndex = true;
        }
    }

    private void updateNonMouseInput() {
        if (this.isUsingNonMouseControl()) {
            if (this.shouldSnapCursorToSelectedIndex && this.rowsHaveBeenPositioned) {
                CInputHelper.setCursor((this.rows.get(GetCurrentIndex())).hb);
                this.shouldSnapCursorToSelectedIndex = false;
            } else {
                int hoveredIndex = -1;

                for(int i = this.topVisibleRowIndex; i < this.topVisibleRowIndex + this.visibleRowCount(); ++i) {
                    if (this.rows.get(i).hb.hovered) {
                        hoveredIndex = i;
                        break;
                    }
                }

                if (hoveredIndex >= 0) {
                    boolean didInputUp = CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed() || InputActionSet.up.isJustPressed();
                    boolean didInputDown = CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed() || InputActionSet.down.isJustPressed();
                    boolean isMoving = didInputUp || didInputDown;
                    if (isMoving) {
                        int targetHoverIndexOffset = didInputDown ? 1 : -1;
                        int targetHoverIndex = (hoveredIndex + targetHoverIndexOffset + this.rows.size()) % this.rows.size();
                        boolean isAboveTheTop = targetHoverIndex < this.topVisibleRowIndex;
                        boolean isBelowTheBottom = targetHoverIndex >= this.topVisibleRowIndex + this.visibleRowCount();
                        if (isAboveTheTop) {
                            if (didInputDown) {
                                CInputHelper.setCursor(this.rows.get(this.topVisibleRowIndex).hb);
                            }

                            this.topVisibleRowIndex = targetHoverIndex;
                        } else if (isBelowTheBottom) {
                            if (didInputUp) {
                                CInputHelper.setCursor(this.rows.get(this.topVisibleRowIndex + this.visibleRowCount() - 1).hb);
                                this.rows.get(targetHoverIndex).hb.hovered = true;
                            }

                            this.topVisibleRowIndex = targetHoverIndex - this.visibleRowCount() + 1;
                        } else {
                            CInputHelper.setCursor(this.rows.get(targetHoverIndex).hb);
                        }

                        if (this.shouldShowSlider()) {
                            this.scrollBar.Scroll(this.scrollPercentForTopVisibleRowIndex(this.topVisibleRowIndex), false);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void Render(SpriteBatch sb) {

        this.hb.render(sb);
        this.button.TryRender(sb);
        this.header.TryRender(sb);
        if (this.isMultiSelect && currentIndices.size() != 0) {
            this.clearButton.Render(sb);
        }
        if (this.rows.size() > 0) {
            RenderRowContent(sb);
            RenderArrows(sb);
        }
    }

    protected void RenderRowContent(SpriteBatch sb) {
        int rowCount = this.isOpen ? this.visibleRowCount() : 0;
        this.layoutRowsBelow(hb.x, hb.y);
        float topY = this.yPositionForRowBelow(hb.y, -1);
        float bottomY = this.yPositionForRowBelow(hb.y, rowCount);
        if (this.isOpen) {
            this.renderBorder(sb, hb.x, bottomY, hb.width, topY - bottomY);
        }

        //this.renderBorderFromTop(sb, hb.x, hb.y, hb.width, this.rowHeight - BOX_EDGE_H * 2.5F);
        if (this.isOpen) {
            for(int i = 0; i < this.visibleRowCount(); ++i) {
                this.rows.get(i + this.topVisibleRowIndex).renderRow(sb);
            }

            if (this.shouldShowSlider()) {
                this.scrollBar.TryRender(sb);
            }
        }
    }

    protected void RenderArrows(SpriteBatch sb) {
        float arrowIconX = hb.x + hb.width - ARROW_ICON_W - Settings.scale * 10.0F;
        Texture dropdownArrowIcon = this.isOpen ? ImageMaster.OPTION_TOGGLE_ON : ImageMaster.FILTER_ARROW;
        sb.draw(dropdownArrowIcon, arrowIconX, hb.y + hb.height / 4, ARROW_ICON_W, ARROW_ICON_H);
    }

    protected void layoutRowsBelow(float originX, float originY) {
        for(int i = 0; i < this.visibleRowCount(); ++i) {
            this.rows.get(this.topVisibleRowIndex + i).move(originX, this.yPositionForRowBelow(originY, i + 1));
        }
        this.rowsHaveBeenPositioned = true;
    }

    protected void renderBorder(SpriteBatch sb, float x, float bottom, float width, float height) {
        float BOX_W = width + 2.0F * BORDER_SIZE;
        float FRAME_X = x - BORDER_SIZE;
        sb.setColor(Color.WHITE);
        float bottomY = bottom - BORDER_SIZE;
        sb.draw(ImageMaster.KEYWORD_BOT, FRAME_X, bottomY, BOX_W, rowHeight);
        float middleHeight = height - 2.0F * rowHeight - BORDER_SIZE;
        sb.draw(ImageMaster.KEYWORD_BODY, FRAME_X, bottomY + rowHeight, BOX_W, middleHeight);
        sb.draw(ImageMaster.KEYWORD_TOP, FRAME_X, bottom + middleHeight + BORDER_SIZE, BOX_W, rowHeight);
    }

    protected void renderBorderFromTop(SpriteBatch sb, float x, float top, float width, float height) {
        float BORDER_TOP_Y = top - BOX_EDGE_H + BORDER_SIZE;
        float BOX_W = width + 2.0F * BORDER_SIZE;
        float FRAME_X = x - BORDER_SIZE;
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, FRAME_X, BORDER_TOP_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, FRAME_X, BORDER_TOP_Y - height - BOX_EDGE_H, BOX_W, height + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, FRAME_X, BORDER_TOP_Y - height - BOX_BODY_H, BOX_W, BOX_EDGE_H);
    }

    public void setSelectedIndex(int i) {
        if (isMultiSelect) {
            if (currentIndices.contains(i)) {
                currentIndices.remove(i);
            }
            else {
                currentIndices.add(i);
            }
        }
        else if (!currentIndices.contains(i)) {
            currentIndices.clear();
            currentIndices.add(Math.max(0, Math.min(i, this.rows.size() - 1)));
        }

        updateForSelection(true);
    }

    public void updateForSelection(boolean shouldInvoke) {
        int temp = currentIndices.size() > 0 ? currentIndices.first() : 0;
        if (isMultiSelect) {
            this.button.text = labelFunctionButton != null ? labelFunctionButton.Invoke(GetCurrentItems()) : currentIndices.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
        }
        else if (currentIndices.size() > 0) {
            this.topVisibleRowIndex = Math.min(temp, this.rows.size() - this.visibleRowCount());
            this.button.text = labelFunctionButton != null ? labelFunctionButton.Invoke(GetCurrentItems()) : rows.get(temp).label.text;
            if (colorFunctionButton != null) {
                this.button.SetTextColor(colorFunctionButton.Invoke(GetCurrentItems()));
            }

            this.scrollBar.Scroll(this.scrollPercentForTopVisibleRowIndex(this.topVisibleRowIndex), false);
        }
        if (shouldInvoke && onChange != null) {
            onChange.Invoke(GetCurrentItems());
        }
    }

    public float scrollPercentForTopVisibleRowIndex(int topIndex) {
        int maxRow = this.rows.size() - this.visibleRowCount();
        return (float)topIndex / (float)maxRow;
    }

    boolean shouldShowSlider() {
        return this.rows.size() > this.maxRows;
    }

    public int topVisibleRowIndexForScrollPercent(float percent) {
        int maxRow = this.rows.size() - this.visibleRowCount();
        return (int)((float)maxRow * percent);
    }

    private int visibleRowCount() {
        return Math.min(this.rows.size(), this.maxRows);
    }

    private float yPositionForRowBelow(float originY, int rowIndex) {
        float extraHeight = rowIndex > 0 ? BORDER_SIZE : 0.0F;
        return originY - rowHeight * (float)rowIndex - extraHeight;
    }

    private static class DropdownRow<T> {
        T item;
        AdvancedHitbox hb;
        GUI_Image checkbox;
        GUI_Label label;
        int index;
        boolean isSelected;
        boolean isSmartText;
        boolean isMultiSelect;

        DropdownRow(AdvancedHitbox hb, T item, FuncT1<String, T> labelFunction, BitmapFont font, float fontScale, int index) {
            this.hb = new RelativeHitbox(hb, 1f, 1f, 0f, 0f).SetIsPopupCompatible(true);
            this.item = item;
            this.index = index;
            this.label = new GUI_Label(font, this.hb).SetFont(font, fontScale).SetText(labelFunction.Invoke(item)).SetAlignment(0.5f, 0f, isSmartText);
            this.checkbox = new GUI_Image(ImageMaster.COLOR_TAB_BOX_UNTICKED,  new RelativeHitbox(hb, 48f, 48f, 0f, -TOGGLE_OFFSET, false));
        }

        public DropdownRow<T> SetIsMultiSelect(boolean value) {
            isMultiSelect = value;
            if (isMultiSelect) {
                this.label.SetAlignment(0.5f, 0.22f, isSmartText);
            }
            else {
                this.label.SetAlignment(0.5f, 0.1f, isSmartText);
            }
            return this;
        }

        public DropdownRow<T> SetLabelFunction(FuncT1<String, T> labelFunction, boolean isSmartText) {
            this.isSmartText = isSmartText;
            this.label.SetSmartText(isSmartText);
            this.label.SetText(labelFunction.Invoke(item));
            return this;
        }

        public void move(float x, float y) {
            this.hb.translate(x,y);
            this.checkbox.hb.translate(x,y - TOGGLE_OFFSET);
        }

        private boolean update(boolean isInRange, boolean isSelected) {
            this.hb.update();
            this.isSelected = isSelected;
            if (!isInRange) {
                return false;
            }
            if (this.hb.hovered) {
                this.label.SetColor(Settings.GREEN_TEXT_COLOR);
                if (InputHelper.justClickedLeft) {
                    this.hb.clickStarted = true;
                }
            }
            else if (isSelected) {
                this.label.SetColor(Settings.GOLD_COLOR);
                this.checkbox.SetTexture(ImageMaster.COLOR_TAB_BOX_TICKED);
            }
            else {
                this.label.SetColor(Color.WHITE);
                this.checkbox.SetTexture(ImageMaster.COLOR_TAB_BOX_UNTICKED);
            }

            if (!this.hb.clicked && (!this.hb.hovered || !CInputActionSet.select.isJustPressed())) {
                return false;
            } else {
                this.hb.clicked = false;
                this.checkbox.SetTexture(isSelected ? ImageMaster.COLOR_TAB_BOX_UNTICKED : ImageMaster.COLOR_TAB_BOX_TICKED);
                return true;
            }
        }

        private void renderRow(SpriteBatch sb) {
            this.hb.render(sb);
            this.label.TryRender(sb);
            if (isMultiSelect) {
                this.checkbox.TryRender(sb);
            }
        }
    }
}