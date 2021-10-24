package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.utilities.GameActions;

public class Sylvia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Sylvia.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Normal).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Konosuba);

    public Sylvia() {
        super(DATA);

        Initialize(1, 1, 0);
        SetUpgrade(0, 0, 0);

        SetAffinity_Earth();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                int attack = card.baseDamage;

                if (attack > 0)
                {
                    DamageModifiers.For(this).Add(attack);
                }

                int block = card.baseBlock;

                if (block > 0)
                {
                    BlockModifiers.For(this).Add(block);
                }

                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.GainBlock(block);
            }
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}