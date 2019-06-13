package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.DarknessPower;

public class Darkness extends AnimatorCard
{
    public static final String ID = CreateFullID(Darkness.class.getSimpleName());

    public Darkness()
    {
        super(ID, 0, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,1);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DarknessPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}