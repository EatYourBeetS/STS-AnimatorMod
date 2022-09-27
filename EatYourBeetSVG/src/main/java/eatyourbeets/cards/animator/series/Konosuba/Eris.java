package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Special_Miracle;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eris.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Special_Miracle(), false));

    public Eris()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainAffinity(Affinity.Light, 1, true);
        GameActions.Bottom.MakeCard(new Special_Miracle(), p.drawPile).Repeat(magicNumber);
        GameActions.Bottom.StackPower(new ErisPower(p, 1));
    }

    public static class ErisPower extends AnimatorPower
    {
        public ErisPower(AbstractCreature owner, int amount)
        {
            super(owner, Eris.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public int onLoseHp(int damageAmount)
        {
            final int hp = GameUtilities.GetHP(owner, true, false);
            if (hp > 0 && damageAmount >= hp)
            {
                owner.decreaseMaxHealth(damageAmount - hp);
                flash();
                return hp - 1;
            }

            return super.onLoseHp(damageAmount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            ReducePower(1);
        }
    }
}