package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Ain extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Ain.class.getSimpleName());

    public Ain()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(3,0, 2);

        this.isMultiDamage = true;

        AddExtendedDescription();

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + GetFocus(player));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }

        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
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

    private int GetFocus(AbstractPlayer player)
    {
        FocusPower focus = (FocusPower) player.getPower(FocusPower.POWER_ID);
        if (focus != null)
        {
            return focus.amount;
        }

        return 0;
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 3 : 2;
    }
}