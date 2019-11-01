package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Asuramaru extends AnimatorCard implements Hidden
{
    public static final String ID = Register(Asuramaru.class.getSimpleName());

    public Asuramaru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);

        Initialize(9,0,2);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        //GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, p, new DemonFormPower(p, this.magicNumber), this.magicNumber);
        PlayerStatistics.GainIntellect(magicNumber);
        PlayerStatistics.GainAgility(magicNumber);

        //GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber);
        //GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(7);
        }
    }
}