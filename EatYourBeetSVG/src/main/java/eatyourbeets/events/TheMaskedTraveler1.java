package eatyourbeets.events;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.Utilities;
import patches.AbstractEnums;

import java.util.ArrayList;

public class TheMaskedTraveler1 extends AnimatorEvent
{
    private static final ArrayList<AbstractCard> replacementStrike = new ArrayList<>();
    private static final ArrayList<AbstractCard> replacementDefend = new ArrayList<>();

    public static final String ID = CreateFullID(TheMaskedTraveler1.class.getSimpleName());

    private boolean completed;
    private AbstractCard toReplace;
    private final int SELLING_PRICE;
    private final int BUYING_PRICE;

    private final int CHOICE_SELL = 0;
    private final int CHOICE_IMPROVE = 1;
    private final int CHOICE_LEAVE = 2;

    public TheMaskedTraveler1()
    {
        super(ID);

        this.completed = false;

        SELLING_PRICE = AbstractDungeon.miscRng.random(30, 50);
        BUYING_PRICE = AbstractDungeon.miscRng.random(85, 100);

        ArrayList<AbstractCard> cards = new ArrayList<>();
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard card : p.masterDeck.group)
        {
            if (card.tags.contains(AbstractEnums.CardTags.IMPROVED_DEFEND) || card.tags.contains(AbstractEnums.CardTags.IMPROVED_STRIKE))
            {
                cards.add(card);
            }
        }

        toReplace = Utilities.GetRandomElement(cards);
        if (toReplace != null)
        {
            this.imageEventText.updateDialogOption(CHOICE_SELL, OPTIONS[0] + toReplace.name + OPTIONS[1] + SELLING_PRICE + OPTIONS[2], toReplace);
        }
        else
        {
            this.imageEventText.updateDialogOption(CHOICE_SELL, OPTIONS[6], true);
        }

        this.imageEventText.updateDialogOption(CHOICE_IMPROVE, OPTIONS[3] + BUYING_PRICE + OPTIONS[4]);
        this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[5]);
    }

    public void onEnterRoom()
    {
//        if (Settings.AMBIANCE_ON)
//        {
//            CardCrawlGame.sound.play("");
//        }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        if (!completed)
        {
            switch (buttonPressed)
            {
                case CHOICE_SELL:
                {
                    this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(OPTIONS[5]);

                    AbstractDungeon.player.masterDeck.removeCard(toReplace);
                    AbstractDungeon.effectList.add(new RainingGoldEffect(SELLING_PRICE));
                    AbstractDungeon.player.gainGold(SELLING_PRICE);

                    completed = true;

                    return;
                }

                case CHOICE_IMPROVE:
                {
                    this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(OPTIONS[5]);

                    SetupCards();

                    ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
                    for (int i = deck.size() - 1; i >= 0; i--)
                    {
                        AbstractCard card = deck.get(i);
                        if (card.tags.contains(BaseModCardTags.BASIC_DEFEND) && !card.tags.contains(AbstractEnums.CardTags.IMPROVED_DEFEND))
                        {
                            deck.remove(i);
                            ObtainCard(replacementDefend, card.upgraded);
                        }
                        else if (card.tags.contains(BaseModCardTags.BASIC_STRIKE) && !card.tags.contains(AbstractEnums.CardTags.IMPROVED_STRIKE))
                        {
                            deck.remove(i);
                            ObtainCard(replacementStrike, card.upgraded);
                        }
                    }

                    completed = true;

                    return;
                }
            }
        }

        this.openMap();
    }

    private static void ObtainCard(ArrayList<AbstractCard> pool, boolean upgraded)
    {
        AbstractCard replacement = Utilities.GetRandomElement(pool).makeCopy();
        if (upgraded)
        {
            replacement.upgrade();
        }
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(replacement, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }

    private static void SetupCards()
    {
        if (replacementStrike.size() > 0)
        {
            return;
        }

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.tags.contains(AbstractEnums.CardTags.IMPROVED_STRIKE))
            {
                replacementStrike.add(c);
            }
            else if (c.tags.contains(AbstractEnums.CardTags.IMPROVED_DEFEND))
            {
                replacementDefend.add(c);
            }
        }
    }
}