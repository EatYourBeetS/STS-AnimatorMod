package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.AinzEffects.AinzEffect;
import eatyourbeets.powers.AinzPower;
import patches.AbstractEnums;

public class Ainz extends AnimatorCard
{
    public static final String ID = CreateFullID(Ainz.class.getSimpleName());
    public static final int BASE_COST = 8;

    private AinzEffect effect = null;

    public Ainz(AinzEffect effect)
    {
        super(AnimatorResources.GetCardStrings(ID), ID + "Alt", AnimatorResources.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);
        this.effect = effect;
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
    }

    public Ainz()
    {
        super(ID, BASE_COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0,0);

        SetSynergy(Synergies.Overlord);

        AddExtendedDescription();
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (this.cost > 0)
        {
            this.updateCost(-1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AinzPower(p, this.upgraded), 1));
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }

    public void setUpgraded(boolean upgrade)
    {
        if (this.effect == null)
        {
            return;
        }

        if (upgrade)
        {
            if (!this.upgraded)
            {
                upgradeName();
            }
        }
        else
        {
            if (this.upgraded)
            {
                --this.timesUpgraded;
                this.upgraded = false;
                this.name = this.name.replace("+", "");
                this.initializeTitle();
            }
        }
    }
}