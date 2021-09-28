package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class AsukaLangley extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(AsukaLangley.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Evangelion);

    public AsukaLangley()
    {
        super(DATA);

        Initialize(13, 0, 1 );
        SetUpgrade(1, 0, 0 );

        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        //GameActions.Bottom.StackPower(new AsukaLangleyPower(p));
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.LockOn, magicNumber * cards.size());
            }
        });
    }
}