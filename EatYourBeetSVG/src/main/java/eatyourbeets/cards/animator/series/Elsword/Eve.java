package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.utilities.GameActions;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class).SetPower(3, CardRarity.RARE);
    static
    {
        DATA.InitializePreview(OrbCore.GetCardForPreview(), false);
    }

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
                    c.use(AbstractDungeon.player, null);
                }
            }
        }));
    }
}