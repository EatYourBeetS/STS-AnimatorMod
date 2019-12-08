package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class DarknessAdrenaline extends AnimatorCard implements Hidden
{
    public static final String ID = Register(DarknessAdrenaline.class.getSimpleName());

    public DarknessAdrenaline()
    {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.VFX(new AdrenalineEffect(), 0.15F);
        GameActionsHelper2.GainEnergy(magicNumber);
        GameActionsHelper2.Draw(2);
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