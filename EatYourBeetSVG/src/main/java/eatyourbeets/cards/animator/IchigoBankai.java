package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.Hidden;

public class IchigoBankai extends AnimatorCard implements Hidden
{
    public static final String ID = Register(IchigoBankai.class.getSimpleName());

    public IchigoBankai()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF);

        Initialize(17, 0, 3);

        this.exhaust = true;

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(5);
        }
    }
}

/*
	"animator:IchigoBankai":
	{
		"DESCRIPTION": "Deal !D! piercing damage to ALL enemies. Draw !M! cards. Exhaust.",
		"NAME": "Ichigo Bankai"
	},
	"animator:IchigoKurosaki":
	{
		"DESCRIPTION": "Next turn, gain X [E] and Temporary Strength. If X is at least !M! , Purge and obtain [#EFC851]Ichigo[] [#EFC851]Bankai[].",
		"NAME": "Ichigo Kurosaki"
	},
*/