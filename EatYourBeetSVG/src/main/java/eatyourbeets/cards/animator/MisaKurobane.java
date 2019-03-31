package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, 2), 2);
        GameActionsHelper.DrawCard(p, this.secondaryValue);

        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.baseMagicNumber = Math.max(0, c.baseMagicNumber - 1);
            c.magicNumber = c.baseMagicNumber;
        }

        if (this.magicNumber == 0)
        {
            this.purgeOnUse = true;

            Yusarin yusarin = new Yusarin();
            if (upgraded)
            {
                yusarin.upgrade();
            }
            GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(yusarin, 1));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}