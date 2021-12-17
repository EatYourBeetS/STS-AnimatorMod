package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Souei extends PCLCard
{
    public static final PCLCardData DATA = Register(Souei.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Souei()
    {
        super(DATA);

        Initialize(1, 0, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        PCLActions.Bottom.ApplyPoison(p, m, magicNumber).AddCallback(
                m, (enemy, __) -> {
                    PoisonPower poison = PCLGameUtilities.GetPower(enemy, PoisonPower.class);
                    if (poison != null)
                    {
                        PCLActions.Top.Callback(new PoisonLoseHpAction(enemy, player, poison.amount, AttackEffects.POISON))
                                .AddCallback(poison, (basePoison, action) ->
                                {
                                    if (PCLGameUtilities.IsFatal(action.target, true))
                                    {
                                        PCLActions.Bottom.GainBlur(1);
                                        PCLActions.Bottom.ApplyPoison(TargetHelper.Enemies(), basePoison.amount);
                                    }
                                });
                    }
                }
        );
    }
}