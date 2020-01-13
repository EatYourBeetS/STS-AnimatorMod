package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.utilities.RandomizedList;

@SuppressWarnings("FieldCanBeLocal")
public class TheDomVedeloper1 extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheDomVedeloper1.class.getSimpleName());

    private final RandomizedList<AnimatorCard> cards = new RandomizedList<>();
    private AnimatorCard currentCard = null;

    private int phase;

    private final int CHOICE_CONTINUE = 0;
    private final int CHOICE_ACCEPT = 0;
    private final int CHOICE_SIGN = 0;
    private final int CHOICE_LEAVE = 1;
    private final int CHOICE_TOOSTRONG   = 0;
    private final int CHOICE_VERYSTRONG  = 1;
    private final int CHOICE_STRONG      = 2;
    private final int CHOICE_WEAK        = 3;
    private final int CHOICE_VERYWEAK    = 4;

    public TheDomVedeloper1()
    {
        super(ID);

        Synergies.AddCards(null, AbstractDungeon.srcCommonCardPool.group, cards.GetInnerList());
        Synergies.AddCards(null, AbstractDungeon.srcUncommonCardPool.group, cards.GetInnerList());
        Synergies.AddCards(null, AbstractDungeon.srcRareCardPool.group, cards.GetInnerList());

        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
        this.imageEventText.updateDialogOption(CHOICE_CONTINUE, OPTIONS[8]);
        this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        boolean leave = true;

        if (phase == 0)
        {
            leave = Continue(buttonPressed);
        }
        else if (phase == 1)
        {
            leave = AcceptSurvey(buttonPressed);
        }
        else if (phase <= 5)
        {
            leave = AnswerSurvey(buttonPressed);
        }
        else if (phase == 6)
        {
            leave = SignPaper(buttonPressed);
        }
        else
        {
            this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);
        }

        if (leave)
        {
            this.openMap();
        }
    }

    private boolean Continue(int button)
    {
        if (button == CHOICE_CONTINUE)
        {
            this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);

            this.imageEventText.clearAllDialogs();
            this.imageEventText.updateDialogOption(CHOICE_ACCEPT, OPTIONS[1]);
            this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);

            phase = 1;

            return false;
        }

        return true;
    }

    private boolean AcceptSurvey(int button)
    {
        if (button == CHOICE_ACCEPT)
        {
            AnswerSurvey(-1);

            return false;
        }

        return true;
    }

    private boolean AnswerSurvey(int button)
    {
        phase += 1;

        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[phase]);

        switch (button)
        {
            case CHOICE_TOOSTRONG:
            {
                break;
            }

            case CHOICE_VERYSTRONG:
            {
                break;
            }

            case CHOICE_STRONG:
            {
                break;
            }

            case CHOICE_WEAK:
            {
                break;
            }

            case CHOICE_VERYWEAK:
            {
                break;
            }
        }

        if (phase < 6)
        {
            currentCard = cards.Retrieve(AbstractDungeon.eventRng, true);

            this.imageEventText.clearAllDialogs();
            this.imageEventText.updateDialogOption(CHOICE_TOOSTRONG , OPTIONS[2], currentCard);
            this.imageEventText.updateDialogOption(CHOICE_VERYSTRONG, OPTIONS[3], currentCard);
            this.imageEventText.updateDialogOption(CHOICE_STRONG    , OPTIONS[4], currentCard);
            this.imageEventText.updateDialogOption(CHOICE_WEAK      , OPTIONS[5], currentCard);
            this.imageEventText.updateDialogOption(CHOICE_VERYWEAK  , OPTIONS[6], currentCard);
        }
        else
        {
            this.imageEventText.clearAllDialogs();
            this.imageEventText.updateDialogOption(CHOICE_SIGN, OPTIONS[7]);
            this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);
        }

        return false;
    }

    private boolean SignPaper(int button)
    {
        if (button == CHOICE_SIGN)
        {
            this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[7]);

            this.imageEventText.clearAllDialogs();
            this.imageEventText.setDialogOption(OPTIONS[0]);

            phase += 1;

            return false;
        }

        return true;
    }
}