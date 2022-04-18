package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;

public class Geryuganshoop extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Geryuganshoop.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Void(), false));

    public Geryuganshoop()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetPurge(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetPurge(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainEnergy(magicNumber);
        GameActions.Bottom.SFX(SFX.ORB_DARK_CHANNEL, 0.3f, 0.4f, 0.9f);
        GameActions.Bottom.MakeCardInDrawPile(new Status_Void());
        GameActions.Bottom.PurgeFromPile(name, secondaryValue, player.exhaustPile)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.FetchFromPile(name, 1, player.drawPile);
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.exhaustPile.size() >= secondaryValue;
    }
}