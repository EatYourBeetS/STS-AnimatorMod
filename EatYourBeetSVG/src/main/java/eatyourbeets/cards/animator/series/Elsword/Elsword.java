package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Elsword extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Elsword.class).SetAttack(2, CardRarity.COMMON);

    public Elsword()
    {
        super(DATA);

        Initialize(13, 0, 2);
        SetUpgrade(4,  0, 0);


        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (m.hasPower(BurningPower.POWER_ID))
        {
            GameActions.Bottom.Motivate();
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Cycle(name, magicNumber);
    }
}