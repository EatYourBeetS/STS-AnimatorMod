package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Hans extends AnimatorCard implements OnTrySpendAffinitySubscriber
{
    public static final EYBCardData DATA = Register(Hans.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 5, 4);
        SetUpgrade(0, 0, 1, 0);

        SetExhaust(true);
        SetAffinity_Star(2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CombatStats.onTrySpendAffinity.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber).AddCallback(() -> {
                for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                    if (GameUtilities.GetPowerAmount(mo, PoisonPower.POWER_ID) > 0) {
                        GameActions.Bottom.DealDamage(player, mo, secondaryValue, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                                .SetDamageEffect((enemy) -> GameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)).duration)
                                .AddCallback(enemy -> {
                                    GameActions.Bottom.GainTemporaryHP(enemy.lastDamageTaken);
                                });
                    }
                }
            }
        );
    }

    @Override
    public int OnTrySpendAffinity(Affinity affinity, int amount, boolean canUseStar, boolean isActuallySpending) {
        if (isActuallySpending && amount >= 7 && player.exhaustPile.contains(this) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.PlayCard(this, GameUtilities.GetRandomEnemy(true)).AddCallback(() -> {
                CombatStats.onTrySpendAffinity.Unsubscribe(this);
                GameActions.Bottom.Purge(this).ShowEffect(true);
            });
        }
        return amount;
    }
}