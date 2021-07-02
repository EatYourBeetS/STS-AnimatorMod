package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_Konosuba extends Strike
{
    public static final String ID = Register(Strike_Konosuba.class).ID;

    public Strike_Konosuba()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 2);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Konosuba);
        SetAlignment(0, 1, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);
    }
}