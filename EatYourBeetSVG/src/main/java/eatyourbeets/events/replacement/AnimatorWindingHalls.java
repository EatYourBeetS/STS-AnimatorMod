package eatyourbeets.events.replacement;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.beyond.WindingHalls;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class AnimatorWindingHalls extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(WindingHalls.ID);
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    // TODO: Localize
    private final static String MadnessMessage_1 = "[Embrace Madness] #gReceive Higaki Rinne. #rLose #r";
    private final static String MadnessMessage_2 = " #rHP.";

    private static final float MAX_HP_LOSS_AMOUNT = 0.05f;
    private static final float HP_LOSS_AMOUNT = 0.125f;
    private static final float HP_LOSS_AMOUNT_A15 = 0.16f; // 0.18f
    private static final float HEAL_AMOUNT = 0.25f;
    private static final float HEAL_AMOUNT_A15 = 0.2f;
    private static final EventStrings eventStrings;
    private static final String INTRO_BODY1;
    private static final String INTRO_BODY2;
    private static final String CHOICE_1_TEXT;
    private static final String CHOICE_2_TEXT;

    private int screenNum = 0;
    private int hpAmt;
    private int healAmt;
    private int maxHPAmt;

    public AnimatorWindingHalls()
    {
        super(NAME, INTRO_BODY1, "images/events/winding.jpg");

        if (AbstractDungeon.ascensionLevel >= 15)
        {
            this.hpAmt = MathUtils.round((float) player.maxHealth * HP_LOSS_AMOUNT_A15);
            this.healAmt = MathUtils.round((float) player.maxHealth * HEAL_AMOUNT_A15);
        }
        else
        {
            this.hpAmt = MathUtils.round((float) player.maxHealth * HP_LOSS_AMOUNT);
            this.healAmt = MathUtils.round((float) player.maxHealth * HEAL_AMOUNT);
        }

        this.maxHPAmt = MathUtils.round((float) player.maxHealth * MAX_HP_LOSS_AMOUNT);
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON)
        {
            CardCrawlGame.sound.play("EVENT_WINDING");
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                this.imageEventText.updateBodyText(INTRO_BODY2);
                this.screenNum = 1;
                this.imageEventText.updateDialogOption(0, MadnessMessage_1 + this.hpAmt + MadnessMessage_2, new HigakiRinne());
                this.imageEventText.setDialogOption(OPTIONS[3] + this.healAmt + OPTIONS[5], CardLibrary.getCopy("Writhe"));
                this.imageEventText.setDialogOption(OPTIONS[6] + this.maxHPAmt + OPTIONS[7]);
                break;
            case 1:
                switch (buttonPressed)
                {
                    case 0:
                        List<String> cards = new ArrayList<>();
                        cards.add(HigakiRinne.DATA.ID);
                        logMetric("Winding Halls", "Embrace Madness", cards, null, null, null, null, null, null, this.hpAmt, 0, 0, 0, 0, 0);
                        this.imageEventText.updateBodyText(CHOICE_1_TEXT);
                        player.damage(new DamageInfo(null, this.hpAmt));
                        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
                        GameEffects.List.Add(new ShowCardAndObtainEffect(new HigakiRinne(), (float) Settings.WIDTH / 2f - 350f * Settings.scale, (float) Settings.HEIGHT / 2f));
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    case 1:
                        this.imageEventText.updateBodyText(CHOICE_2_TEXT);
                        player.heal(this.healAmt);
                        AbstractCard c = new Writhe();
                        GameEffects.List.Add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2f + 10f * Settings.scale, (float) Settings.HEIGHT / 2f));
                        logMetricObtainCardAndHeal("Winding Halls", "Writhe", c, this.healAmt);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    case 2:
                        this.screenNum = 2;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        logMetricMaxHPLoss("Winding Halls", "Max HP", this.maxHPAmt);
                        player.decreaseMaxHealth(this.maxHPAmt);
                        CardCrawlGame.screenShake.shake(ShakeIntensity.LOW, ShakeDur.SHORT, true);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    default:
                        return;
                }
            default:
                this.openMap();
        }
    }

    static
    {
        eventStrings = CardCrawlGame.languagePack.getEventString(com.megacrit.cardcrawl.events.beyond.WindingHalls.ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        INTRO_BODY1 = DESCRIPTIONS[0];
        INTRO_BODY2 = DESCRIPTIONS[1];
        CHOICE_1_TEXT = DESCRIPTIONS[2];
        CHOICE_2_TEXT = DESCRIPTIONS[3];
    }
}
