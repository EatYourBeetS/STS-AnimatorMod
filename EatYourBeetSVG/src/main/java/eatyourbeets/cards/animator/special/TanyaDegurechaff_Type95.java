package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.rare.TanyaDegurechaff;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff_Type95.class)
            .SetSkill(4, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(TanyaDegurechaff.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new OrbCore_Plasma(), false));

    public TanyaDegurechaff_Type95()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);
        SetAffinity_Light(1);

        SetExhaust(true);
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
            GameActions.Bottom.ModifyAllInstances(uuid, c -> CostModifiers.For(c).Add(cardID, -1));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.PlayCard(new OrbCore_Plasma(), m)
        .SetCurrentPosition(this.target_x, this.target_y);
        GameActions.Bottom.ModifyAllInstances(uuid, c -> CostModifiers.For(c).Remove(cardID));
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