package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.SupportDamagePower;

public class Guren extends AnimatorCard
{
    public static final String ID = CreateFullID(Guren.class.getSimpleName());

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 9);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        AbstractCard attack = Utilities.GetRandomElement(p.hand.getAttacks().group);
        if (attack != null)
        {
            int damage = attack.baseDamage;
            if (damage > 0)
            {
                GameActionsHelper.AddToTop(new ApplyPowerAction(p, p, new SupportDamagePower(p, damage), damage));
            }

            GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(attack, p.hand, false));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}