package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.AinzEffects.AinzEffect;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper2;
import patches.AbstractEnums;

public class Ainz extends AnimatorCard
{
    public static final String ID = Register(Ainz.class.getSimpleName(), EYBCardBadge.Drawn);
    public static final int BASE_COST = 7;

    public Ainz()
    {
        super(ID, BASE_COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, AinzPower.CHOICES);

        SetHealing(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.cost > 0)
        {
            this.updateCost(-1);

            GameActionsHelper2.GainRandomStat(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.StackPower(new AinzPower(p, 1));
        EffectHistory.TryActivateLimited(Ainz.ID);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(6);
        }
    }

    // Dynamic Effect Card:

    private AinzEffect effect = null;

    public Ainz(AinzEffect effect, String description)
    {
        super(staticCardData.get(ID), ID + "Alt", Resources_Animator.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);

        this.effect = effect;
        this.cardText.OverrideDescription(description, "-", true);
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