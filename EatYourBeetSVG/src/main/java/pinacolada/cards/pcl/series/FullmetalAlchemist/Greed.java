package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.status.Crystallize;
import pinacolada.misc.GenericEffects.GenericEffect_StackPower;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Greed extends PCLCard
{
    public static final PCLCardData DATA = Register(Greed.class)
            .SetPower(2, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage().PostInitialize(data ->
            {
                data.AddPreview(new Crystallize(), false);
            });
    public static final int THRESHOLD = 4;
    public static final int GOLD = 25;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Greed()
    {
        super(DATA);

        Initialize(0,0, 5, 3);
        SetUpgrade(0,0, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
        SetAffinity_Dark(2);

        SetObtainableInCombat(false);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription( THRESHOLD, GOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.Malleable, magicNumber));
        choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.Metallicize, secondaryValue));
        choices.Select(1, m);
        PCLActions.Bottom.StackPower(new GreedPower(p, 1));
    }

    public static class GreedPower extends PCLPower
    {
        protected int counter;
        public GreedPower(AbstractCreature owner, int amount)
        {
            super(owner, Greed.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn() {
            for (int i = 0; i < amount; i++) {
                Crystallize c = new Crystallize();
                PCLGameUtilities.ModifyCostForCombat(c, 0, false);
                PCLActions.Bottom.MakeCardInDrawPile(c);
            }
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(counter, Color.WHITE, c.a);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, THRESHOLD, GOLD, counter);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {

            super.onPlayCard(card, m);

            if (Crystallize.DATA.ID.equals(card.cardID) && counter < THRESHOLD) {
                counter += 1;
                if (counter == THRESHOLD && CombatStats.TryActivateLimited(Greed.DATA.ID)) {
                    PCLActions.Bottom.GainGold(GOLD);
                }
            }
        }

    }
}