package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class HiedaNoAkyuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiedaNoAkyuu.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public HiedaNoAkyuu()
    {
        super(DATA);

        Initialize(0, 0, 5, 0);
        SetAffinity_Blue(1, 1, 0);

        SetExhaust(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.MoveCards(player.drawPile, player.discardPile)
        .SetDuration(0.01f, false);

        GameActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile)
        .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(magicNumber))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.drawPile)
                .ShowEffect(true, true);
            }
        });

        GameActions.Bottom.StackPower(new NoDrawPower(p));
    }
}

