package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

public class Sebas extends AnimatorCard
{
    public static final String ID = Register(Sebas.class.getSimpleName(), EYBCardBadge.Discard);

    public Sebas()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,7, 2, 3);

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
        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            if (GameUtilities.IsAttacking(m1.intent))
            {
                GameActionsHelper2.GainBlock(block);
                GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, magicNumber), magicNumber);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }
}