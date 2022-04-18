package eatyourbeets.events.replacement;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.DrugDealer;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.special.Khajiit;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AnimatorNest extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(com.megacrit.cardcrawl.events.city.Nest.ID);
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String INTRO_BODY_M;
    private static final String INTRO_BODY_M_2;
    private static final String ACCEPT_BODY;
    private static final String EXIT_BODY;
    private static final float HP_DRAIN = 0.1F;
    private int hpLoss;
    private int goldGain;
    private int screenNum = 0;

    public AnimatorNest()
    {
        super(NAME, INTRO_BODY_M, "images/events/theNest.jpg");
        this.imageEventText.setDialogOption(OPTIONS[3]);

        this.hpLoss = MathUtils.ceil((float) AbstractDungeon.player.maxHealth * HP_DRAIN);

        if (AbstractDungeon.ascensionLevel >= 15)
        {
            this.goldGain = 50;
            this.hpLoss += 2f;
        }
        else
        {
            this.goldGain = 99;
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                AbstractCard c = new Khajiit();
                this.imageEventText.updateBodyText(INTRO_BODY_M_2);
                this.imageEventText.setDialogOption(JUtils.Format(OPTIONS[0], JUtils.ModifyString(c.name, w -> "#g" + w), hpLoss), c);
                UnlockTracker.markCardAsSeen(Khajiit.DATA.ID);
                this.imageEventText.updateDialogOption(0, JUtils.Format(OPTIONS[1], this.goldGain));
                this.screenNum = 1;
                break;
            case 1:
                switch (buttonPressed)
                {
                    case 0:
                        logMetricGainGold("Nest", "Stole From Cult", this.goldGain);
                        this.imageEventText.updateBodyText(EXIT_BODY);
                        this.screenNum = 2;
                        AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldGain));
                        AbstractDungeon.player.gainGold(this.goldGain);
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    case 1:
                        AbstractCard c2 = new Khajiit();
                        logMetricObtainCardAndDamage("Nest", "Joined the Cult", c2, 6);
                        this.imageEventText.updateBodyText(ACCEPT_BODY);
                        AbstractDungeon.player.damage(new DamageInfo(null, hpLoss));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c2, (float) Settings.WIDTH * 0.3F, (float) Settings.HEIGHT / 2.0F));
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    default:
                        return;
                }
            case 2:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    static
    {
        eventStrings = GR.GetEventStrings(ID);
        final EventStrings original = CardCrawlGame.languagePack.getEventString(DrugDealer.ID);
        NAME = original.NAME;
        DESCRIPTIONS = original.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        INTRO_BODY_M = DESCRIPTIONS[0];
        INTRO_BODY_M_2 = DESCRIPTIONS[1];
        ACCEPT_BODY = DESCRIPTIONS[2];
        EXIT_BODY = DESCRIPTIONS[3];
    }
}
