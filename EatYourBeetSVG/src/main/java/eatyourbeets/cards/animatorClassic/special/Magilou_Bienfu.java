package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Magilou_Bienfu extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Magilou_Bienfu.class).SetSkill(-2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Magilou_Bienfu()
    {
        super(DATA);

        Initialize(0, 0);

        SetEthereal(true);
        this.series = CardSeries.TalesOfBerseria;
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        GameActions.Bottom.GainIntellect(1, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Last.MoveCard(this, player.drawPile).ShowEffect(true, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        this.cantUseMessage = UNPLAYABLE_MESSAGE;
        return false;
    }
}