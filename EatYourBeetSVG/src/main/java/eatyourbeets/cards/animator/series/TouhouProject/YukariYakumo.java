package eatyourbeets.cards.animator.series.TouhouProject;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class YukariYakumo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YukariYakumo.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Light(2);
        SetAffinity_Dark(2);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Delayed.Callback(() ->
        {
            final ArrayList<AbstractPower> debuffs = new ArrayList<>();
            for (AbstractPower power : player.powers)
            {
                if (power.type == AbstractPower.PowerType.DEBUFF && !(power instanceof InvisiblePower))
                {
                    debuffs.add(power);
                }
            }

            if (debuffs.size() > 0 && player.powers.removeAll(debuffs))
            {
                GameActions.Bottom.StackPower(new YukariYakumoPower(player, debuffs));

                for (AbstractPower debuff : debuffs)
                {
                    final boolean level1 = rng.randomBoolean();
                    final int amount = (upgraded ? 1 : 0) + (level1 ? 2 : 1);
                    final PowerHelper toApply = GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs(level1 ? 1 : 2));
                    GameActions.Bottom.StackPower(TargetHelper.Enemies(player), toApply, amount)
                    .ShowEffect(true, true);
                }
            }
        });
    }

    public static class YukariYakumoPower extends AnimatorPower
    {
        private static int counter = 0;
        private final ArrayList<AbstractPower> debuffs;

        public YukariYakumoPower(AbstractCreature owner, ArrayList<AbstractPower> debuffs)
        {
            super(owner, YukariYakumo.DATA);

            this.ID += (counter++);
            this.debuffs = debuffs;
            this.priority += 100;
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            RemovePower();
            GameActions.Bottom.Callback(() ->
            {
                for (AbstractPower debuff : debuffs)
                {
                    GameUtilities.ApplyPowerInstantly(owner, debuff, debuff.amount);
                }
            });
        }
    }
}

