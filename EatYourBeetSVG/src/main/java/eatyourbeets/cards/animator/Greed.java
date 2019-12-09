package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Greed extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(Greed.class.getSimpleName(), EYBCardBadge.Special);

    public Greed()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,2, 2, 8);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int discount = Math.floorDiv(AbstractDungeon.player.gold, 100);
        if (this.costForTurn > 0 && !this.freeToPlayOnce)
        {
            this.modifyCostForTurn(-discount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainPlatedArmor(magicNumber);
        GameActions.Bottom.GainMetallicize(magicNumber);
        GameActions.Bottom.StackPower(new MalleablePower(p, magicNumber));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        AbstractPlayer p = AbstractDungeon.player;

        for (int i = 0; i < this.secondaryValue; ++i)
        {
            AbstractDungeon.effectList.add(new GainPennyEffect(p.hb.cX, p.hb.cY + (p.hb.height / 2)));
        }

        p.gainGold(this.secondaryValue);

        return true;
    }
}