package eatyourbeets.cards.animator.series.TenseiSlime;

import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Souei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souei.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Souei()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Green(2);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (info.TryActivateSemiLimited())
        {
            GameActions.Bottom.Callback(() ->
            {
                final int intangible = GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID);
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    PoisonPower poison = GameUtilities.GetPower(enemy, PoisonPower.class);
                    if (poison != null)
                    {
                        GameActions.Top.Callback(new PoisonLoseHpAction(enemy, player, poison.amount, AttackEffects.POISON))
                        .AddCallback(intangible, (baseIntangible, action) ->
                        {
                            if (GameUtilities.IsFatal(action.target, true))
                            {
                                if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) == baseIntangible)
                                {
                                    GameActions.Top.StackPower(new IntangiblePlayerPower(player, 1));
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}