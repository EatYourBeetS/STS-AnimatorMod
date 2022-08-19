package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class ElricEdward extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ElricEdward.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public ElricEdward()
    {
        super(DATA);

        Initialize(4, 0, 1);
        SetUpgrade(4, 0, 0);
        SetScaling(1, 0, 0);

        SetEvokeOrbCount(1);
        SetSeries(CardSeries.FullmetalAlchemist);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, 1).AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                switch (cards.get(0).type)
                {
                    case ATTACK:
                        GameActions.Bottom.ChannelOrb(new Lightning());
                        break;

                    case SKILL:
                        GameActions.Bottom.ChannelOrb(new Frost());
                        break;

                    case POWER:
                        GameActions.Bottom.ChannelOrb(new Earth());
                        break;
                }
            }
        });
    }
}