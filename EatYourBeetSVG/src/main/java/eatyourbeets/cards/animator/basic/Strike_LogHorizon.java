package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_LogHorizon extends Strike
{
    public static final String ID = Register(Strike_LogHorizon.class).ID;

    public Strike_LogHorizon()
    {
        super(ID, 0, CardTarget.ENEMY);

        Initialize(3, 0);
        SetUpgrade(2, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.hasTag(MARTIAL_ARTIST) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}