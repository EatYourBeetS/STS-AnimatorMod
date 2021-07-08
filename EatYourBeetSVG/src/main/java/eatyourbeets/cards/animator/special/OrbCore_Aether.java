package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.animator.OrbCore_AetherPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Aether extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Aether.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Aether()
    {
        super(DATA);

        Initialize(0, 0, VALUE, 1);

        SetEvokeOrbCount(secondaryValue);
        SetAffinity_Green(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Aether::new, secondaryValue);
        GameActions.Bottom.StackPower(new OrbCore_AetherPower(p, 1));
    }
}