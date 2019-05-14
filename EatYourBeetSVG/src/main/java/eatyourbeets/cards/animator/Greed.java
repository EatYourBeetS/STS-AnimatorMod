package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Greed extends AnimatorCard
{
    public static final String ID = CreateFullID(Greed.class.getSimpleName());

    public Greed()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 2);

        baseSecondaryValue = secondaryValue = 1;

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int discount = AbstractDungeon.player.gold / 100;
        if (this.costForTurn > 0 && !this.freeToPlayOnce)
        {
            this.setCostForTurn(this.cost - discount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.secondaryValue), this.secondaryValue);
        GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new MalleablePower(p, 3), 3);

        if (GetMasterDeckInstance() != null)
        {
            p.gainGold(8);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(2);
        }
    }
}