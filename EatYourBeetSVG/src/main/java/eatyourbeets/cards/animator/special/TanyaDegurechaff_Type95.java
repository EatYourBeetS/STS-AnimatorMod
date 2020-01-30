package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final String ID = Register(TanyaDegurechaff_Type95.class);

    public TanyaDegurechaff_Type95()
    {
        super(ID, 4, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        Initialize(0, 0);

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (cost > 0)
        {
            GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.modifyCostForCombat(-1));
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (cost > 0)
        {
            GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.modifyCostForCombat(-1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Plasma(), true);
        GameActions.Bottom.ModifyAllCombatInstances(uuid, c ->
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