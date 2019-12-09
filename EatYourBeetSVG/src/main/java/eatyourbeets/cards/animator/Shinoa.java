package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Shinoa extends AnimatorCard
{
    public static final String ID = Register(Shinoa.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Exhaust);

    public Shinoa()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,6, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyWeak(AbstractDungeon.player, enemy, magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyVulnerable(AbstractDungeon.player, enemy, magicNumber);

            if (HasActiveSynergy())
            {
                GameActions.Bottom.ApplyWeak(AbstractDungeon.player, enemy, magicNumber);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}