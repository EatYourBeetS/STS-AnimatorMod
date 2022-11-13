package eatyourbeets.cards.animatorClassic.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class ItamiYouji extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ItamiYouji.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged);

    public ItamiYouji()
    {
        super(DATA);

        Initialize(3, 0, 3, 2);
        SetUpgrade(0, 0, 1);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber)
        .AddCallback(m, (enemy, __) ->
        {
            for (AbstractCard card : player.hand.group)
            {
                if (card.type == CardType.ATTACK)
                {
                    GameActions.Bottom.SFX("ATTACK_FIRE");
                    GameActions.Bottom.DealDamage(this, enemy, AttackEffects.NONE);
                }
            }
        });

        if (info.IsSynergizing)
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}