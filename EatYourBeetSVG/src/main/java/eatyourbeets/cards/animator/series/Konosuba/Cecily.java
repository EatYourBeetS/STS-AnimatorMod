package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cecily extends AnimatorCard implements OnCardResetListener
{
    public static final EYBCardData DATA = Register(Cecily.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int LIMIT = 4;

    private ColoredString magicNumberString = new ColoredString("X", Colors.Cream(1));

    public Cecily()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0,0,1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
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

        magicNumber = Math.min(LIMIT,CombatStats.Affinities.GetAffinityLevel(Affinity.General, true));
        magicNumberString = super.GetMagicNumberString();
        SetAffinityRequirement(Affinity.General, magicNumber);
    }

    @Override
    public void OnReset()
    {
        magicNumberString.SetText("X").SetColor(Colors.Cream(1));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new CecilyPower(p, 1));
        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            GameActions.Bottom.Cycle(name, magicNumber).AddCallback(() -> {
                if (info.IsSynergizing && info.TryActivateLimited()) {
                    GameActions.Bottom.Motivate(secondaryValue);
                }
            });
        });
    }

    public static class CecilyPower extends AnimatorPower
    {
        public CecilyPower(AbstractCreature owner, int amount)
        {
            super(owner, Cecily.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            for (Affinity affinity : Affinity.Basic()) {
                GameUtilities.MaintainPower(affinity);
            }
            RemovePower();
        }
    }
}