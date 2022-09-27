package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SakuyaIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuyaIzayoi.class)
            .SetSkill(X_COST, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public SakuyaIzayoi()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 2);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
        SetFading(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        int x = GameUtilities.UseXCostEnergy(this);
        if (CheckSpecialCondition(true))
        {
            x += 1;
        }

        if (x > 0)
        {
            GameActions.Bottom.GainAgility(x);
            GameActions.Bottom.StackPower(new SakuyaIzayoiPower(p, x));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        for (AbstractCard c : player.hand.group)
        {
            if (c instanceof ThrowingKnife)
            {
                return true;
            }
        }

        return false;
    }

    public static class SakuyaIzayoiPower extends AnimatorPower
    {
        public SakuyaIzayoiPower(AbstractCreature owner, int amount)
        {
            super(owner, SakuyaIzayoi.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.CreateThrowingKnives(amount);
            RemovePower();
        }
    }
}