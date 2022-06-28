package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Tyuule extends AnimatorCard implements OnCardResetListener
{
    public static final EYBCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    //TODO: Standard way to handle this
    private ColoredString magicNumberString = new ColoredString();

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 2, 0, 2);
        SetUpgrade(0, 1, 0, 0);

        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Green(1);

        SetExhaust(true);
        OnReset();
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);

        if (magicNumberString.text.endsWith("X"))
        {
            OnReset();
        }
    }

    @Override
    public void OnReset()
    {
        magicNumberString.SetText(secondaryValue+"X").SetColor(Colors.Cream(1));
    }

    @Override
    public void displayUpgrades()
    {
        super.displayUpgrades();

        magicNumberString.SetColor(Colors.Green(1));
    }

    @Override
    public ColoredString GetMagicNumberString()
    {
        return magicNumberString;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        final int totalAffinity = CombatStats.Affinities.GetUsableAffinity(Affinity.Green);
        SetAffinityRequirement(Affinity.Green, totalAffinity);
        magicNumber = totalAffinity * secondaryValue;
        isMagicNumberModified = magicNumber > secondaryValue;
        magicNumberString = super.GetMagicNumberString();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            for (AbstractPower debuff : enemy.powers)
            {
                for (PowerHelper pw1 : GameUtilities.GetCommonDebuffs())
                {
                    if (pw1.ID.equals(debuff.ID))
                    {
                        GameActions.Bottom.StackPower(TargetHelper.Normal(enemy), pw1, 1);
                        break;
                    }
                }
            }
        }

        if (TryUseAffinity(Affinity.Green))
        {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
    }
}