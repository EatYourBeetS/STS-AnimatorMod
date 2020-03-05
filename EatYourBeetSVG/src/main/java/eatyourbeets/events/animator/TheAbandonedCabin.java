package eatyourbeets.events.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

public class TheAbandonedCabin extends EYBEvent
{
    public static final String ID = CreateFullID(TheAbandonedCabin.class);
    public int Medallions = 0;

    public TheAbandonedCabin()
    {
        super(ID, new EventStrings(), "Cabin1.png");

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
            SetText(text.EnteringCabin());
            SetOption(text.ContinueOption());
        }
    }

    private static class EncounteringCreature extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            dialog.loadImage("images/events/Cabin2.png");
            CardCrawlGame.music.playTempBGM(GR.Common.Audio_TheCreature);
            SetText(text.EncounteringCreature());
            SetOption(text.ContinueOption());
        }
    }

    private static class FirstTradeProposal extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        private final int HP_LOSS = GetMaxHP(30);

        @Override
        protected void OnEnter()
        {
            dialog.loadImage("images/events/Cabin2.png");
            CardCrawlGame.music.playTempBGM(GR.Common.Audio_TheCreature);
            SetText(text.FirstTradeProposal());
            SetOption(text.AcceptTradeOption(HP_LOSS)).AddCallback(this::AcceptTrade);
            SetOption(text.RunOption()).AddCallback(this::RanAway);
        }

        private void AcceptTrade()
        {
            CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
            player.damage(new DamageInfo(null, HP_LOSS));
            event.Medallions += 1;
            ProgressPhase();
        }

        private void RanAway()
        {
            ChangePhase(RanAwaySuccessfully.class);
        }
    }

    private static class FirstTradeCompleted extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            SetText(text.FirstTradeCompleted());
            SetOption(text.ContinueOption());
        }
    }

    private static class RanAwaySuccessfully extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            SetText(text.RanAwaySuccessfully());
            SetOption(text.LeaveOption()).AddCallback(this::OpenMap);
        }
    }

    private static class RanAwayDamaged extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            SetText(text.RanAwayDamaged());
            SetOption(text.LeaveOption()).AddCallback(this::OpenMap);
        }
    }

    private static class SecondTradeProposal extends EYBEventPhase<TheAbandonedCabin, EventStrings>
    {
        private final int HP_LOSS_TRADE = GetMaxHP(40);
        private final int HP_LOSS_RUN = GetMaxHP(20);

        @Override
        protected void OnEnter()
        {
            dialog.loadImage("images/events/Cabin2.png");
            CardCrawlGame.music.playTempBGM(GR.Common.Audio_TheCreature);
            SetText(text.SecondTradeProposal());
            SetOption(text.AcceptTradeOption(HP_LOSS_TRADE)).AddCallback(this::AcceptTrade);
            SetOption(text.AttemptToRunOption(HP_LOSS_RUN)).AddCallback(this::AttemptToRun);
        }

        private void AcceptTrade()
        {
            CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
            GameEffects.List.Add(new BorderLongFlashEffect(Color.RED));
            AbstractDungeon.player.damage(new DamageInfo(null, HP_LOSS_TRADE));
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
                CardCrawlGame.sound.play("EVENT_VAMP_BITE", 0.05F);
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
            SetText(text.SecondTradeCompleted());
            SetOption(text.LeaveOption()).AddCallback(this::OpenMap);
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

        public String ContinueOption()
        {
            return GetOption(0);
        }

        public String AcceptTradeOption(int hpLoss)
        {
            return GetOption(1, hpLoss);
        }

        public String RunOption()
        {
            return GetOption(2);
        }

        public String AttemptToRunOption(int hpLoss)
        {
            return GetOption(3, hpLoss);
        }

        public String LeaveOption()
        {
            return GetOption(4);
        }
    }
}