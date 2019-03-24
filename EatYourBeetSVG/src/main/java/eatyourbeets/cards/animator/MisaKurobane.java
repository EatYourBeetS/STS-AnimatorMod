package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;

public class MisaKurobane extends AnimatorCard
{
    public static final String ID = CreateFullID(MisaKurobane.class.getSimpleName());

    public MisaKurobane()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0, 0,2);

        this.baseSecondaryValue = this.secondaryValue = 2;

        AddExtendedDescription();

        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hand.size() > 0)
        {
            GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.secondaryValue), this.secondaryValue);
            GameActionsHelper.DrawCard(p, 1);

            if (this.magicNumber > 1)
            {
                AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumberAction(this.uuid, -1));
            }
        }

        if (this.magicNumber <= 1)
        {
            this.purgeOnUse = true;
        }
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