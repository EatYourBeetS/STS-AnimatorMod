package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ranga extends PCLCard implements OnAttackSubscriber
{
    public static final PCLCardData DATA = Register(Ranga.class)
            .SetAttack(0, CardRarity.UNCOMMON, PCLAttackType.Elemental)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();

    public Ranga()
    {
        super(DATA);

        Initialize(4, 0, 2);

        SetAffinity_Green(1);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetCooldown(3, 0, this::OnCooldownCompleted);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(eatyourbeets.cards.base.EYBCardTarget.ALL);
        SetMultiDamage(true);
        upgradedDamage = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded)
        {
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.LIGHTNING);
        }
        else
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.LIGHTNING);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onAttack.Subscribe(this);
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (player.exhaustPile.contains(this) && PCLGameUtilities.IsPlayer(info.owner) && target instanceof AbstractMonster && target.hasPower(VulnerablePower.POWER_ID)) {
            PCLActions.Bottom.MoveCard(this,player.drawPile).SetDestination(CardSelection.Top).ShowEffect(true, true);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.GainSupportDamage(magicNumber);
        PCLActions.Bottom.Purge(this).ShowEffect(true);
    }
}