package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YasutoraSado extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YasutoraSado.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal);

    public YasutoraSado()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(3, 0, 0);
        SetScaling(0,0,1);
        SetCooldown(2, 0, this::OnCooldownCompleted);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    if (enemy.currentBlock > 0 || IsInflictingNegativeEffect(enemy.intent))
                    {
                        return true;
                    }
                }
            }
            else
            {
                if (m.currentBlock > 0 || IsInflictingNegativeEffect(m.intent))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.GainStrength(magicNumber);
        GameActions.Bottom.ApplyPower(new LoseStrengthPower(player, magicNumber));
    }

    private boolean IsInflictingNegativeEffect(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.DEBUFF ||
                intent == AbstractMonster.Intent.DEFEND_DEBUFF || intent == AbstractMonster.Intent.STRONG_DEBUFF);
    }
}