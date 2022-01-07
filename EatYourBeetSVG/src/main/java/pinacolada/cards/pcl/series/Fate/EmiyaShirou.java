package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import pinacolada.actions.cardManipulation.RandomCardUpgrade;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class EmiyaShirou extends PCLCard implements OnAttackSubscriber
{
    public static final PCLCardData DATA = Register(EmiyaShirou.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Fire)
            .SetSeriesFromClassPackage();
    private Fire fireOrb;

    public EmiyaShirou()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(3, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Blue(0,0,1);

        SetProtagonist(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        PCLActions.Bottom.Add(new RandomCardUpgrade());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);
        PCLActions.Delayed.Callback(() -> {
            fireOrb = PCLJUtils.SafeCast(PCLGameUtilities.GetFirstOrb(Fire.ORB_ID),Fire.class);
            if (fireOrb != null) {
                fireOrb.SetBasePassiveAmount(magicNumber, true);
                fireOrb.SetBaseEvokeAmount(magicNumber, true);
            }
            else {
                PCLActions.Bottom.ChannelOrb(new Fire());
            }

            PCLCombatStats.onAttack.Subscribe(this);
        });

        if (info.IsSynergizing) {
            PCLActions.Bottom.Add(new RandomCardUpgrade());
        }

    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractOrb o: player.orbs) {
            if (o == fireOrb) {
                PCLActions.Bottom.EvokeOrb(1, fireOrb);
            }
        }
        PCLCombatStats.onAttack.Unsubscribe(this);
    }
}