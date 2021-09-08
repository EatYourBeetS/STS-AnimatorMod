package eatyourbeets.events.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.effects.special.GenericChooseCardsToObtainEffect;
import eatyourbeets.effects.special.GenericChooseCardsToRemoveEffect;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.beta.GranviaShieldCrest;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

import static eatyourbeets.resources.GR.Enums.CardTags.PROTAGONIST;

public class TheHeroAssociation extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheHeroAssociation.class);
    private static final int PRICE_MIN = 70;
    private static final int PRICE_MAX = 120;

    public static TheHeroAssociation TryCreate(Random rng)
    {
        if (AbstractDungeon.floorNum > 8 && !(GameUtilities.HasRelic(GranviaShieldCrest.ID)) && rng.randomBoolean(0.15f)) {
            return new TheHeroAssociation();
        }
        return null;
    }

    public TheHeroAssociation()
    {
        super(ID, new EventStrings(),IMAGES.HeroAssociation.Path());
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
            if (heroes.size() > 0)
            {
                AddOption(text.HeroOption()).AddCallback(this::Hero);
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
            ClearOptions();
            GameEffects.List.Add(new GenericChooseCardsToRemoveEffect(1, c -> c.hasTag(PROTAGONIST))).AddCallback(() -> {
                ProgressPhase();
            });
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
            AddText(text.Hero1());
            AddContinueOption();
        }
    }

    private static class Hero2 extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Hero2());
            AddLeaveOption();
        }
    }

    private static class Hire extends EYBEventPhase<TheHeroAssociation, TheHeroAssociation.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            GameEffects.List.Add(new GenericChooseCardsToObtainEffect(1));

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

        public final String Hero1()
        {
            return GetDescription(1);
        }

        public final String Hero2()
        {
            return GetDescription(2);
        }

        public final String Hire()
        {
            return GetDescription(3);
        }

        public final String HeroOption()
        {
            return GetOption(0);
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
}