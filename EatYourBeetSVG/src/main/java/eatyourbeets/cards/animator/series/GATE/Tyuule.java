package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tyuule extends AnimatorCard implements OnCardResetListener
{
    public static final EYBCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    private ColoredString magicNumberString = new ColoredString("X", Colors.Cream(1));

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 2, 0);
        SetUpgrade(0, 2, 0);

        SetAffinity_Dark(1, 1, 0);

        SetAffinityRequirement(Affinity.Dark, 2);
        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    public ColoredString GetMagicNumberString()
    {
        return magicNumberString;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = GetHandAffinity(Affinity.Blue) + GetHandAffinity(Affinity.Dark);
        isMagicNumberModified = magicNumber > 0;
        magicNumberString = super.GetMagicNumberString();
    }

    @Override
    public void OnReset()
    {
        magicNumberString.SetText("X").SetColor(Colors.Cream(1));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            for (AbstractPower debuff : enemy.powers)
            {
                if (WeakPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyWeak(player, enemy, 1);
                }
                else if (VulnerablePower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyVulnerable(player, enemy, 1);
                }
                else if (PoisonPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyPoison(player, enemy, 1);
                }
                else if (BurningPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyBurning(player, enemy, 1);
                }
                else if (GainStrengthPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ReduceStrength(enemy, 1, true);
                }
            }
        }

        if (magicNumber > 0)
        {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
    }
}