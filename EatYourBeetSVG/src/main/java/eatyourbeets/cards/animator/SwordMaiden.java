package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.RemoveRightmostDebuffAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

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
        GameActionsHelper.AddToBottom(new RemoveRightmostDebuffAction(p));
        GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);
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
        if (PlayerStatistics.TryActivateLimited(cardID))
        {
            int roll = AbstractDungeon.cardRandomRng.random(2);
            switch (roll)
            {
                case 0:
                {
                    GameActionsHelper.GainIntellect(2);
                    break;
                }
                case 1:
                {
                    GameActionsHelper.GainAgility(2);
                    break;
                }
                case 2:
                {
                    GameActionsHelper.GainForce(2);
                    break;
                }
            }

            return true;
        }

        return false;
    }
}