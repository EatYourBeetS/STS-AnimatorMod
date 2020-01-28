package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

public class Azekura extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Azekura.class, EYBCardBadge.Synergy, EYBCardBadge.Exhaust);

    public Azekura()
    {
        super(ID, 2, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 6, 2, 2);
        SetUpgrade(0, 1, 2, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddSuffix("x2");
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

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new PlatedArmorPower(p, secondaryValue));
        }
    }
}