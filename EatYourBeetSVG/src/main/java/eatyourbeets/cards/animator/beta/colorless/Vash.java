package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Vash extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vash.class)
            .SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Trigun);
    public static final int HITS = 3;

    public Vash()
    {
        super(DATA);

        Initialize(1, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);

        SetLoyal(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        return super.ModifyDamage(enemy, damage + GameActionManager.totalDiscardedThisTurn * magicNumber);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(HITS);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < HITS; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.GUNSHOT).SetSoundPitch(0.5f, 0.7f);
        }

        GameActions.Bottom.Reload(name, cards -> GameActions.Bottom.StackPower(new VashPower(p, cards.size() * secondaryValue)));
    }

    public static class VashPower extends AnimatorPower
    {
        public VashPower(AbstractCreature owner, int amount)
        {
            super(owner, Vash.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.Callback(() -> GameActions.Bottom.Cycle(name, amount));
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
    }
}