package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

public class Azekura extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Azekura.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Exhaust);

    public Azekura()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,6,2, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.StackPower(new PlatedArmorPower(AbstractDungeon.player, secondaryValue));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.StackPower(new EarthenThornsPower(p, magicNumber));

        if (HasActiveSynergy())
        {
            GameActions.Bottom.StackPower(new PlatedArmorPower(p, secondaryValue));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeMagicNumber(2);
        }
    }
}