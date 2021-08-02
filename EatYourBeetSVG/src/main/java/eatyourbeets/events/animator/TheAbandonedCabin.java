package eatyourbeets.events.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.effects.SFX;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.utilities.GameEffects;

public class TheAbandonedCabin extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheAbandonedCabin.class);
    public int Medallions = 0;

    public TheAbandonedCabin()
    {
        super(ID, STRINGS, IMAGES.Cabin1.Path());

        this.noCardsInRewards = true;

        RegisterSpecialPhase(new RanAwaySuccessfully());
        RegisterSpecialPhase(new RanAwayDamaged());

        RegisterPhase(0, new EnteringCabin());
        RegisterPhase(1, new EncounteringCreature());
        RegisterPhase(2, new FirstTradeProposal());
        RegisterPhase(3, new FirstTradeCompleted());
        RegisterPhase(4, new SecondTradeProposal());
        RegisterPhase(5, new SecondTradeCompleted());
        ProgressPhase();
    }

    @Override
    public void OpenMap()
    {
        super.OpenMap();

        if (Medallions > 0)
        {
            AbstractRelic relic = new AncientMedallion(Medallions);
            relic.instantObtain();
            Medallions = 0;
        }
    }

    private static class EnteringCabin extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.EnteringCabin());
            AddContinueOption();
        }
    }

    private static class EncounteringCreature extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            dialog.loadImage(IMAGES.Cabin2.Path());
            CardCrawlGame.music.playTempBGM(SFX.ANIMATOR_THE_CREATURE);
            AddText(text.EncounteringCreature());
            AddContinueOption();
        }
    }

    private static class FirstTradeProposal extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        private final int HP_LOSS = GetMaxHP(30);

        @Override
        protected void OnEnter()
        {
            dialog.loadImage(IMAGES.Cabin2.Path());
            CardCrawlGame.music.playTempBGM(SFX.ANIMATOR_THE_CREATURE);
            AddText(text.FirstTradeProposal());
            AddOption(text.AcceptTradeOption(HP_LOSS)).AddCallback(this::AcceptTrade);
            AddPhaseChangeOption(text.RunOption(), RanAwaySuccessfully.class);
        }

        private void AcceptTrade()
        {
            CardCrawlGame.sound.play(SFX.EVENT_VAMP_BITE, 0.05f);
            player.damage(new DamageInfo(null, HP_LOSS));
            event.Medallions += 1;
            ProgressPhase();
        }
    }

    private static class FirstTradeCompleted extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.FirstTradeCompleted());
            AddContinueOption();
        }
    }

    private static class RanAwaySuccessfully extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.RanAwaySuccessfully());
            AddLeaveOption();
        }
    }

    private static class RanAwayDamaged extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.RanAwayDamaged());
            AddLeaveOption();
        }
    }

    private static class SecondTradeProposal extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        private final int HP_LOSS_TRADE = GetMaxHP(40);
        private final int HP_LOSS_RUN = GetMaxHP(20);

        @Override
        protected void OnEnter()
        {
            dialog.loadImage(IMAGES.Cabin2.Path());
            CardCrawlGame.music.playTempBGM(SFX.ANIMATOR_THE_CREATURE);
            AddText(text.SecondTradeProposal());
            AddOption(text.AcceptTradeOption(HP_LOSS_TRADE)).AddCallback(this::AcceptTrade);
            AddOption(text.AttemptToRunOption(HP_LOSS_RUN)).AddCallback(this::AttemptToRun);
        }

        private void AcceptTrade()
        {
            CardCrawlGame.sound.play(SFX.EVENT_VAMP_BITE, 0.05f);
            GameEffects.List.BorderLongFlash(Color.RED);
            player.damage(new DamageInfo(null, HP_LOSS_TRADE));
            event.Medallions += 1;
            ProgressPhase();
        }

        private void AttemptToRun()
        {
            if (RNG.randomBoolean())
            {
                ChangePhase(RanAwaySuccessfully.class);
            }
            else
            {
                CardCrawlGame.sound.play(SFX.EVENT_VAMP_BITE, 0.05f);
                player.damage(new DamageInfo(null, HP_LOSS_RUN));
                ChangePhase(RanAwayDamaged.class);
            }
        }
    }

    private static class SecondTradeCompleted extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.SecondTradeCompleted());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public String EnteringCabin()
        {
            return GetDescription(0);
        }

        public String EncounteringCreature()
        {
            return GetDescription(1);
        }

        public String FirstTradeProposal()
        {
            return GetDescription(2);
        }

        public String RanAwaySuccessfully()
        {
            return GetDescription(3);
        }

        public String FirstTradeCompleted()
        {
            return GetDescription(4);
        }

        public String SecondTradeProposal()
        {
            return GetDescription(5);
        }

        public String RanAwayDamaged()
        {
            return GetDescription(6);
        }

        public String SecondTradeCompleted()
        {
            return GetDescription(7);
        }

        public String AcceptTradeOption(int hpLoss)
        {
            return GetOption(0, hpLoss);
        }

        public String RunOption()
        {
            return GetOption(1);
        }

        public String AttemptToRunOption(int hpLoss)
        {
            return GetOption(2, hpLoss);
        }
    }
}