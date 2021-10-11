package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.Magilou;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Magilou_Bienfu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Magilou_Bienfu.class)
            .SetSkill(-2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Magilou.DATA.Series);

    public Magilou_Bienfu()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Water(1);
        SetAffinity_Dark(1);

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
        GameActions.Bottom.RaiseWaterLevel(1, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Last.MoveCard(this, player.drawPile).ShowEffect(true, true);
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return false;
    }
}