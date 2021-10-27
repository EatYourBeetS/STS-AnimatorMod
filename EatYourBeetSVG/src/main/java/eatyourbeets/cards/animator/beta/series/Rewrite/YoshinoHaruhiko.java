package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class YoshinoHaruhiko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random).SetSeriesFromClassPackage();

    public YoshinoHaruhiko()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);
        SetAffinity_Red(2, 0, 1);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);

        if (IsStarter())
        {
            GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            GameActions.Bottom.IncreaseScaling(c, Affinity.Red, 1);
                        }
                    });
        }
    }
}