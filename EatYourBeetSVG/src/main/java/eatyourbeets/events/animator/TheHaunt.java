package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.blights.animator.Haunted;
import eatyourbeets.effects.SFX;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.utilities.GameEffects;

public class TheHaunt extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheHaunt.class);

    public TheHaunt()
    {
        super(ID, STRINGS, "GoldRiver.png");

        RegisterSpecialPhase(new Healed());
        RegisterSpecialPhase(new Blighted());

        RegisterPhase(0, new MainPhase());
        ProgressPhase();
    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON)
        {
            CardCrawlGame.music.playTempBGM("SHRINE");
        }
    }

    private static class MainPhase extends EYBEventPhase<TheHaunt, EventStrings>
    {
        private final int GOLD_AMOUNT = RNG.random(222, 266);
        private final int HEAL_AMOUNT = GetMaxHP(12);

        @Override
        protected void OnEnter()
        {
            AddText(text.MainPhase());
            AddOption(text.GoldOption(GOLD_AMOUNT)).AddCallback(this::TakeGold);
            AddOption(text.HealOption(HEAL_AMOUNT)).AddCallback(this::Heal);
        }

        private void TakeGold()
        {
            GameEffects.List.Add(new RainingGoldEffect(600));
            GameEffects.List.Callback(new WaitAction(3f), this::Blighted);

            AddText("", true);

            player.gainGold(GOLD_AMOUNT);
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.silenceTempBgmInstantly();
            CardCrawlGame.music.playTempBgmInstantly(SFX.ANIMATOR_THE_HAUNT, false);
        }

        private void Blighted()
        {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(player.hb.cX, player.hb.cY, new Haunted());
            ChangePhase(Blighted.class);
        }

        private void Heal()
        {
            player.heal(HEAL_AMOUNT);
            ChangePhase(Healed.class);
        }
    }

    private static class Healed extends EYBEventPhase<TheHaunt, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText("", true);
            AddLeaveOption();
        }
    }

    private static class Blighted extends EYBEventPhase<TheHaunt, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.ObtainedBlight());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public final String MainPhase()
        {
            return GetDescription(0);
        }

        public final String ObtainedBlight()
        {
            return GetDescription(1);
        }

        public final String GoldOption(int gold)
        {
            return GetOption(0, gold);
        }

        public final String HealOption(int heal)
        {
            return GetOption(1, heal);
        }
    }
}