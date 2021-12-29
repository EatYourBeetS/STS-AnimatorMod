package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.red.Evolve;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EvolvePower;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Azriel extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Azriel.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.NoGameNoLife)
            .SetMultiformData(2)
            .PostInitialize(data ->
            {
                data.AddPreview(new FakeAbstractCard(new Evolve()), false);
            });
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Azriel()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Silver(1);

        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new EvolvePower(p, 1));
        PCLActions.Bottom.StackPower(new AzrielPower(p, this, magicNumber));
    }

    public class AzrielPower extends PCLPower
    {
        private final PCLCard source;
        protected CardType cardType;

        public AzrielPower(AbstractCreature owner, PCLCard source, int amount)
        {
            super(owner, Azriel.DATA);
            this.source = source;

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (choices.TryInitialize(source))
            {
                choices.AddEffect(new GenericEffect_Azriel(CardType.ATTACK));
                choices.AddEffect(new GenericEffect_Azriel(CardType.SKILL));
                choices.AddEffect(new GenericEffect_Azriel(CardType.POWER));
                choices.AddEffect(new GenericEffect_Azriel(CardType.CURSE));
                choices.AddEffect(new GenericEffect_Azriel(CardType.STATUS));
            }
            choices.Select(1, null);
        }
    }

    protected static class GenericEffect_Azriel extends GenericEffect
    {
        private final CardType cardType;

        public GenericEffect_Azriel(CardType cardType)
        {
            this.cardType = cardType;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format("{{0}} NL ({1} cards in draw pile)", StringUtils.capitalize(cardType.toString().toLowerCase()), PCLJUtils.Count(player.drawPile.group, c -> c.type == cardType));
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Last.Callback(() -> {
                int count = PCLJUtils.Count(player.hand.group, c -> c.type == cardType);
                if (count >= player.hand.size() / 2) {
                    PCLActions.Bottom.Motivate(count);
                }
                else {
                    PCLActions.Bottom.DiscardFromHand("", 999, true)
                            .SetOptions(true, true, true)
                            .SetFilter(c -> c.type != cardType);
                }
            });
        }
    }
}