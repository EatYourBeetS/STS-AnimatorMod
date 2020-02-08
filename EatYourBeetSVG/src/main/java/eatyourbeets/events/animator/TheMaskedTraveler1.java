package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class TheMaskedTraveler1 extends AnimatorEvent
{
    private static final ArrayList<AbstractCard> replacementStrike = new ArrayList<>();
    private static final ArrayList<AbstractCard> replacementDefend = new ArrayList<>();

    public static final String ID = CreateFullID(TheMaskedTraveler1.class.getSimpleName());

    private AbstractCard toReplace;

    private final int SELLING_PRICE;
    private final int BUYING_PRICE;

    public TheMaskedTraveler1()
    {
        super(ID);

        SELLING_PRICE = AbstractDungeon.miscRng.random(30, 50);
        BUYING_PRICE = AbstractDungeon.miscRng.random(85, 100);

        RegisterPhase(1, this::CreatePhase1, this::HandlePhase1);
        RegisterPhase(2, this::CreatePhase2, this::HandlePhase2);
        ProgressPhase();
    }

    private void CreatePhase1()
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard card : p.masterDeck.group)
        {
            if (card.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND) || card.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE))
            {
                cards.add(card);
            }
        }

        toReplace = JavaUtilities.GetRandomElement(cards);
        if (toReplace != null)
        {
            this.imageEventText.updateDialogOption(0, OPTIONS[0] + toReplace.name + OPTIONS[1] + SELLING_PRICE + OPTIONS[2], toReplace);
        }
        else
        {
            this.imageEventText.updateDialogOption(0, OPTIONS[6], true);
        }

        if (p.gold >= BUYING_PRICE)
        {
            this.imageEventText.updateDialogOption(1, OPTIONS[3] + BUYING_PRICE + OPTIONS[4]);
        }
        else
        {
            this.imageEventText.updateDialogOption(1, OPTIONS[7], true);
        }

        this.imageEventText.updateDialogOption(2, OPTIONS[5]);
    }

    private void HandlePhase1(int button)
    {
        if (button == 0) // SELL
        {
            AbstractDungeon.player.masterDeck.removeCard(toReplace);
            GameEffects.List.Add(new RainingGoldEffect(SELLING_PRICE));
            AbstractDungeon.player.gainGold(SELLING_PRICE);

            ProgressPhase();
        }
        else if (button == 1) // IMPROVE
        {
            AbstractDungeon.player.loseGold(BUYING_PRICE);

            SetupCards();

            RandomizedList<AbstractCard> strikes = new RandomizedList<>(replacementStrike);
            RandomizedList<AbstractCard> defends = new RandomizedList<>(replacementDefend);
            ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
            ArrayList<AbstractCard> strikesToReplace = new ArrayList<>();
            ArrayList<AbstractCard> defendsToReplace = new ArrayList<>();

            for (AbstractCard card : deck)
            {
                if (card.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE) || card.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND))
                {
                    if (card.tags.contains(AbstractCard.CardTags.HEALING))
                    {
                        strikes.GetInnerList().removeIf(c -> c.cardID.equals(card.cardID));
                    }
                }
                else if (card.tags.contains(AbstractCard.CardTags.STARTER_DEFEND))
                {
                    defendsToReplace.add(card);
                }
                else if (card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE))
                {
                    strikesToReplace.add(card);
                }
            }

            for (AbstractCard card : strikesToReplace)
            {
                deck.remove(card);
                ObtainCard(strikes.Retrieve(AbstractDungeon.miscRng), card.upgraded);
            }

            for (AbstractCard card : defendsToReplace)
            {
                deck.remove(card);
                ObtainCard(defends.Retrieve(AbstractDungeon.miscRng), card.upgraded);
            }

            ProgressPhase();
        }
        else
        {
            this.openMap();
        }
    }

    private void CreatePhase2()
    {
        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[5]);
    }

    private void HandlePhase2(int button)
    {
        this.openMap();
    }

    private static void ObtainCard(AbstractCard replacement, boolean upgraded)
    {
        replacement = replacement.makeCopy();

        if (upgraded)
        {
            replacement.upgrade();
        }

        GameEffects.List.Add(new ShowCardAndObtainEffect(replacement, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
    }

    private static void SetupCards()
    {
        if (!replacementStrike.isEmpty())
        {
            return;
        }

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE))
            {
                replacementStrike.add(c);
            }
            else if (c.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND))
            {
                replacementDefend.add(c);
            }
        }
    }
}