package eatyourbeets.events.replacement;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.exordium.Sssserpent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.resources.GR;

public class AnimatorSsssserpent extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(AnimatorSsssserpent.ID);
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String DIALOG_1;
    private static final String AGREE_DIALOG;
    private static final String DISAGREE_DIALOG;
    private static final String GOLD_RAIN_MSG;
    private AnimatorSsssserpent.CUR_SCREEN screen;
    private static final int GOLD_REWARD = 250;
    private static final int A_2_GOLD_REWARD = 230;
    private int goldReward;
    private AbstractCard curse;

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON)
        {
            CardCrawlGame.sound.play("EVENT_SERPENT");
        }
    }

    public AnimatorSsssserpent()
    {
        super(NAME, DIALOG_1, "images/events/liarsGame.jpg");
        this.screen = AnimatorSsssserpent.CUR_SCREEN.INTRO;
        if (AbstractDungeon.ascensionLevel >= 2)
        {
            this.goldReward = A_2_GOLD_REWARD;
        }
        else
        {
            this.goldReward = GOLD_REWARD;
        }

        this.curse = GR.CardLibrary.GetCurrentClassCard(Doubt.ID, false);
        this.imageEventText.setDialogOption(OPTIONS[0] + this.goldReward + OPTIONS[1], curse.makeStatEquivalentCopy());
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screen)
        {
            case INTRO:
                if (buttonPressed == 0)
                {
                    this.imageEventText.updateBodyText(AGREE_DIALOG);
                    this.imageEventText.removeDialogOption(1);
                    this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                    this.screen = AnimatorSsssserpent.CUR_SCREEN.AGREE;
                    AbstractEvent.logMetricGainGoldAndCard("Liars Game", "AGREE", this.curse, this.goldReward);
                }
                else
                {
                    this.imageEventText.updateBodyText(DISAGREE_DIALOG);
                    this.imageEventText.removeDialogOption(1);
                    this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                    this.screen = AnimatorSsssserpent.CUR_SCREEN.DISAGREE;
                    AbstractEvent.logMetricIgnored("Liars Game");
                }
                break;
            case AGREE:
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldReward));
                AbstractDungeon.player.gainGold(this.goldReward);
                this.imageEventText.updateBodyText(GOLD_RAIN_MSG);
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.screen = AnimatorSsssserpent.CUR_SCREEN.COMPLETE;
                break;
            default:
                this.openMap();
        }

    }

    static
    {
        eventStrings = CardCrawlGame.languagePack.getEventString(Sssserpent.ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        DIALOG_1 = DESCRIPTIONS[0];
        AGREE_DIALOG = DESCRIPTIONS[1];
        DISAGREE_DIALOG = DESCRIPTIONS[2];
        GOLD_RAIN_MSG = DESCRIPTIONS[3];
    }

    private enum CUR_SCREEN
    {
        INTRO,
        AGREE,
        DISAGREE,
        COMPLETE
    }
}
