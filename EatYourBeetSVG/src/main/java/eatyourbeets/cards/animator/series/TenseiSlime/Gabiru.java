package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Gabiru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gabiru.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Gabiru()
    {
        super(DATA);

        Initialize(0, 4, 2, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Earth(1);
        SetAffinity_Air(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (IsStarter()) {
            GameActions.Bottom.Motivate(player.drawPile);
        }
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> GameUtilities.CanRetain(c) && (CheckPrimaryCondition(true) ? GameUtilities.HasBlueAffinity(c) : GameUtilities.HasOrangeAffinity(c))).AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameUtilities.Retain(c);
            }
        });
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GetHandAffinity(Affinity.Air) > GetHandAffinity(Affinity.Earth);
    }
}