package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Isami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Isami.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Soujiro(), false);
    }

    public Soujiro_Isami()
    {
        super(DATA);

        Initialize(0, 8, 0);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }
}