package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Hans extends PCLCard implements OnTrySpendAffinitySubscriber
{
    public static final PCLCardData DATA = Register(Hans.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Hans()
    {
        super(DATA);

        Initialize(0, 3, 5, 4);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Dark(1,0,2);

        SetHarmonic(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLCombatStats.onTrySpendAffinity.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyPoison(p, m, magicNumber).AddCallback(() -> {
                for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                    if (PCLGameUtilities.GetPowerAmount(mo, PoisonPower.POWER_ID) > 0) {
                        PCLActions.Bottom.DealDamage(player, mo, secondaryValue, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                                .SetDamageEffect((enemy) -> PCLGameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)).duration)
                                .AddCallback(enemy -> {
                                    PCLActions.Bottom.GainTemporaryHP(enemy.lastDamageTaken);
                                });
                    }
                }
            }
        );
    }

    @Override
    public int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending) {
        if (isActuallySpending && amount >= 7 && player.exhaustPile.contains(this) && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.PlayCard(this, PCLGameUtilities.GetRandomEnemy(true)).AddCallback(() -> {
                PCLCombatStats.onTrySpendAffinity.Unsubscribe(this);
                PCLActions.Bottom.Purge(this).ShowEffect(true);
            });
        }
        return amount;
    }
}