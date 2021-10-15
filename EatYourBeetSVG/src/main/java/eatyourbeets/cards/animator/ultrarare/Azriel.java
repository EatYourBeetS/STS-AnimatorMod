package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EvolvePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Azriel.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.NoGameNoLife);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Azriel()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

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
        GameActions.Bottom.StackPower(new EvolvePower(p, 1));
        GameActions.Bottom.StackPower(new AzrielPower(p, this, magicNumber));
    }

    public class AzrielPower extends AnimatorPower
    {
        private final AnimatorCard source;

        public AzrielPower(AbstractCreature owner, AnimatorCard source, int amount)
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
            return JUtils.Format("{{0}} NL ({1} cards in draw pile)", StringUtils.capitalize(cardType.toString().toLowerCase()), JUtils.Count(player.drawPile.group, c -> c.type == cardType));
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            int count = JUtils.Count(player.hand.group, c -> c.type == cardType);
            if (JUtils.Count(player.hand.group, c -> c.type == cardType) > player.hand.size()) {
                GameActions.Bottom.SelectFromPile("",count,player.hand).SetOptions(true,false).AddCallback(cards -> {
                    for (AbstractCard ca : cards) {
                        GameActions.Bottom.Motivate(ca, 1);
                    }
                });
            }
            else {
                GameActions.Bottom.DiscardFromHand("", 999, true).SetFilter(c -> c.type != cardType);
            }
            GameActions.Bottom.Draw(1).SetFilter(c -> c.type == cardType, false);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}