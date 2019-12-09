package eatyourbeets.events.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.blights.animator.Haunted;
import eatyourbeets.effects.CallbackEffect;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.resources.Resources_Common;

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
                AbstractDungeon.effectList.add(new RainingGoldEffect(600));
                AbstractDungeon.effectList.add(new CallbackEffect(new WaitAction(3f), this, this::OnCompletion));

                this.imageEventText.clearAllDialogs();
                this.imageEventText.updateBodyText("");

                AbstractDungeon.player.gainGold(goldAmount);
                AbstractDungeon.scene.fadeOutAmbiance();
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.playTempBgmInstantly(Resources_Common.Audio_TheHaunt, false);

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
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.getCurrRoom().spawnBlightAndObtain(p.hb.cX, p.hb.cY, new Haunted());

        //PlayerStatistics.SaveData.TheHaunt += 1;
        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[5]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }
}