package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class ItamiYouji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ItamiYouji.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged);

    public ItamiYouji()
    {
        super(DATA);

        Initialize(3, 0, 3, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber)
        .AddCallback(m, (enemy, __) ->
        {
            for (AbstractCard card : player.hand.group)
            {
                if (card.type == CardType.ATTACK)
                {
                    GameActions.Bottom.SFX("ATTACK_FIRE");
                    GameActions.Bottom.DealDamage(this, enemy, AbstractGameAction.AttackEffect.NONE);
                }
            }
        });

        if (isSynergizing)
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}