package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.RemoveRightmostDebuffAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class SwordMaiden extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(SwordMaiden.class.getSimpleName(), EYBCardBadge.Special);

    public SwordMaiden()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 8);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Add(new RemoveRightmostDebuffAction(p));
        GameActions.Bottom.GainTemporaryHP(this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainRandomStat(2);

            return true;
        }

        return false;
    }
}