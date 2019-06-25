package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Hibiki extends AnimatorCard
{
    public static final String ID = CreateFullID(Hibiki.class.getSimpleName());

    public Hibiki()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL_ENEMY);

        Initialize(2, 0, 3, 1);

        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }

        GameActionsHelper.AddToBottom(new ModifyMagicNumberAction(this.uuid, secondaryValue));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}