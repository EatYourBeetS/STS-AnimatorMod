package pinacolada.events.pcl;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import pinacolada.effects.special.GenericChooseCardsToObtainEffect;
import pinacolada.effects.special.GenericChooseCardsToRemoveEffect;
import pinacolada.events.base.PCLEvent;
import pinacolada.relics.pcl.GranviaShieldCrest;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static pinacolada.resources.GR.Enums.CardTags.PROTAGONIST;

public class TheHeroAssociation extends PCLEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheHeroAssociation.class);
    private static final int PRICE_MIN = 70;
    private static final int PRICE_MAX = 120;

    public static TheHeroAssociation TryCreate(Random rng)
    {
        if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) && AbstractDungeon.floorNum > 8 && !(PCLGameUtilities.HasRelic(GranviaShieldCrest.ID)) && rng.randomBoolean(0.1f)) {
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

    private static class Offering extends EYBEventPhase<TheHeroAssociation, EventStrings>
    {
        private static int price = MathUtils.random(PRICE_MIN,PRICE_MAX);

        @Override
        protected void OnEnter()
        {
            price = MathUtils.random(PRICE_MIN,Math.max(PRICE_MIN,Math.min(player.gold,PRICE_MAX)));

            AddText(text.Offering());

            boolean hasEnoughGold = (player.gold >= price);
            AbstractCard hero = null;
            ArrayList<AbstractCard> heroes = PCLJUtils.Filter(player.masterDeck.group, c -> c.hasTag(PROTAGONIST));
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
            PCLGameEffects.Queue.Add(new GenericChooseCardsToRemoveEffect(1, c -> c.hasTag(PROTAGONIST))).AddCallback(result -> {
                if (result.cards.size() > 0) {
                    GranviaShieldCrest relic = new GranviaShieldCrest();
                    relic.instantObtain();
                    CardCrawlGame.metricData.addRelicObtainData(relic);

                    AbstractCard.CardRarity rarity = result.cards.get(0).rarity;
                    int gold;
                    switch (rarity) {
                        case RARE:
                            gold = MathUtils.random(70,130);
                            break;
                        case UNCOMMON:
                            gold = MathUtils.random(30,70);
                        case COMMON:
                            gold = MathUtils.random(10,30);
                        default:
                            gold = MathUtils.random(1,10);
                    }
                    PCLGameEffects.Queue.Add(new RainingGoldEffect(gold));
                    player.gainGold(gold);
                }
            });
            ChangePhase(Hero1.class);
        }

        private void Hire(EYBEventOption option)
        {
            player.loseGold(price);
            PCLGameEffects.Queue.Add(new GenericChooseCardsToObtainEffect(1, 5, c -> c.hasTag(PROTAGONIST)));
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