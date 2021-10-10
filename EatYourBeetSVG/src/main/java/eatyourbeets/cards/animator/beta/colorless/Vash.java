package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Vash extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vash.class)
            .SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random)
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
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.GUNSHOT).SetSoundPitch(0.5f, 0.7f);
        GameActions.Bottom.GainBlock(block);


        GameActions.Bottom.Reload(name, cards -> {
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
        private final AnimatorCard source;
        private final ArrayList<AbstractCard> cards;


        public GenericEffect_Vash(CardType cardType, AnimatorCard source, ArrayList<AbstractCard> cards)
        {
            this.cardType = cardType;
            this.source = source;
            this.cards = cards;
        }

        @Override
        public String GetText()
        {
            return JUtils.Format("{{0}}", StringUtils.capitalize(cardType.toString().toLowerCase()));
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            if (p.drawPile.size() > 0)
            {
                AbstractCard topCard = p.drawPile.getTopCard();
                GameEffects.List.ShowCardBriefly(topCard);
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
                    GameActions.Bottom.Exhaust(topCard);
                    GameActions.Bottom.Exhaust(source);
                    for (AbstractCard ca : cards) {
                        GameActions.Bottom.Exhaust(ca);
                    }
                }
            }
        }
    }
}