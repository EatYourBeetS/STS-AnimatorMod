package eatyourbeets.events.replacement;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.ForgottenAltar;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodyIdol;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.GoldenIdol;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AnimatorForgottenAltar extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(ForgottenAltar.ID);
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String DIALOG_1;
    private static final String DIALOG_2;
    private static final String DIALOG_3;
    private static final String DIALOG_4;
    private static final float HP_LOSS_PERCENT = 0.25F;
    private static final float A_2_HP_LOSS_PERCENT = 0.35F;
    private static final int MAX_HP_GAIN = 5;
    private int hpLoss;
    private int goldAmount;
    private AbstractCard curse;

    public AnimatorForgottenAltar()
    {
        super(NAME, DIALOG_1, "images/events/forgottenAltar.jpg");
        if (AbstractDungeon.player.hasRelic("Golden Idol"))
        {
            this.imageEventText.setDialogOption(OPTIONS[0], false, new BloodyIdol());
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[1], true, new BloodyIdol());
        }

        if (AbstractDungeon.ascensionLevel >= 15)
        {
            this.hpLoss = MathUtils.round((float) AbstractDungeon.player.maxHealth * 0.35F);
            this.goldAmount = 120;
        }
        else
        {
            this.hpLoss = MathUtils.round((float) AbstractDungeon.player.maxHealth * 0.25F);
            this.goldAmount = 150;
        }

        this.curse = CardLibrary.getCopy(Decay.ID);
        this.imageEventText.setDialogOption(JUtils.Format(OPTIONS[2], MAX_HP_GAIN, hpLoss));
        this.imageEventText.setDialogOption(JUtils.Format(OPTIONS[4], curse.name, goldAmount), curse);
    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON)
        {
            CardCrawlGame.sound.play("EVENT_FORGOTTEN");
        }
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                switch (buttonPressed)
                {
                    case 0:
                        this.gainChalice();
                        this.showProceedScreen(DIALOG_2);
                        CardCrawlGame.sound.play("HEAL_1");
                        return;
                    case 1:
                        AbstractDungeon.player.increaseMaxHp(5, false);
                        AbstractDungeon.player.damage(new DamageInfo(null, this.hpLoss));
                        CardCrawlGame.sound.play("HEAL_3");
                        this.showProceedScreen(DIALOG_3);
                        logMetricDamageAndMaxHPGain("Forgotten Altar", "Shed Blood", this.hpLoss, 5);
                        return;
                    case 2:
                        CardCrawlGame.sound.play("BLUNT_HEAVY");
                        CardCrawlGame.screenShake.shake(ShakeIntensity.HIGH, ShakeDur.MED, true);
                        AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldAmount));
                        AbstractDungeon.player.gainGold(this.goldAmount);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                        this.showProceedScreen(DIALOG_4);
                        logMetricObtainCard("Forgotten Altar", "Smashed Altar", curse);
                        return;
                    default:
                        return;
                }
            default:
                this.openMap();
        }
    }

    public void gainChalice()
    {
        int relicAtIndex = 0;

        for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i)
        {
            if (AbstractDungeon.player.relics.get(i).relicId.equals("Golden Idol"))
            {
                relicAtIndex = i;
                break;
            }
        }

        if (AbstractDungeon.player.hasRelic("Bloody Idol"))
        {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), RelicLibrary.getRelic("Circlet").makeCopy());
            logMetricRelicSwap("Forgotten Altar", "Gave Idol", new Circlet(), new GoldenIdol());
        }
        else
        {
            AbstractDungeon.player.relics.get(relicAtIndex).onUnequip();
            AbstractRelic bloodyIdol = RelicLibrary.getRelic("Bloody Idol").makeCopy();
            bloodyIdol.instantObtain(AbstractDungeon.player, relicAtIndex, false);
            logMetricRelicSwap("Forgotten Altar", "Gave Idol", new BloodyIdol(), new GoldenIdol());
        }
    }

    static
    {
        eventStrings = GR.GetEventStrings(ID);
        final EventStrings original = CardCrawlGame.languagePack.getEventString(ForgottenAltar.ID);
        NAME = original.NAME;
        DESCRIPTIONS = original.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        DIALOG_1 = DESCRIPTIONS[0];
        DIALOG_2 = DESCRIPTIONS[1];
        DIALOG_3 = DESCRIPTIONS[2];
        DIALOG_4 = DESCRIPTIONS[3];
    }
}
