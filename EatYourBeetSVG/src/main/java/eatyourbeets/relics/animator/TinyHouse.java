package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class TinyHouse extends AnimatorRelic {
    public static final String ID = CreateFullID(TinyHouse.class);
    private static final int MAX_CARDS = 1;

    private boolean addingCards;
    private int index;
    private boolean awaitingInput;

    public TinyHouse() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        GameEffects.TopLevelQueue.Add(new ObtainPotionEffect(PotionHelper.getRandomPotion(rng)));
        player.gainGold(50);
        player.increaseMaxHp(5, true);
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
                //Ending card add cycle and upgrading random card
                addingCards = false;
                awaitingInput = false;

                RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
                for (AbstractCard c : player.masterDeck.group)
                {
                    if (c.canUpgrade()) {
                        upgradableCards.Add(c);
                    }
                }

                if (upgradableCards.Size() > 0)
                {
                    AbstractCard card = upgradableCards.Retrieve(rng);
                    card.upgrade();
                    GameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy());
                }

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
        return new TinyHouse();
    }
}