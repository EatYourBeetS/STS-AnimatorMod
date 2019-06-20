package eatyourbeets.cards.unnamed;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.common.IncreaseMaxHpAction;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.unnamed.UnnamedDollPlayerPower;
import eatyourbeets.utilities.GameActionsHelper;

public class UnnamedDoll extends UnnamedCard
{
    public static final String ID = CreateFullID(UnnamedDoll.class.getSimpleName());

    public UnnamedDoll()
    {
        super(ID, -1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 0, 2);

        tags.add(CardTags.HEALING);

        FleetingField.fleeting.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.energyOnUse < EnergyPanel.totalCount)
        {
            this.energyOnUse = EnergyPanel.totalCount;
        }

        int amount = energyOnUse + 2;
        if (!this.freeToPlayOnce)
        {
            p.energy.use(EnergyPanel.totalCount);
        }

        GameActionsHelper.AddToBottom(new IncreaseMaxHpAction(p, amount, true));
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