package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.beta.special.Ganyu;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class TheMysteriousPeak extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheMysteriousPeak.class);

    public TheMysteriousPeak()
    {
        super(ID, new EventStrings(),IMAGES.QingyunPeak.Path());

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Hunt());
        RegisterPhase(2, new Fight1());
        RegisterPhase(3, new Fight2());
        RegisterPhase(4, new Trap());
        RegisterPhase(5, new Conclusion1());
        RegisterPhase(6, new Conclusion2());
        RegisterPhase(7, new Conclusion3());
        RegisterPhase(8, new Flee());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Hunt extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        private final AbstractCard obtainedCard = new Ganyu();
        private final int HP_LOSS = 40;

        @Override
        protected void OnEnter()
        {
            RandomizedList<AbstractRelic> relics = new RandomizedList<>();

            for (AbstractRelic r : player.relics)
            {
                if (r.tier == AbstractRelic.RelicTier.UNCOMMON)
                {
                    relics.Add(r);
                }
            }
            AbstractRelic relic = relics.Retrieve(RNG);
            if (relic == null) {
                for (AbstractRelic r : player.relics)
                {
                    if (r.tier == AbstractRelic.RelicTier.RARE)
                    {
                        relics.Add(r);
                    }
                }
                relic = relics.Retrieve(RNG);
            }

            AddText(text.Hunt());
            AddOption(text.FightOption(HP_LOSS), obtainedCard).AddCallback(this::LoseHealth);
            if (relic != null)
            {
                AddOption(text.TrapOption(relic.name), relic).AddCallback(this::LoseRelic);
            }
            else
            {
                AddOption(text.TrapLockedOption()).SetDisabled(true);
            }

            AddOption(text.FleeOption()).AddCallback(() -> ChangePhase(Flee.class));
        }

        private void LoseHealth()
        {
            player.damage(new DamageInfo(null, HP_LOSS));
            GameEffects.List.Add(new ShowCardAndObtainEffect(obtainedCard, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2f));
            ChangePhase(Fight1.class);
        }

        private void LoseRelic(EYBEventOption option)
        {
            player.relics.remove(option.relic);
            GameEffects.List.Add(new ShowCardAndObtainEffect(obtainedCard, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2f));
            ChangePhase(Trap.class);
        }
    }

    private static class Fight1 extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Fight1());
            AddContinueOption();
        }
    }

    private static class Fight2 extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Fight2());
            ChangePhase(5);
        }
    }

    private static class Trap extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Trap());
            ChangePhase(5);
        }
    }

    private static class Conclusion1 extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion1());
            AddContinueOption();
        }
    }

    private static class Conclusion2 extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion2());
            AddContinueOption();
        }
    }

    private static class Conclusion3 extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion3());
            AddLeaveOption();
        }
    }

    private static class Flee extends EYBEventPhase<TheMysteriousPeak, TheMysteriousPeak.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Flee());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public final String Introduction()
        {
            return GetDescription(0);
        }

        public final String Hunt()
        {
            return GetDescription(1);
        }

        public final String Fight1()
        {
            return GetDescription(2);
        }

        public final String Fight2()
        {
            return GetDescription(3);
        }

        public final String Trap()
        {
            return GetDescription(4);
        }

        public final String Conclusion1()
        {
            return GetDescription(5);
        }

        public final String Conclusion2()
        {
            return GetDescription(6);
        }

        public final String Conclusion3()
        {
            return GetDescription(6);
        }

        public final String Flee()
        {
            return GetDescription(6);
        }

        public final String FightOption(int gold)
        {
            return GetOption(0, gold);
        }

        public final String TrapOption(String card)
        {
            return GetOption(1, card);
        }

        public final String TrapLockedOption()
        {
            return GetOption(2);
        }

        public final String FleeOption()
        {
            return GetOption(3);
        }
    }
}