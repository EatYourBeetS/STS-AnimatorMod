package eatyourbeets.ui;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButton;
import eatyourbeets.cards.*;

import java.util.Collections;

public class CustomCardLibSortHeader extends CardLibSortHeader
{
    private SortHeaderButton[] override = null;
    private SortHeaderButton seriesButton = null;
    private SortHeaderButton rarityButton;
    private SortHeaderButton typeButton;
    private SortHeaderButton nameButton;
    private SortHeaderButton costButton;

    public CustomCardLibSortHeader(CardGroup group)
    {
        super(group);
    }

    public void SetupButtons()
    {
        if (override == null)
        {
            final float START_X = 430.0F * Settings.scale;
            final float SPACE_X = (226.0F * Settings.scale);
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

            seriesButton = new SortHeaderButton("Series", xPosition, 0.0F, this);

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

        buttons = override;
    }

    private void SetupButton(SortHeaderButton button, float offsetX, int index)
    {
        override[index] = button;

        Hitbox hitbox = button.hb;
        hitbox.resize(hitbox.width + offsetX, hitbox.height);
        hitbox.move(hitbox.cX + (offsetX * index), hitbox.cY);
    }

    @Override
    public void setGroup(CardGroup group)
    {
        super.setGroup(group);

        for (AnimatorCard_UltraRare card : AnimatorCard_UltraRare.GetCards().values())
        {
            if (AnimatorCard_UltraRare.IsSeen(card.cardID))
            {
                if (!group.contains(card))
                {
                    group.group.add(card);
                }
            }
        }
    }

    @Override
    public void didChangeOrder(SortHeaderButton button, boolean isAscending)
    {
        if (button == this.rarityButton)
        {
            this.group.sortByRarity(isAscending);
        }
        else if (button == this.typeButton)
        {
            this.group.sortByType(isAscending);
        }
        else if (button == this.costButton)
        {
            this.group.sortByCost(isAscending);
        }
        else if (button == this.nameButton)
        {
            this.group.sortAlphabetically(isAscending);
        }
        else if (button == this.seriesButton)
        {
            if (!isAscending)
            {
                this.group.group.sort(new CardSeriesComparator());
            }
            else
            {
                this.group.group.sort(Collections.reverseOrder(new CardSeriesComparator()));
            }
        }
        else
        {
            return;
        }

        this.group.sortByStatus(false);
        this.justSorted = true;
        button.setActive(true);
    }
}
