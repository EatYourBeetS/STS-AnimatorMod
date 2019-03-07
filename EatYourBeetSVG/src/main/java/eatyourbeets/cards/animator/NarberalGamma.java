package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class NarberalGamma extends AnimatorCard
{
    public static final String ID = CreateFullID(NarberalGamma.class.getSimpleName());

    public NarberalGamma()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.Overlord, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Lightning(), true));
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedBluePower(p, 1), 1));

        if (HasActiveSynergy() && upgraded)
        {
            AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(this.magicNumber));
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}