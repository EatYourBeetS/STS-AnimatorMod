package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Strike_Kancolle extends Strike
{
    public static final String ID = Register(Strike_Kancolle.class.getSimpleName());

    public Strike_Kancolle()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0, 5);

        SetHealing(true);
        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (EffectHistory.TryActivateLimited(cardID))
        {
            for (int i = 0; i < this.magicNumber; ++i)
            {
                GameEffects.List.Add(new GainPennyEffect(p.hb.cX, p.hb.cY + (p.hb.height / 2)));
            }
            p.gainGold(this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}