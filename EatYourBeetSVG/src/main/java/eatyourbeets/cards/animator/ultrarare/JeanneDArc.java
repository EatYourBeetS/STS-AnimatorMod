package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;

public class JeanneDArc extends AnimatorCard_UltraRare implements StartupCard
{
    public static final String ID = Register(JeanneDArc.class.getSimpleName(), EYBCardBadge.Special);

    public JeanneDArc()
    {
        super(ID, 1, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(12,4, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);

        if (HasActiveSynergy())
        {
            GameActions.Top.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsCurseOrStatus);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        return true;
    }
}