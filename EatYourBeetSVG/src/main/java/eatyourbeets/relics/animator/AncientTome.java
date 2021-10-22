package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

public class AncientTome extends AnimatorRelic
{
    public static final String ID = CreateFullID(AncientTome.class);

    private boolean awaitingInput;

    public AncientTome()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(int i = 0; i < 20; ++i) {
            AbstractCard cardToAdd = GR.Common.Dungeon.GetRandomRewardCard(choices.group, true, true);
            cardToAdd.upgrade();
            choices.addToRandomSpot(cardToAdd);
        }

        if (choices.size() > 0)
        {
            awaitingInput = true;
        }
        else
        {
            awaitingInput = false;
            return;
        }

        if (!AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.gridSelectScreen.open(choices, 1, GetGridSelectMessage(), false, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(choices, 1, GetGridSelectMessage(), false, false, false, false);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (awaitingInput)
        {
            if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();

                if (c != null) {
                    GameEffects.TopLevelList.ShowAndObtain(c);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                awaitingInput = false;
            }
        }
    }

    private String GetGridSelectMessage()
    {
        return DESCRIPTIONS[0].replace("#b","").replace("#y", "");
    }
}