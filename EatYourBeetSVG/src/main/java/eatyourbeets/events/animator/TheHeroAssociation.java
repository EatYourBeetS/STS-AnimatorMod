package eatyourbeets.events.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.animator.series.GATE.ItamiYouji;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.animator.series.Konosuba.Kazuma;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Yuuichirou;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.beta.GranviaShieldCrest;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

import static eatyourbeets.resources.GR.Enums.CardTags.PROTAGONIST;

public class TheHeroAssociation extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheHeroAssociation.class);
    private static final int PRICE_MIN = 70;
    private static final int PRICE_MAX = 120;
    private static AbstractCard hero;

    public static TheHeroAssociation TryCreate(Random rng)
    {
        if (!(GameUtilities.HasRelic(GranviaShieldCrest.ID)) && rng.randomBoolean(0.15f)) {
            return new TheHeroAssociation();
        }
        return null;
    }

    public TheHeroAssociation()
    {
        super(ID, new EventStrings(),IMAGES.HeroAssociation.Path());
        hero = null;
        RegisterPhase(0, new Offering());
        RegisterPhase(1, new Hero1());
        RegisterPhase(2, new Hero2());
        RegisterPhase(3, new Hire());
        ProgressPhase();
    }

    private static class Offering extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        private static int price = MathUtils.random(PRICE_MIN,PRICE_MAX);

        @Override
        protected void OnEnter()
        {
            price = MathUtils.random(PRICE_MIN,Math.max(PRICE_MIN,Math.min(player.gold,PRICE_MAX)));

            AddText(text.Offering());

            boolean hasEnoughGold = (player.gold >= price);
            AbstractCard hero = null;
            ArrayList<AbstractCard> heroes = JUtils.Filter(player.masterDeck.group, c -> c.hasTag(PROTAGONIST));
            if (heroes.size() > 0) {
                hero = JUtils.Random(heroes);
            }

            if (hero != null)
            {
                AddOption(text.HeroOption(hero.name), hero).AddCallback(this::Hero);
            }
            else
            {
                AddOption(text.HeroLockedOption()).SetDisabled(true);
            }

            if (hasEnoughGold)
            {
                AddOption(text.HireOption(price)).AddCallback(this::Hire);
            }
            else
            {
                AddOption(text.HireLockedOption()).SetDisabled(true);
            }
            AddLeaveOption();
        }

        private void Hero(EYBEventOption option)
        {
            player.masterDeck.removeCard(option.card);
            hero = option.card;
            ProgressPhase();
        }

        private void Hire(EYBEventOption option)
        {
            player.loseGold(price);
            ChangePhase(Hire.class);
        }
    }

    private static class Hero1 extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Hero1(GetHeroQuote(hero), hero.name));
            AddContinueOption();
        }
    }

    private static class Hero2 extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            GranviaShieldCrest relic = new GranviaShieldCrest();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            AddText(text.Hero2(hero.name));
            AddLeaveOption();
        }
    }

    private static class Hire extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            RewardItem rewardItem = new RewardItem(GR.Animator.CardColor);
            RandomizedList<AbstractCard> cards = new RandomizedList<>(GameUtilities.GetAvailableCards(c -> c.hasTag(PROTAGONIST)));

            room.rewards.clear();
            rewardItem.cards.clear();
            for (int i = 0; i < 3; i++)
            {
                AbstractCard card = cards.Retrieve(RNG).makeCopy();
                card.upgrade();
                rewardItem.cards.add(card);
            }


            room.addCardReward(rewardItem);
            AbstractDungeon.combatRewardScreen.open();

            AddText(text.Hire());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {

        public final String Offering()
        {
            return GetDescription(0);
        }

        public final String Hero1(String quote, String name)
        {
            return GetDescription(1, quote, name);
        }

        public final String Hero2(String name)
        {
            return GetDescription(2, name);
        }

        public final String Hire()
        {
            return GetDescription(3);
        }

        public final String HeroOption(String name)
        {
            return GetOption(0, name);
        }

        public final String HireOption(int gold)
        {
            return GetOption(1, gold);
        }

        public final String HeroLockedOption()
        {
            return GetOption(2);
        }

        public final String HireLockedOption()
        {
            return GetOption(3);
        }
    }

    private static String GetHeroQuote(AbstractCard c) {
        if (c == null) {
            return "I'm not a hero...";
        }
        if (c.cardID.equals(Kazuma.DATA.ID)) {
            return "This isn’t how I imagined life in a parallel world would be.";
        }
        else if (c.cardID.equals(ItamiYouji.DATA.ID)) {
            return "I work to support my hobby.";
        }
        else if (c.cardID.equals(GoblinSlayer.DATA.ID)) {
            return "Yeah.";
        }
        else if (c.cardID.equals(Yuuichirou.DATA.ID)) {
            return "Even if I have to use myself as bait, I'll still save her.";
        }
        else {
            return "Let's go.";
        }
    }
}