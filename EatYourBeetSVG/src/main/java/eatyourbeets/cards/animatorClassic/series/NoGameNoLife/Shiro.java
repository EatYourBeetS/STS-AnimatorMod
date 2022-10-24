package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ShiroPower;
import eatyourbeets.utilities.GameActions;

public class Shiro extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Shiro.class).SetSeriesFromClassPackage().SetPower(4, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Sora(), true);
    }

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        Refresh(null);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ShiroPower(p, 1));
        GameActions.Bottom.Draw(1)
        .ShuffleIfEmpty(false)
        .SetFilter(c -> Sora.DATA.ID.equals(c.cardID), false);
    }

    public void RefreshCost()
    {
        CostModifiers.For(this).Set(-CombatStats.SynergiesThisTurn().size());
    }
}