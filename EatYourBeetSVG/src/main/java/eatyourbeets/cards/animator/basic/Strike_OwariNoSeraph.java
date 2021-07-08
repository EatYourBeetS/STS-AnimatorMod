package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Strike_OwariNoSeraph extends Strike
{
    public static final String ID = Register(Strike_OwariNoSeraph.class).ID;

    public Strike_OwariNoSeraph()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0, 1);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.OwariNoSeraph);
        SetAffinity_Red(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}