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
            if (card.tags.contains(AbstractEnums.CardTags.IMPROVED_DEFEND) || card.tags.contains(AbstractEnums.CardTags.IMPROVED_STRIKE))
            {
                cards.add(card);
            }
        }

        toReplace = Utilities.GetRandomElement(cards);
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
            AbstractDungeon.effectList.add(new RainingGoldEffect(SELLING_PRICE));
            AbstractDungeon.player.gainGold(SELLING_PRICE);

            ProgressPhase();
        }
        else if (button == 1) // IMPROVE
        {
            AbstractDungeon.player.loseGold(BUYING_PRICE);

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

    private static void ObtainCard(ArrayList<AbstractCard> pool, boolean upgraded)
    {
        AbstractCard replacement = Utilities.GetRandomElement(pool).makeCopy();
        if (upgraded)
        {
            replacement.upgrade();
        }
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(replacement, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
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