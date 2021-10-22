package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

public class Orrery extends AnimatorRelic {
    public static final String ID = CreateFullID(Orrery.class);
    private static final int MAX_CARDS = 5;

    private boolean addingCards;
    private int index;
    private boolean awaitingInput;

    public Orrery() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {

        addingCards = true;
    }

    @Override
    public void update()
    {
        super.update();

        if (addingCards)
        {
            if (index >= MAX_CARDS)
            {
                //Ending card add cycle
                addingCards = false;
                awaitingInput = false;
                return;
            }
            else if (awaitingInput && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                //Card chosen
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy();

                if (c != null) {
                    GameEffects.TopLevelList.ShowAndObtain(c);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                awaitingInput = false;
                index++;
            }
            else if (!awaitingInput) {
                //Bring up next card to add
                CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for(int i = 0; i < 3; ++i) {
                    AbstractCard cardToAdd = GR.Common.Dungeon.GetRandomRewardCard(choices.group, true, true);
                    cardToAdd.upgrade();
                    choices.addToRandomSpot(cardToAdd);
                }

                if (choices.size() <= 0)
                {
                    addingCards = false;
                    awaitingInput = false;
                    return;
                }
                else
                {
                    awaitingInput = true;
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
        }
    }

    private String GetGridSelectMessage()
    {
        return DESCRIPTIONS[0].replace("#b","").replace("#y", "");
    }

    public AbstractRelic makeCopy() {
        return new Orrery();
    }
}