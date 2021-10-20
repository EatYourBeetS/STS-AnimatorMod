package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public RoyMustang()
    {
        super(DATA);

        Initialize(4, 0, 2, 25);
        SetUpgrade(0, 0, 0, 15);

        SetAffinity_Fire(2);
        SetAffinity_Light();

        SetEvokeOrbCount(GameUtilities.GetEnemies(true).size());
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void Refresh(AbstractMonster enemy) {
        super.Refresh(enemy);

        SetEvokeOrbCount(GameUtilities.GetEnemies(true).size());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<magicNumber; i++) {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        }

        for (int i=0; i<GameUtilities.GetEnemies(true).size(); i++) {
            GameActions.Bottom.ChannelOrb(new Fire());
        }

        if (GameUtilities.HasFullHand())
        {
            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(secondaryValue));
        }
    }
}