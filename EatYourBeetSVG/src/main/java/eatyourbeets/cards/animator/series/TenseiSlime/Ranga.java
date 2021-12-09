package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ranga extends AnimatorCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(Ranga.class)
            .SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental)
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
        SetAttackTarget(EYBCardTarget.ALL);
        SetMultiDamage(true);
        upgradedDamage = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded)
        {
            GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.LIGHTNING);
        }
        else
        {
            GameActions.Bottom.DealCardDamage(this, m, AttackEffects.LIGHTNING);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAttack.Subscribe(this);
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (player.exhaustPile.contains(this) && GameUtilities.IsPlayer(info.owner) && target instanceof AbstractMonster && target.hasPower(VulnerablePower.POWER_ID)) {
            GameActions.Bottom.MoveCard(this,player.drawPile).SetDestination(CardSelection.Top).ShowEffect(true, true);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.GainSupportDamage(magicNumber);
        GameActions.Bottom.Purge(this).ShowEffect(true);
    }
}