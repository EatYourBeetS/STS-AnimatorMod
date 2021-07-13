package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.utilities.GameActions;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class).SetPower(3, CardRarity.RARE).SetMaxCopies(1);
    static
    {
        for (OrbCore core : OrbCore.GetAllCores())
        {
            DATA.AddPreview(core, false);
        }
    }

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetSeries(CardSeries.Elsword);
        SetAffinity(0, 0, 2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!p.hasPower(EvePower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new EvePower(p, 1));
        }

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
        .AddCallback(orbCores ->
        {
            if (orbCores != null && orbCores.size() > 0)
            {
                for (AbstractCard c : orbCores)
                {
                    c.applyPowers();
                    c.use(player, null);
                }
            }
        }));
    }
}