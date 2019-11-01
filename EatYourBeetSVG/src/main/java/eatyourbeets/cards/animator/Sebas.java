package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Sebas extends AnimatorCard
{
    public static final String ID = Register(Sebas.class.getSimpleName(), EYBCardBadge.Discard);

    public Sebas()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,8, 3, 3);

        SetExhaust(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (PlayerStatistics.IsAttacking(m1.intent))
            {
                GameActionsHelper.GainBlock(p, block);
                GameActionsHelper.ApplyPower(p, p, new EarthenThornsPower(p, magicNumber), magicNumber);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }
}