package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EvolvePower;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Azriel extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Azriel.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.NoGameNoLife);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Azriel()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Silver(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new EvolvePower(p, 1));
        PCLActions.Bottom.StackPower(new AzrielPower(p, this, magicNumber));
    }

    public class AzrielPower extends PCLPower
    {
        private final PCLCard source;

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

    protected static class GenericEffect_Azriel extends GenericEffect implements OnStartOfTurnPostDrawSubscriber
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
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            int count = PCLJUtils.Count(player.hand.group, c -> c.type == cardType);
            if (PCLJUtils.Count(player.hand.group, c -> c.type == cardType) > player.hand.size()) {
                PCLActions.Bottom.SelectFromPile("",count,player.hand).SetOptions(true,false).AddCallback(cards -> {
                    for (AbstractCard ca : cards) {
                        PCLActions.Bottom.Motivate(ca, 1);
                    }
                });
            }
            else {
                PCLActions.Bottom.DiscardFromHand("", 999, true).SetFilter(c -> c.type != cardType);
            }
            PCLActions.Bottom.Draw(1).SetFilter(c -> c.type == cardType, false);
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}