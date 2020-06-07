package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SatoriKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SatoriKomeiji.class).SetPower(0, CardRarity.RARE);

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetInnate(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new SatoriPower(p));
    }

    public static class SatoriPower extends AnimatorPower
    {
        public SatoriPower(AbstractCreature owner)
        {
            super(owner, SatoriKomeiji.DATA);
            updateDescription();
        }
        //TODO: Implement Satori's preview intent effect

        @Override
        public void updateDescription()
        {
            this.description = powerStrings.DESCRIPTIONS[0];
        }
    }
}

