package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButton;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardSeriesComparator;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import patches.screens.CardLibraryScreenPatches;

import java.util.Collections;

public class CustomCardLibSortHeader extends CardLibSortHeader
{
    public static CardLibraryScreen Screen;
    public static CustomCardLibSortHeader Instance;
    public static boolean ShowSpecial = GR.TEST_MODE;
    public CardGroup originalGroup;
    public FuncT1<Boolean, AbstractCard> filter;
    public boolean isColorless;

    private SortHeaderButton[] override = null;
    private SortHeaderButton seriesButton = null;
    private SortHeaderButton rarityButton;
    private SortHeaderButton typeButton;
    private SortHeaderButton nameButton;
    private SortHeaderButton costButton;

    public CustomCardLibSortHeader(CardGroup group)
    {
        super(group);

        originalGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Instance = this;
    }

    public void SetupButtons(boolean isColorless)
    {
        if (override == null)
        {
            final float START_X = 430f * Settings.scale;
            final float SPACE_X = (226f * Settings.scale);
            final float xPosition = START_X + (SPACE_X * buttons.length);

            override = new SortHeaderButton[buttons.length + 1];

            rarityButton = buttons[0];
            typeButton = buttons[1];
            costButton = buttons[2];

            if (Settings.removeAtoZSort)
            {
                nameButton = null;
            }
            else
            {
                nameButton = buttons[3];
            }

            seriesButton = new SortHeaderButton(CardSeries.GetLocalizedSeriesString(), xPosition, 0f, this);

            float offsetX = -(Settings.scale * 30f);

            SetupButton(rarityButton, offsetX, 0);
            SetupButton(typeButton, offsetX, 1);
            SetupButton(costButton, offsetX, 2);

            if (nameButton != null)
            {
                SetupButton(nameButton, offsetX, 3);
                SetupButton(seriesButton, offsetX, 4);
            }
            else
            {
                SetupButton(seriesButton, offsetX, 3);
            }
        }

        this.isColorless = isColorless;
        this.buttons = override;
    }

    private void SetupButton(SortHeaderButton button, float offsetX, int index)
    {
        override[index] = button;

        final Hitbox hitbox = button.hb;
        hitbox.resize(hitbox.width + offsetX, hitbox.height);
        hitbox.move(hitbox.cX + (offsetX * index), hitbox.cY);
    }

    @Override
    public void setGroup(CardGroup group)
    {
        if (isColorless)
        {
            this.originalGroup = group;
            super.setGroup(group);
            return;
        }

        this.originalGroup = group;
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.isSeen && c.color == AbstractCard.CardColor.COLORLESS && !originalGroup.contains(c))
            {
                this.originalGroup.group.add(c);
            }
        }
        for (AnimatorCard_UltraRare c : AnimatorCard_UltraRare.GetCards().values())
        {
            if (AnimatorCard_UltraRare.IsSeen(c.cardID) && !originalGroup.contains(c))
            {
                this.originalGroup.group.add(c);
            }
        }

        this.group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        this.group.group.addAll(originalGroup.group);
        ApplyFilter(null);
        super.setGroup(this.group);
    }

    @Override
    public void didChangeOrder(SortHeaderButton button, boolean isAscending)
    {
        if (button == this.rarityButton)
        {
            this.originalGroup.sortByRarity(isAscending);
        }
        else if (button == this.typeButton)
        {
            this.originalGroup.sortByType(isAscending);
        }
        else if (button == this.costButton)
        {
            this.originalGroup.sortByCost(isAscending);
        }
        else if (button == this.nameButton)
        {
            this.originalGroup.sortAlphabetically(isAscending);
        }
        else if (button == this.seriesButton)
        {
            if (!isAscending)
            {
                this.originalGroup.group.sort(new CardSeriesComparator());
            }
            else
            {
                this.originalGroup.group.sort(Collections.reverseOrder(new CardSeriesComparator()));
            }
        }
        else
        {
            return;
        }

        this.originalGroup.sortByStatus(false);
        this.justSorted = true;
        button.setActive(true);
        ApplyFilter(filter);
    }

    public void ApplyFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        if (isColorless)
        {
            return;
        }

        group.clear();

        this.filter = filter;
        for (AbstractCard c : originalGroup.group)
        {
            if (filter != null)
            {
                if (filter.Invoke(c))
                {
                    group.group.add(c);
                }
            }
            else
            {
                if (c instanceof AnimatorCard_UltraRare || (c.color != AbstractCard.CardColor.COLORLESS && (ShowSpecial || c.rarity != AbstractCard.CardRarity.SPECIAL)))
                {
                    group.group.add(c);
                }
            }
        }

        if (group.isEmpty())
        {
            group.group.add(new QuestionMark());
        }

        if (Screen != null)
        {
            CardLibraryScreenPatches.CalculateScrollBounds.Invoke(Screen);
            CardLibraryScreenPatches.VisibleCards.Set(Screen, group);
        }
    }
}
