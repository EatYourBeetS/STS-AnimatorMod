package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Geryuganshoop extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Geryuganshoop.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Geryuganshoop()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,0,1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Silver(1);

        SetDrawPileCardPreview(GameUtilities::HasDarkAffinity);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        drawPileCardPreview.SetEnabled(IsStarter());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IsStarter())
        {
            GameActions.Bottom.FetchFromPile(name, 1, player.drawPile)
            .SetFilter(GameUtilities::HasDarkAffinity);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!IsStarter())
        {
            GameActions.Bottom.Cycle(name, magicNumber);
        }

        GameActions.Delayed.PurgeFromPile(name, secondaryValue, player.exhaustPile)
        .SetMessage(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.GainEnergyNextTurn(cards.size());
            }
        });
    }
}