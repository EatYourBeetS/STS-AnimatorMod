package eatyourbeets.cards.animatorClassic.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(AcuraShin.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing);

    public AcuraShin()
    {
        super(DATA);

        Initialize(3,0,2);
        SetScaling(0, 1, 0);
        SetCostUpgrade(-1);

        SetMartialArtist();
        
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.StackPower(new PoisonAffinityPower(p, 1));
    }
}