package pinacolada.events.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.pcl.colorless.Yoimiya;
import pinacolada.events.base.PCLEvent;
import pinacolada.relics.pcl.BountyMap2;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class TheSecludedHarbor extends PCLEvent // TODO make Abstract Bounty class
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheSecludedHarbor.class);

    public static TheSecludedHarbor TryCreate(Random rng)
    {
        BountyMap2 bmap = PCLGameUtilities.GetRelic(BountyMap2.class);
        if (bmap != null && bmap.GetCurrentRequiredRoom().equals(EventRoom.class) && bmap.IsEnabled()) {
            return new TheSecludedHarbor();
        }
        return null;
    }

    public TheSecludedHarbor()
    {
        super(ID, new EventStrings(),IMAGES.SecludedHarbor.Path());

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

    private static class Introduction extends EYBEventPhase<TheSecludedHarbor, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Hunt extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        private final AbstractCard obtainedCard = new Yoimiya();
        private final int HP_LOSS = player.currentHealth / 2;
        private AbstractRelic relicToLose;

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
            relicToLose = relics.Retrieve(RNG);
            if (relicToLose == null) {
                for (AbstractRelic r : player.relics)
                {
                    if (r.tier == AbstractRelic.RelicTier.RARE)
                    {
                        relics.Add(r);
                    }
                }
                relicToLose = relics.Retrieve(RNG);
            }

            AddText(text.Hunt());
            AddOption(text.FightOption(HP_LOSS), obtainedCard).AddCallback(this::LoseHealth);
            if (relicToLose != null)
            {
                AddOption(text.TrapOption(relicToLose.name)).AddCallback(this::LoseRelic);
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
            PCLGameEffects.List.Add(new ShowCardAndObtainEffect(obtainedCard, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2f));
            ChangePhase(Fight1.class);
        }

        private void LoseRelic()
        {
            player.relics.remove(relicToLose);
            PCLGameEffects.List.Add(new ShowCardAndObtainEffect(obtainedCard, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2f));
            ChangePhase(Trap.class);
        }
    }

    private static class Fight1 extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Fight1());
            AddContinueOption();
        }
    }

    private static class Fight2 extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Fight2());
            ChangePhase(5);
        }
    }

    private static class Trap extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Trap());
            ChangePhase(5);
        }
    }

    private static class Conclusion1 extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion1());
            AddContinueOption();
        }
    }

    private static class Conclusion2 extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion2());
            AddContinueOption();
        }
    }

    private static class Conclusion3 extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Conclusion3());
            AddLeaveOption();
        }
    }

    private static class Flee extends EYBEventPhase<TheSecludedHarbor, TheSecludedHarbor.EventStrings>
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
            return GetDescription(7);
        }

        public final String Flee()
        {
            return GetDescription(8);
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