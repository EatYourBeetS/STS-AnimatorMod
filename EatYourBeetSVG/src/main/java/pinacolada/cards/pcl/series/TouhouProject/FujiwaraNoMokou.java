package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class FujiwaraNoMokou extends PCLCard
{
    public static final PCLCardData DATA = Register(FujiwaraNoMokou.class).SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage(true);

    public FujiwaraNoMokou()
    {
        super(DATA);

        Initialize(9, 0, 3, 0);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Red(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.FetchFromPile(name, 1, player.exhaustPile).SetOptions(true, false).SetFilter(c -> !PCLGameUtilities.IsHindrance(c));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.TriggerOrbPassive(magicNumber, false, true).SetFilter(o -> Fire.ORB_ID.equals(o.ID)).AddCallback(orbs -> {
            if (orbs.size() > 0) {
                PCLActions.Bottom.StackPower(new VigorPower(player, orbs.size()));
            }
        });
    }
}

