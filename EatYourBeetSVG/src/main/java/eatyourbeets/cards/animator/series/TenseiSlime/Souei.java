package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Souei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souei.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Souei()
    {
        super(DATA);

        Initialize(1, 0, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Air(2);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber).AddCallback(
                m, (enemy, __) -> {
                    PoisonPower poison = GameUtilities.GetPower(enemy, PoisonPower.class);
                    if (poison != null)
                    {
                        GameActions.Top.Callback(new PoisonLoseHpAction(enemy, player, poison.amount, AttackEffects.POISON))
                                .AddCallback(poison, (basePoison, action) ->
                                {
                                    if (GameUtilities.IsFatal(action.target, true))
                                    {
                                        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), basePoison.amount);
                                    }
                                });
                    }
                }
        );
    }
}