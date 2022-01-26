package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Vash extends PCLCard
{
    public static final PCLCardData DATA = Register(Vash.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Ranged, PCLCardTarget.Random)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Trigun);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Vash()
    {
        super(DATA);

        Initialize(4, 4, 1, 0);
        SetUpgrade(1, 1, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);

        SetLoyal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.5f, 0.7f));
        PCLActions.Bottom.GainBlock(block);


        PCLActions.Top.ReshuffleDiscardPile(false);
        PCLActions.Bottom.Reload(name, cards -> {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_Vash(CardType.ATTACK, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.SKILL, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.POWER, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.CURSE, this, cards));
                choices.AddEffect(new GenericEffect_Vash(CardType.STATUS, this, cards));
            }
            choices.Select(1, m);
        });

    }

    protected static class GenericEffect_Vash extends GenericEffect
    {
        private final CardType cardType;
        private final PCLCard source;
        private final ArrayList<AbstractCard> cards;


        public GenericEffect_Vash(CardType cardType, PCLCard source, ArrayList<AbstractCard> cards)
        {
            this.cardType = cardType;
            this.source = source;
            this.cards = cards;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format("{{0}} NL ({1} cards in draw pile)", StringUtils.capitalize(cardType.toString().toLowerCase()), PCLJUtils.Count(player.drawPile.group, c -> c.type == cardType));
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            if (p.drawPile.size() > 0)
            {
                int amount = source.damage * cards.size();
                AbstractCard topCard = p.drawPile.getTopCard();
                PCLGameEffects.List.ShowCardBriefly(topCard);
                if (topCard.type.equals(cardType))
                {
                    PCLActions.Bottom.GainSupportDamage(amount);
                }
                else {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, amount);
                    for (AbstractCard ca : cards) {
                        PCLActions.Bottom.Exhaust(ca);
                    }
                }
            }
        }
    }
}