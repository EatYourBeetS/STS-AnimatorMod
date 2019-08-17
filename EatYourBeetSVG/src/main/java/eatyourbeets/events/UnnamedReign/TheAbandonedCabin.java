package eatyourbeets.events.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.resources.Resources_Common;

public class TheAbandonedCabin extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheAbandonedCabin.class.getSimpleName());

    private static final AncientMedallion medallion = new AncientMedallion();

    private final int HP_TRADE_1;
    private final int HP_TRADE_2;
    private final int RUN_DAMAGE;

    private int medallions = 0;

    public TheAbandonedCabin()
    {
        super(ID, "Cabin1.png");

        this.noCardsInRewards = true;

        HP_TRADE_1 = CalculateDamage(30);
        HP_TRADE_2 = CalculateDamage(40);
        RUN_DAMAGE = CalculateDamage(20);

        RegisterPhase(-1, this::CreateSpecialPhase, this::HandleSpecialPhase);
        RegisterPhase(1, this::CreatePhase1, this::HandlePhase1);
        RegisterPhase(2, this::CreatePhase2, this::HandlePhase2);
        RegisterPhase(3, this::CreatePhase3, this::HandlePhase3);
        RegisterPhase(4, this::CreatePhase4, this::HandlePhase4);
        RegisterPhase(5, this::CreatePhase5, this::HandlePhase5);
        RegisterPhase(6, this::CreatePhase6, this::HandlePhase6);
        ProgressPhase();
    }

    private void CreatePhase1() // Enter cabin
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[0], true);
        UpdateDialogOption(0, OPTIONS[0]); // Continue
    }

    private void HandlePhase1(int button)
    {
        ProgressPhase();
    }

    private void CreatePhase2() // Encounter Creature
    {
        this.imageEventText.loadImage("images/events/Cabin2.png");
        CardCrawlGame.music.playTempBGM(Resources_Common.Audio_TheCreature);
        UpdateBodyText(eventStrings.DESCRIPTIONS[1], true);
        UpdateDialogOption(0, OPTIONS[0]); // Continue
    }

    private void HandlePhase2(int button)
    {
        ProgressPhase();
    }

    private void CreatePhase3() // Evaluate Trade
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        UpdateDialogOption(0, OPTIONS[1].replace("{0}", String.valueOf(HP_TRADE_1)), medallion); // Take damage, Obtain Medallion
        UpdateDialogOption(1, OPTIONS[4]); // Leave
    }

    private void HandlePhase3(int button)
    {
        if (button == 0)
        {
            CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
            AbstractDungeon.player.damage(new DamageInfo(null, HP_TRADE_1));
            medallions += 1;

            ProgressPhase();
        }
        else
        {
            UpdateBodyText(eventStrings.DESCRIPTIONS[3], true);
            UpdateDialogOption(0, OPTIONS[4]); // Leave
            ProgressPhase(-1);
        }
    }

    private void CreatePhase4() // Took damage
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[4], true);
        UpdateDialogOption(0, OPTIONS[0]); // Continue
    }

    private void HandlePhase4(int button)
    {
        ProgressPhase();
    }

    private void CreatePhase5() // Evaluate second trade
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[5], true);
        UpdateDialogOption(0, OPTIONS[1].replace("{0}", String.valueOf(HP_TRADE_2)), medallion); // Take damage, Obtain Medallion
        UpdateDialogOption(1, OPTIONS[3].replace("{0}", String.valueOf(RUN_DAMAGE))); // Try to run
    }

    private void HandlePhase5(int button)
    {
        if (button == 0)
        {
            CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
            AbstractDungeon.effectList.add(new BorderLongFlashEffect(Color.RED));
            AbstractDungeon.player.damage(new DamageInfo(null, HP_TRADE_2));
            medallions += 1;

            ProgressPhase();
        }
        else
        {
            if (AbstractDungeon.miscRng.randomBoolean())
            {
                UpdateBodyText(eventStrings.DESCRIPTIONS[3], true);
            }
            else
            {
                UpdateBodyText(eventStrings.DESCRIPTIONS[6], true);
                CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
                AbstractDungeon.player.damage(new DamageInfo(null, RUN_DAMAGE));
            }

            UpdateDialogOption(0, OPTIONS[4]); // Leave

            ProgressPhase(-1);
        }
    }

    private void CreatePhase6() // Took damage again
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[7], true);
        UpdateDialogOption(0, OPTIONS[4]); // Leave
        ProgressPhase(-1);
    }

    private void HandlePhase6(int button)
    {
        ProgressPhase(-1);
    }

    private void CreateSpecialPhase()
    {

    }

    private void HandleSpecialPhase(int button)
    {
        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MAP;
        this.openMap();

        if (medallions > 0)
        {
            AbstractRelic relic = new AncientMedallion(medallions);
            relic.instantObtain();
            medallions = 0;
        }
    }

    private static int CalculateDamage(int percentage)
    {
        return (int)Math.ceil(AbstractDungeon.player.maxHealth * percentage / 100.0);
    }
}