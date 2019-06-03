package eatyourbeets.events.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.AnimatorResources_Audio;
import eatyourbeets.effects.CallbackEffect;
import eatyourbeets.effects.SequentialEffect;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.powers.PlayerStatistics;

public class TheHaunt extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheHaunt.class.getSimpleName());

    private boolean completed;

    private final int goldAmount;
    private final int healAmount;
    private final int CHOICE_TAKE_GOLD = 0;

    public TheHaunt()
    {
        super(ID, "GoldRiver.png");

        this.completed = false;

        this.goldAmount = 222;
        this.healAmount = 12;

        String[] desc = eventStrings.DESCRIPTIONS;
        this.body = desc[0] + desc[1] + desc[2] + desc[3] + desc[4];

        UpdateDialogOption(CHOICE_TAKE_GOLD, OPTIONS[0].replace("{0}", String.valueOf(goldAmount)));
        UpdateDialogOption(1, OPTIONS[1].replace("{0}", String.valueOf(healAmount)));
    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON)
        {
            CardCrawlGame.music.playTempBGM("SHRINE");
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        if (!completed)
        {
            if (buttonPressed == CHOICE_TAKE_GOLD)
            {
                SequentialEffect effect = new SequentialEffect();
                effect.Enqueue(new RainingGoldEffect(1000));
                effect.Enqueue(new CallbackEffect(new WaitAction(0.5f), this::OnCompletion, this));
                AbstractDungeon.effectList.add(effect);

                this.imageEventText.clearAllDialogs();
                this.imageEventText.updateBodyText("");

                AbstractDungeon.player.gainGold(goldAmount);
                AbstractDungeon.scene.fadeOutAmbiance();
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.playTempBgmInstantly(AnimatorResources_Audio.TheHaunt, false);

                completed = true;
                return;
            }
            else
            {
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[2]);
                this.imageEventText.updateBodyText("");

                AbstractDungeon.player.heal(healAmount);
                completed = true;

                this.openMap();
                return;
            }
        }

        this.openMap();
    }

    private void OnCompletion(Object state, AbstractGameAction action)
    {
        PlayerStatistics.SaveData.TheHaunt += 1;
        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[5]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }
}