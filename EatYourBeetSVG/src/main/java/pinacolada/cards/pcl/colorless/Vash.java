package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Vash extends PCLCard
{
    public static final PCLCardData DATA = Register(Vash.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Trigun);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Vash()
    {
        super(DATA);

        Initialize(2, 11, 1, 0);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.5f, 0.7f));
        PCLActions.Bottom.GainBlock(block);


        PCLActions.Bottom.Reload(name, cards -> {
            baseDamage += cards.size() * magicNumber;
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
                AbstractCard topCard = p.drawPile.getTopCard();
                PCLGameEffects.List.ShowCardBriefly(topCard);
                if (topCard.type.equals(cardType))
                {
                    if (topCard.baseDamage > 0) {
                        topCard.baseDamage += source.damage;
                    }
                    if (topCard.baseBlock > 0) {
                        topCard.baseBlock += source.damage;
                    }
                    //GameActions.Bottom.PlayCard(topCard,m);
                }
                else {
                    PCLActions.Bottom.Exhaust(topCard);
                    PCLActions.Bottom.Exhaust(source);
                    for (AbstractCard ca : cards) {
                        PCLActions.Bottom.Exhaust(ca);
                    }
                }
            }
        }
    }
}