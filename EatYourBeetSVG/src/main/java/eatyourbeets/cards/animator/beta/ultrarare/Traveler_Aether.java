package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Traveler_Aether extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Traveler_Aether.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.Normal).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public Traveler_Aether()
    {
        super(DATA);

        Initialize(0, 0, 10, 2);
        SetAffinity_Light(2, 0, 0);
        SetAffinity_Dark(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Aether::new, secondaryValue);
        GameActions.Bottom.ApplyBurning(TargetHelper.Normal(m), magicNumber);
        GameActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), magicNumber);
    }
}