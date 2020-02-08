package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Cocytus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cocytus.class).SetAttack(1, CardRarity.COMMON);

    public Cocytus()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 2);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.GainForce(1);

        if (HasSynergy())
        {
            if (AbstractDungeon.cardRandomRng.randomBoolean())
            {
                GameActions.Bottom.GainThorns(1);
            }
            else
            {
                GameActions.Bottom.GainPlatedArmor(1);
            }
        }
    }
}