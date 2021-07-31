package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.animator.ApplyAmountToOrbs;
import eatyourbeets.cards.animator.beta.special.Zadkiel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class YoshinoHimekawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(3, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Zadkiel(), true);
    }

    public YoshinoHimekawa()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetAffinity_Green(2, 0, 0);

        SetExhaust(true);
        SetHaste(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (this.hasTag(HASTE))
        {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
                    .AddCallback(() -> GameActions.Top.MakeCardInDiscardPile(new Zadkiel()).SetUpgrade(upgraded, false))
                    .SetDuration(0.15f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Last.Callback(cards ->
            GameActions.Bottom.Add(new ApplyAmountToOrbs(Frost.ORB_ID, 1))
        );
    }
}