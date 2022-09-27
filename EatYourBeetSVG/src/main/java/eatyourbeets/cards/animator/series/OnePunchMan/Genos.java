package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnModifyDamageFirstSubscriber;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Genos extends AnimatorCard implements OnModifyDamageFirstSubscriber
{
    public static final EYBCardData DATA = Register(Genos.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 2, 5);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (!player.hasPower(ArtifactPower.POWER_ID))
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddPlayerBurning();
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.ApplyBurning(p, p, magicNumber);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }

    @Override
    public int OnModifyDamageFirst(AbstractCreature target, DamageInfo info, int damage)
    {
        if (target == player && info.type == DamageInfo.DamageType.NORMAL && damage > player.currentBlock
        && player.drawPile.contains(this) && GameUtilities.GetHealthPercentage(player) < 0.1f)
        {
            player.addBlock(secondaryValue);
            GameActions.Bottom.Exhaust(this);
        }

        return damage;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onModifyDamageFirst.Subscribe(this);
    }
}