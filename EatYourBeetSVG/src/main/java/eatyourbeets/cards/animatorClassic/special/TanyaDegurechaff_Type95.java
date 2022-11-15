package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff_Type95 extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff_Type95.class).SetSkill(4, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public TanyaDegurechaff_Type95()
    {
        super(DATA);

        Initialize(0, 0);

        this.series = CardSeries.YoujoSenki;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (cost > 0)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.modifyCostForCombat(-1));
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (cost > 0)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.modifyCostForCombat(-1));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Plasma());
        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            c.isCostModified = c.isCostModifiedForTurn = false;
            c.cost = c.costForTurn = 4;
        });
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}