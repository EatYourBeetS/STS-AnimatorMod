package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Add extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Add.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Crystallize(), false);
                data.AddPreviews(OrbCore.GetAllCores(), false);
            });

    public Add()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, -1, 0);

        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1))
                .AddCallback(cards ->
                {
                    GameActions.Bottom.MakeCard(cards.get(0), player.drawPile).SetDestination(CardSelection.Top);
                });
        GameActions.Bottom.MakeCardInDrawPile(new Crystallize()).Repeat(magicNumber).SetDestination(CardSelection.Top);
    }
}