package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class UnnamedGift extends AnimatorRelic
{
    public static final String ID = CreateFullID(UnnamedGift.class);

    private boolean awaitingInput;

    public UnnamedGift()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.decreaseMaxHealth((int)Math.ceil(player.maxHealth * (1/2f)));
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        RandomizedList<AnimatorCard> ultraRares = new RandomizedList<>();

        for (AnimatorCard_UltraRare ultraRare : AnimatorCard_UltraRare.GetCards().values())
        {
            ultraRares.Add((AnimatorCard) ultraRare);
        }

        if (ultraRares.Size() < 2)
        {
            return;
        }

        for(int i = 0; i < 2; ++i) {
            group.addToRandomSpot(ultraRares.Retrieve(rng));
        }

        if (group.size() > 0)
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
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), false, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), false, false, false, false);
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
