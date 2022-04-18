package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class ChainLightning extends UnnamedCard
{
    public static final EYBCardData DATA = Register(ChainLightning.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.Random);

    public ChainLightning()
    {
        super(DATA);

        Initialize(3, 0, 7, 4);
        SetUpgrade(1, 0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void SetAttackTarget(EYBCardTarget attackTarget)
    {
        super.SetAttackTarget(attackTarget);
        this.target = CardTarget.ENEMY;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.LIGHTNING)
        .SetSoundPitch(2f - (0.175f * magicNumber))
        .AddCallback(magicNumber, this::OnHit)
        .SetDuration(0.025f + (0.025f * magicNumber), true);
    }

    private void Attack(AbstractCreature lastEnemy, int remainingHits)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.LIGHTNING)
        .SetSoundPitch(2f - (0.175f * remainingHits))
        .SetFilter(lastEnemy, (a, b) -> a != b)
        .AddCallback(remainingHits, this::OnHit)
        .SetDuration(0.025f + (0.025f * remainingHits), true);
    }

    private void OnHit(int remainingHits, AbstractCreature lastEnemy)
    {
        GameActions.Bottom.StackAmplification(player, lastEnemy, secondaryValue);
        if (remainingHits > 1)
        {
            Attack(lastEnemy, remainingHits - 1);
        }
    }
}