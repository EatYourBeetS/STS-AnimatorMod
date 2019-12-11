package eatyourbeets.cards.unnamed;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Strike extends UnnamedCard
{
    public static final String ID = Register(Strike.class.getSimpleName());

    public Strike()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);

        Initialize(6,0);

        this.tags.add(BaseModCardTags.BASIC_STRIKE);
        this.tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}