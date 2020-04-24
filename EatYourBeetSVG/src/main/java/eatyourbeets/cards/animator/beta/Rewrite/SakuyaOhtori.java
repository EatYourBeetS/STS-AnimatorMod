package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SakuyaOhtori extends AnimatorCard {
    public static final EYBCardData DATA = Register(SakuyaOhtori.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public SakuyaOhtori() {
        super(DATA);

        Initialize(0, 3, 2,1);
        SetUpgrade(0, 0, 0);
        SetScaling(1,0,1);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    protected void OnUpgrade() {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
            .AddCallback(() -> GameActions.Top.GainForce(secondaryValue, upgraded))
            .SetDuration(0.15f, true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);

        int forceToConvert = (GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID))/2;

        if (forceToConvert > 0)
        {
            GameActions.Bottom.GainForce(-forceToConvert);
            GameActions.Bottom.GainIntellect(forceToConvert);
        }
    }
}