//package eatyourbeets.events;
//
//import com.megacrit.cardcrawl.cards.curses.Writhe;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.events.AbstractImageEvent;
//import com.megacrit.cardcrawl.helpers.CardLibrary;
//import com.megacrit.cardcrawl.localization.EventStrings;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
//
//public class TheMausoleumAnimator extends AbstractImageEvent
//{
//    public static final String ID = "The Mausoleum";
//    private static final EventStrings eventStrings;
//    public static final String NAME;
//    public static final String[] DESCRIPTIONS;
//    public static final String[] OPTIONS;
//    private static final String DIALOG_1;
//    private static final String CURSED_RESULT;
//    private static final String NORMAL_RESULT;
//    private static final String NOPE_RESULT;
//    private TheMausoleumAnimator.CurScreen screen;
//    private static final int PERCENT = 50;
//    private static final int A_2_PERCENT = 100;
//    private int percent;
//
//    public TheMausoleumAnimator()
//    {
//        super(NAME, DIALOG_1, "images/events/mausoleum.jpg");
//        this.screen = TheMausoleumAnimator.CurScreen.INTRO;
//        if (AbstractDungeon.ascensionLevel >= 15)
//        {
//            this.percent = 100;
//        }
//        else
//        {
//            this.percent = 50;
//        }
//
//        this.imageEventText.setDialogOption(OPTIONS[0] + this.percent + OPTIONS[1], CardLibrary.getCopy("Writhe"));
//        this.imageEventText.setDialogOption(OPTIONS[2]);
//    }
//
//    public void onEnterRoom()
//    {
//        if (Settings.AMBIANCE_ON)
//        {
//            CardCrawlGame.sound.play("EVENT_GHOSTS");
//        }
//
//    }
//
//    public void update()
//    {
//        super.update();
//    }
//
//    protected void buttonEffect(int buttonPressed)
//    {
//        switch (this.screen)
//        {
//            case INTRO:
//                switch (buttonPressed)
//                {
//                    case 0:
//                        boolean result = AbstractDungeon.miscRng.randomBoolean();
//                        if (AbstractDungeon.ascensionLevel >= 15)
//                        {
//                            result = true;
//                        }
//
//                        if (result)
//                        {
//                            this.imageEventText.updateBodyText(CURSED_RESULT);
//                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Writhe(), (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
//                        }
//                        else
//                        {
//                            this.imageEventText.updateBodyText(NORMAL_RESULT);
//                        }
//
//                        CardCrawlGame.sound.play("BLUNT_HEAVY");
//                        CardCrawlGame.screenShake.rumble(2.0F);
//                        AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
//                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
//                        if (result)
//                        {
//                            logMetricObtainCardAndRelic("The Mausoleum", "Opened", new Writhe(), r);
//                        }
//                        else
//                        {
//                            logMetricObtainRelic("The Mausoleum", "Opened", r);
//                        }
//                        break;
//                    default:
//                        this.imageEventText.updateBodyText(NOPE_RESULT);
//                        logMetricIgnored("The Mausoleum");
//                }
//
//                this.imageEventText.clearAllDialogs();
//                this.imageEventText.setDialogOption(OPTIONS[2]);
//                this.screen = TheMausoleumAnimator.CurScreen.RESULT;
//                break;
//            default:
//                this.openMap();
//        }
//
//    }
//
//    static
//    {
//        eventStrings = CardCrawlGame.languagePack.getEventString("The Mausoleum");
//        NAME = eventStrings.NAME;
//        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
//        OPTIONS = eventStrings.OPTIONS;
//        DIALOG_1 = DESCRIPTIONS[0];
//        CURSED_RESULT = DESCRIPTIONS[1];
//        NORMAL_RESULT = DESCRIPTIONS[2];
//        NOPE_RESULT = DESCRIPTIONS[3];
//    }
//
//    private enum CurScreen
//    {
//        INTRO,
//        RESULT
//    }
//}