package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Greed extends AnimatorCard
{
    public static final String ID = Register(Greed.class.getSimpleName(), EYBCardBadge.Special);

    public Greed()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

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
        if (upgraded)
        {
            GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, 3), 3);
        }

        GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, 3), 3);
        GameActionsHelper.ApplyPower(p, p, new MalleablePower(p, 3), 3);
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}