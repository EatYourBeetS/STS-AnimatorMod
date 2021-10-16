package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Bienfu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Bienfu.class).SetSkill(-2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Bienfu()
    {
        super(DATA);

        Initialize(0, 0);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        //GameActions.Bottom.GainIntellect(1, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Last.MoveCard(this, player.drawPile).ShowEffect(true, true);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        this.cantUseMessage = UNPLAYABLE_MESSAGE;
        return false;
    }
}